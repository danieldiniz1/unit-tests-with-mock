package br.com.unitTests.test.builder;

import br.com.unitTests.test.model.Filme;
import br.com.unitTests.test.model.Locacao;
import br.com.unitTests.test.model.Usuario;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class LocacaoBuilder {

    private Locacao locacao;

    private LocacaoBuilder() {
        this.locacao = locacao;
    }

    public static LocacaoBuilder builder(){
        LocacaoBuilder locacaoBuilder = new LocacaoBuilder();
       locacaoBuilder.locacao = new Locacao();
        return locacaoBuilder;
    }

    public LocacaoBuilder setDadosBasicos(Usuario usuario,
                                          List<Filme> filmes,
                                          LocalDateTime dataLocacao,
                                          LocalDateTime dataRetorno,
                                          BigDecimal valor){
        locacao.setUsuario(usuario);
        locacao.setFilmes(filmes);
        locacao.setDataLocacao(dataLocacao);
        locacao.setDataRetorno(dataRetorno);
        locacao.setValor(valor);
        locacao.setEstaAtrasado(false);
        return this;
    }

    public Locacao build(){
        return locacao;
    }
}
