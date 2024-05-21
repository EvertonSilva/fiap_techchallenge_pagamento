package br.com.edu.fiap.techchallengelanchonete.adapter;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import br.com.edu.fiap.techchallengelanchonete.domain.OrdemCompra;
import br.com.edu.fiap.techchallengelanchonete.domain.Pedido;
import br.com.edu.fiap.techchallengelanchonete.domain.Pagador.Pagador;
import br.com.edu.fiap.techchallengelanchonete.domain.valueobject.CPF;
import br.com.edu.fiap.techchallengelanchonete.domain.valueobject.Codigo;
import br.com.edu.fiap.techchallengelanchonete.domain.valueobject.Email;
import br.com.edu.fiap.techchallengelanchonete.domain.valueobject.Nome;
import br.com.edu.fiap.techchallengelanchonete.domain.valueobject.Produto;
import br.com.edu.fiap.techchallengelanchonete.domain.valueobject.Valor;
import br.com.edu.fiap.techchallengelanchonete.dto.ItemPedidoDTO;
import br.com.edu.fiap.techchallengelanchonete.dto.PedidoDTO;

@Component
public class PedidoAdapter implements IAdapter<OrdemCompra, PedidoDTO> {

    @Override
    public OrdemCompra toDomain(PedidoDTO dto) {
        if (dto == null)
            return null;

        var ordemCompra = new OrdemCompra();

        var produtos = new ArrayList<Produto>();

        if (dto.getItens() != null) {
            dto.getItens().forEach(item -> {
                produtos.add(new Produto(item.getDescricaoProduto()));
            });
        }

        var pedido = new Pedido();
        pedido.setCodigo(new Codigo(dto.getCodigo()));
        pedido.setProdutos(produtos);

        var pagador = new Pagador();
        pagador.setCpf(new CPF(dto.getCpfCliente()));
        pagador.setNome(new Nome(dto.getNomeCliente()));
        pagador.setEmail(new Email(dto.getEmailCliente()));

        ordemCompra.setPedido(pedido);
        ordemCompra.setPagador(pagador);
        ordemCompra.setValorTotal(new Valor(dto.getValorTotal()));

        return ordemCompra;
    }

    @Override
    public PedidoDTO toDTO(OrdemCompra ordemCompra) {
        if (ordemCompra == null)
            return null;

        var pedidoDTO = new PedidoDTO();

        var itensDTO = new ArrayList<ItemPedidoDTO>();

        if (ordemCompra.getPedido() != null) {
            ordemCompra.getPedido().getProdutos().forEach(produto -> {
                var itemPedido = new ItemPedidoDTO();
                itemPedido.setDescricaoProduto(produto.getNome());
                itensDTO.add(itemPedido);
            });

            pedidoDTO.setCodigo(ordemCompra.getPedido().getCodigo().getValor());
        }

        if (ordemCompra.getPagador() != null) {
            pedidoDTO.setCpfCliente(ordemCompra.getPagador().getCpf().getValor());
            pedidoDTO.setEmailCliente(ordemCompra.getPagador().getEmail().getValor());
            pedidoDTO.setNomeCliente(ordemCompra.getPagador().getNome().getValor());
        }

        pedidoDTO.setValorTotal(ordemCompra.getValorTotal().getValor());
        pedidoDTO.setItens(itensDTO);

        return pedidoDTO;
    }

}
