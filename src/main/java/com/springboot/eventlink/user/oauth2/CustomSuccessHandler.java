package com.springboot.eventlink.user.oauth2;

import com.springboot.eventlink.user.dto.CustomOAuth2User;
import com.springboot.eventlink.user.jwt.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    public CustomSuccessHandler(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String username = customUserDetails.getUsername();
        String name = customUserDetails.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(username, role, 60*60*60*60L);

        response.addCookie(createCookie("Authorization", token));
        String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
        String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8.toString());
//        response.sendRedirect("http://localhost:3000/mypage");
        response.sendRedirect("http://localhost:3000/mypage?token=" + encodedToken);

//        response.sendRedirect("http://localhost:3000/mypage?user_nm=" + encodedName + "&token=" + encodedToken);


//        response.sendRedirect("http://localhost:3000/welcome?user_nm=" + encodedName);
        //response.sendRedirect("http://localhost:3000/");
        //response.sendRedirect("http://localhost:8080/test");
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60*60);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
