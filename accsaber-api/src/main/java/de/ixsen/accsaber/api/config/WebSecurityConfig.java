package de.ixsen.accsaber.api.config;

import de.ixsen.accsaber.business.AccSaberUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final String secret;
    private final long expirationTime;

    private final AccSaberUserDetailsService userDetailsService;
    private final PasswordEncoder bCryptPasswordEncoder;

    public WebSecurityConfig(AccSaberUserDetailsService userService,
                             PasswordEncoder bCryptPasswordEncoder,
                             @Value("${accsaber.auth.secret}") String secret,
                             @Value("${accsaber.auth.expiration-time}") long expirationTime) {
        this.userDetailsService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;

        this.secret = secret;
        this.expirationTime = expirationTime;
    }

    @Override
    protected void configure(HttpSecurity http)
            throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/staff/**").authenticated()
                .antMatchers(HttpMethod.GET, "/ranked-maps/statistics").authenticated()
                .antMatchers(HttpMethod.GET, "/**").permitAll()
                .antMatchers(HttpMethod.POST, "/players/**").permitAll()
                .antMatchers(HttpMethod.POST, "/login**").permitAll()
                .antMatchers(HttpMethod.POST, "/**").authenticated()
                .antMatchers(HttpMethod.PUT, "/**").authenticated()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .and()
                .addFilter(new AuthenticationFilter(this.authenticationManager(), this.secret, this.expirationTime))
                .addFilter(new AuthorizationFilter(this.authenticationManager(), this.secret));
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService).passwordEncoder(this.bCryptPasswordEncoder);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}