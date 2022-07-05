package cs.ui.pacilfindgroup.filter;

import cs.ui.pacilfindgroup.exceptions.ApiRequestException;
import cs.ui.pacilfindgroup.exceptions.AuthException;
import cs.ui.pacilfindgroup.exceptions.JWTException;
import cs.ui.pacilfindgroup.exceptions.UsernameNotFoundException;
import cs.ui.pacilfindgroup.services.JwtUserDetailsService;
import cs.ui.pacilfindgroup.util.ExceptionMapper;
import cs.ui.pacilfindgroup.util.JwtUtility;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component("jwtfilter")
@Slf4j
public class JWTRequestFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JWTRequestFilter.class);
    @Autowired
    @Qualifier("jwtuserdetailsservice")
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    @Qualifier("jwtutility")
    private JwtUtility jwtUtility;

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
            throws IOException, ApiRequestException {

        String requestTokenHeader = request.getHeader("Authorization");
        final List<String> whiteListURI = Arrays.asList("/api/v1/auth/register",
                "/api/v1/auth/authenticate", "/favicon.ico");

        String username = null;
        String jwtToken = null;
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            username = this.getUsernameFromToken(jwtToken, response);

        } else if (!whiteListURI.contains(request.getRequestURI())) {
            if (request.getRequestURI().startsWith("/h2-console"))
                LOGGER.warn("Accessing h2-console");
            else
                ExceptionMapper.mapResponse(response, new JWTException("JWT Token does not begin with Bearer String"));
        }

        // Once we get the token validate it.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                this.validate(jwtToken, username, request);
            } catch (final UsernameNotFoundException e) {
                ExceptionMapper.mapResponse(response, new JWTException("JWT Token Invalid"));
            }

        }

        try {
            chain.doFilter(request, response);
        } catch (final Exception e) {
            ExceptionMapper.mapResponse(response, new AuthException(e.getCause().getMessage()));
        }

    }

    private String getUsernameFromToken(final String jwtToken, final HttpServletResponse response) throws IOException {
        String username = null;
        try {
            username = this.jwtUtility.getUsernameFromToken(jwtToken);
        } catch (final IllegalArgumentException e) {
            ExceptionMapper.mapResponse(response, new JWTException("Unable to get JWT Token"));
        } catch (final ExpiredJwtException e) {
            ExceptionMapper.mapResponse(response, new JWTException("JWT Token has expired"));
        } catch (final SignatureException e) {
            ExceptionMapper.mapResponse(response, new JWTException("JWT Token Invalid"));
        }
        return username;
    }

    private void validate(final String jwtToken, final String username, final HttpServletRequest request) {

        final var userDetails = jwtUserDetailsService.loadUserByUsername(username);

        if (Boolean.TRUE.equals(this.jwtUtility.validateToken(jwtToken, userDetails))) {

            final var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
    }


}
