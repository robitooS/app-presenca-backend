/**
 * @author higor.robinn on 09/06/2025.
 */

// Em: br.edu.mouralacerda.app.config.FirebaseTokenFilter.java
package br.edu.mouralacerda.app.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class FirebaseTokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        // Se não houver cabeçalho ou não começar com "Bearer ", continua sem autenticar.
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String idToken = header.substring(7); // Remove o "Bearer "

        try {
            // Usa o Firebase Admin SDK para verificar o token
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String uid = decodedToken.getUid();

            // Se o token for válido, informa ao Spring Security que o usuário está autenticado
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    uid, null, new ArrayList<>()); // uid é o "principal" (identificador do usuário)

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            // Se o token for inválido, limpa o contexto de segurança
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido ou expirado.");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
