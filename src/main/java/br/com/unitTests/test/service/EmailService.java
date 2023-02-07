package br.com.unitTests.test.service;

import br.com.unitTests.test.model.Locacao;

public interface EmailService {

    void notificarAtraso(Locacao locacao);
}
