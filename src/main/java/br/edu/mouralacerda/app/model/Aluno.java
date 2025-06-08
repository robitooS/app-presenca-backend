/**
 * @author higor.robinn on 27/05/2025.
 */

package br.edu.mouralacerda.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Aluno extends Usuario {

    private String curso; // NOVO: Adicionado 'curso'

    @Column(unique = true)
    private String ra;

    @OneToMany(mappedBy = "aluno")
    private List<RegistroPresenca> registrosPresenca;

    // Construtor ajustado para incluir 'curso'
    public Aluno(String firebaseUid, String email, String nome, TipoUsuario tipoUsuario, StatusUsuario status, String curso, String ra) {
        super(null, firebaseUid, email, nome, tipoUsuario, status, null, null);
        this.curso = curso;
        this.ra = ra;
    }
}
