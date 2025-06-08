/**
 * @author higor.robinn on 08/06/2025.
 */

package br.edu.mouralacerda.app.dto.response;

import br.edu.mouralacerda.app.model.Aluno;
import br.edu.mouralacerda.app.model.Usuario;
import lombok.Data;

@Data
public class AlunoDTO {
    // Apenas os dados que você quer expor na resposta da API
    private Long id;
    private String nome;
    private String email;
    private String curso;
    private String ra;
    private Usuario.StatusUsuario status;

    public AlunoDTO(Aluno aluno) {
        this.id = aluno.getId();
        this.nome = aluno.getNome();
        this.email = aluno.getEmail();
        this.curso = aluno.getCurso();
        this.ra = aluno.getRa();
        this.status = aluno.getStatus();
    }
}
