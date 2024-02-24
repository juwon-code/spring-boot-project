package juwoncode.commonblogproject.config;

import juwoncode.commonblogproject.service.MemberDetailsService;
import juwoncode.commonblogproject.service.JwtTokenService;
import juwoncode.commonblogproject.util.JwtTokenParser;
import juwoncode.commonblogproject.util.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final static String[] ADMIN_LINKS = {};
    private final static String[] SINGED_LINKS = {"/member/change/**"};
    private final static String[] ANONYMOUS_LINKS = {"/api/member/register/**", "/member/login**", "/member/register/verify"
            , "/member/register**", "/email/verify/**"};
    private final static String[] PUBLIC_LINKS = {"/", "/home"};
    private final static String[] STATIC_RESOURCES = {"/layout/**", "/css/**", "/js/**", "/image/**"};

    private final MemberDetailsService memberDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenParser jwtTokenParser;
    private final JwtTokenService jwtTokenService;

    public SecurityConfig(MemberDetailsService memberDetailsService, AuthenticationConfiguration authenticationConfiguration
            , JwtTokenProvider jwtTokenProvider, JwtTokenParser jwtTokenParser, JwtTokenService jwtTokenService) {
        this.memberDetailsService = memberDetailsService;
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtTokenParser = jwtTokenParser;
        this.jwtTokenService = jwtTokenService;
    }

    @Bean
    public AuthenticationManager getAuthenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 비밀번호 암호 및 복호화를 위한 PasswordEncoder 빈을 생성한다.<br>
     * 현재 BCrypt 해시 함수를 사용해서 암호 및 복호화를 수행한다.
     * @return
     *      BCryptPasswordEncoder 인스턴스.
     */
    @Bean
    public PasswordEncoder createPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Spring Security를 체인 형식으로 설정한다.
     * @param httpSecurity
     *      HTTP 보안 설정 객체.
     * @return
     *      설정이 완료된 SecurityFilterChain 빈.
     * @throws Exception
     *      보안 설정 중에 발생할 수 있는 예외.
     */
    @Bean
    public SecurityFilterChain configureSecurity(HttpSecurity httpSecurity) throws Exception {
        /*
            1. Spring Security 보안 설정
            - csrf: JWT 토큰은 비연결을 지향하므로 비활성화.
            - cors: 동일한 도메인에서 뷰와 API를 사용할 것이므로 비활성화.
            - httpBasic: JWT 토큰을 사용할 것이므로 비활성화.
            - formLogin: 자체적인 로그인 필터를 사용할 것이므로 비활성화.
            - sessionManagement: 세션 생성 규칙을 stateless 상태로 설정.
         */
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        /*
            2. 회원 역할 액세스 설정
            회원 역할 별 접근할 수 있는 엔드포인트를 설정한다.
            - 관리자: 관리자는 모든 엔드포인트에 접근할 수 있다.
            - 회원: 회원은 관리자 링크를 제외한 엔드포인트에 접근할 수 있다.
            - 익명: 회원 및 관리자 링크를 제외한 엔드포인트에 접근할 수 있다.
         */
        httpSecurity
                .authorizeHttpRequests((request) -> request
                        .requestMatchers(ADMIN_LINKS).hasRole("ADMIN")
                        .requestMatchers(SINGED_LINKS).hasAnyRole("USER", "ADMIN")
                        .requestMatchers(ANONYMOUS_LINKS).anonymous()
                        .requestMatchers(PUBLIC_LINKS).permitAll()
                        .requestMatchers(STATIC_RESOURCES).permitAll()
                        .anyRequest().authenticated());

        /*
            4. 로그아웃 폼 설정
            로그아웃이 성공할 경우, 로그인 페이지로 이동한다.
         */
        httpSecurity
                .logout((logout) -> logout
                        .logoutUrl("/api/member/logout")
                        .logoutSuccessUrl("/member/login")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll());

        /*
            5. 필터 설정
            로그인, 로그아웃과 관련된 필터들을 설정한다.
            - LoginAuthorizationFilter: JWT 토큰을 검증하고 인가를 처리하는 필터.
            - LoginAuthenticationFilter: 로그인을 처리하여 JWT 토큰을 발급하는 필터.
         */
        httpSecurity
                .addFilter(new LoginAuthorizationFilter(getAuthenticationManager(), jwtTokenParser, jwtTokenService
                        , memberDetailsService))
                .addFilter(new LoginAuthenticationFilter(getAuthenticationManager(), jwtTokenProvider
                        , jwtTokenService, memberDetailsService));

        return httpSecurity.build();
    }
}
