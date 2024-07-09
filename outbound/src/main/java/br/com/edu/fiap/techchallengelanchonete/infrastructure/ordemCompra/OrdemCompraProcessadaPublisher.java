package br.com.edu.fiap.techchallengelanchonete.infrastructure.ordemCompra;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.edu.fiap.techchallengelanchonete.adapter.dto.PagamentoAdapterDTO;
import br.com.edu.fiap.techchallengelanchonete.domain.Pagamento;
import br.com.edu.fiap.techchallengelanchonete.exception.ApplicationException;
import br.com.edu.fiap.techchallengelanchonete.infrastructure.IOrdemCompraProcessadaPublisher;
import br.com.edu.fiap.techchallengelanchonete.messaging.RabbitMqActor;
import br.com.edu.fiap.techchallengelanchonete.messaging.RabbitMqConnFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class OrdemCompraProcessadaPublisher extends RabbitMqActor implements IOrdemCompraProcessadaPublisher {
    
    private String nomeFilaPagamentoProcessado;
    private PagamentoAdapterDTO pagamentoAdapterDTO;

    public OrdemCompraProcessadaPublisher(RabbitMqConnFactory rabbitMqConnFactory, PagamentoAdapterDTO pagamentoAdapterDTO,
        @Value("${messaging.fila-pedido-criado}") String nomeFilaPagamentoProcessado) throws IOException {
        super(rabbitMqConnFactory);
        this.pagamentoAdapterDTO = pagamentoAdapterDTO;
        this.nomeFilaPagamentoProcessado = nomeFilaPagamentoProcessado;
    }

    @Override
    public void publica(Pagamento pagamento) {
        try {
            var pagamentoDTO = this.pagamentoAdapterDTO.toDTO(pagamento);
            
            var objectMapper = new ObjectMapper();
            var mensagem = objectMapper.writeValueAsString(pagamentoDTO);
            channel.basicPublish("", nomeFilaPagamentoProcessado, null, mensagem.getBytes());
        } catch (Exception ex) {
            throw new ApplicationException("Erro ao publicar na fila " + nomeFilaPagamentoProcessado, ex);
        }
    }

}
