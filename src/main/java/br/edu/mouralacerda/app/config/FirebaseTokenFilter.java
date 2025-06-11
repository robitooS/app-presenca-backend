package br.edu.mouralacerda.app.config;

import br.edu.mouralacerda.app.model.Usuario;
import br.edu.mouralacerda.app.repository.UsuarioRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class FirebaseTokenFilter extends OncePerRequestFilter {

    // 1. INJETAR O REPOSITÓRIO DE USUÁRIO
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String idToken = header.substring(7);

        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String uid = decodedToken.getUid();

            // 2. BUSCAR O USUÁRIO NO BANCO DE DADOS PELO UID
            Optional<Usuario> userOptional = usuarioRepository.findByFirebaseUid(uid);

            if (userOptional.isPresent()) {
                Usuario user = userOptional.get();

                // 3. CRIAR A LISTA DE PERMISSÕES (ROLES) DO USUÁRIO
                List<GrantedAuthority> authorities = new ArrayList<>();
                // A role no Spring Security precisa ser o nome completo do enum
                authorities.add(new SimpleGrantedAuthority(user.getTipoUsuario().name()));

                // 4. CRIAR O TOKEN DE AUTENTICAÇÃO COM AS PERMISSÕES
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        user, // Pode usar o objeto de usuário completo como principal
                        null,
                        authorities // Passa a lista de permissões
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                // Usuário autenticado no Firebase, mas não existe no nosso banco de dados.
                SecurityContextHolder.clearContext();
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuário não registrado no sistema.");
                return;
            }

        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido ou expirado.");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
