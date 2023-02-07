package br.com.unitTests.test.service;

import br.com.unitTests.test.builder.FilmeBuilder;
import br.com.unitTests.test.builder.LocacaoBuilder;
import br.com.unitTests.test.builder.UsuarioBuilder;
import br.com.unitTests.test.exceptions.FilmeSemEstoqueException;
import br.com.unitTests.test.exceptions.LocadoraException;
import br.com.unitTests.test.exceptions.MovieReturnException;
import br.com.unitTests.test.model.Filme;
import br.com.unitTests.test.model.Locacao;
import br.com.unitTests.test.model.Usuario;
import br.com.unitTests.test.repository.LocacaoRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class LocacaoServiceTest {

    @InjectMocks
    private LocacaoService locacaoService;
    @Mock
    private Usuario usuarioMock;
    private List<Filme> filmes = new ArrayList<>();
    private LocalDateTime diaOk;
    private LocalDateTime diaNOk;

    @Mock
    private LocacaoRepository locacaoRepository;
    @Mock
    private SPCService spcService;
    @Mock
    private EmailService emailService;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        diaNOk = LocalDateTime.of(2023, Month.FEBRUARY,4,0,0);
        diaOk = LocalDateTime.of(2023, Month.FEBRUARY,3,0,0);
        filmes.add(FilmeBuilder.filmeBuilder().setNome("filme1").setEstoque(10).setPreco(BigDecimal.TEN).build());
    }

    @Test
    public void testeLocacao() throws Exception {
        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        Filme filme = new Filme("Filme 1", 1, BigDecimal.valueOf(Double.valueOf("5.0")));

        //acao
        Locacao locacao = locacaoService.alugarFilme(usuario, Arrays.asList(filme),diaOk);

        //verificacao
        assertEquals("Rent Value is incorrect",BigDecimal.valueOf(Double.valueOf("5.0")),locacao.getValor());
        System.out.println("dia1: " + diaOk.getDayOfWeek());
        System.out.println("dia2: " + locacao.getDataLocacao().getDayOfWeek());
        assertEquals("Date Value is incorrect", diaOk.getDayOfWeek(),locacao.getDataLocacao().getDayOfWeek());
        assertEquals("Date Value is incorrect", diaOk.plusDays(1L).getDayOfWeek(),locacao.getDataRetorno().getDayOfWeek());

    }

    @Test
    public void testLocacao_filmeSemEstoque() throws Exception{
        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        Filme filmeSemEstoque = new Filme("Filme 1", 0, BigDecimal.valueOf(Double.valueOf("5.0")));

        //acao e verificação
        assertThrows(FilmeSemEstoqueException.class,
                () ->locacaoService.alugarFilme(usuario, Arrays.asList(filmeSemEstoque),diaOk));
    }

    @Test
    public void shouldReceiveListOfFilms() throws FilmeSemEstoqueException, LocadoraException, MovieReturnException {
        Usuario usuario = new Usuario("Usuario 1");
        Filme filme1 = new Filme("Filme 1", 10, BigDecimal.valueOf(Double.valueOf("6.0")));
        Filme filme2 = new Filme("Filme 1", 10, BigDecimal.valueOf(Double.valueOf("6.0")));
        //acao
        Locacao locacao = locacaoService.alugarFilme(usuario, Arrays.asList(filme1,filme2),diaOk);

        //verificacao
        assertEquals("Rent Value is incorrect",BigDecimal.valueOf(Double.valueOf("12")),locacao.getValor());
    }

    @Test
    public void testLocacao_usuarioVazio_filmesVazio() throws FilmeSemEstoqueException{
        //cenario
//        Filme filme = new Filme("Filme 2", 1, BigDecimal.valueOf(Double.valueOf("12")));
        Filme filme = FilmeBuilder.filmeBuilder()
                .setPreco(BigDecimal.valueOf(12.4))
                .setNome("filme2")
                .setEstoque(12)
                .build();

        //verificacao
        assertThrows(LocadoraException.class,() -> locacaoService.alugarFilme(null, Arrays.asList(filme),diaOk));
        assertThrows(LocadoraException.class,() -> locacaoService.alugarFilme(new Usuario("teste"), null,diaOk));
        assertThrows(LocadoraException.class,() -> locacaoService.alugarFilme(new Usuario("teste"), Collections.EMPTY_LIST,diaOk));
    }

    @Test
    public void testLocacao_FilmeVazio() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = UsuarioBuilder.builder().getUsuario();
        //verificacao
        assertThrows(LocadoraException.class,() -> locacaoService.alugarFilme(usuario, Arrays.asList(),diaOk));
    }

    @Test
    public void testeLocacao_Desconto25PorCentoNoTeceiroFilme() throws FilmeSemEstoqueException, LocadoraException, MovieReturnException {
        Filme filme1 = new Filme("Filme 1", 10, BigDecimal.valueOf(Double.valueOf("6.0")));
        Filme filme2 = new Filme("Filme 2", 10, BigDecimal.valueOf(Double.valueOf("6.0")));
        Filme filme3 = new Filme("Filme 3", 10, BigDecimal.valueOf(Double.valueOf("6.0")));
        Locacao locacao = locacaoService.alugarFilme(usuarioMock, Arrays.asList(filme1,filme2,filme3),diaOk);

        BigDecimal valueExpected = BigDecimal.valueOf(6).multiply(BigDecimal.valueOf(Double.valueOf("0.75"))).add(BigDecimal.valueOf(Double.valueOf("12.0")));
        assertEquals("Rent Value is incorrect",valueExpected,locacao.getValor().setScale(2, RoundingMode.HALF_UP));
    }
    @Test
    public void testeLocacao_Desconto25PorCentoNoTeceiroFilmeEQuartoFilme() throws FilmeSemEstoqueException, LocadoraException, MovieReturnException {
        Filme filme1 = new Filme("Filme 1", 10, BigDecimal.valueOf(Double.valueOf("6.0")));
        Filme filme2 = new Filme("Filme 2", 10, BigDecimal.valueOf(Double.valueOf("6.0")));
        Filme filme3 = new Filme("Filme 3", 10, BigDecimal.valueOf(Double.valueOf("6.0")));
        Filme filme4 = new Filme("Filme 4", 10, BigDecimal.valueOf(Double.valueOf("10.0")));
        Locacao locacao = locacaoService.alugarFilme(usuarioMock, Arrays.asList(filme1,filme2,filme3,filme4),diaOk);

        BigDecimal threeMovies = BigDecimal.valueOf(6).multiply(BigDecimal.valueOf(Double.valueOf("0.75"))).add(BigDecimal.valueOf(Double.valueOf("12.0")));
        BigDecimal valueExpected = BigDecimal.valueOf(10).multiply(BigDecimal.valueOf(Double.valueOf("0.5"))).add(threeMovies);
        assertEquals("Rent Value is incorrect",valueExpected,locacao.getValor().setScale(2, RoundingMode.HALF_UP));
    }

    @Test
//    @Ignore // ignora o teste quando não posso ser executado;
    public void shouldNotReturnMovieWhenIsSundae() throws FilmeSemEstoqueException, LocadoraException {
        Filme filme = new Filme("Filme 1", 10, BigDecimal.valueOf(Double.valueOf("6.0")));
        assertThrows(MovieReturnException.class,
                () -> locacaoService.alugarFilme(usuarioMock, Arrays.asList(filme),diaNOk));


    }

    @Test
    public void shouldNotRentIfUserHasDebts(){
        given(spcService.possuiNegativacao(usuarioMock)).willReturn(true);
        assertThrows(LocadoraException.class,() -> locacaoService.alugarFilme(usuarioMock,filmes,diaOk));
    }

    @Test
    public void shouldSendEmailWithLocacaoIsLate(){
        Locacao locacao = LocacaoBuilder.builder().setDadosBasicos(usuarioMock,
                filmes,
                diaOk,
                diaOk.plusDays(1L),
                BigDecimal.TEN).build();
        given(locacaoRepository.findByEstaAtrasado(true)).willReturn(Arrays.asList(locacao));
        locacaoService.noitificarClientesComAluguelAtrasado();

        Mockito.verify(emailService).notificarAtraso(locacao);
    }

}
