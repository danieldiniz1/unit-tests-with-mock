package br.com.unitTests.test.service;

import br.com.unitTests.test.exceptions.FilmeSemEstoqueException;
import br.com.unitTests.test.exceptions.LocadoraException;
import br.com.unitTests.test.exceptions.MovieReturnException;
import br.com.unitTests.test.model.Filme;
import br.com.unitTests.test.model.Locacao;
import br.com.unitTests.test.model.Usuario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

@Service
public class LocacaoService {

    private static final Logger LOGGER = LogManager.getLogger();

    public Locacao alugarFilme(Usuario usuario, final List<Filme> filmes, LocalDateTime dateLocation) throws FilmeSemEstoqueException, LocadoraException, MovieReturnException {
        if(usuario == null) {
            throw new LocadoraException("Usuario vazio");
        }

        if(filmes == null || filmes.isEmpty()) {
            throw new LocadoraException("Filme vazio");
        }

        List<Filme> filmes1 = filmes.stream().filter(filme -> filme.getEstoque().equals(0)).collect(Collectors.toList());
        if(!filmes1.isEmpty()) {
            throw new FilmeSemEstoqueException("Existem filmes sem estoque");
        }



        //aplicador de desconto progressivo
        aplicadescontoprogressivo(filmes);

        //TODO criar um builder para Locação
        Locacao locacao = new Locacao(null,
                filmes,
                dateLocation,
                dateLocation.plusDays(1L),
                calculaValorTotal(filmes));


        //Verifica se entrega no dia seguinte não é dia da loja estar fechada
        LocalDateTime sundayDay = LocalDateTime.of(2023, Month.FEBRUARY, 5, 0, 0);
        if (locacao.getDataRetorno().getDayOfWeek().equals(sundayDay.getDayOfWeek())){
            throw new MovieReturnException("Data de retorno com a loja fechada!");
        }

        //Salvando a locacao...
        //TODO adicionar método para salvar

        return locacao;
    }

    private void aplicadescontoprogressivo(List<Filme> filmes) {
        LOGGER.info("Lista de filmes com: " + filmes.stream().count());
        if (filmes.stream().count() < 3){
            return;
        }
        for (int i = 0; i <= filmes.stream().count(); i++){
            if (i == 3) {
                filmes.get(i-1).setPrecoLocacao(filmes.get(i-1).getPrecoLocacao().multiply(BigDecimal.valueOf(Double.parseDouble("0.75"))));
            }
            if (i >= 4) {
                filmes.get(i-1).setPrecoLocacao(filmes.get(i-1).getPrecoLocacao().multiply(BigDecimal.valueOf(Double.parseDouble("0.5"))));
            }
        }
    }

    private BigDecimal calculaValorTotal(List<Filme> filmes) {
        double totalRent = filmes.stream().collect(Collectors.summingDouble(f -> Double.parseDouble(f.getPrecoLocacao().toString()))).doubleValue(); // variável para eu ver o valor no debug
        return BigDecimal.valueOf(totalRent);
    }
}
