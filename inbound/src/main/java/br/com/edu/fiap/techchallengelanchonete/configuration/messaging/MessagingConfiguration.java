package br.com.edu.fiap.techchallengelanchonete.configuration.messaging;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfiguration {

    @Bean
    public Exchange lancadorPedidoCriado(@Value("${messaging.lancador-pedido-criado}") String lancadorPedidoCriado) {
        return new FanoutExchange(lancadorPedidoCriado);
    }

    @Bean
    public Queue filaPedidoCriado(@Value("${messaging.fila-pedido-criado}") String filaPedidoCriado) {
        return new Queue(filaPedidoCriado);
    }

    @Bean
    public Binding bindingPedidoCriado(Queue filaPedidoCriado, FanoutExchange lancadorPedidoCriado) {
        return BindingBuilder.bind(filaPedidoCriado).to(lancadorPedidoCriado);
    }

    @Bean
    public Exchange lancadorPagamentoProcessado(@Value("{$messaging.lancador-pagamento-processado}") String lancadorPagamentoProcessado) {
        return new DirectExchange(lancadorPagamentoProcessado);
    }

    @Bean
    public Queue filaPagamentoProcessado(@Value("${messaging.fila-pagamento-processado}") String filaPagamentoProcessado) {
        return new Queue(filaPagamentoProcessado);
    }

    @Bean
    public Binding bindingPagamentoProcessado(Queue filaPagamentoProcessado, DirectExchange lancadorPagamentoProcessado) {
        return BindingBuilder.bind(filaPagamentoProcessado).to(lancadorPagamentoProcessado).withQueueName();
    }

}
