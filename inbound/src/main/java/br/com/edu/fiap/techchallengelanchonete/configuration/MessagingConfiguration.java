package br.com.edu.fiap.techchallengelanchonete.configuration;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.rabbitmq.client.BuiltinExchangeType;

import br.com.edu.fiap.techchallengelanchonete.messaging.RabbitMqActor;
import br.com.edu.fiap.techchallengelanchonete.messaging.RabbitMqConnFactory;

@Configuration
public class MessagingConfiguration extends RabbitMqActor {

    protected MessagingConfiguration(RabbitMqConnFactory rabbitMqConnFactory,
        @Value("${messaging.fila-pagamento-processado}") String filaPagamentoProcessado, 
        @Value("{$messaging.lancador-pagamento-processado}") String lancadorPagamentoProcessado) throws IOException {
            
        super(rabbitMqConnFactory);
        super.configuraFila(filaPagamentoProcessado, lancadorPagamentoProcessado, BuiltinExchangeType.DIRECT);
    }

}
