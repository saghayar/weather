package com.paymentology.weather.adapter.rest;

import com.paymentology.weather.application.exception.InfraException;
import com.paymentology.weather.application.exception.ServiceErrorCodes;
import com.paymentology.weather.port.ClientKeyRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private static final String X_API_KEY = "X-API-KEY";

    private final ClientKeyRepository clientKeyRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        if (permit(request)) {
            filterChain.doFilter(request, response);
        } else {
            response.sendError(HttpStatus.FORBIDDEN.value());
        }
    }

    private boolean permit(HttpServletRequest request) {
        return authHeaderPermit(request) || openApiPermit(request);
    }

    //TODO consider writing better rule: gather all open-api paths to a list and do exact matching
    private boolean openApiPermit(HttpServletRequest request) {
        String path = request.getRequestURI().substring(8);
        return path.contains("swagger") || path.contains("docs");
    }

    private Boolean authHeaderPermit(HttpServletRequest request) {
        return Optional
                .ofNullable(request.getHeader(X_API_KEY))
                .flatMap(clientKeyRepository::find)
                .map(key -> !key.revoked())
                .orElse(false);
    }

}
