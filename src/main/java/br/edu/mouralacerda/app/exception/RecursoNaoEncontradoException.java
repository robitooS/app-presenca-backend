/**
 * @author higor.robinn on 08/06/2025.
 */

package br.edu.mouralacerda.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // Isso já ajuda, mas o Handler global é mais completo.
public class RecursoNaoEncontradoException extends RuntimeException {
    public RecursoNaoEncontradoException(String message) {
        super(message);
    }
}
