package br.com.edu.fiap.techchallengelanchonete.messaging;

import java.io.IOException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

public abstract class RabbitMqActor {
    
    protected Channel channel;

    protected RabbitMqActor(RabbitMqConnFactory rabbitMqConnFactory) throws IOException {
        var connection = rabbitMqConnFactory.getConnection();
        if (channel == null || !channel.isOpen()) {
            channel = connection.createChannel();
        }
    }

    protected void declaraFila(String nomeFila) throws IOException {
        channel.queueDeclare(nomeFila, true, false, false, null);
    }

    protected void declaraLancador(String nomeLancador, BuiltinExchangeType tipoLancador) throws IOException {
        channel.exchangeDeclare(nomeLancador, tipoLancador, true, false, null);
    }

    protected void relacionaFilaLancador(String nomeFila, String nomeLancador) throws IOException {
        channel.queueBind(nomeFila, nomeLancador, "");
    }

    protected void configuraFila(String nomeFila, String nomeLancador, BuiltinExchangeType tipoLancador) throws IOException {
        this.declaraFila(nomeFila);
        this.declaraLancador(nomeLancador, tipoLancador);
        this.relacionaFilaLancador(nomeFila, nomeLancador);
    }

}
