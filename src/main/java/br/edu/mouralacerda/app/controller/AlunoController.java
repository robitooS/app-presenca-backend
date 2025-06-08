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
import br.edu.mouralacerda.app.service.AlunoService; // Importe o serviço
import br.edu.mouralacerda.app.service.PresencaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students") // Mantive o nome original que você usou
public class AlunoController {
    @Autowired
    private PresencaService presencaService;
    @Autowired
    private AlunoService alunoService; // Injetamos o Service em vez do Repository

    @PostMapping("/create")
    public ResponseEntity<AlunoDTO> createAluno(@RequestBody AlunoRegistroDTO alunoDTO) { // Recebe DTO
        Aluno novoAluno = alunoService.registrarAluno(alunoDTO);
        // Retorna um DTO de resposta, com status 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(new AlunoDTO(novoAluno));
    }

    @GetMapping("/{ra}")
    public ResponseEntity<AlunoDTO> getAluno(@PathVariable String ra) { // Retorna DTO
        Aluno aluno = alunoService.buscarPorRa(ra);
        return ResponseEntity.ok(new AlunoDTO(aluno));
    }

    @PostMapping("/presenca")
    public ResponseEntity<RegistroPresencaDTO> marcarPresenca(@RequestBody MarcacaoPresenteDTO marcacaoDTO) {
        RegistroPresenca registroSalvo = presencaService.registrarPresenca(marcacaoDTO);
        return ResponseEntity.ok(new RegistroPresencaDTO(registroSalvo));
    }
}