package com.otmanel.todoList.repositorie;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.otmanel.todoList.metier.TodoList;

public interface ITodoDao extends PagingAndSortingRepository<TodoList, Integer>{
	List<TodoList> findByLibelleContaining(String libelle);
	List<TodoList> findByPrioriteGreaterThan(int priorite);
	List<TodoList> findByDateLimiteBefore(LocalDate dateLimite);
	
	Page<TodoList> findByLibelleContaining(String libelle, Pageable p);
	Page<TodoList> findByPrioriteGreaterThan(int priorite, Pageable p);
	Page<TodoList> findByDateLimiteBefore(LocalDate dateLimite, Pageable p);
	Page<TodoList> findByLibelleContainingAndPrioriteGreaterThan(String libelle, int priorite, Pageable p);
}
