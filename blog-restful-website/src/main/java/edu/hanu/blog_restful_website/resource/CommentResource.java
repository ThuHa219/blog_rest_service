package edu.hanu.blog_restful_website.resource;

import java.net.URI;
import java.util.List;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import edu.hanu.blog_restful_website.model.Comment;
import edu.hanu.blog_restful_website.model.FilterBean;
import edu.hanu.blog_restful_website.service.CommentService;

//@Path("/comments") // /posts and /comments
@Path("") //posts/{id}/comment/{id}
@Consumes(value = {MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces(value = {MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
public class CommentResource {
	private CommentService commentService = new CommentService();

	@GET
	public List<Comment> getAll(@PathParam("postId") long postId, @BeanParam FilterBean filterBean, @Context UriInfo uriInfo) {
		if (filterBean.getStart() >= 0 && filterBean.getSize() > 0) {
			List<Comment> commentPaginated = commentService.getCommentPaginated(filterBean.getStart(), filterBean.getSize(), postId);
			for(Comment c : commentPaginated) {
				c.addLink(getUriForSelfGetAll(uriInfo, c), "self");
			}
			return commentPaginated;
		}
		List<Comment> comments = commentService.getAll(postId);
		for(Comment c : comments) {
			c.addLink(getUriForSelfGetAll(uriInfo, c), "self");
			System.out.println(c.toString());
		}
		return comments;
	}

	@GET
	@Path("/{commentId}")
	public Response get(@PathParam("postId") long postId, @PathParam("commentId") long commentId, @Context UriInfo uriInfo) {
		 Comment entity = commentService.get(commentId, postId);
		 entity.addLink(getUriForSelf(uriInfo), "self");
		 return Response.ok()
				 		.entity(entity)
				 		.build();
	}

	@POST
	public Response add(Comment comment, @Context UriInfo uriInfo) {
		Comment entity = commentService.add(comment);
		long id = entity.getId();
		URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(id)).build();
		return Response.created(uri)
						.entity(entity)
						.build();
	}

	@PUT
	@Path("/{commentId}")
	public Comment update(@PathParam("postId") long postId, @PathParam("commentId") long commentId, Comment comment) {
		comment.setId(commentId);
		return commentService.update(comment, postId);
	}

	@DELETE
	@Path("/{commentId}")
	public void delete(@PathParam("commentId") long commentId) {
		commentService.remove(commentId);
	}
	
	public String getUriForSelf(UriInfo uriInfo) {
		String uri = uriInfo.getAbsolutePathBuilder().build().toString();
		return uri;
	}
	public String getUriForSelfGetAll(UriInfo uriInfo, Comment c) {
		String uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(c.getId())).build().toString();
		return uri;
	}
}
