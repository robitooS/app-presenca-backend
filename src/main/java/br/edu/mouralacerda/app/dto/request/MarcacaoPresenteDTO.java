/**
 * @author higor.robinn on 08/06/2025.
 */

package br.edu.mouralacerda.app.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MarcacaoPresenteDTO {
    @NotBlank(message = "O RA do aluno é obrigatório.")
    private String raAluno;

    @NotBlank(message = "O UUID da sessão é obrigatório.")
    private String uuidSessao;
}
