/**
 * @author higor.robinn on 08/06/2025.
 */

package br.edu.mouralacerda.app.dto.response;

import br.edu.mouralacerda.app.model.SessaoPresenca;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SessaoPresencaDTO {

    private Long id;
    private String uuid; // Essencial para o QR Code

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataHoraCriacao;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataHoraExpiracao;

    private Boolean ativo;
    private String nomeProfessor;

    public SessaoPresencaDTO(SessaoPresenca sessao) {
        this.id = sessao.getId();
        this.uuid = sessao.getUuid();
        this.dataHoraCriacao = sessao.getDataHoraCriacao();
        this.dataHoraExpiracao = sessao.getDataHoraExpiracao();
        this.ativo = sessao.getAtivo();
        this.nomeProfessor = sessao.getProfessor().getNome();
    }
}
