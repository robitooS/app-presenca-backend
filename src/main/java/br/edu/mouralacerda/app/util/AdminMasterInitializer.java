/**
 * @author higor.robinn on 10/06/2025.
 */

package br.edu.mouralacerda.app.util;

import br.edu.mouralacerda.app.model.Usuario;
import br.edu.mouralacerda.app.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AdminMasterInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // --- IMPORTANTE: SUBSTITUA COM OS DADOS REAIS DO SEU ADMIN ---
    private final String ADMIN_EMAIL = "higorrobin5@gmail.com";
    private final String ADMIN_FIREBASE_UID = "YbfOvISV9ngn1lDIewHkRmY8B4g1";
    private final String ADMIN_NOME = "Administrador Master";

    // ----------------------------------------------------------------

    @Override
    @Transactional // Garante que a operação seja atômica
    public void run(String... args) throws Exception {
        // 1. Verifica se o usuário admin já existe no banco de dados
        if (usuarioRepository.findByEmail(ADMIN_EMAIL).isEmpty()) {

            System.out.println(">>> Usuário ADMIN_MASTER não encontrado. Criando...");

            // 2. Se não existir, cria uma nova instância de Usuario
            Usuario admin = new Usuario();
            admin.setFirebaseUid(ADMIN_FIREBASE_UID);
            admin.setEmail(ADMIN_EMAIL);
            admin.setNome(ADMIN_NOME);

            // 3. Define o tipo e o status do usuário
            admin.setTipoUsuario(Usuario.TipoUsuario.ADMIN_MASTER); //
            admin.setStatus(Usuario.StatusUsuario.APROVADO); //

            // 4. Salva o novo usuário no banco de dados
            usuarioRepository.save(admin);

            System.out.println(">>> Usuário ADMIN_MASTER criado com sucesso!");
        } else {
            System.out.println(">>> Usuário ADMIN_MASTER já existe. Nenhuma ação necessária.");
        }
    }
}
