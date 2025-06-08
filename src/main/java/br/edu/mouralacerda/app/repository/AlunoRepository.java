/**
 * @author higor.robinn on 27/05/2025.
 */

package br.edu.mouralacerda.app.repository;

import br.edu.mouralacerda.app.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    Optional<Aluno> findByRa(String ra);
}
