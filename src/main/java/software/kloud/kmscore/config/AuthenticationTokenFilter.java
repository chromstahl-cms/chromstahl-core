package software.kloud.kmscore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import software.kloud.kmscore.persistence.entities.TokenJpaRecord;
import software.kloud.kmscore.persistence.repositories.TokenRepository;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {
    private final TokenRepository tokenRepository;
    private final AuthenticationProvider provider;

    @Autowired
    public AuthenticationTokenFilter(UserDetailsServiceImpl userService, TokenRepository tokenRepository, AuthenticationProvider provider) {
        this.tokenRepository = tokenRepository;
        this.provider = provider;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String header = request.getHeader("Authorization");
        if (null == header) {
            filterChain.doFilter(request, response);
            return;
        }

        final String tokenCode = header.replace("Bearer ", "");
        Optional<TokenJpaRecord> tokenOptional = tokenRepository.findByToken(tokenCode);
        if (!tokenOptional.isPresent()) {
            filterChain.doFilter(request, response);
            return;
        }

        TokenJpaRecord token = tokenOptional.get();
        if (new Date().after(token.getExpiryDate())) {
            tokenRepository.delete(token);
            filterChain.doFilter(request, response);
            return;
        }

        var user = token.getUser();
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(provider.authenticate(authentication));
        // continue through the filter chain
        filterChain.doFilter(request, response);
    }
}

