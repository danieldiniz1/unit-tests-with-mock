package br.com.unitTests.test.builder;

import br.com.unitTests.test.model.Filme;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter

public class FilmeBuilder {

    private Filme filme;

    private FilmeBuilder() {
    }

    public static FilmeBuilder filmeBuilder(){
        FilmeBuilder filmeBuilder = new FilmeBuilder();
        filmeBuilder.filme = new Filme();
        return filmeBuilder;
    }

    public FilmeBuilder setNome(String nome){
        filme.setNome(nome);
        return this;
    }

    public FilmeBuilder setEstoque(Integer estoque){
        filme.setEstoque(estoque);
        return this;
    }

    public FilmeBuilder setPreco(BigDecimal preco){
        filme.setPrecoLocacao(preco);
        return this;
    }

    public Filme build(){
        return filme;
    }
}
