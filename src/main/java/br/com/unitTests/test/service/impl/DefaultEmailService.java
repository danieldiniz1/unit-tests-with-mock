package br.com.unitTests.test.service.impl;

import br.com.unitTests.test.model.Locacao;
import br.com.unitTests.test.service.EmailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class DefaultEmailService implements EmailService {

    private static final Logger LOGGER = LogManager.getLogger();
    @Override
    public void notificarAtraso(Locacao locacao) {
        LOGGER.info(locacao.getId());
    }
}
