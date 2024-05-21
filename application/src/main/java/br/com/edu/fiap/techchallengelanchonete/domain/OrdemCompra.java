package br.com.edu.fiap.techchallengelanchonete.domain;

import java.math.BigDecimal;

import br.com.edu.fiap.techchallengelanchonete.domain.Pagador.Pagador;
import br.com.edu.fiap.techchallengelanchonete.domain.valueobject.Valor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class OrdemCompra {
    
    private Pedido pedido;
    private Pagador pagador;
    private Valor valorTotal;

    public OrdemCompra() {
        pedido = new Pedido();
        pagador = new Pagador();
        valorTotal = new Valor(BigDecimal.valueOf(0L));
    }

}
