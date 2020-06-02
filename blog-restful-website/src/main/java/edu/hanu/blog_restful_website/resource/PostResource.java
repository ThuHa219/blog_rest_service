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

import edu.hanu.blog_restful_website.model.FilterBean;
import edu.hanu.blog_restful_website.model.Post;
import edu.hanu.blog_restful_website.service.PostService;

@Path("/posts") // define path for resource
@Consumes(value = {MediaType.APPLICATION_JSON, MediaType.TEXT_XML}) // define the input of resource
@Produces(value = {MediaType.APPLICATION_JSON, MediaType.TEXT_XML})	// define the output of resource
public class PostResource {
	private PostService postService = new PostService(); // call service => define service

	@GET
	public List<Post> getAll(@BeanParam FilterBean filterBean, @Context UriInfo uriInfo) {
		if (filterBean.getStart() >= 0 && filterBean.getSize() > 0) {
			List<Post> statusPaginated = postService.getStatusPaginated(filterBean.getStart(), filterBean.getSize());
			for(Post p : statusPaginated) {
				p.addLink(getUriForSelfGetAll(uriInfo, p), "self");
			}
			return statusPaginated;
		}
		List<Post> posts = postService.getAll();
		for(Post p : posts) {
			p.addLink(getUriForSelfGetAll(uriInfo, p), "self");
		}
		return posts;
	}

	@GET
	@Path("/{postId}")
	public Response get(@PathParam("postId") long postId, @Context UriInfo uriInfo) {
		Post post = postService.get(postId);
		post.addLink(getUriForSelf(uriInfo), "self");
		post.addLink(getUriForComment(uriInfo, post), "comment");
//		System.out.println(post.toString());
		return Response.ok() // status code 200
						.entity(post) 
						.build();
	}

	private String getUriForComment(UriInfo uriInfo, Post post) {
		// posts/{id}/comments/{id}
		String uri = uriInfo.getBaseUriBuilder() //http://localhost:8080/blog-restful-website/webapi/
						.path(PostResource.class) // posts/
						.path(PostResource.class, "getCommentResource") // {postId}/comments
						.path(CommentResource.class) // comments
						.resolveTemplate("postId", post.getId())
						.build()
						.toString();
		System.out.println(uriInfo.getBaseUri().toString());
		return uri;
	}

	private String getUriForSelf(UriInfo uriInfo) {
		String uri = uriInfo.getAbsolutePathBuilder().build().toString();
		return uri;
	}

	@POST
	public Response add(Post post, @Context UriInfo uriInfo) {
		Post entity = postService.add(post);
		entity.addLink(getUriForSelf(uriInfo), "self");
		entity.addLink(getUriForComment(uriInfo, entity), "post");
		String newId = String.valueOf(post.getId());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build();
		return Response.created(uri) // status code 201 created
					.entity(entity)
					.build();
	}

	@PUT
	@Path("/{postId}")
	public Post update(@PathParam("postId") long id, Post post) {
		post.setId(id);
		return postService.update(post);
	}

	@DELETE
	@Path("/{postId}")
	public void deleteMessage(@PathParam("postId") long id) {
		postService.remove(id); // status code 204 no content
	}

	@Path("/{postId}/comments")
	public CommentResource getCommentResource() {
		System.out.println("hello");
		return new CommentResource();
	}
	
	public String getUriForSelfGetAll(UriInfo uriInfo, Post p) {
		String uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(p.getId())).build().toString();
		System.out.println(uriInfo.getAbsolutePath().toString());
		return uri;
	}
}
