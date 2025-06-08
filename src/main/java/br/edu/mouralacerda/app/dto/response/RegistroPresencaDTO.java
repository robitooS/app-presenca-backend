/**
 * @author higor.robinn on 08/06/2025.
 */

package br.edu.mouralacerda.app.dto.response;

import br.edu.mouralacerda.app.model.RegistroPresenca;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RegistroPresencaDTO {

    private Long id;
    private String nomeAluno;
    private String raAluno;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataHoraRegistro;
    private String nomeProfessor;

    public RegistroPresencaDTO(RegistroPresenca registro) {
        this.id = registro.getId();
        this.nomeAluno = registro.getAluno().getNome();
        this.raAluno = registro.getAluno().getRa();
        this.dataHoraRegistro = registro.getDataHoraRegistro();
        this.nomeProfessor = registro.getSessaoPresenca().getProfessor().getNome();
    }
}
