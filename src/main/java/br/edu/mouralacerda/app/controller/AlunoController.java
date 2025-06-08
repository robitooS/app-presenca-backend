/**
 * @author higor.robinn on 27/05/2025.
 */

package br.edu.mouralacerda.app.controller;

import br.edu.mouralacerda.app.model.Aluno;
import br.edu.mouralacerda.app.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/students")
public class AlunoController {

    @Autowired
    AlunoRepository alunoRepository;

    @GetMapping("/{ra}")
    public ResponseEntity<Aluno> getAluno(@PathVariable String ra) throws Exception {
        Aluno aluno = alunoRepository.findByRa(ra).orElseThrow(RuntimeException::new);

        return ResponseEntity.ok(aluno);
    }

    @PostMapping("/create")
    public ResponseEntity<Aluno> createAluno(@RequestBody Aluno aluno){
        Aluno novoAluno = alunoRepository.save(aluno);

        return ResponseEntity.ok(novoAluno);
    }
}
