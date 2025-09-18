package com.example.ecom.auth.config;


import com.example.ecom.Lang.MessageUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private MessageUtil messageUtil;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        response.setContentType("application/json; charset=UTF-8"); // 👈 UTF-8 هنا مهم
        response.setCharacterEncoding("UTF-8"); // 👈 ضمان الترميز
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

//        String jsonResponse = """
//            {
//              "error": 403,
//              "message": "Access Denied."
//            }
//            """;
//
//        response.getWriter().write(jsonResponse);
        String lang = request.getParameter("lang");
        if (lang == null || lang.isBlank()) {
            lang = request.getHeader("Accept-Language");
        }
        if (lang == null || lang.isBlank()) {
            lang = "en"; // اللغة الافتراضية
        }

        Map<String, Object> responseMap = Map.of(
                "error", 403,
                "message", messageUtil.get("sys.access_denied",null, new Locale(lang))
        );

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);

        String jsonResponse = mapper.writeValueAsString(responseMap);

        response.getWriter().write(jsonResponse);
    }
    }


