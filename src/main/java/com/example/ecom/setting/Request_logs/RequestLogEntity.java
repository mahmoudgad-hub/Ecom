package com.example.ecom.setting.Request_logs;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Table(name = "ecom_request_logs_tab", indexes = {
        @Index(name = "idx_request_time", columnList = "request_time"),
        @Index(name = "idx_ip_address", columnList = "ip_address"),
        @Index(name = "idx_method", columnList = "method"),
        @Index(name = "idx_status_code", columnList = "status_code")
})
@Setter
@Getter
public class RequestLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "Log_SEQ_GENERATOR")
    @SequenceGenerator(name = "Log_SEQ_GENERATOR", sequenceName = "ecom_request_logs_tab_seq", allocationSize = 1)
    private Long id;

    @Column(name = "method", nullable = false, length = 10)
    private String method;

    @Column(name = "url", nullable = false, length = 1000)
    private String url;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "request_time", nullable = false)
    private LocalDateTime requestTime;

    @Column(name = "response_time")
    private LocalDateTime responseTime;

    @Column(name = "status_code")
    private Integer statusCode;

    @Column(name = "processing_time_ms")
    private Long processingTimeMs;

    @Lob
    @Column(name = "request_body")
    private String requestBody;

    @Column(name = "session_id", length = 100)
    private String sessionId;

    @Column(name = "request_size")
    private Long requestSize;

    @Column(name = "response_size")
    private Long responseSize;

    @Column(name = "referer", length = 500)
    private String referer;


    private Long userId;

    private String tokenId;



    // ====== Constructors ======
    public RequestLogEntity() {
    }

    public RequestLogEntity(String method, String url, String ipAddress,
                            String userAgent, LocalDateTime requestTime) {
        this.method = method;
        this.url = url;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.requestTime = requestTime;
    }

    // ====== Getters & Setters ======
    // ... (نفس اللي عندك قبل كده)

    @Override
    public String toString() {
        return "RequestLog{" +
                "id=" + id +
                ", method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", requestTime=" + requestTime +
                ", statusCode=" + statusCode +
                ", processingTimeMs=" + processingTimeMs +
                '}';
    }
}