/**
 * @author higor.robinn on 10/06/2025.
 */

package br.edu.mouralacerda.app.controller;

import br.edu.mouralacerda.app.dto.response.UsuarioDTO;
import br.edu.mouralacerda.app.model.Usuario;
import br.edu.mouralacerda.app.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Verifica se um usuário existe na base de dados a partir do seu Firebase UID.
     * O app cliente chama este endpoint após um login bem-sucedido no Firebase
     * para obter os dados e a role (permissão) do usuário no sistema.
     *
     * @param firebaseUid O UID único do usuário vindo do Firebase.
     * @return ResponseEntity com o UsuarioDTO em caso de sucesso (200 OK)
     * ou um erro 404 Not Found se o usuário não existir.
     */
    @GetMapping("/check/{firebaseUid}")
    public ResponseEntity<UsuarioDTO> verificarUsuarioPorFirebaseUid(@PathVariable String firebaseUid) {
        Usuario usuario = usuarioService.buscarPorFirebaseUid(firebaseUid);
        // Se o usuário for encontrado, o GlobalExceptionHandler não será acionado.
        // Retornamos os dados do usuário encapsulados em um DTO.
        return ResponseEntity.ok(new UsuarioDTO(usuario));
    }
}