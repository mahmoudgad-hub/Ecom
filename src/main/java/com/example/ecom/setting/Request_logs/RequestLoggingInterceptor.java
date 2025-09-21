package com.example.ecom.setting.Request_logs;

import com.example.ecom.auth.CurrentUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private final RequestLogRepo requestLogRepository;

    public RequestLoggingInterceptor(RequestLogRepo requestLogRepository) {
        this.requestLogRepository = requestLogRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        // نحفظ البيانات الأساسية مع وقت الطلب
        RequestLogEntity log = new RequestLogEntity();
        log.setMethod(request.getMethod());
        log.setUrl(request.getRequestURI());
        log.setIpAddress(request.getRemoteAddr());
        log.setUserAgent(request.getHeader("User-Agent"));
        log.setRequestTime(LocalDateTime.now());
        log.setReferer(request.getHeader("Referer"));
      //  log.setSessionId(request.getRequestedSessionId());
        log.setSessionId(request.getSession().getId());

        log.setUserId(CurrentUser.getUserId());

        // نستخدم attribute علشان نضيف باقي البيانات بعد ما الطلب يخلص
        request.setAttribute("requestLog", log);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        RequestLogEntity log = (RequestLogEntity) request.getAttribute("requestLog");
        if (log != null) {
            log.setResponseTime(LocalDateTime.now());
            log.setStatusCode(response.getStatus());

            // وقت المعالجة
            if (log.getRequestTime() != null) {
                long processingTime = java.time.Duration.between(
                        log.getRequestTime(), log.getResponseTime()
                ).toMillis();
                log.setProcessingTimeMs(processingTime);
            }

            if(log.getUserId() == null ){
                log.setUserId(CurrentUser.getUserId());
            }

            String authHeader = request.getHeader("Authorization");
            String token = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7); // قص كلمة "Bearer "
                log.setTokenId(token);
            }




            requestLogRepository.save(log);
        }
    }
}
