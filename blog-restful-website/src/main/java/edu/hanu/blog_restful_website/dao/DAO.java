package edu.hanu.blog_restful_website.dao;

import java.util.List;

public interface DAO<T> {
	
	T get(long id);
	
	List<T> getAll();
	
	long save(T t);
	
	void update(T t);
	
	void delete(long id);
	
	void deleteAll();
	
}