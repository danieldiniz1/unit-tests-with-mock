package br.com.unitTests.test.builder;

import br.com.unitTests.test.model.Usuario;
import lombok.Getter;

@Getter
public class UsuarioBuilder {

    private Usuario usuario;

    private UsuarioBuilder() {
    }

    public static UsuarioBuilder builder(){
        UsuarioBuilder builder = new UsuarioBuilder();
        builder.usuario = new Usuario("usuario 1");
        return builder;
    }


}
