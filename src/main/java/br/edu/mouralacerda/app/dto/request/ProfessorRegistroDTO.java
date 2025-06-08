/**
 * @author higor.robinn on 08/06/2025.
 */

package br.edu.mouralacerda.app.dto.request;

import lombok.Data;

@Data
public class ProfessorRegistroDTO {
    private String firebaseUid;
    private String email;
    private String nome;
    private String curso;
}
