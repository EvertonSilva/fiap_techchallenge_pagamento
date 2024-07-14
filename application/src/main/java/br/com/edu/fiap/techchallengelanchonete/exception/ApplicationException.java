package br.com.edu.fiap.techchallengelanchonete.exception;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ApplicationException extends RuntimeException {
    public ApplicationException (String mensagem) {
        super(mensagem);
    }

    public ApplicationException (String mensagem, Exception ex) {
        super(mensagem);
        log.error(mensagem, ex);
    }
}
