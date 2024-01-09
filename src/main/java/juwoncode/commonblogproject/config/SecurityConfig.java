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
    private final static String[] ANONYMOUS_LINKS = {"/member/api/register/**", "/member/login", "/member/register"};
    private final static String[] PUBLIC_LINKS = {"/layout/**", "/css/**", "/js/**", "/home", "/member/change-password/**", "/member/validate/email"};
    private final MemberDetailsService memberDetailsService;

    public SecurityConfig(MemberDetailsService memberDetailsService) {
        this.memberDetailsService = memberDetailsService;
    }

    @Bean
    public PasswordEncoder createPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain setupSecurityChain(HttpSecurity security) throws Exception {
        security
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        security
                .authorizeHttpRequests((request) -> request
                        .requestMatchers(ADMIN_LINKS).hasRole("ADMIN")
                        .requestMatchers(SINGED_LINKS).hasAnyRole("USER", "ADMIN")
                        .requestMatchers(ANONYMOUS_LINKS).anonymous()
                        .requestMatchers(PUBLIC_LINKS).permitAll()
                        .anyRequest().authenticated());

        security
                .formLogin((login) -> login
                        .loginPage("/member/login")
                        .successHandler((request, response, authentication) -> {
                            response.sendRedirect("/home");
                        })
                        .failureUrl("/member/login")
                        .permitAll());

        security
                .logout((logout) -> logout
                        .logoutUrl("/member/logout")
                        .logoutSuccessUrl("/member/login")
                        .permitAll());

        return security.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(memberDetailsService);
        provider.setPasswordEncoder(createPasswordEncoder());

        return provider;
    }
}
