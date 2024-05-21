package br.com.edu.fiap.techchallengelanchonete.domain;

import br.com.edu.fiap.techchallengelanchonete.domain.valueobject.Codigo;
import br.com.edu.fiap.techchallengelanchonete.domain.valueobject.Produto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class Pedido {
    private Codigo codigo;
    private List<Produto> produtos;

    public Pedido() {
        this.codigo = new Codigo("");
        this.produtos = new ArrayList<>();
    }

}
