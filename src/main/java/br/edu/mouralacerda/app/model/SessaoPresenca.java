/**
 * @author higor.robinn on 08/06/2025.
 */

package br.edu.mouralacerda.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessaoPresenca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // UUID para ser usado no QR Code, garantindo que o ID primário não seja exposto.
    @Column(unique = true, nullable = false)
    private String uuid;

    @Column(nullable = false)
    private LocalDateTime dataHoraCriacao;

    @Column(nullable = false)
    private LocalDateTime dataHoraExpiracao;

    @Column(nullable = false)
    private Boolean ativo;

    // Relacionamento: Muitos para Um com Professor
    // Um professor pode criar várias sessões de presença.
    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    // Relacionamento: Uma SessaoPresenca tem muitos Registros de Presença
    @OneToMany(mappedBy = "sessaoPresenca")
    private List<RegistroPresenca> registrosPresenca;


    @PrePersist
    protected void onCreate() {
        this.uuid = UUID.randomUUID().toString();
        this.dataHoraCriacao = LocalDateTime.now();
        this.ativo = true;
    }

}
