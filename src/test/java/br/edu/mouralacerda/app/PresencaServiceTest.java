/**
 * @author higor.robinn on 08/06/2025.
 */

package br.edu.mouralacerda.app;

import br.edu.mouralacerda.app.dto.request.MarcacaoPresenteDTO;
import br.edu.mouralacerda.app.exception.RecursoNaoEncontradoException;
import br.edu.mouralacerda.app.model.Aluno;
import br.edu.mouralacerda.app.model.Professor;
import br.edu.mouralacerda.app.model.SessaoPresenca;
import br.edu.mouralacerda.app.model.Usuario;
import br.edu.mouralacerda.app.repository.AlunoRepository;
import br.edu.mouralacerda.app.repository.ProfessorRepository;
import br.edu.mouralacerda.app.repository.RegistroPresencaRepository;
import br.edu.mouralacerda.app.service.PresencaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // Garante que cada teste rode em sua própria transação e seja desfeito ao final
class PresencaServiceTest {

    @Autowired
    private PresencaService presencaService;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private RegistroPresencaRepository registroPresencaRepository;

    private Professor professor;
    private Aluno aluno;

    @BeforeEach
    void setUp() {
        // Limpa tudo para garantir um ambiente limpo
        registroPresencaRepository.deleteAll();
        alunoRepository.deleteAll();
        professorRepository.deleteAll();

        // Cria entidades base para os testes
        professor = new Professor("prof-uid", "prof@test.com", "Professor Testador", Usuario.TipoUsuario.PROFESSOR, Usuario.StatusUsuario.APROVADO, "Computação");
        professor = professorRepository.save(professor);

        aluno = new Aluno("aluno-uid", "aluno@test.com", "Aluno Testador", Usuario.TipoUsuario.ALUNO, Usuario.StatusUsuario.APROVADO, "Computação", "123456");
        aluno = alunoRepository.save(aluno);
    }

    @Test
    void criarSessao_deveCriarSessaoAtivaComExpiracaoCorreta() {
        int duracaoMinutos = 10;
        SessaoPresenca sessao = presencaService.criarSessao(professor.getId(), duracaoMinutos);

        assertNotNull(sessao);
        assertNotNull(sessao.getUuid());
        assertTrue(sessao.getAtivo());
        assertEquals(professor.getId(), sessao.getProfessor().getId());
        // Verifica se a expiração é aproximadamente a esperada
        assertTrue(sessao.getDataHoraExpiracao().isAfter(LocalDateTime.now().plusMinutes(duracaoMinutos - 1)));
    }

    @Test
    void registrarPresenca_emSessaoValida_deveSalvarRegistro() {
        SessaoPresenca sessao = presencaService.criarSessao(professor.getId(), 5);

        MarcacaoPresenteDTO marcacao = new MarcacaoPresenteDTO();
        marcacao.setRaAluno(aluno.getRa());
        marcacao.setUuidSessao(sessao.getUuid());

        assertDoesNotThrow(() -> {
            presencaService.registrarPresenca(marcacao);
        });

        assertEquals(1, registroPresencaRepository.count());
    }

    @Test
    void registrarPresenca_emSessaoExpirada_deveLancarExcecao() throws InterruptedException {
        // Cria uma sessão com duração de -1 minuto (já expirada)
        SessaoPresenca sessao = presencaService.criarSessao(professor.getId(), -1);

        MarcacaoPresenteDTO marcacao = new MarcacaoPresenteDTO();
        marcacao.setRaAluno(aluno.getRa());
        marcacao.setUuidSessao(sessao.getUuid());

        // A exceção esperada é IllegalStateException
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            presencaService.registrarPresenca(marcacao);
        });

        assertEquals("O tempo para marcar presença nesta sessão expirou.", exception.getMessage());
    }

    @Test
    void registrarPresenca_duasVezesNaMesmaSessao_deveLancarExcecao() {
        SessaoPresenca sessao = presencaService.criarSessao(professor.getId(), 5);

        MarcacaoPresenteDTO marcacao = new MarcacaoPresenteDTO();
        marcacao.setRaAluno(aluno.getRa());
        marcacao.setUuidSessao(sessao.getUuid());

        // Primeira marcação (sucesso)
        presencaService.registrarPresenca(marcacao);

        // Segunda marcação (deve falhar)
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            presencaService.registrarPresenca(marcacao);
        });

        assertEquals("Presença já registrada para este aluno nesta sessão.", exception.getMessage());
    }

    @Test
    void registrarPresenca_comRaInexistente_deveLancarExcecao() {
        SessaoPresenca sessao = presencaService.criarSessao(professor.getId(), 5);

        MarcacaoPresenteDTO marcacao = new MarcacaoPresenteDTO();
        marcacao.setRaAluno("RA_INEXISTENTE"); // RA que não existe
        marcacao.setUuidSessao(sessao.getUuid());

        RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class, () -> {
            presencaService.registrarPresenca(marcacao);
        });

        assertEquals("Aluno não encontrado com RA: RA_INEXISTENTE", exception.getMessage());
    }
}