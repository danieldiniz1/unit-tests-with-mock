package br.com.unitTests.test.service;

import br.com.unitTests.test.exceptions.FilmeSemEstoqueException;
import br.com.unitTests.test.exceptions.LocadoraException;
import br.com.unitTests.test.model.Filme;
import br.com.unitTests.test.model.Locacao;
import br.com.unitTests.test.model.Usuario;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
public class LocacaoServiceTest {

    @InjectMocks
    private LocacaoService locacaoService;

    @Before
    public void setup(){
        locacaoService = new LocacaoService();
    }

    @Test
    public void testeLocacao() throws Exception {
        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        Filme filme = new Filme("Filme 1", 1, BigDecimal.valueOf(Double.valueOf("5.0")));

        //acao
        Locacao locacao = locacaoService.alugarFilme(usuario, Arrays.asList(filme));

        //verificacao
        Assert.assertEquals("Rent Value is incorrect",BigDecimal.valueOf(Double.valueOf("5.0")),locacao.getValor());
        Assert.assertEquals("Date Value is incorrect", LocalDateTime.now().getDayOfWeek(),locacao.getDataLocacao().getDayOfWeek());
        Assert.assertEquals("Date Value is incorrect", LocalDateTime.now().plusDays(1L).getDayOfWeek(),locacao.getDataRetorno().getDayOfWeek());

    }

    @Test
    public void testLocacao_filmeSemEstoque() throws Exception{
        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        Filme filmeSemEstoque = new Filme("Filme 1", 0, BigDecimal.valueOf(Double.valueOf("5.0")));

        //acao e verificação
        Assert.assertThrows(FilmeSemEstoqueException.class,() ->locacaoService.alugarFilme(usuario, Arrays.asList(filmeSemEstoque)));
    }

    @Test
    public void shouldReceiveListOfFilms() throws FilmeSemEstoqueException, LocadoraException {
        Usuario usuario = new Usuario("Usuario 1");
        Filme filme1 = new Filme("Filme 1", 10, BigDecimal.valueOf(Double.valueOf("6.0")));
        Filme filme2 = new Filme("Filme 1", 10, BigDecimal.valueOf(Double.valueOf("6.0")));
        //acao
        Locacao locacao = locacaoService.alugarFilme(usuario, Arrays.asList(filme1,filme2));

        //verificacao
        Assert.assertEquals("Rent Value is incorrect",BigDecimal.valueOf(Double.valueOf("12")),locacao.getValor());
    }

    @Test
    public void testLocacao_usuarioVazio() throws FilmeSemEstoqueException{
        //cenario
        Filme filme = new Filme("Filme 2", 1, BigDecimal.valueOf(Double.valueOf("12")));
        //verificacao
        Assert.assertThrows(LocadoraException.class,() -> locacaoService.alugarFilme(null, Arrays.asList(filme)));
    }

    @Test
    public void testLocacao_FilmeVazio() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        //verificacao
        Assert.assertThrows(LocadoraException.class,() -> locacaoService.alugarFilme(usuario, Arrays.asList()));

    }
}
