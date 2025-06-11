package br.edu.mouralacerda.app.service;

import br.edu.mouralacerda.app.dto.response.ProfessorDTO;
import br.edu.mouralacerda.app.exception.RecursoNaoEncontradoException;
import br.edu.mouralacerda.app.model.Usuario;
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

    /**
     * Altera o status de um usuário para APROVADO.
     * @param uid O UID do usuário a ser aprovado.
     * @return O usuário com o status atualizado.
     */
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

    /**
     * Busca todos os professores com status ATIVO e os converte para uma lista de DTOs.
     * @return Uma lista de ProfessorDTO contendo os professores ativos.
     */
    @Transactional(readOnly = true)
    public List<ProfessorDTO> buscarProfessoresAtivos() {
        // 1. Busca no banco todos os professores com status "APROVADO"
        List<Usuario> professoresAtivos = usuarioRepository.findByTipoUsuarioAndStatus(
                Usuario.TipoUsuario.PROFESSOR,
                Usuario.StatusUsuario.APROVADO
        );

        // 2. Converte a lista de Entidades (Usuario) para uma lista de DTOs (ProfessorDTO)
        return professoresAtivos.stream()
                .map(this::converterParaDTO) // Para cada usuário na lista, chama o método de conversão
                .collect(Collectors.toList()); // Coleta os resultados em uma nova lista
    }

    /**
     * Método auxiliar privado para converter uma entidade Usuario em um ProfessorDTO.
     * Isso desacopla a representação do banco de dados da representação que a API expõe.
     *
     * @param usuario A entidade Usuario a ser convertida.
     * @return Um objeto ProfessorDTO preenchido com os dados do usuário.
     */
    private ProfessorDTO converterParaDTO(Usuario usuario) {
        // Cria uma nova instância do DTO
        ProfessorDTO dto = new ProfessorDTO();

        // Mapeia os campos da entidade para o DTO
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());

        // O status no DTO é uma String, então convertemos o Enum para String.
        dto.setStatus(usuario.getStatus());

        // Retorna o DTO preenchido
        return dto;
    }
}