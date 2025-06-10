// Caminho: robitoos/app-presenca/app-presenca-1130d2251b21483494fadca7a35ba1092cab8047/src/main/java/br/edu/mouralacerda/app/service/AlunoService.java
package br.edu.mouralacerda.app.service;

import br.edu.mouralacerda.app.dto.request.AlunoRegistroDTO;
import br.edu.mouralacerda.app.exception.RecursoNaoEncontradoException;
import br.edu.mouralacerda.app.model.Aluno;
import br.edu.mouralacerda.app.model.Usuario;
import br.edu.mouralacerda.app.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Transactional
    public Aluno registrarAluno(AlunoRegistroDTO alunoDTO) {
        alunoRepository.findByRa(alunoDTO.getRa()).ifPresent(a -> {
            throw new IllegalStateException("Já existe um aluno cadastrado com o RA: " + alunoDTO.getRa());
        });

        Aluno aluno = new Aluno();
        aluno.setFirebaseUid(alunoDTO.getFirebaseUid());
        aluno.setEmail(alunoDTO.getEmail());
        aluno.setNome(alunoDTO.getNome());
        aluno.setCurso(alunoDTO.getCurso());
        aluno.setRa(alunoDTO.getRa());
        aluno.setTipoUsuario(Usuario.TipoUsuario.ALUNO);
        aluno.setStatus(Usuario.StatusUsuario.PENDENTE);

        return alunoRepository.save(aluno);
    }

    @Transactional(readOnly = true)
    public Aluno buscarPorRa(String ra) {
        return alunoRepository.findByRa(ra)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado com o RA: " + ra));
    }
}