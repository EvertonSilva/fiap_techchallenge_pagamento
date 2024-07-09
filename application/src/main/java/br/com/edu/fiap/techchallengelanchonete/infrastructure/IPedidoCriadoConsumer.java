package br.com.edu.fiap.techchallengelanchonete.infrastructure;

import java.util.function.Consumer;

import br.com.edu.fiap.techchallengelanchonete.domain.OrdemCompra;

public interface IPedidoCriadoConsumer {
    
    void consome(Consumer<OrdemCompra> consumidorMensagem);

}
