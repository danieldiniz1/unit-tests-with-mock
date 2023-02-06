package br.com.unitTests.test.model;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Filme {

    private String nome;
    private Integer estoque;
    private BigDecimal precoLocacao;

    public static Filme valueOf(String nome, Integer estoque, BigDecimal precoLocacao){
        return new Filme(nome,estoque,precoLocacao);
    }
}
