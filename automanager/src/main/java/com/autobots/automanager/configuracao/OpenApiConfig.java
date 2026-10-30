package com.autobots.automanager.configuracao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI configuracao() {
        return new OpenAPI()
                .info(new Info()
                        .title("AutoManager API")
                        .version("1.0.0")
                        .description("API REST da AutoBots para gestão de lojas de manutenção veicular e venda de autopeças: "
                                + "empresas, usuários, credenciais, veículos, mercadorias, serviços e vendas, com HATEOAS."));
    }
}
