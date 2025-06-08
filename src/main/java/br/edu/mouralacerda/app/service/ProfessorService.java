/**
 * @author higor.robinn on 08/06/2025.
 */

package br.edu.mouralacerda.app.service;

import br.edu.mouralacerda.app.dto.request.ProfessorRegistroDTO;
import br.edu.mouralacerda.app.exception.RecursoNaoEncontradoException;
import br.edu.mouralacerda.app.model.Professor;
import br.edu.mouralacerda.app.model.Usuario;
import br.edu.mouralacerda.app.repository.ProfessorRepository;
import br.edu.mouralacerda.app.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public Professor registrarProfessor(ProfessorRegistroDTO professorDTO) {
        // Validação: Verifica se o email já está em uso por qualquer tipo de usuário
        usuarioRepository.findByEmail(professorDTO.getEmail()).ifPresent(u -> {
            throw new IllegalStateException("O e-mail '" + professorDTO.getEmail() + "' já está em uso.");
        });

        // Converte o DTO para a entidade Professor
        Professor professor = new Professor();
        professor.setFirebaseUid(professorDTO.getFirebaseUid());
        professor.setEmail(professorDTO.getEmail());
        professor.setNome(professorDTO.getNome());
        professor.setCurso(professorDTO.getCurso());
        professor.setTipoUsuario(Usuario.TipoUsuario.PROFESSOR);
        professor.setStatus(Usuario.StatusUsuario.PENDENTE); // Status inicial

        return professorRepository.save(professor);
    }

    @Transactional(readOnly = true)
    public Professor buscarPorId(Long id) {
        return professorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Professor não encontrado com o ID: " + id));
    }
}
