package com.generation.blogpessoal.configuration;

import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

import io.swagger.v3.oas.models.responses.ApiResponse; // Importa você mesmo caso não importa sozinho
import io.swagger.v3.oas.models.responses.ApiResponses; // Sempre importa a (classe)
 


@Configuration
public class SwaggerConfig {
	
	//Documentação das informações pessoais
	@Bean
	public OpenAPI springBlogPessoalOpenAPI() {
		
        return new OpenAPI()
				.info(new Info()
					.title("Projeto Blog Pessoal")
					.description("Projeto Blog Pessoal - Generation Brasil")
					.version("v0.0.1") // Versão da aplicação, atualizando cada vez que mudar
				.license(new License()
					.name("Generation Brasil")
					.url("https://brazil.generation.org/")) // Informações da empresa (padrão)
				.contact(new Contact() // Informações de contato pessoal / repositório do projeto
					.name("Michael Silva")
					.url("https://github.com/miichaels/BlogPessoal")
					.email("michael_araujo2012@hotmail.com")))
				.externalDocs(new ExternalDocumentation()
					.description("Meu Linkedin")
					.url("https://www.linkedin.com/in/miichaels/"));
	}

	@Bean
	public OpenApiCustomiser customerGlobalHeaderOpenApiCustomiser() {

		return openApi -> {
			openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {

				ApiResponses apiResponses = operation.getResponses();

				apiResponses.addApiResponse("200", createApiResponse("Sucesso!"));
				apiResponses.addApiResponse("201", createApiResponse("Objeto Persistido!"));
				apiResponses.addApiResponse("204", createApiResponse("Objeto Excluído!"));
				apiResponses.addApiResponse("400", createApiResponse("Erro na Requisição!"));
				apiResponses.addApiResponse("401", createApiResponse("Acesso Não Autorizado!"));
				apiResponses.addApiResponse("404", createApiResponse("Objeto Não Encontrado!"));
				apiResponses.addApiResponse("500", createApiResponse("Erro na Aplicação!"));

			}));
		};

	}
	
		// Cada vez que vem um status, ele cria uma resposta e envia ela para o cliente (No navegador)
		private ApiResponse createApiResponse(String message) {
			return new ApiResponse().description(message);
		}

		
	}

