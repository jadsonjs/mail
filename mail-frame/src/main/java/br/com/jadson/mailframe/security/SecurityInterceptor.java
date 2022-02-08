package br.com.jadson.mailframe.security;

import br.com.jadson.mailframe.client.security.JwtManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Classe to security the email service
 */
public class SecurityInterceptor implements HandlerInterceptor {

    @Autowired
    JwtManager jwt;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        return validateToken(request, response);
    }

    private boolean validateToken(HttpServletRequest request, HttpServletResponse response) {
        String headerAuth = request.getHeader("Authorization");

        if (headerAuth != null && ! headerAuth.isBlank() && headerAuth.startsWith("Bearer ")) {
            String jwtToken = headerAuth.substring(7);

            if ( jwt.validate(jwtToken) ){
                 return true;
            }
        }
        return unauthorized(response, "Access Denied");
    }

    private boolean unauthorized(HttpServletResponse response, String msg) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type");
            response.setHeader("Access-Control-Allow-Headers", "Content-Length");
            response.setHeader("Access-Control-Allow-Headers", "Date");

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(msg);

        } catch (IOException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
        return false;
    }
}
