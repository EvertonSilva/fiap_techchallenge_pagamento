package br.com.edu.fiap.techchallengelanchonete.integration.mercadopago;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.edu.fiap.techchallengelanchonete.domain.OrdemCompra;
import br.com.edu.fiap.techchallengelanchonete.domain.Pedido;
import br.com.edu.fiap.techchallengelanchonete.domain.StatusPagamento;
import br.com.edu.fiap.techchallengelanchonete.domain.valueobject.Codigo;
import br.com.edu.fiap.techchallengelanchonete.integration.gatewaypagamento.mercadopago.mock.GatewayMercadoPagoRegistradorMock;

class GatewayMercadoPagoRegistradorMockTest {
    
    GatewayMercadoPagoRegistradorMock gatewayMercadoPagoRegistradorMock;

    @BeforeEach
    void setUp() {
        this.gatewayMercadoPagoRegistradorMock = new GatewayMercadoPagoRegistradorMock();
    }

    @Test
    void deveRegistrarPagamentoMercadoPagoMockado() {
        var ordemCompra = new OrdemCompra();
        var pedido = new Pedido();
        var codigoPedido = UUID.randomUUID().toString();
        pedido.setCodigo(new Codigo(codigoPedido));
        ordemCompra.setPedido(pedido);
        
        var pagamento = gatewayMercadoPagoRegistradorMock.registroPagamento(ordemCompra);

        assertThat(pagamento)
            .isNotNull();

        assertAll("Atributos mockados", 
            () -> assertThat(pagamento).isNotNull(),
            () -> assertThat(pagamento.getCodigoPedido()).isNotNull(),
            () -> assertThat(pagamento.getCodigoPedido().getValor()).isNotBlank(),
            () -> assertThat(pagamento.getCodigoPedido().getValor().equals(codigoPedido)),
            () -> assertThat(pagamento.getStatus().equals(StatusPagamento.AGUARDANDO)),
            () -> assertThat(pagamento.getDataExpiracaoPagamento()).isNotNull(),
            () -> assertThat(pagamento.getDataExpiracaoPagamento().getData()).isNotNull(),
            () -> assertThat(pagamento.getPixCopiaECola()).isNotNull(),
            () -> assertThat(pagamento.getPixCopiaECola().getValor()).isNotBlank(),
            () -> assertThat(pagamento.getPixQRCode64()).isNotNull(),
            () -> assertThat(pagamento.getPixQRCode64().getValor()).isNotBlank());
    }

}
