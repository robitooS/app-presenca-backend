/**
 * @author higor.robinn on 08/06/2025.
 */

package br.edu.mouralacerda.app.repository;

import br.edu.mouralacerda.app.model.Professor;
import br.edu.mouralacerda.app.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    Optional<Professor> findByEmail(String email);
    Optional<Professor> findByFirebaseUid(String firebaseUid);
    List<Professor> findByStatus(Usuario.StatusUsuario status);

}
