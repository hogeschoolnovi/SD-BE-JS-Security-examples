package nl.novi.stuivenberg.springboot.example.security.service.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Now we create AuthEntryPointJwt class that implements AuthenticationEntryPoint interface. Then we override the
 * commence() method. This method will be triggered anytime unauthenticated User requests a secured HTTP resource and an
 * AuthenticationException is thrown.
 */
@Component
public class AuthEntryPointJwt  implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    /**
     * HttpServletResponse.SC_UNAUTHORIZED is the 401 Status code. It indicates that the request requires HTTP
     * authentication.
     * @param request request
     * @param response response
     * @param authException exception
     * @throws IOException exception
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        logger.error("Unauthorized error: {}", authException.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
    }

}
