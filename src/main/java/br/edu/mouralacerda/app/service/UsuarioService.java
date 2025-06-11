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
     * @param id O UID do usuário a ser aprovado.
     * @return O usuário com o status atualizado.
     */
    @Transactional
    public Usuario aprovarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com ID: " + id));

        if (usuario.getStatus() != Usuario.StatusUsuario.PENDENTE) {
            throw new IllegalStateException("O usuário não pode ser aprovado, pois não está com status PENDENTE.");
        }

        usuario.setStatus(Usuario.StatusUsuario.APROVADO);
        return usuarioRepository.save(usuario);
    }

    /**
     * Realiza um "soft delete" do usuário, alterando seu status para EXCLUIDO.
     * @param uid O ID do usuário a ser excluído.
     * @return O usuário com o status atualizado.
     */
    @Transactional
    public Usuario excluirUsuario(String uid) {
        Usuario usuario = usuarioRepository.findByFirebaseUid(uid)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com ID: " + uid));

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

    /**
     * Busca um usuário pelo seu Firebase UID.
     * Essencial para verificar se um usuário autenticado no Firebase já existe no banco de dados local.
     * @param firebaseUid O UID fornecido pelo Firebase Authentication.
     * @return O objeto Usuario correspondente.
     * @throws RecursoNaoEncontradoException se o usuário não for encontrado.
     */
    @Transactional(readOnly = true)
    public Usuario buscarPorFirebaseUid(String firebaseUid) {
        return usuarioRepository.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com Firebase UID: " + firebaseUid));
    }
}