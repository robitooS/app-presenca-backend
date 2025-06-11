/**
 * @author higor.robinn on 08/06/2025.
 */

package br.edu.mouralacerda.app.dto.response;

import br.edu.mouralacerda.app.model.Professor;
import br.edu.mouralacerda.app.model.Usuario;
import lombok.Data;

@Data
public class ProfessorDTO {
    private Long id;
    private String uid;
    private String nome;
    private String email;
    private String curso;
    private Usuario.StatusUsuario status;

    public ProfessorDTO(Professor professor) {
        this.id = professor.getId();
        this.uid = professor.getFirebaseUid();
        this.nome = professor.getNome();
        this.email = professor.getEmail();
        this.curso = professor.getCurso();
        this.status = professor.getStatus();
    }
}
