package br.com.edu.fiap.techchallengelanchonete.infrastructure.ordemCompra;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import br.com.edu.fiap.techchallengelanchonete.adapter.dto.PagamentoAdapterDTO;
import br.com.edu.fiap.techchallengelanchonete.domain.Pagamento;
import br.com.edu.fiap.techchallengelanchonete.exception.ApplicationException;
import br.com.edu.fiap.techchallengelanchonete.infrastructure.IOrdemCompraProcessadaPublisher;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class OrdemCompraProcessadaPublisher implements IOrdemCompraProcessadaPublisher {
    
    private RabbitTemplate rabbitTemplate;
    private String nomeFilaPagamentoProcessado;
    private PagamentoAdapterDTO pagamentoAdapterDTO;

    public OrdemCompraProcessadaPublisher(RabbitTemplate rabbitTemplate, PagamentoAdapterDTO pagamentoAdapterDTO,
        @Value("${messaging.fila-pagamento-processado}") String nomeFilaPagamentoProcessado) {
        this.rabbitTemplate = rabbitTemplate;
        this.pagamentoAdapterDTO = pagamentoAdapterDTO;
        this.nomeFilaPagamentoProcessado = nomeFilaPagamentoProcessado;
    }

    @Override
    public void publica(Pagamento pagamento) {
        try {
            var pagamentoDTO = this.pagamentoAdapterDTO.toDTO(pagamento);
            
            var objectMapper = new ObjectMapper();
            var mensagem = objectMapper.writeValueAsString(pagamentoDTO);
            rabbitTemplate.convertAndSend(nomeFilaPagamentoProcessado, "", mensagem);
        } catch (Exception ex) {
            throw new ApplicationException("Erro ao publicar na fila " + nomeFilaPagamentoProcessado, ex);
        }
    }

}
