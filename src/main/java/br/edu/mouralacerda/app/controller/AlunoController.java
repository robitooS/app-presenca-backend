/**
 * @author higor.robinn on 27/05/2025.
 */

package br.edu.mouralacerda.app.controller;

import br.edu.mouralacerda.app.dto.request.MarcacaoPresenteDTO;
import br.edu.mouralacerda.app.dto.response.RegistroPresencaDTO;
import br.edu.mouralacerda.app.model.Aluno;
import br.edu.mouralacerda.app.model.RegistroPresenca;
import br.edu.mouralacerda.app.service.AlunoService; // Importe o serviço
import br.edu.mouralacerda.app.service.PresencaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students") // Mantive o nome original que você usou
public class AlunoController {
    @Autowired
    private PresencaService presencaService;
    @Autowired
    private AlunoService alunoService; // Injetamos o Service em vez do Repository

    @GetMapping("/{ra}")
    public ResponseEntity<Aluno> getAluno(@PathVariable String ra) {
        // A lógica de busca agora está no serviço
        Aluno aluno = alunoService.buscarPorRa(ra);
        return ResponseEntity.ok(aluno);
    }

    @PostMapping("/create")
    public ResponseEntity<Aluno> createAluno(@RequestBody Aluno aluno) {
        // A lógica de criação e validação está no serviço
        Aluno novoAluno = alunoService.registrarAluno(aluno);
        // Futuramente, vamos retornar um DTO aqui
        return ResponseEntity.ok(novoAluno);
    }
    @PostMapping("/presenca")
    public ResponseEntity<RegistroPresencaDTO> marcarPresenca(@RequestBody MarcacaoPresenteDTO marcacaoDTO) {
        RegistroPresenca registroSalvo = presencaService.registrarPresenca(marcacaoDTO);
        return ResponseEntity.ok(new RegistroPresencaDTO(registroSalvo));
    }
}