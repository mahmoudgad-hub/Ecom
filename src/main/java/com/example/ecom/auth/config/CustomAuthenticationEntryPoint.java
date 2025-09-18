package com.example.ecom.auth.config;

import com.example.ecom.Lang.MessageUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private MessageUtil messageUtil;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        response.setContentType("application/json; charset=UTF-8"); // 👈 UTF-8 هنا مهم
        response.setCharacterEncoding("UTF-8"); // 👈 ضمان الترميز
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
     //   response.getWriter().write("{\"message\": \"Unauthenticated.\"}");
//        String jsonResponse = """
//            {
//              "error": 401,
//              "message": "Unauthenticated."
//            }
//            """;
        String lang = request.getParameter("lang");
        if (lang == null || lang.isBlank()) {
            lang = request.getHeader("Accept-Language");
        }
        if (lang == null || lang.isBlank()) {
            lang = "en"; // اللغة الافتراضية
        }
        Map<String, Object> responseMap = Map.of(
                "error", 401,
                "message", messageUtil.get("sys.unauthenticated",null, new Locale(lang))
        );

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);

        String jsonResponse = mapper.writeValueAsString(responseMap);

        response.getWriter().write(jsonResponse);
    }


}

