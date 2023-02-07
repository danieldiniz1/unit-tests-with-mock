package br.com.unitTests.test.repository;

import br.com.unitTests.test.model.Locacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocacaoRepository extends JpaRepository<Locacao,Long> {

    List<Locacao> findByEstaAtrasado(boolean estaAtrasado);
}
