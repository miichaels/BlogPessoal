package com.generation.blogpessoal.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.repository.UsuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UsuarioRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		/**
		 * Para buscar o usuário no Banco de dados, utilizaremos o método findByUsuario,
		 * que foi assinado na interface UsuarioRepository
		 */
		
		//Start 
		Optional<Usuario> usuario = userRepository.findByUsuario(userName);
		/**
		 * Se o usuário não existir, o método lança uma Exception do tipo UsernameNotFoundException.
		 */ 
		usuario.orElseThrow(()-> new UsernameNotFoundException(userName + "Usuario não encontrado"));
		
		/**
		 * Retorna um objeto do tipo UserDetailsImpl criado com os dados recuperados do
		 * Banco de dados.
		 * 
		 * O operador :: faz parte de uma expressão que referencia um método, complementando
		 * uma função lambda. Neste exemplo, o operador faz referência ao construtor da 
		 * Classe UserDetailsImpl. 
		 */
		return usuario.map(UserDetailsImpl::new).get(); 
	}

}
