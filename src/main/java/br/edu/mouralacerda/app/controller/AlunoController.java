/**
 * @author higor.robinn on 27/05/2025.
 */

package br.edu.mouralacerda.app.controller;

import br.edu.mouralacerda.app.dto.request.AlunoRegistroDTO;
import br.edu.mouralacerda.app.dto.request.MarcacaoPresenteDTO;
import br.edu.mouralacerda.app.dto.response.AlunoDTO;
import br.edu.mouralacerda.app.dto.response.RegistroPresencaDTO;
import br.edu.mouralacerda.app.model.Aluno;
import br.edu.mouralacerda.app.model.RegistroPresenca;
import br.edu.mouralacerda.app.service.AlunoService;
import br.edu.mouralacerda.app.service.PresencaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class AlunoController {
    @Autowired
    private PresencaService presencaService;
    @Autowired
    private AlunoService alunoService;

    @PostMapping("/create")
    public ResponseEntity<AlunoDTO> createAluno(@Valid @RequestBody AlunoRegistroDTO alunoDTO) {
        Aluno novoAluno = alunoService.registrarAluno(alunoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AlunoDTO(novoAluno));
    }

    @GetMapping("/{ra}")
    public ResponseEntity<AlunoDTO> getAluno(@PathVariable String ra) {
        Aluno aluno = alunoService.buscarPorRa(ra);
        return ResponseEntity.ok(new AlunoDTO(aluno));
    }

    /**
     * Marca a presença de um aluno em uma sessão ativa via leitura de QR Code.
     * Mapeado da funcionalidade "Marcar presença (Ler QR CODE)" do diagrama.
     */
    @PostMapping("/presenca")
    @PreAuthorize("hasAuthority('ALUNO')")
    public ResponseEntity<RegistroPresencaDTO> marcarPresenca(@Valid @RequestBody MarcacaoPresenteDTO marcacaoDTO) {
        // Validação futura: garantir que o RA do DTO corresponde ao aluno autenticado.
        RegistroPresenca registroSalvo = presencaService.registrarPresenca(marcacaoDTO);
        return ResponseEntity.ok(new RegistroPresencaDTO(registroSalvo));
    }

    /**
     * Permite que o aluno consulte seu próprio histórico de presenças.
     */
    @GetMapping("/{ra}/presencas")
    @PreAuthorize("hasAuthority('ALUNO')")
    public ResponseEntity<List<RegistroPresencaDTO>> meuHistoricoDePresenca(@PathVariable String ra) {
        // Validação futura: garantir que o {ra} corresponde ao aluno autenticado.
        List<RegistroPresencaDTO> historico = presencaService.historicoDoAluno(ra);
        return ResponseEntity.ok(historico);
    }
}