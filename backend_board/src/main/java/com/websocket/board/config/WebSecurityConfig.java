package com.websocket.board.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static org.springframework.http.HttpMethod.OPTIONS;

/**
 * Web Security 설정
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().mvcMatchers(OPTIONS, "/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // 기본값이 on인 csrf 취약점 보안을 해제한다. on으로 설정해도 되나 설정할경우 웹페이지에서 추가처리가 필요함.
                .headers()
                .frameOptions().sameOrigin(); // SockJS는 기본적으로 HTML iframe 요소를 통한 전송을 허용하지 않도록 설정되는데 해당 내용을 해제한다.
                //.and()
                //.formLogin() // 권한없이 페이지 접근하면 로그인 페이지로 이동한다.
                //.and()
                //.httpRequest.cors()
                //.authorizeRequests()
                //.antMatchers(OPTIONS, "/**").permitAll()
                //.antMatchers("/board/**").hasRole("USER") // chat으로 시작하는 리소스에 대한 접근 권한 설정
                //.anyRequest().permitAll(); // 나머지 리소스에 대한 접근 설정

        http
                //.sessionManagement()
                //.sessionCreationPolicy(STATELESS)
                //.and()
                .exceptionHandling()
                //.defaultAuthenticationEntryPointFor(forbiddenEntryPoint(), PROTECTED_URLS)
                .and()
                //.authenticationProvider(provider)
                //.addFilterBefore(restAuthenticationFilter(), AnonymousAuthenticationFilter::class.java)
                //.authorizeRequests()
                //.requestMatchers(PROTECTED_URLS)
                //.authenticated()
                //.and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable();
    }

}
