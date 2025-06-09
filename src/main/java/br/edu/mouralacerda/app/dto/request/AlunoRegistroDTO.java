/**
 * @author higor.robinn on 08/06/2025.
 */

package br.edu.mouralacerda.app.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AlunoRegistroDTO {
    @NotBlank(message = "O firebaseUid é obrigatório.")
    private String firebaseUid;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "O e-mail deve ser válido.")
    private String email;

    @NotBlank(message = "O nome é obrigatório.")
    @Size(min = 3, message = "O nome deve ter no mínimo 3 caracteres.")
    private String nome;

    @NotBlank(message = "O curso é obrigatório.")
    private String curso;

    @NotBlank(message = "O RA é obrigatório.")
    private String ra;
}
