/**
 * @author higor.robinn on 08/06/2025.
 */

package br.edu.mouralacerda.app.dto.response;

import br.edu.mouralacerda.app.model.Usuario;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UsuarioDTO {
    private Long id;
    private String nome;
    private String email;
    private Usuario.TipoUsuario tipoUsuario;
    private Usuario.StatusUsuario status;
    private LocalDateTime dataCriacao;

    public UsuarioDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.tipoUsuario = usuario.getTipoUsuario();
        this.status = usuario.getStatus();
        this.dataCriacao = usuario.getDataCriacao();
    }
}