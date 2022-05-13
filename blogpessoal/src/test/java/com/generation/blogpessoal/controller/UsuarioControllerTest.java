package com.generation.blogpessoal.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.service.UsuarioService;

/* Indica que a classe UsuarioRepositoryTest é uma classe de test, 
que vai rodar em uma porta aleatoria a cada teste realizado */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)

/*
 * Cria uma instancia de testes, que define o ciclo de vida do teste, vai
 * respeitar o ciclo da classe (será executado e resetado após o uso)
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

//indica em qual ordem os testes serãoexecutados
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private UsuarioService usuarioService;

	@Test // indica que este método executará um teste
	@Order(1) // indica que o método será o primeiro a ser executado

	// Cria um objeto da Classe Usuario e insere dentro de um Objeto da Classe
	// HttpEntity (Entidade HTTP)
	@DisplayName("Cadastrar um usuario")
	public void DeveCriarUmUsuario() {
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L, "Paulo Antunes",
				"paulo_antunes@email.com.br", "13465278", "https://i.imgur.com/JR7kUFU.jpg"));

		ResponseEntity<Usuario> resposta = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao,
				Usuario.class);

		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
		assertEquals(requisicao.getBody().getNome(), resposta.getBody().getNome());
		assertEquals(requisicao.getBody().getUsuario(), resposta.getBody().getUsuario());

	}

	@Test // indica que este método executará um teste
	@Order(2) // indica que o método será o primeiro a ser executado
	@DisplayName("Não deve permitir duplicação do Usuário")
	public void naoDeveDuplicarUsuario() {

		usuarioService.cadastrarUsuario(new Usuario(0L, "Maria da Silva", "maria_silva@email.com.br", "13465278",
				"https://i.imgur.com/T12NIp9.jpg"));

		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L, "Maria da Silva",
				"maria_silva@email.com.br", "13465278", "https://i.imgur.com/T12NIp9.jpg"));

		ResponseEntity<Usuario> resposta = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao,
				Usuario.class);

		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
	}

	/*
	 * @Test // indica que este método executará um teste
	 * 
	 * @Order(3) // indica que o método será o primeiro a ser executado
	 * 
	 * @DisplayName("Atualizar um usuario") public void deveAtualizarUmUsuario() {
	 */
	@Test
	@Order(3)
	@DisplayName("Atualizar um Usuário")
	public void deveAtualizarUmUsuario() {

		/**
		 * Persiste um objeto da Classe Usuario no Banco de dados através do Objeto da
		 * Classe UsuarioService e guarda o objeto persistido no Banco de Dadoas no
		 * Objeto usuarioCadastrado, que será reutilizado abaixo.
		 * 
		 * O Objeto usuarioCadastrado será do tipo Optional porquê caso o usuário não
		 * seja persistido no Banco de dados, o Optional evitará o erro
		 * NullPointerException (Objeto Nulo).
		 */
		Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(new Usuario(0L, "Israel Igarashi",
				"israel_igarashi@email.com.br", "igarashi123", "https://i.imgur.com/yDRVeK7.jpg"));
		/**
		 * Cria um Objeto da Classe Usuário contendo os dados do Objeto
		 * usuarioCadastrado, que foi persistido na linha anterior, alterando os
		 * Atributos Nome e Usuário (Atualização dos Atributos).
		 * 
		 * Observe que para obter o Id de forma automática, foi utilizado o método
		 * getId() do Objeto usuarioCadastrado.
		 */
		Usuario usuarioUpdate = new Usuario(usuarioCadastrado.get().getId(), "Gustavo dos Anjos",
				"gustavo_anjos@email.com.br", "gust123", "https://i.imgur.com/yDRVeK7.jpg");

		/**
		 * Insere o objeto da Classe Usuario (usuarioUpdate) dentro de um Objeto da
		 * Classe HttpEntity (Entidade HTTP)
		 */
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(usuarioUpdate);

		ResponseEntity<Usuario> resposta = testRestTemplate.withBasicAuth("root", "root")
				.exchange("/usuarios/atualizar", HttpMethod.PUT, requisicao, Usuario.class);

		/**
		 * Verifica se a requisição retornou o Status Code OK (200) Se for verdadeira, o
		 * teste passa, se não, o teste falha.
		 */
		assertEquals(HttpStatus.OK, resposta.getStatusCode());

		/**
		 * Verifica se o Atributo Nome do Objeto da Classe Usuario retornado no Corpo da
		 * Requisição é igual ao Atributo Nome do Objeto da Classe Usuario Retornado no
		 * Corpo da Resposta Se for verdadeiro, o teste passa, senão o teste falha.
		 */
		assertEquals(requisicao.getBody().getNome(), resposta.getBody().getNome());

		/**
		 * Verifica se o Atributo Usuario do Objeto da Classe Usuario retornado no Corpo
		 * da Requisição é igual ao Atributo Usuario do Objeto da Classe Usuario
		 * Retornado no Corpo da Resposta Se for verdadeiro, o teste passa, senão o
		 * teste falha.
		 */
		assertEquals(requisicao.getBody().getUsuario(), resposta.getBody().getUsuario());

	}

	@Test // indica que este método executará um teste
	@Order(4) // indica que o método será o primeiro a ser executado
	@DisplayName("Listar todos os usuarios")
	public void deveMostrarTodosUsuarios() {

		/**
		 * Persiste dois objetos diferentes da Classe Usuario no Banco de dados através
		 * do Objeto da Classe UsuarioService
		 */
		usuarioService.cadastrarUsuario(new Usuario(0L, "Sabrina Sanches", "sabrina_sanches@email.com.br", "sabrina123",
				"https://i.imgur.com/5M2p5Wb.jpg"));

		usuarioService.cadastrarUsuario(new Usuario(0L, "Ricardo Marques", "ricardo_marques@email.com.br", "ricardo123",
				"https://i.imgur.com/Sk5SjWE.jpg"));

		ResponseEntity<String> resposta = testRestTemplate.withBasicAuth("root", "root").exchange("/usuarios/all",
				HttpMethod.GET, null, String.class);

		assertEquals(HttpStatus.OK, resposta.getStatusCode());

	}
	


	/** RESPOSTA DO DESAFIO! */

	@Test
	@Order(5) // indica que o método será o primeiro a ser executado
	@DisplayName("Listar Um Usuario Especifico")
	public void deveListarApenasUmUsuario() {

		Optional<Usuario> usuarioBusca = usuarioService.cadastrarUsuario(new Usuario(0L, "Laura Santolia",
				"laura_santolia@email.com.br", "laura12345", "https://i.imgur.com/EcJG8kB.jpg"));

		ResponseEntity<String> resposta = testRestTemplate.withBasicAuth("root", "root")
				.exchange("/usuarios/" + usuarioBusca.get().getId(), HttpMethod.GET, null, String.class);

		assertEquals(HttpStatus.OK, resposta.getStatusCode());

	}

}