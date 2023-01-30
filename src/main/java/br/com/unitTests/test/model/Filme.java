package br.com.unitTests.test.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
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
