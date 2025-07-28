package com.vfd.server.securities

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}") val jwtSecret: String
) {
    val key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret))

    private val jwtExpirationMs: Long = 3600_000

    fun generateToken(auth: Authentication): String {
        val user = auth.principal as org.springframework.security.core.userdetails.User
        val now = Date()
        val expiry = Date(now.time + jwtExpirationMs)
        return Jwts.builder()
            .setSubject(user.username)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }
}
