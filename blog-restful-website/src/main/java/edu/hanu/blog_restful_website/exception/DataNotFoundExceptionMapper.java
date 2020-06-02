package edu.hanu.blog_restful_website.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import edu.hanu.blog_restful_website.model.ErrorMessage;

@Provider
public class DataNotFoundExceptionMapper implements ExceptionMapper<DataNotFoundException> {

	@Override
	public Response toResponse(DataNotFoundException e) {
		ErrorMessage errorMessage = new ErrorMessage(e.getMessage(), 404, "http://google.com");
		Response response = Response.status(Status.NOT_FOUND)
									.entity(errorMessage)
									.build();
		return response;
	}
}
