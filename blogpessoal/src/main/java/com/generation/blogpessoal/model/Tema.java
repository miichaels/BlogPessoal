package com.generation.blogpessoal.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "tb_tema")
	public class Tema {

	@Id
	// Equivalente ao auto_increment no mysql
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;

	@NotNull  // que o espaço nao pode ser null ou @NotBlank que não deixa o espaço ser vazio com espaços p/exemplo
	public String descricao;

	// Relacionamento do tipo um para muitos (um tema para varias postagens)
	// o cascade serve para que todas as atualizacoes ou delecoes de um tema, afetem
	// todas as postagens atreladas a ela
	@OneToMany(mappedBy = "tema", cascade = CascadeType.ALL)
	@JsonIgnoreProperties("tema")
	private List<Postagem> postagem;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<Postagem> getPostagem() {
		return postagem;
	}

	public void setPostagem(List<Postagem> postagem) {
		this.postagem = postagem;
	}

}
