package com.boki.bokiapiclienttest.util

import com.boki.bokiapiclienttest.dto.UserInfo
import com.boki.bokiapiclienttest.dto.asMap
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.util.*

@Component
class JWTUtil {

    final val ONE_DAY = 1000L * 60L * 60L * 24L
    final val KEY = Jwts.SIG.HS256.key().build()
    final val SUBJECT = "code-boki"

    fun generateJWT(userInfo: UserInfo): String {
        val expireDate = Date(Date().time + ONE_DAY)
        val token = Jwts
            .builder()
            .subject(SUBJECT)
            .claims(userInfo.asMap())
            .expiration(expireDate)
            .signWith(KEY)
            .compact()
        return token
    }

    fun readJWT(token: String): Claims {
        validateJWT(token)
        val claims = Jwts.parser().verifyWith(KEY).build().parseSignedClaims(token).payload
        return claims
    }

    private fun validateJWT(token: String) {
        try {
            Jwts.parser().verifyWith(KEY).build().parseSignedClaims(token)
        } catch (e: JwtException) {
            throw RuntimeException("invalid jwt")
        }
    }
}