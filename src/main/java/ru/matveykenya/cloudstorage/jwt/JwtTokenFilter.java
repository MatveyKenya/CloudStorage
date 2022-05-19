package ru.matveykenya.cloudstorage.jwt;

import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.matveykenya.cloudstorage.repository.UserRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;

    public JwtTokenFilter(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain)
            throws ServletException, IOException {

        // Get jwt token and validate
        final String token = jwtTokenUtil.resolveToken(request);
        if (token == null || !jwtTokenUtil.validate(token)) {
            System.out.println("doFilterInternal   ---- return");
            chain.doFilter(request, response);
            return;
        }

        // Get user identity and set it on the spring security context
        Authentication auth = jwtTokenUtil.getAuthentication(token);
        System.out.println(auth);
        SecurityContextHolder.getContext().setAuthentication(auth);
        System.out.println("set auth");
        chain.doFilter(request, response);
    }

}


//---------------------------------------------------------------------
//        // Get user identity and set it on the spring security context
//        UserDetails userDetails = userRepo.findUserByUsername(jwtTokenUtil.getUsername(token));
//
//        UsernamePasswordAuthenticationToken
//                authentication = new UsernamePasswordAuthenticationToken(
//                userDetails, null,
//                userDetails == null ?
//                        List.of() : userDetails.getAuthorities()
//        );
//
//        authentication.setDetails(
//                new WebAuthenticationDetailsSource().buildDetails(request)
//        );