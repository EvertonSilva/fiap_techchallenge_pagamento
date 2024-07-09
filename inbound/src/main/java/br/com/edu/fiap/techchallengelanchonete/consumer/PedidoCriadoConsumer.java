package br.com.edu.fiap.techchallengelanchonete.consumer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.DeliverCallback;

import br.com.edu.fiap.techchallengelanchonete.adapter.PedidoAdapter;
import br.com.edu.fiap.techchallengelanchonete.domain.OrdemCompra;
import br.com.edu.fiap.techchallengelanchonete.dto.PedidoDTO;
import br.com.edu.fiap.techchallengelanchonete.exception.ApplicationException;
import br.com.edu.fiap.techchallengelanchonete.infrastructure.IPedidoCriadoConsumer;
import br.com.edu.fiap.techchallengelanchonete.messaging.RabbitMqActor;
import br.com.edu.fiap.techchallengelanchonete.messaging.RabbitMqConnFactory;

@Component
public class PedidoCriadoConsumer extends RabbitMqActor implements IPedidoCriadoConsumer {

    private String nomeFilaPedidoCriado;
    private PedidoAdapter pedidoAdapter;

    public PedidoCriadoConsumer(RabbitMqConnFactory rabbitMqConnFactory, PedidoAdapter pedidoAdapter,
    @Value("${messaging.fila-pedido-criado}") String nomeFilaPedidoCriado) throws IOException {
        super(rabbitMqConnFactory);
        this.pedidoAdapter = pedidoAdapter;
        this.nomeFilaPedidoCriado = nomeFilaPedidoCriado;
    }

    @Override
    public void consome(Consumer<OrdemCompra> consumidorMensagem) {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            try {
                String mensagem = new String(delivery.getBody(), StandardCharsets.UTF_8);
    
                var objectMapper = new ObjectMapper();
                var pedidoDTO = objectMapper.readValue(mensagem, PedidoDTO.class);
                var ordemCompra = pedidoAdapter.toDomain(pedidoDTO);
    
                consumidorMensagem.accept(ordemCompra);
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
            catch (Exception ex) {
                channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
                throw new ApplicationException("Erro ao processar mensagem da fila " + nomeFilaPedidoCriado, ex);
            }
        };

        try {
            channel.basicConsume(nomeFilaPedidoCriado, false, deliverCallback, consumerTag -> {});
        }
        catch (Exception ex) {
            throw new ApplicationException("Erro ao consumir a fila " + nomeFilaPedidoCriado, ex);
        }
    }

}
