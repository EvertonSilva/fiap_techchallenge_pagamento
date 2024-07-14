package br.com.edu.fiap.techchallengelanchonete.consumer;

import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.ChannelCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.impl.AMQImpl.Basic.GetOk;

import br.com.edu.fiap.techchallengelanchonete.adapter.PedidoAdapter;
import br.com.edu.fiap.techchallengelanchonete.configuration.messaging.Mensageria;
import br.com.edu.fiap.techchallengelanchonete.dto.PedidoDTO;
import br.com.edu.fiap.techchallengelanchonete.exception.ApplicationException;
import br.com.edu.fiap.techchallengelanchonete.infrastructure.IPedidoCriadoConsumer;
import br.com.edu.fiap.techchallengelanchonete.usecase.PagamentoUseCase;

@Component
public class PedidoCriadoConsumer implements IPedidoCriadoConsumer {

    private PagamentoUseCase pagamentoUseCase;
    private String nomeFilaPedidoCriado;
    private PedidoAdapter pedidoAdapter;
    private RabbitTemplate rabbitTemplate;

    public PedidoCriadoConsumer(PagamentoUseCase pagamentoUseCase, PedidoAdapter pedidoAdapter,
        @Value("${messaging.fila-pedido-criado}") String nomeFilaPedidoCriado, RabbitTemplate rabbitTemplate) {
        this.pagamentoUseCase = pagamentoUseCase;
        this.pedidoAdapter = pedidoAdapter;
        this.nomeFilaPedidoCriado = nomeFilaPedidoCriado;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    @RabbitListener(queues = "FILA_PEDIDO_CRIADO_MICROSERVICO_PAGAMENTO", ackMode = "MANUAL")
    public void consome(String mensagem, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        try {

            var objectMapper = new ObjectMapper();
            var pedidoDTO = objectMapper.readValue(mensagem, PedidoDTO.class);
            var ordemCompra = pedidoAdapter.toDomain(pedidoDTO);

            this.pagamentoUseCase.registraPagamento(ordemCompra);
        }
        catch (Exception ex) {
            this.rabbitTemplate.execute((channel) -> {
                channel.basicReject(tag, false);
                return null;
            });

            throw new ApplicationException("Erro ao processar mensagem da fila " + nomeFilaPedidoCriado, ex);
        }
    }

}
