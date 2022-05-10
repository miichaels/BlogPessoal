package com.generation.blogpessoal.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity //Notação de security
public class BasicSecurityConfig extends WebSecurityConfigurerAdapter {


	@Autowired //Serve para comparar os dados digitados com os dados salvos no BD
	private UserDetailsService userDetailsService;

	@Override //Sobrecarga
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		//Usuario em memoria PARA TESTE
		auth.inMemoryAuthentication()
		.withUser("root") //Login
		.password(passwordEncoder().encode("root")) //Senha
		.authorities("ROLE_USER"); //Validador de usuario valido tem que ser "ROLE_USER"
	}
	
	//Notação que deixa umas função acessivel globalmente(em toda a minha aplicação)
	@Bean
	
	//Função que criptografa a senha digitada
	public PasswordEncoder passwordEncoder() { //Chama o Bcrypycoder para criptografar a senha
		return new BCryptPasswordEncoder(); 
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
				http.authorizeRequests() //Rota de acesso
				.antMatchers("/usuarios/logar").permitAll() //Define que em qualquer lugar toda vez que acessar ao "/usuarios/logar" estará aberta  
				.antMatchers("/usuarios/cadastrar").permitAll() //Define que em qualquer lugar toda vez que acessar ao "/usuarios/cadastrar" estará aberta
				.antMatchers(HttpMethod.OPTIONS).permitAll() //Para saber quais opções de metodos tenho acessiveis para utilizar
				.anyRequest().authenticated() //Define que as rotas estará aberta com o usuario em banco ou memoria
				.and().httpBasic() //Metodo de comunicação dentro da API dentro do HTTPBasic (CRUD)
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //Defini que toda requisição tem um começo meio e fim, status para uma requisição por vez
				.and().cors() //Serve para que independente da porta 8080 a API ira identificar e mostrar oque tem. (Liberar acesso do Back-end e Front-end)
				.and().csrf().disable(); //Autoriza o Put e Delete
		}

	}

