package edu.hanu.blog_restful_website.service;

import java.util.ArrayList;
import java.util.List;

import edu.hanu.blog_restful_website.dao.CommentDAO;
import edu.hanu.blog_restful_website.exception.DataNotFoundException;
import edu.hanu.blog_restful_website.model.Comment;
import edu.hanu.blog_restful_website.model.Post;

public class CommentService {
	private CommentDAO dao = new CommentDAO();

	public CommentService() {
		// do nothing
	}

	public List<Comment> getAll(long postId) {
		return dao.getAll(postId);
	}

	public Comment get(long id, long postId) throws DataNotFoundException {
		Comment comment = dao.get(id, postId);
		if (comment.getId() == 0) {
			throw new DataNotFoundException("Can not found comment with comment id: " + id);
		}
		return comment;
	}

	public List<Comment> getCommentPaginated(int start, int size, long postId) {
		List<Comment> list = dao.getAll(postId);
		if (start + size > list.size()) {
			return new ArrayList<>();
		}
		return list.subList(start, start + size);
	}

	public Comment add(Comment comment) {
		dao.save(comment);
		return comment;
	}

	public Comment update(Comment comment, long postId) {
		if (comment.getId() <= 0) {
			return null;
		}
		comment.getPost().setId(postId);
		dao.update(comment);
		return comment;
	}

	public void remove(long id) throws DataNotFoundException {
		Comment comment = dao.get(id);
		if (comment == null) {
			throw new DataNotFoundException("Can not found comment with comment id: " + id);
		}
		dao.delete(id);
	}
}
