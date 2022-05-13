package com.generation.blogpessoal.service;

import java.nio.charset.Charset;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.model.UsuarioLogin;
import com.generation.blogpessoal.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;

	// Função para cadastrar um usuario
	public Optional<Usuario> cadastrarUsuario(Usuario usuario) {

		// Primeiro valida se o usuáio já existe no banco
		if (repository.findByUsuario(usuario.getUsuario()).isPresent())
			return Optional.empty();

		// Criptografo a senha do usuario caso não exista
		usuario.setSenha(criptografarSenha(usuario.getSenha()));

		// Salvar o usuario com a senha já criptografada no BD
		return Optional.of(repository.save(usuario));
	};
	
	
	public Optional<Usuario> atualizarUsuario(Usuario usuario) {

		// procurar usuario por id
		if (repository.findById(usuario.getId()).isPresent()) {

			// criptografar a senha nova
			usuario.setSenha(criptografarSenha(usuario.getSenha()));

			// retornar a senha cript
			return Optional.of(repository.save(usuario));
		}
		return Optional.empty();
	}


	/*
	* Método Criptografar Senhas.
	*   
	* Instancia um objeto da Classe BCryptPasswordEncoder para criptografar
	* uma senha do usuário.
	*
	* O método de codificação retorna a senha criptografada no formato BCrypt. Detalhes,
	* consulte a documentação do BCryptPasswordEncoder.
	*/
	//Função para criptograffar a senha digitada pelo usuario
	private String criptografarSenha(String senha) {
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		return encoder.encode(senha);
	}

	
	/*
	 * A principal função do autenticador Usuario, que é um método executado no endpoint logar,
	 * é gerar o token do usuário codificado em Base64. O login propriamente aqui é concluído
	 * pela BasicSecurityConfig em conjunto com as classes UserDetailsService e Userdetails
	 */
	public Optional<UsuarioLogin> autenticarUsuario(Optional<UsuarioLogin> usuarioLogin) {
		
		//Chega se o usuario existe
		Optional<Usuario> usuario = repository.findByUsuario(usuarioLogin.get().getUsuario());
		/*
		 * Cria um objeto Optional do tipo Usuario para receber o resultado do
		 * método findByUsuario().
		 *
		 * Observe que o autenticarUsuario recebe como parâmetro um objeto da
		 * Classe UsuarioLogin, ao invés de Usuario.
		 *
		 * get() -> Se um valor estiver presente no objeto ele retorna o valor, caso contrário,
		 * lança uma exceção NoSuchElementException. Então para usar é preciso ter certeza
		 * de que o Opcional não está vazio.
		 * O get() funciona como uma chave que abre o Objeto Optional e permite acessar os métodos
		 * do Objeto encapsulado.
		 */
		

		if (usuario.isPresent()) {
			/*
			 * Checa se a senha enviada, depois de criptografada, é igual a senha
			 * Método no Banco de Dados, através do comparandoSenhas.
			 *
			 * O Método Retorna verdadeiro se as senhas são iguais, e falso caso contrário.
			 */
			if (compararSenhas(usuarioLogin.get().getSenha(), usuario.get().getSenha())) { 
				/*
				 * Se as senhas for equal, atualiza o objeto usuarioLogin com os dados
				 * recuperados do Banco de Dados e insere o Token Gerado através do Método
				 * gerarBasicToken.
				 * Desta forma, será possível exibir o nome e a foto do usuário no Frontend.
				 */
				usuarioLogin.get().setId(usuario.get().getId());
				usuarioLogin.get().setNome(usuario.get().getNome());
				usuarioLogin.get().setFoto(usuario.get().getFoto());
				usuarioLogin.get().setToken(gerarBasicToken(usuarioLogin.get().getUsuario(),usuarioLogin.get().getSenha()));
				usuarioLogin.get().setSenha(usuario.get().getSenha());

				
				 /* Retorna o objeto usarioLogin atualizado para a classe UsuarioController.
				  A Classe controladora checará se deu tudo certo nesta operação e retornará o estado */
				return usuarioLogin;
			}

		}

		// vazio -> Retorna uma instância de Optional vazio, caso o usuário não seja encontrado.
		 
		return Optional.empty(); // Retorna um status vazio

	}
	
	/*
	* Método Comparar Senhas.
	*   
	* Checa se a senha enviada, depois de criptografada, é igual a senha
	* no Banco de Dados.
	*
	* Instancia um objeto da Classe BCryptPasswordEncoder para comparação
	* a senha do usuário com a senha gravad no Banco de dados.
	*
	* correspondências -> Verifca se a senha foi codificada do banco de dados corresponde à
	* enviado depois que ela também for codificada. Retorna verdadeiro se as
	* senhas coincidem e falso se não coincidem.  
	*/
	private boolean compararSenhas(String senhaDigitada, String senhaBanco) {

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		return encoder.matches(senhaDigitada, senhaBanco);
		// Retorna um True ou false

	}

	
	/**
	* Método Gerar Token Básico
	*
	* A primeira linha, monta uma String (token) seguindo o padrão Basic, através
	* da concatenação de caracteres que será codificado (Não criptografado) no formato
	* Base64, através da Dependência Apache Commons Codec.
	*
	* Essa String tem o formato padrão: <username>:<password> que não pode ser
	* alterado
	*
	*Na segunda linha, faremos uma classificação em Base 64 daString.
	*
	* Observe que o vetor tokenBase64 é do tipo Byte para o receber
	* resultado da codificação, como durante o processo é necessário funcionar
	* diretamente com os bits (0 e 1) da String
	*
	* Base64.encodeBase64 -> aplicativo de código do Código Decimal para Base64,
	* que foi gerado no próximo método. Para mais detalhes, veja Codificação 64 bits na
	* Documentação.
	*
	* Charset.forName("US-ASCII") -> Retornar o código ASCII (formato Decimal) de cada
	* caractere da String. Para mais detalhes, veja a Tabela ASCII na Documentação.
	*
	* Na terceira linha, acrescenta a palavra Basic aplicável de um espaço em branco (Obrigatório),
	* além de converter o vetor de Bytes novamente em String e concatenar tudo em uma única String.
	*
	* O espaço depois da palavra Basic é obrigatório. Caso não seja inserido, o Token não
	* será reconhecido.
	*/
	private String gerarBasicToken(String usuario, String senha) {

		String token = usuario + ":" + senha;
		byte[] tokenBase64 = Base64.encodeBase64(token.getBytes(Charset.forName("US-ASCII")));
		return "Basic " + new String(tokenBase64);

	}

}
