package br.com.unitTests.test.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Locacao {

    private Usuario usuario;
    private List<Filme> filmes;
    private LocalDateTime dataLocacao;
    private LocalDateTime dataRetorno;
    private BigDecimal valor;


}
