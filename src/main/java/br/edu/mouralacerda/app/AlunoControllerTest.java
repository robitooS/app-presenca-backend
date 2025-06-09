/**
 * @author higor.robinn on 08/06/2025.
 */

package br.edu.mouralacerda.app;

import br.edu.mouralacerda.app.model.Aluno;
import br.edu.mouralacerda.app.repository.AlunoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AlunoControllerTest {

    @Autowired
    private MockMvc mockMvc; // Ferramenta para simular requisições HTTP

    @Autowired
    private ObjectMapper objectMapper; // Ferramenta para converter objetos Java em JSON

    @Autowired
    private AlunoRepository alunoRepository;

    // Limpa o repositório antes de cada teste para evitar interferência
    @BeforeEach
    void setUp() {
        alunoRepository.deleteAll();
    }

    // --- Teste 1: Caminho Feliz (Registro com sucesso) ---
    @Test
    void registrarAluno_comDadosValidos_deveRetornarStatusCreated() throws Exception {
        String alunoJson = "{\"firebaseUid\": \"uid-test-123\", \"email\": \"aluno.teste@email.com\", \"nome\": \"Aluno de Teste\", \"curso\": \"Computacao\", \"ra\": \"12345\"}";

        mockMvc.perform(post("/api/students/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(alunoJson))
                .andExpect(status().isCreated()) // Espera o status 201 Created
                .andExpect(jsonPath("$.nome").value("Aluno de Teste"))
                .andExpect(jsonPath("$.ra").value("12345"));
    }

    // --- Teste 2: Falha de Validação (Campo em branco) ---
    @Test
    void registrarAluno_comRaEmBranco_deveRetornarStatusBadRequest() throws Exception {
        String alunoJson = "{\"firebaseUid\": \"uid-test-456\", \"email\": \"aluno.mal@email.com\", \"nome\": \"Aluno Malicioso\", \"curso\": \"Computacao\", \"ra\": \"\"}";

        mockMvc.perform(post("/api/students/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(alunoJson))
                .andExpect(status().isBadRequest()) // Espera o status 400 Bad Request
                .andExpect(jsonPath("$.message").value("ra: O RA é obrigatório.")); // Verifica a mensagem de erro da validação
    }

    // --- Teste 3: Conflito (RA já existe) ---
    @Test
    void registrarAluno_comRaDuplicado_deveRetornarStatusConflict() throws Exception {
        // Primeiro, insere um aluno no banco
        String alunoExistente = "{\"firebaseUid\": \"uid-existente\", \"email\": \"existente@email.com\", \"nome\": \"Aluno Existente\", \"curso\": \"Direito\", \"ra\": \"54321\"}";
        mockMvc.perform(post("/api/students/create").contentType(MediaType.APPLICATION_JSON).content(alunoExistente));

        // Tenta inserir outro aluno com o mesmo RA
        String alunoDuplicado = "{\"firebaseUid\": \"uid-duplicado\", \"email\": \"duplicado@email.com\", \"nome\": \"Aluno Duplicado\", \"curso\": \"Medicina\", \"ra\": \"54321\"}";

        mockMvc.perform(post("/api/students/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(alunoDuplicado))
                .andExpect(status().isConflict()) // Espera o status 409 Conflict
                .andExpect(jsonPath("$.message").value("Já existe um aluno cadastrado com o RA: 54321"));
    }

    // --- Teste 4: Busca por Recurso Inexistente ---
    @Test
    void buscarAluno_porRaInexistente_deveRetornarStatusNotFound() throws Exception {
        mockMvc.perform(get("/api/students/99999"))
                .andExpect(status().isNotFound()) // Espera o status 404 Not Found
                .andExpect(jsonPath("$.message").value("Aluno não encontrado com o RA: 99999"));
    }
}