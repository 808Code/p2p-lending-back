package com.bonsai.accountservice.config;

import com.bonsai.sharedservice.dtos.response.ErrorResponse;
import com.google.gson.Gson;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import static com.bonsai.accountservice.constants.SecurityConstant.HEADER_STRING;
import static com.bonsai.accountservice.constants.SecurityConstant.TOKEN_PREFIX;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-06-11
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String bearerToken = req.getHeader(HEADER_STRING);

        if (bearerToken == null || !bearerToken.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        String token = bearerToken.replace(TOKEN_PREFIX, "");

        //if token has expired send error response
        if (TokenHandler.hasTokenExpired(token)) {

            PrintWriter out = res.getWriter();

            Gson gson = new Gson();
            String tokenExpired = gson.toJson(new ErrorResponse("Access token has expired"));

            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            res.setStatus(400);

            out.print(tokenExpired);
            out.flush();

            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String bearerToken = request.getHeader(HEADER_STRING);
        String token = bearerToken.replace(TOKEN_PREFIX, "");

        String userEmail = TokenHandler.getEmailFromToken(token);

        if (userEmail == null) {
            return null;
        }

        return new UsernamePasswordAuthenticationToken(userEmail, null, new ArrayList<>());
    }
}