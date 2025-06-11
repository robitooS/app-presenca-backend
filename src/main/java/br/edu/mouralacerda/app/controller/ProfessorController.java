package br.edu.mouralacerda.app.controller;

import br.edu.mouralacerda.app.dto.request.ProfessorRegistroDTO;
import br.edu.mouralacerda.app.dto.response.ProfessorDTO;
import br.edu.mouralacerda.app.dto.response.RegistroPresencaDTO;
import br.edu.mouralacerda.app.dto.response.SessaoPresencaDTO;
import br.edu.mouralacerda.app.dto.response.UsuarioDTO;
import br.edu.mouralacerda.app.model.Professor;
import br.edu.mouralacerda.app.model.SessaoPresenca;
import br.edu.mouralacerda.app.model.Usuario;
import br.edu.mouralacerda.app.service.PresencaService;
import br.edu.mouralacerda.app.service.ProfessorService;
import br.edu.mouralacerda.app.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/professores")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private PresencaService presencaService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registrar")
    public ResponseEntity<ProfessorDTO> registrarProfessor(@Valid @RequestBody ProfessorRegistroDTO professorDTO) {
        Professor professorSalvo = professorService.registrarProfessor(professorDTO);
        ProfessorDTO responseDTO = new ProfessorDTO(professorSalvo);
        URI location = URI.create("/api/professores/" + professorSalvo.getId());
        return ResponseEntity.created(location).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorDTO> getProfessor(@PathVariable Long id) {
        Professor professor = professorService.buscarPorId(id);
        return ResponseEntity.ok(new ProfessorDTO(professor));
    }

    /**
     * Cria uma nova sessão de presença (gera o UUID para o QR Code).
     * Mapeado da funcionalidade "Criar QR Code" do diagrama.
     */
    @PostMapping("/{idProfessor}/sessoes")
    @PreAuthorize("hasAuthority('PROFESSOR')")
    public ResponseEntity<SessaoPresencaDTO> criarSessaoDePresenca(
            @PathVariable Long idProfessor,
            @RequestParam(defaultValue = "5") int duracaoMinutos) {
        // Validação futura: garantir que o {idProfessor} corresponde ao professor autenticado.
        SessaoPresenca novaSessao = presencaService.criarSessao(idProfessor, duracaoMinutos);
        SessaoPresencaDTO responseDTO = new SessaoPresencaDTO(novaSessao);
        URI location = URI.create("/api/sessoes/" + novaSessao.getUuid());
        return ResponseEntity.created(location).body(responseDTO);
    }

    /**
     * Aprova o cadastro de um aluno que está com status PENDENTE.
     * Mapeado da funcionalidade "Aprovar alunos pendentes" do diagrama.
     */
    @PostMapping("/alunos/{id}/aprovar")
    @PreAuthorize("hasAuthority('PROFESSOR')")
    // CORREÇÃO: O parâmetro agora é um Long 'id' para corresponder à URL e ao Service.
    public ResponseEntity<UsuarioDTO> aprovarAluno(@PathVariable Long id) {
        // A chamada ao serviço agora passa o ID correto.
        Usuario alunoAprovado = usuarioService.aprovarUsuario(id);
        return ResponseEntity.ok(new UsuarioDTO(alunoAprovado));
    }

    /**
     * Lista todos os alunos que estão aguardando aprovação.
     */
    @GetMapping("/alunos/pendentes")
    @PreAuthorize("hasAuthority('PROFESSOR')")
    public ResponseEntity<List<UsuarioDTO>> listarAlunosPendentes() {
        List<Usuario> pendentes = usuarioService.listarUsuariosPendentes(Usuario.TipoUsuario.ALUNO);
        List<UsuarioDTO> dtos = pendentes.stream().map(UsuarioDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Gera um relatório de presença para uma sessão específica.
     * Mapeado da funcionalidade "Relatório de Faltas" do diagrama.
     */
    @GetMapping("/sessoes/{uuidSessao}/relatorio")
    @PreAuthorize("hasAuthority('PROFESSOR')")
    public ResponseEntity<List<RegistroPresencaDTO>> relatorioDePresenca(@PathVariable String uuidSessao) {
        List<RegistroPresencaDTO> relatorio = presencaService.gerarRelatorioPresenca(uuidSessao);
        return ResponseEntity.ok(relatorio);
    }
}