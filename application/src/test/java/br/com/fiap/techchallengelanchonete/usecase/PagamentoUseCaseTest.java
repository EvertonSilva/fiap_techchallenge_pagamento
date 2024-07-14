package br.com.fiap.techchallengelanchonete.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.edu.fiap.techchallengelanchonete.domain.OrdemCompra;
import br.com.edu.fiap.techchallengelanchonete.domain.Pagamento;
import br.com.edu.fiap.techchallengelanchonete.domain.StatusPagamento;
import br.com.edu.fiap.techchallengelanchonete.domain.valueobject.Codigo;
import br.com.edu.fiap.techchallengelanchonete.exception.ApplicationException;
import br.com.edu.fiap.techchallengelanchonete.exception.NotFoundResourceException;
import br.com.edu.fiap.techchallengelanchonete.infrastructure.IOrdemCompraProcessadaPublisher;
import br.com.edu.fiap.techchallengelanchonete.infrastructure.IPagamentoPersistence;
import br.com.edu.fiap.techchallengelanchonete.integration.gatewaypagamento.IGatewayPagamentoRegistrador;
import br.com.edu.fiap.techchallengelanchonete.usecase.PagamentoUseCase;

class PagamentoUseCaseTest {
    
    AutoCloseable mock;

    PagamentoUseCase pagamentoUseCase;

    @Mock
    IPagamentoPersistence pagamentoPersistence;
    @Mock
    IGatewayPagamentoRegistrador gatewayPagamentoRegistrador;
    @Mock
    IOrdemCompraProcessadaPublisher ordemCompraProcessadaPublisher;

    @BeforeEach
    void setUp() {
        this.mock = MockitoAnnotations.openMocks(this);
        this.pagamentoUseCase = new PagamentoUseCase(pagamentoPersistence, gatewayPagamentoRegistrador, ordemCompraProcessadaPublisher);
    }

    @AfterEach
    void teardown() throws Exception {
        this.mock.close();
    }

    @Test
    void deveRegistrarPagamento() {
        when(gatewayPagamentoRegistrador.registroPagamento(any(OrdemCompra.class)))
            .thenReturn(new Pagamento());
        when(pagamentoPersistence.salvaPagamento(any(Pagamento.class)))
            .thenReturn(new Pagamento());

        var pagamento = pagamentoUseCase.registraPagamento(new OrdemCompra());

        verify(gatewayPagamentoRegistrador, times(1)).registroPagamento(any(OrdemCompra.class));
        verify(pagamentoPersistence, times(1)).salvaPagamento(any(Pagamento.class));
    }

    @Nested
    class ConfirmacaoPagamento {

        @Test
        void deveConfirmarPagamento_quandoPagamentoInexistente() {
            when(pagamentoPersistence.consultaPagamentoPorCodigoPedido(any(Codigo.class)))
                .thenReturn(Optional.empty());
            
            var notFoundResourceException = assertThrows(NotFoundResourceException.class, () -> {
                pagamentoUseCase.confirmacaoPagamento(new Codigo(UUID.randomUUID().toString()), StatusPagamento.APROVADO);
            });

            assertThat(notFoundResourceException)
                .isNotNull()
                .hasMessage("Pagamento não encontrado!");

            verify(pagamentoPersistence, times(1)).consultaPagamentoPorCodigoPedido(any(Codigo.class));
        }

        @Test
        void deveConfirmarPagamento_quandoFilaInoperante() throws IOException {
            when(pagamentoPersistence.consultaPagamentoPorCodigoPedido(any(Codigo.class)))
                .thenReturn(Optional.of(new Pagamento()));
            doThrow(new IOException())
                .when(ordemCompraProcessadaPublisher).publica(any(Pagamento.class));
            
            var applicationException = assertThrows(ApplicationException.class, () -> {
                var pagamento = pagamentoUseCase.confirmacaoPagamento(new Codigo(UUID.randomUUID().toString()), StatusPagamento.APROVADO);
            });

            assertThat(applicationException)
                .isNotNull()
                .hasMessageContaining("Erro ao tentar notificar fila de pagamentos processados");

            verify(pagamentoPersistence, times(1)).consultaPagamentoPorCodigoPedido(any(Codigo.class));
        }

        @Test
        void deveConfirmarPagamento() throws IOException {
            when(pagamentoPersistence.consultaPagamentoPorCodigoPedido(any(Codigo.class)))
                .thenReturn(Optional.of(new Pagamento()));
            doNothing()
                .when(ordemCompraProcessadaPublisher).publica(any(Pagamento.class));

            var pagamento = pagamentoUseCase.confirmacaoPagamento(new Codigo(UUID.randomUUID().toString()), StatusPagamento.APROVADO);

            assertThat(pagamento)
                .isNotNull();

            assertThat(pagamento.getStatus())
                .isEqualTo(StatusPagamento.APROVADO);

            verify(pagamentoPersistence, times(1)).consultaPagamentoPorCodigoPedido(any(Codigo.class));
        }

    }
    

    @Test
    void deveConsultarPagamento() {
        when(pagamentoPersistence.consultaPagamentoPorCodigoPedido(any(Codigo.class)))
            .thenReturn(Optional.of(new Pagamento()));
        
        var pagamento = pagamentoUseCase.consultaPagamento(new Codigo(UUID.randomUUID().toString()));

        verify(pagamentoPersistence, times(1)).consultaPagamentoPorCodigoPedido(any(Codigo.class));
    }

}
