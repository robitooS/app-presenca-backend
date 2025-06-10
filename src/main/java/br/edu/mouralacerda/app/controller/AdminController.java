/**
 * @author higor.robinn on 08/06/2025.
 */

package br.edu.mouralacerda.app.controller;

import br.edu.mouralacerda.app.dto.response.UsuarioDTO;
import br.edu.mouralacerda.app.model.Usuario;
import br.edu.mouralacerda.app.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ADMIN_MASTER')") // Apenas o ADMIN_MASTER pode acessar estes endpoints
public class AdminController {

    @Autowired
    private UsuarioService usuarioService;

    // Conforme diagrama: "Aprovar profs pendentes"
    @PostMapping("/professores/{id}/aprovar")
    public ResponseEntity<UsuarioDTO> aprovarProfessor(@PathVariable Long id) {
        Usuario usuarioAprovado = usuarioService.aprovarUsuario(id);
        return ResponseEntity.ok(new UsuarioDTO(usuarioAprovado));
    }

    // Conforme diagrama: "Excluir profs"
    @DeleteMapping("/professores/{id}")
    public ResponseEntity<UsuarioDTO> excluirProfessor(@PathVariable Long id) {
        Usuario usuarioExcluido = usuarioService.excluirUsuario(id);
        return ResponseEntity.ok(new UsuarioDTO(usuarioExcluido));
    }

    @GetMapping("/professores/pendentes")
    public ResponseEntity<List<UsuarioDTO>> listarProfessoresPendentes() {
        List<Usuario> pendentes = usuarioService.listarUsuariosPendentes(Usuario.TipoUsuario.PROFESSOR);
        List<UsuarioDTO> dtos = pendentes.stream().map(UsuarioDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}