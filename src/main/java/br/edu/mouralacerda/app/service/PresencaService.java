/**
 * @author higor.robinn on 08/06/2025.
 */

package br.edu.mouralacerda.app.service;

import br.edu.mouralacerda.app.dto.request.MarcacaoPresenteDTO;
import br.edu.mouralacerda.app.exception.RecursoNaoEncontradoException;
import br.edu.mouralacerda.app.model.Aluno;
import br.edu.mouralacerda.app.model.Professor;
import br.edu.mouralacerda.app.model.RegistroPresenca;
import br.edu.mouralacerda.app.model.SessaoPresenca;
import br.edu.mouralacerda.app.repository.AlunoRepository;
import br.edu.mouralacerda.app.repository.ProfessorRepository;
import br.edu.mouralacerda.app.repository.RegistroPresencaRepository;
import br.edu.mouralacerda.app.repository.SessaoPresencaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PresencaService {

    @Autowired
    private AlunoRepository alunoRepository;
    @Autowired
    private SessaoPresencaRepository sessaoPresencaRepository;
    @Autowired
    private RegistroPresencaRepository registroPresencaRepository;
    @Autowired
    private ProfessorRepository professorRepository;

    // Adicionaremos mais dependências aqui para o próximo fluxo

    @Transactional
    public SessaoPresenca criarSessao(Long professorId, int minutosDuracao) {
        // 1. Busca o professor que está abrindo a sessão
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Professor não encontrado com ID: " + professorId));

        // 2. Cria a nova sessão
        SessaoPresenca novaSessao = new SessaoPresenca();
        novaSessao.setProfessor(professor);
        novaSessao.setDataHoraExpiracao(LocalDateTime.now().plusMinutes(minutosDuracao));

        // Os outros campos (uuid, dataCriacao, ativo) são preenchidos pelo @PrePersist na entidade

        return sessaoPresencaRepository.save(novaSessao);
    }
    @Transactional
    public RegistroPresenca registrarPresenca(MarcacaoPresenteDTO marcacaoDTO) {
        // 1. Busca o aluno pelo RA
        Aluno aluno = alunoRepository.findByRa(marcacaoDTO.getRaAluno())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado com RA: " + marcacaoDTO.getRaAluno()));

        // 2. Busca a sessão pelo UUID
        SessaoPresenca sessao = sessaoPresencaRepository.findByUuid(marcacaoDTO.getUuidSessao())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sessão de presença inválida ou não encontrada."));

        // 3. Validações de negócio
        if (!sessao.getAtivo()) {
            throw new IllegalStateException("Esta sessão de presença não está mais ativa.");
        }

        if (LocalDateTime.now().isAfter(sessao.getDataHoraExpiracao())) {
            sessao.setAtivo(false);
            sessaoPresencaRepository.save(sessao);
            throw new IllegalStateException("O tempo para marcar presença nesta sessão expirou.");
        }

        registroPresencaRepository.findByAlunoAndSessaoPresenca(aluno, sessao).ifPresent(r -> {
            throw new IllegalStateException("Presença já registrada para este aluno nesta sessão.");
        });

        // 4. Se tudo estiver OK, cria o registro
        RegistroPresenca novoRegistro = new RegistroPresenca();
        novoRegistro.setAluno(aluno);
        novoRegistro.setSessaoPresenca(sessao);

        return registroPresencaRepository.save(novoRegistro);
}
