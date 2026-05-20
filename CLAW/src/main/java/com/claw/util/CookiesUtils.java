package com.claw.util;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Utility for handling browser cookies easily.
 */
public class CookiesUtils {
    /**
     * Adds a cookie to the response.
     */
    public static void addCookie(HttpServletResponse response, String
            name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(&quot;/&quot;);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(true); // Security: Prevents JS access to
        cookies
        response.addCookie(cookie);
    }
    /**
     * Retrieves a cookie value by name.
     */
    public static String getCookieValue(HttpServletRequest request, String
            name) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
    /**
     * Deletes a cookie by setting its age to zero.
     */
    public static void deleteCookie(HttpServletResponse response, String
            name) {
        addCookie(response, name, &quot;&quot;, 0);
    }
}