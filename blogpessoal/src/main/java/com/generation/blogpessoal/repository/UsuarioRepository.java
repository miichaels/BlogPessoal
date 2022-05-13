package com.generation.blogpessoal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.generation.blogpessoal.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	/* Quando não se sabe qual tipo de resposta pode ter ao buscar na requisição e 
	tem mais de uma possível.
	Basicamente que nem o List (Mas este só tem duas respostas, o Optional várias) */
	public Optional<Usuario> findByUsuario(String usuario);
	
	public Optional<Usuario> findByNome(String nome);
	
	public List<Usuario> findAllByNomeContainingIgnoreCase(String nome); 
}
