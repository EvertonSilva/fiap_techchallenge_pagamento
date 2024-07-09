package br.com.edu.fiap.techchallengelanchonete.configuration;

import com.mercadopago.MercadoPagoConfig;

import br.com.edu.fiap.techchallengelanchonete.infrastructure.IPedidoCriadoConsumer;
import br.com.edu.fiap.techchallengelanchonete.usecase.PagamentoUseCase;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

import javax.annotation.PostConstruct;

@Configuration
public class StartupConfiguration {

    @Value( "${mercadopago.token}" )
    private String TOKEN_MERCADO_PAGO;

    public StartupConfiguration(IPedidoCriadoConsumer pedidoCriadoConsumer, PagamentoUseCase pagamentoUseCase) {
        pedidoCriadoConsumer.consome(pagamentoUseCase::registraPagamento);
    }

    @PostConstruct
    public void init() throws IOException {
        MercadoPagoConfig.setAccessToken(TOKEN_MERCADO_PAGO);
    }
}
