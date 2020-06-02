package edu.hanu.blog_restful_website.service;

import java.util.ArrayList;
import java.util.List;

import edu.hanu.blog_restful_website.dao.PostDAO;
import edu.hanu.blog_restful_website.exception.DataNotFoundException;
import edu.hanu.blog_restful_website.model.Post;

public class PostService {
	private PostDAO dao = new PostDAO();

	public PostService() {
		// do nothing
	}

	public List<Post> getAll() {
		return dao.getAll();
	}

	public Post get(long id) throws DataNotFoundException {
		Post post = dao.get(id);
		if (post == null) {
			throw new DataNotFoundException("Can not found post with post id: " + id);
		}
		return post;
	}

	public List<Post> getStatusPaginated(int start, int size) {
		List<Post> list = dao.getAll();
		if (start + size > list.size()) {
			return new ArrayList<>();
		}
		return list.subList(start, start + size);
	}

	public Post add(Post post) {
		dao.save(post);
		return post;
	}

	public Post update(Post post) {
		if (post.getId() <= 0) {
			return null;
		}
		dao.update(post);
		return post;
	}

	public void remove(long id) throws DataNotFoundException {
		Post post = dao.get(id);
		if (post == null) {
			throw new DataNotFoundException("Can not found post with post id: " + id);
		}
		dao.delete(id);
	}
}
