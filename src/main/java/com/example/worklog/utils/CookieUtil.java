package com.example.worklog.utils;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;

public class CookieUtil {
    public static void addCookie(
            String name,
            String value,
            HttpServletResponse response
            ) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
//                .sameSite("None")
//                .path("/")
//                .httpOnly(true)
//                .secure(true)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }
}
