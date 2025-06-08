/**
 * @author higor.robinn on 08/06/2025.
 */

package br.edu.mouralacerda.app.controller;

import br.edu.mouralacerda.app.dto.request.ProfessorRegistroDTO;
import br.edu.mouralacerda.app.dto.response.ProfessorDTO;
import br.edu.mouralacerda.app.dto.response.SessaoPresencaDTO;
import br.edu.mouralacerda.app.model.Professor;
import br.edu.mouralacerda.app.model.SessaoPresenca;
import br.edu.mouralacerda.app.service.PresencaService;
import br.edu.mouralacerda.app.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/professores")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @PostMapping("/registrar")
    public ResponseEntity<ProfessorDTO> registrarProfessor(@RequestBody ProfessorRegistroDTO professorDTO) {
        // 1. Chama o serviço para executar a lógica de negócio
        Professor professorSalvo = professorService.registrarProfessor(professorDTO);

        // 2. Converte a entidade salva para um DTO de resposta
        ProfessorDTO responseDTO = new ProfessorDTO(professorSalvo);

        // 3. Cria a URI para o novo recurso
        URI location = URI.create("/api/professores/" + professorSalvo.getId());

        // 4. Retorna a resposta (201 Created)
        return ResponseEntity.created(location).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorDTO> getProfessor(@PathVariable Long id) {
        // 1. Chama o serviço para buscar o professor
        Professor professor = professorService.buscarPorId(id);

        // 2. Converte para DTO e retorna (200 OK)
        return ResponseEntity.ok(new ProfessorDTO(professor));
    }

    @Autowired
    private PresencaService presencaService;

    @PostMapping("/{idProfessor}/sessoes")
    public ResponseEntity<SessaoPresencaDTO> criarSessaoDePresenca(
            @PathVariable Long idProfessor,
            @RequestParam(defaultValue = "5") int duracaoMinutos) {

        SessaoPresenca novaSessao = presencaService.criarSessao(idProfessor, duracaoMinutos);
        SessaoPresencaDTO responseDTO = new SessaoPresencaDTO(novaSessao);

        URI location = URI.create("/api/sessoes/" + novaSessao.getUuid());

        return ResponseEntity.created(location).body(responseDTO);
    }
}
