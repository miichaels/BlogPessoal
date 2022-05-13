package com.generation.blogpessoal.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.blogpessoal.model.Tema;
import com.generation.blogpessoal.repository.TemaRepository;

@RestController
@RequestMapping("/tema") // ponto de acesso
@CrossOrigin("*") // Entrar independente do lugar (web,db)
public class TemaController {
	
	//Transfere a responsabilidade de construir as consultas
	@Autowired
	private TemaRepository repository;
	
	@GetMapping  //Metodo para pegar tudo, vai usar a mesma rota principal
	public ResponseEntity<List<Tema>> GetAll(){
		return ResponseEntity.ok(repository.findAll());
	}
	
	@GetMapping("/{id}") //Metodo para pegar pelo id, vai usar a rota principal porem vai incluir uma / para puxar parametro
	public ResponseEntity<Tema>getById(@PathVariable Long id){
		return repository.findById(id).map(resp -> ResponseEntity.ok(resp))
				.orElse(ResponseEntity.notFound().build());
	}
	
	 @GetMapping("/nome/{nome}")
	public ResponseEntity<List<Tema>> getByName(@PathVariable String nome){
		return ResponseEntity.ok(repository.findAllByDescricaoContainingIgnoreCase(nome));
	}
	
	@PostMapping
	/* @Valid: valida todas as regras definidas na model*/
	/*RequestBody: put e post*/
	public ResponseEntity<Tema> postTema(@Valid @RequestBody Tema tema){
		return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(tema));
	}
	
	@PutMapping
	/* @Valid: valida todas as regras definidas na model*/
	/*RequestBody: put e post*/
	public ResponseEntity<Tema> put(@Valid @RequestBody Tema tema){
		return ResponseEntity.ok(repository.save(tema));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletePostagem(@PathVariable Long id) {
	return repository.findById(id).map(resposta -> {repository.deleteById(id);
	return ResponseEntity.status(HttpStatus.NO_CONTENT).build();})
	.orElse(ResponseEntity.notFound().build());
	}
	
}
