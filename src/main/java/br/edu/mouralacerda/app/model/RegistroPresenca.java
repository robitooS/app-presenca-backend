/**
 * @author higor.robinn on 08/06/2025.
 */

package br.edu.mouralacerda.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroPresenca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataHoraRegistro;

    // Relacionamento: Muitos para Um com Aluno
    // Um aluno pode ter vários registros de presença.
    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    // Relacionamento: Muitos para Um com SessaoPresenca
    // Uma sessão de presença pode ter vários registros.
    @ManyToOne
    @JoinColumn(name = "sessao_presenca_id", nullable = false)
    private SessaoPresenca sessaoPresenca;

    @PrePersist
    protected void onCreate() {
        this.dataHoraRegistro = LocalDateTime.now();
    }
}
