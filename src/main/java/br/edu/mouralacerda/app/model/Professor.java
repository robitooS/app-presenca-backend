/**
 * @author higor.robinn on 08/06/2025.
 */

package br.edu.mouralacerda.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Professor extends Usuario {
    private String curso;

    @OneToMany(mappedBy = "professor")
    private List<SessaoPresenca> sessoesPresenca;

    // Construtor ajustado para incluir 'curso'
    public Professor(String firebaseUid, String email, String nome, TipoUsuario tipoUsuario, StatusUsuario status, String curso) {
        super(null, firebaseUid, email, nome, tipoUsuario, status, null, null);
        this.curso = curso;
    }
}