package de.ixsen.accsaber.api.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.ObjectMapper
import de.ixsen.accsaber.database.model.staff.StaffUser
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationFilter(
    private val accsaberAuthenticationManager: AuthenticationManager,
    private val secret: String, // 15 mins
    private val expirationTime: Long
) : UsernamePasswordAuthenticationFilter() {
    init {
        setFilterProcessesUrl("/staff/login")
    }

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(
        req: HttpServletRequest,
        res: HttpServletResponse
    ): Authentication {
        return try {
            val creds = ObjectMapper()
                .readValue(req.inputStream, StaffUser::class.java)
            this.accsaberAuthenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    creds.username,
                    creds.password,
                    ArrayList()
                )
            )
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    @Throws(IOException::class)
    override fun successfulAuthentication(
        req: HttpServletRequest,
        res: HttpServletResponse,
        chain: FilterChain,
        auth: Authentication
    ) {
        val token = JWT.create()
            .withSubject((auth.principal as UserDetails).username)
            .withExpiresAt(Date(System.currentTimeMillis() + expirationTime))
            .sign(Algorithm.HMAC512(secret.toByteArray()))
        val body = "Bearer $token"
        res.writer.write(body)
        res.writer.flush()
    }
}