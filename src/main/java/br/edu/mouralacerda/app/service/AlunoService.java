/**
 * @author higor.robinn on 08/06/2025.
 */

package br.edu.mouralacerda.app.service;

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
    public Aluno registrarAluno(Aluno aluno) {
        // Lógica de negócio: Verificar se já existe um aluno com o mesmo RA
        alunoRepository.findByRa(aluno.getRa()).ifPresent(a -> {
            throw new IllegalStateException("Já existe um aluno cadastrado com o RA: " + aluno.getRa());
        });

        // Define valores padrão antes de salvar
        aluno.setTipoUsuario(Usuario.TipoUsuario.ALUNO);
        aluno.setStatus(Usuario.StatusUsuario.PENDENTE); // Novos alunos podem precisar de aprovação

        return alunoRepository.save(aluno);
    }

    @Transactional(readOnly = true)
    public Aluno buscarPorRa(String ra) {
        return alunoRepository.findByRa(ra)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado com o RA: " + ra));
    }
}
