package br.com.edu.fiap.techchallengelanchonete.infrastructure;

import br.com.edu.fiap.techchallengelanchonete.domain.Pagamento;

public interface IOrdemCompraProcessadaPublisher {
    
    void publica(Pagamento pagamento);

}
