package br.com.edu.fiap.techchallengelanchonete.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import br.com.edu.fiap.techchallengelanchonete.domain.OrdemCompra;
import br.com.edu.fiap.techchallengelanchonete.domain.Pedido;
import br.com.edu.fiap.techchallengelanchonete.domain.valueobject.Produto;
import br.com.edu.fiap.techchallengelanchonete.dto.ItemPedidoDTO;
import br.com.edu.fiap.techchallengelanchonete.dto.PedidoDTO;

class PedidoAdapterTest {
    
    PedidoAdapter pedidoAdapter;

    @BeforeEach
    void setUp() {
        this.pedidoAdapter = new PedidoAdapter();
    }

    @Nested
    class ConversaoParaDominio {
        
        @Test
        void deveConverterParaDominio_quandoDTONulo() {
            PedidoDTO pedidoDTO = null;

            var ordemCompra = pedidoAdapter.toDomain(pedidoDTO);

            assertThat(ordemCompra)
                .isNull();
        }

        @Test
        void deveConverterParaDominio_quandoDTOVazio() {
            var pedidoDTO = new PedidoDTO();

            var ordemCompra = pedidoAdapter.toDomain(pedidoDTO);

            assertThat(ordemCompra)
                .isNotNull();
        }

        @Test
        void deveConverterParaDominio_comItens() {
            var pedidoDTO = new PedidoDTO();
            var itemPedidoDTO = new ItemPedidoDTO();
            pedidoDTO.setItens(Arrays.asList(itemPedidoDTO));

            var ordemCompra = pedidoAdapter.toDomain(pedidoDTO);

            assertThat(ordemCompra)
                .isNotNull();
        }

    }

    @Nested
    class ConversaoParaDTO {

        @Test
        void deveConverterParaDTO_quandoDominioNulo() {
            OrdemCompra ordemCompra = null;

            var pedidoDTO = pedidoAdapter.toDTO(ordemCompra);

            assertThat(pedidoDTO)
                .isNull();
        }

        @Test
        void deveConverterParaDTO_quandoDominioVazio() {
            var ordemCompra = new OrdemCompra();

            var pedidoDTO = pedidoAdapter.toDTO(ordemCompra);

            assertThat(pedidoDTO)
                .isNotNull();
        }

        @Test
        void deveConverterParaDTO_comProdutos() {
            var ordemCompra = new OrdemCompra();
            ordemCompra.setPedido(new Pedido());
            ordemCompra.getPedido().setProdutos(Arrays.asList(new Produto("")));

            var pedidoDTO = pedidoAdapter.toDTO(ordemCompra);

            assertThat(pedidoDTO)
                .isNotNull();
        }

    }

}
