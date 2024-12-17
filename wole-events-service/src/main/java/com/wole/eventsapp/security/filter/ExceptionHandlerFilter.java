package com.wole.eventsapp.security.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.wole.eventsapp.exceptions.EntityNotFoundException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request, response);
        } catch (EntityNotFoundException ex){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Email does not exist");
            response.getWriter().flush();
        } catch (JWTVerificationException ex){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Invalid JWT");
            response.getWriter().flush();
        } catch (RuntimeException ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("BAD REQUEST");
            response.getWriter().flush();
        }
    }
}
