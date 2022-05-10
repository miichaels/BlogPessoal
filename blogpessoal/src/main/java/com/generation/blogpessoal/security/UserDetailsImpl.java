package com.generation.blogpessoal.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.generation.blogpessoal.model.Usuario;

public class UserDetailsImpl implements UserDetails{
	
	//Linha padrão para usar o implements UserDetails
	private static final long serialVersionUID = 1L; 
	
	private String userName;
	
	private String password;
	
	///Linha padrão que autoriza todos os privilegios de usuario
	private List<GrantedAuthority> authorities; 
	

	public UserDetailsImpl(Usuario usuario) { 
		this.userName = usuario.getUsuario();
		this.password = usuario.getSenha();
		//Quando eu precisar usar um usuario e senha irei puxar da classe usuario
	}
	
	//Metodos padrões do Basic Security
	@Override
	//Todos os usuarios tera os mesmos privilegios 
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	
	@Override
	public String getPassword() {
		return password;
	}
	
	@Override
	public String getUsername() {

		return userName;
	}

	//Padrão para ver se a conta não está inspirada
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	//Padrão para ver se a conta está desbloqueada
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	//Padrão para se a credencial não está expirada
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	//Padrão para ver se está conta está ativa
	@Override
	public boolean isEnabled() {
		return true;
	}


	


}
