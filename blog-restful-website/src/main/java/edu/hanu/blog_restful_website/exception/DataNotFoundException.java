package edu.hanu.blog_restful_website.exception;

public class DataNotFoundException extends RuntimeException {
	/*
	 * 
	 */
	private static final long serialVersionUID = 2352237297210690786L;

	public DataNotFoundException(String message) {
		super(message);
	}
}
