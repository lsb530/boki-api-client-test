package com.boki.bokiapiclienttest.controller

import com.boki.bokiapiclienttest.dto.LoginRequest
import com.boki.bokiapiclienttest.util.JWTUtil
import com.boki.bokiapiclienttest.dto.UserInfo
import com.boki.bokiapiclienttest.dto.toUserInfo
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.view.RedirectView
import java.util.*

@RequestMapping("/api")
@RestController
class BokiController(
    private val jwtUtil: JWTUtil,
) {

    @GetMapping
    fun get(request: HttpServletRequest): ResponseEntity<Any> {
        val isForwarded = request.getAttribute("isForwarded") as? Boolean ?: false
        return if (isForwarded) {
            ResponseEntity.ok("FORWARD 요청에 대한 응답입니다.")
        } else {
            ResponseEntity.ok("GET 요청에 대한 응답입니다.")
        }
    }

    @GetMapping("/random")
    fun random(@RequestParam id: UUID, @RequestParam email: String, @RequestParam ts: Long): ResponseEntity<Any> {
        val response = mapOf("id" to id, "email" to email, "ts" to ts)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{name}/{age}")
    fun pathVariable(@PathVariable name: String, @PathVariable age: Int): ResponseEntity<Any> {
        return ResponseEntity.ok("GET 요청에 대한 응답입니다.\n$name is $age years old")
    }

    @GetMapping("/param")
    fun parameters(
        @RequestParam(defaultValue = "tester", required = false) name: String,
        @RequestParam(defaultValue = "20", required = false) age: Int,
    ): ResponseEntity<Any> {
        return ResponseEntity.ok(
            """GET 요청에 대한 응답입니다.
                |$name is $age years old""".trimMargin()
        )
    }

    @GetMapping("/forward")
    fun forward(request: HttpServletRequest, response: HttpServletResponse) {
        request.setAttribute("isForwarded", true)
        request.getRequestDispatcher("/api").forward(request, response)
    }

    @GetMapping("/redirect")
    fun redirect(): RedirectView {
        return RedirectView("https://www.google.com")
    }

    @GetMapping("/cookie-set")
    fun cookieSet(response: HttpServletResponse): ResponseEntity<Any> {
        val cookie = Cookie("boki", "strong").apply {
            path = "/"
            maxAge = 60 * 60 * 24 // 24 hours
//            isHttpOnly = true
//             secure = true
        }

        response.addCookie(cookie)

        return ResponseEntity.ok().body("쿠키가 설정되었습니다.")
    }

    @GetMapping("/cookie-get")
    fun cookieGet(request: HttpServletRequest): ResponseEntity<Any> {
        val cookies = request.cookies ?: return ResponseEntity.ok("쿠키가 없습니다.")

        return ResponseEntity.ok(cookies)
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<Any> {
        val (email, password) = loginRequest
        if (email != "admin@boki.com" && password != "1234") {
            throw RuntimeException("Invalid login request")
        }
        val jwt = jwtUtil.generateJWT(UserInfo(name = "boki", age = 20))
//        return ResponseEntity.ok(mapOf("token" to jwt))
        return ResponseEntity.ok(object {
            val token = jwt
        })
    }

    @GetMapping("/me")
    fun me(@RequestHeader(HttpHeaders.AUTHORIZATION) authorizationHeader: String?): ResponseEntity<Any> {
        if (authorizationHeader == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        else {
            val token = if (authorizationHeader.startsWith("Bearer ")) {
                authorizationHeader.substring(7)
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
            }
            val userInfo = jwtUtil.readJWT(token).toUserInfo()
            return ResponseEntity.ok(userInfo)
        }
    }

    @PutMapping
    fun put(): ResponseEntity<Any> {
        return ResponseEntity.ok("PUT 요청에 대한 응답입니다.")
    }

    @PatchMapping
    fun patch(): ResponseEntity<Any> {
        return ResponseEntity.ok("PATCH 요청에 대한 응답입니다.")
    }

    @DeleteMapping
    fun delete(): ResponseEntity<Any> {
        return ResponseEntity.ok("DELETE 요청에 대한 응답입니다.")
    }

    @RequestMapping(method = [RequestMethod.HEAD])
    fun head(): ResponseEntity<Any> {
        return ResponseEntity.ok().build()
    }

    @RequestMapping(method = [RequestMethod.OPTIONS])
    fun options(): ResponseEntity<String> {
        return ResponseEntity.ok().allow(
            HttpMethod.GET,
            HttpMethod.POST,
            HttpMethod.PUT,
            HttpMethod.PATCH,
            HttpMethod.DELETE,
            HttpMethod.HEAD,
            HttpMethod.OPTIONS
        ).build()
    }
}