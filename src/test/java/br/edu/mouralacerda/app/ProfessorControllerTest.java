/**
 * @author higor.robinn on 08/06/2025.
 */

package br.edu.mouralacerda.app;

import br.edu.mouralacerda.app.repository.ProfessorRepository;
import br.edu.mouralacerda.app.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProfessorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Limpa os repositórios antes de cada teste
    @BeforeEach
    void setUp() {
        // A ordem é importante para evitar erros de chave estrangeira
        professorRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    @Test
    void registrarProfessor_comDadosValidos_deveRetornarStatusCreated() throws Exception {
        String professorJson = "{\"firebaseUid\": \"prof-uid-123\", \"email\": \"professor.teste@email.com\", \"nome\": \"Professor Teste\", \"curso\": \"Engenharia\"}";

        mockMvc.perform(post("/api/professores/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(professorJson))
                .andExpect(status().isCreated()) // Espera 201 Created
                .andExpect(jsonPath("$.nome").value("Professor Teste"))
                .andExpect(jsonPath("$.email").value("professor.teste@email.com"))
                .andExpect(jsonPath("$.status").value("PENDENTE"));
    }

    @Test
    void registrarProfessor_comEmailDuplicado_deveRetornarStatusConflict() throws Exception {
        // Primeiro, registra um professor
        String professorExistenteJson = "{\"firebaseUid\": \"prof-uid-123\", \"email\": \"professor.duplicado@email.com\", \"nome\": \"Professor Existente\", \"curso\": \"Engenharia\"}";
        mockMvc.perform(post("/api/professores/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(professorExistenteJson));

        // Tenta registrar outro com o mesmo e-mail
        String professorNovoJson = "{\"firebaseUid\": \"prof-uid-456\", \"email\": \"professor.duplicado@email.com\", \"nome\": \"Professor Novo\", \"curso\": \"Arquitetura\"}";

        mockMvc.perform(post("/api/professores/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(professorNovoJson))
                .andExpect(status().isConflict()) // Espera 409 Conflict
                .andExpect(jsonPath("$.message").value("O e-mail 'professor.duplicado@email.com' já está em uso."));
    }

    @Test
    void registrarProfessor_comNomeEmBranco_deveRetornarStatusBadRequest() throws Exception {
        String professorJson = "{\"firebaseUid\": \"prof-uid-789\", \"email\": \"professor.invalido@email.com\", \"nome\": \"\", \"curso\": \"Engenharia\"}";

        mockMvc.perform(post("/api/professores/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(professorJson))
                .andExpect(status().isBadRequest()) // Espera 400 Bad Request
                .andExpect(jsonPath("$.error").value("Validation Error"));
    }
}
