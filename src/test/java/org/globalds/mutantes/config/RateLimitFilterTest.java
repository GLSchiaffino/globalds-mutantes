package org.globalds.mutantes.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class RateLimitFilterTest {

    @Test
    void allowsSwaggerRequests() throws Exception {
        RateLimitFilter filter = new RateLimitFilter();

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(req.getRequestURI()).thenReturn("/swagger-ui/index.html");

        filter.doFilter(req, res, chain);

        verify(chain, times(1)).doFilter(req, res);
    }
}
