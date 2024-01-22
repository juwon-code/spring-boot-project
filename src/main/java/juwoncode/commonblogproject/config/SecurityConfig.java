package juwoncode.commonblogproject.config;

import juwoncode.commonblogproject.service.MemberDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final static String[] ADMIN_LINKS = {};

    private final static String[] SINGED_LINKS = {"/member/change/**"};

    private final static String[] ANONYMOUS_LINKS = {"/api/member/register/**", "/member/login", "/member/register"};

    private final static String[] PUBLIC_LINKS = {"/home", "/email/verify/**"};

    private final static String[] STATIC_RESOURCES = {"/layout/**", "/css/**", "/js/**", "/image/**"};

    private final MemberDetailsService memberDetailsService;

    public SecurityConfig(MemberDetailsService memberDetailsService) {
        this.memberDetailsService = memberDetailsService;
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
            - csrf: Spring MVC는 공격에 취약할 수 있으므로 기본값으로 활성화.
            - cors: 동일한 도메인에서 뷰와 API를 사용할 것이므로 비활성화.
            - httpBasic: formLogin()을 사용하여 인증할 것이므로 비활성화.
         */
        httpSecurity
                .cors(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

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
            3. 로그인 폼 설정
            로그인이 성공할 경우, 홈 페이지로 이동한다.
            로그인이 실패할 경우, 로그인 페이지로 이동하며, (미구현) 실패를 사용자에게 알린다.
         */
        httpSecurity
                .formLogin((login) -> login
                        .loginPage("/member/login")
                        .successHandler((request, response, authentication) -> {
                            response.sendRedirect("/home");
                        })
                        .failureUrl("/member/login")
                        .permitAll());

        /*
            4. 로그아웃 폼 설정
            로그아웃이 성공할 경우, 로그인 페이지로 이동한다.
         */
        httpSecurity
                .logout((logout) -> logout
                        .logoutUrl("/member/logout")
                        .logoutSuccessUrl("/member/login")
                        .permitAll());

        return httpSecurity.build();
    }

    /**
     * Spring Security의 인증 공급자를 생성하고 구성한다.<br>
     * DaoAuthentication에 UserDetailsService와 PasswordEncoder를 할당하고 인스턴스를 반환한다.
     * @return 설정이 완료된 {@link DaoAuthenticationProvider} 인스턴스.
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(memberDetailsService);
        provider.setPasswordEncoder(createPasswordEncoder());

        return provider;
    }
}
