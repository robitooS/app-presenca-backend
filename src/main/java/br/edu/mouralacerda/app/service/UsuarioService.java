package br.edu.mouralacerda.app.service;

import br.edu.mouralacerda.app.dto.response.ProfessorDTO;
import br.edu.mouralacerda.app.exception.RecursoNaoEncontradoException;
import br.edu.mouralacerda.app.model.Professor; // Importe a entidade Professor
import br.edu.mouralacerda.app.model.Usuario;
import br.edu.mouralacerda.app.repository.ProfessorRepository; // Importe o ProfessorRepository
import br.edu.mouralacerda.app.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // 1. INJEÇÃO DO REPOSITÓRIO DE PROFESSOR
    @Autowired
    private ProfessorRepository professorRepository;

    // ... (seus outros métodos como aprovarUsuario, excluirUsuario, etc. continuam aqui sem alteração)
    @Transactional
    public Usuario aprovarUsuario(String uid) {
        Usuario usuario = usuarioRepository.findByFirebaseUid(uid)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com ID: " + uid));

        if (usuario.getStatus() != Usuario.StatusUsuario.PENDENTE) {
            throw new IllegalStateException("O usuário não pode ser aprovado, pois não está com status PENDENTE.");
        }

        usuario.setStatus(Usuario.StatusUsuario.APROVADO);
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario excluirUsuario(String uid) {
        Usuario usuario = usuarioRepository.findByFirebaseUid(uid)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com ID: " + uid));
        usuario.setStatus(Usuario.StatusUsuario.EXCLUIDO);
        return usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarUsuariosPendentes(Usuario.TipoUsuario tipoUsuario) {
        return usuarioRepository.findByTipoUsuarioAndStatus(tipoUsuario, Usuario.StatusUsuario.PENDENTE);
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorFirebaseUid(String firebaseUid) {
        return usuarioRepository.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com Firebase UID: " + firebaseUid));
    }


    /**
     * Busca todos os professores com status APROVADO e os converte para uma lista de DTOs.
     * @return Uma lista de ProfessorDTO contendo os professores ativos.
     */
    @Transactional(readOnly = true)
    public List<ProfessorDTO> buscarProfessoresAtivos() {
        List<Professor> professoresAtivos = professorRepository.findByStatus(Usuario.StatusUsuario.APROVADO);
        return professoresAtivos.stream()
                .map(ProfessorDTO::new) // Para cada objeto Professor na lista, chama new ProfessorDTO(professor)
                .collect(Collectors.toList());
    }}
