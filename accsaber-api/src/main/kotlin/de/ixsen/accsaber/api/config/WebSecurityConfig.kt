package de.ixsen.accsaber.api.config

import de.ixsen.accsaber.business.AccSaberUserDetailsService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    private val userDetailsService: AccSaberUserDetailsService,
    private val bCryptPasswordEncoder: PasswordEncoder,
    @param:Value("\${accsaber.auth.secret}") private val secret: String,
    @param:Value("\${accsaber.auth.expiration-time}") private val expirationTime: Long
) : WebSecurityConfigurerAdapter() {
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.cors().and().csrf().disable()
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/staff/**").authenticated()
            .antMatchers(HttpMethod.GET, "/ranked-maps/statistics").authenticated()
            .antMatchers(HttpMethod.GET, "/**").permitAll()
            .antMatchers(HttpMethod.POST, "/players/**").permitAll()
            .antMatchers(HttpMethod.POST, "/**").authenticated()
            .antMatchers(HttpMethod.PUT, "/**").authenticated()
            .antMatchers(HttpMethod.DELETE, "/**").authenticated()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .and()
            .addFilter(AuthenticationFilter(authenticationManager(), secret, expirationTime))
            .addFilter(AuthorizationFilter(authenticationManager(), secret))
    }

    @Throws(Exception::class)
    public override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder)
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        val corsConfiguration = CorsConfiguration().applyPermitDefaultValues()
        source.registerCorsConfiguration("/**", corsConfiguration)
        return source
    }
}