package org.globalds.mutantes.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter implements Filter {

    private static final int MAX_REQUESTS = 10;
    private static final long WINDOW_MS = 60_000; // 1 minuto
    private final Map<String, AccessData> accessMap = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request  = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getRequestURI();

        if (path.startsWith("/h2-console")
                || path.startsWith("/swagger")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/health")) {
            chain.doFilter(req, res);
            return;
        }

        String ip = request.getRemoteAddr();
        long now = Instant.now().toEpochMilli();

        accessMap.putIfAbsent(ip, new AccessData(0, now));

        AccessData data = accessMap.get(ip);

        // Reiniciar ventana si pasó el minuto
        if (now - data.startTime > WINDOW_MS) {
            data.requests = 0;
            data.startTime = now;
        }

        // Verificar límite
        if (data.requests >= MAX_REQUESTS) {
            response.setStatus(429); // Too Many Requests
            response.getWriter().write("Rate limit exceeded (10 req/min). Try again later.");
            return;
        }

        data.requests++;

        chain.doFilter(req, res);
    }

    private static class AccessData {
        int requests;
        long startTime;

        AccessData(int r, long t) {
            this.requests = r;
            this.startTime = t;
        }
    }
}
