/**
 * @author higor.robinn on 08/06/2025.
 */

package br.edu.mouralacerda.app.repository;

import br.edu.mouralacerda.app.model.SessaoPresenca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessaoPresencaRepository extends JpaRepository<SessaoPresenca, Long> {
    Optional<SessaoPresenca> findByUuid(String uuid);
}
