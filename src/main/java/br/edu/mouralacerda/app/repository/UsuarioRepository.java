/**
 * @author higor.robinn on 08/06/2025.
 */

package br.edu.mouralacerda.app.repository;

import br.edu.mouralacerda.app.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByFirebaseUid(String firebaseUid);
}
