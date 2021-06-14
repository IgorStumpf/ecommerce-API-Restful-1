package com.residencia.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.residencia.ecommerce.entities.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer>{
	
	public Categoria findByNome(String nome);

}