package com.Security.Filters;

import lombok.extern.java.Log;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.springframework.util.StringUtils.hasText;
@Component
@Log
public class JaasStartFilter extends GenericFilterBean
{
    private static final String AUTHORIZATION = "Authorization";
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        if (hasText(bearer))
        {
            if (bearer.startsWith("Bearer "))
                return bearer.substring(7);
        }
        return null;
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest= (HttpServletRequest) request;
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String token=getTokenFromRequest((HttpServletRequest) request);
        if ((authentication==null)&&(token!=null))
            httpServletRequest.login("",token);
        chain.doFilter(request, response);

    }
}
