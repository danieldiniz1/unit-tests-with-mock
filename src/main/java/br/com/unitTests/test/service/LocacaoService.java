package br.com.unitTests.test.service;

import br.com.unitTests.test.exceptions.FilmeSemEstoqueException;
import br.com.unitTests.test.exceptions.LocadoraException;
import br.com.unitTests.test.model.Filme;
import br.com.unitTests.test.model.Locacao;
import br.com.unitTests.test.model.Usuario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

@Service
public class LocacaoService {

    private static final Logger LOGGER = LogManager.getLogger();

    public Locacao alugarFilme(Usuario usuario, final List<Filme> filmes) throws FilmeSemEstoqueException, LocadoraException {
        if(usuario == null) {
            throw new LocadoraException("Usuario vazio");
        }

        if(filmes.isEmpty()) {
            throw new LocadoraException("Filme vazio");
        }

        List<Filme> filmes1 = filmes.stream().filter(filme -> filme.getEstoque().equals(0)).collect(Collectors.toList());
        if(!filmes1.isEmpty()) {
            throw new FilmeSemEstoqueException("Existem filmes sem estoque");
        }
        //TODO criar um builder para Locação
        Locacao locacao = new Locacao(null,
                filmes,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1L),
                calculaValorTotal(filmes));
//        locacao.setFilmes(filmes);
//        locacao.setUsuario(usuario);
//        locacao.setDataLocacao(LocalDateTime.now());
//
//        locacao.setValor();

        //Entrega no dia seguinte
        locacao.setDataRetorno(LocalDateTime.now().plusDays(1L));

        //Salvando a locacao...
        //TODO adicionar método para salvar

        return locacao;
    }

    private BigDecimal calculaValorTotal(List<Filme> filmes) {
        double totalRent = filmes.stream().collect(Collectors.summingDouble(f -> Double.parseDouble(f.getPrecoLocacao().toString()))).doubleValue(); // variável para eu ver o valor no debug
        return BigDecimal.valueOf(totalRent);
    }
}
