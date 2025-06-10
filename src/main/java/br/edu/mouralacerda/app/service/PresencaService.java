// Caminho: robitoos/app-presenca/app-presenca-1130d2251b21483494fadca7a35ba1092cab8047/src/main/java/br/edu/mouralacerda/app/service/PresencaService.java
package br.edu.mouralacerda.app.service;

import br.edu.mouralacerda.app.dto.request.MarcacaoPresenteDTO;
import br.edu.mouralacerda.app.dto.response.RegistroPresencaDTO;
import br.edu.mouralacerda.app.exception.RecursoNaoEncontradoException;
import br.edu.mouralacerda.app.model.*;
import br.edu.mouralacerda.app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional
    public SessaoPresenca criarSessao(Long professorId, int minutosDuracao) {
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Professor não encontrado com ID: " + professorId));

        SessaoPresenca novaSessao = new SessaoPresenca();
        novaSessao.setProfessor(professor);
        novaSessao.setDataHoraExpiracao(LocalDateTime.now().plusMinutes(minutosDuracao));

        return sessaoPresencaRepository.save(novaSessao);
    }

    @Transactional
    public RegistroPresenca registrarPresenca(MarcacaoPresenteDTO marcacaoDTO) {
        Aluno aluno = alunoRepository.findByRa(marcacaoDTO.getRaAluno())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado com RA: " + marcacaoDTO.getRaAluno()));

        SessaoPresenca sessao = sessaoPresencaRepository.findByUuid(marcacaoDTO.getUuidSessao())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sessão de presença inválida ou não encontrada."));

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

        RegistroPresenca novoRegistro = new RegistroPresenca();
        novoRegistro.setAluno(aluno);
        novoRegistro.setSessaoPresenca(sessao);

        return registroPresencaRepository.save(novoRegistro);
    }

    /**
     * Gera um relatório com todos os registros de presença de uma determinada sessão.
     * @param uuidSessao O UUID da sessão.
     * @return Uma lista de DTOs dos registros de presença.
     */
    @Transactional(readOnly = true)
    public List<RegistroPresencaDTO> gerarRelatorioPresenca(String uuidSessao) {
        SessaoPresenca sessao = sessaoPresencaRepository.findByUuid(uuidSessao)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sessão não encontrada com UUID: " + uuidSessao));

        List<RegistroPresenca> registros = registroPresencaRepository.findAllBySessaoPresenca(sessao);
        return registros.stream().map(RegistroPresencaDTO::new).collect(Collectors.toList());
    }

    /**
     * Busca todos os registros de presença de um aluno específico.
     * @param ra O RA do aluno.
     * @return O histórico de presença do aluno.
     */
    @Transactional(readOnly = true)
    public List<RegistroPresencaDTO> historicoDoAluno(String ra) {
        Aluno aluno = alunoRepository.findByRa(ra)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado com RA: " + ra));

        List<RegistroPresenca> registros = registroPresencaRepository.findAllByAluno(aluno);
        return registros.stream().map(RegistroPresencaDTO::new).collect(Collectors.toList());
    }
}