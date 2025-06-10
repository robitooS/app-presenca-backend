package br.edu.mouralacerda.app.service;

import br.edu.mouralacerda.app.exception.RecursoNaoEncontradoException;
import br.edu.mouralacerda.app.model.Usuario;
import br.edu.mouralacerda.app.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Altera o status de um usuário para APROVADO.
     * @param usuarioId O ID do usuário a ser aprovado.
     * @return O usuário com o status atualizado.
     */
    @Transactional
    public Usuario aprovarUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com ID: " + usuarioId));

        if (usuario.getStatus() != Usuario.StatusUsuario.PENDENTE) {
            throw new IllegalStateException("O usuário não pode ser aprovado, pois não está com status PENDENTE.");
        }

        usuario.setStatus(Usuario.StatusUsuario.APROVADO);
        return usuarioRepository.save(usuario);
    }

    /**
     * Realiza um "soft delete" do usuário, alterando seu status para EXCLUIDO.
     * @param usuarioId O ID do usuário a ser excluído.
     * @return O usuário com o status atualizado.
     */
    @Transactional
    public Usuario excluirUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com ID: " + usuarioId));

        // Para não quebrar constraints de chave estrangeira, apenas mudamos o status.
        // Em um cenário real, você poderia também anonimizar os dados do usuário aqui.
        usuario.setStatus(Usuario.StatusUsuario.EXCLUIDO);
        return usuarioRepository.save(usuario);
    }

    /**
     * Lista todos os usuários de um determinado tipo que estão com status PENDENTE.
     * @param tipoUsuario O tipo de usuário (ALUNO ou PROFESSOR) a ser listado.
     * @return Uma lista de usuários pendentes.
     */
    @Transactional(readOnly = true)
    public List<Usuario> listarUsuariosPendentes(Usuario.TipoUsuario tipoUsuario) {
        return usuarioRepository.findByTipoUsuarioAndStatus(tipoUsuario, Usuario.StatusUsuario.PENDENTE);
    }
}