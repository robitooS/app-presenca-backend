/**
 * @author higor.robinn on 08/06/2025.
 */

package br.edu.mouralacerda.app.repository;

import br.edu.mouralacerda.app.model.Aluno;
import br.edu.mouralacerda.app.model.RegistroPresenca;
import br.edu.mouralacerda.app.model.SessaoPresenca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistroPresencaRepository extends JpaRepository<RegistroPresenca, Long> {

    Optional<RegistroPresenca> findByAlunoAndSessaoPresenca(Aluno aluno, SessaoPresenca sessaoPresenca);
    List<RegistroPresenca> findAllBySessaoPresenca(SessaoPresenca sessaoPresenca);
}
