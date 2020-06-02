package edu.hanu.blog_restful_website.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.hanu.blog_restful_website.model.Comment;
import edu.hanu.blog_restful_website.utils.DbUtils;

public class CommentDAO implements DAO<Comment>{
	private static final String INSERT_SQL_QUERY = "INSERT INTO comments(comment, post_id) VALUES(?, ?)";
//	private static final String UPDATE_SQL_QUERY = "UPDATE posts SET comment = ?," + " post_id = ?,"
//			+ "	WHERE comments.id = ?";
	private static final String UPDATE_SQL_QUERY_BY_POST = "UPDATE comments SET comment = ?, post_id = ? WHERE id = ? AND post_id = ?";
	private static final String SELECT_SQL_QUERY = "SELECT * FROM comments WHERE comments.id = ?";
	private static final String SELECT_SQL_QUERY_BY_POST = "SELECT * FROM comments WHERE comments.id = ? AND post_id = ?";
	private static final String SELECT_ALL_SQL_QUERY = "SELECT * FROM comments";
	private static final String SELECT_ALL_SQL_QUERY_BY_POST = "SELECT * FROM comments WHERE post_id = ?";
	private static final String DELETE_SQL_QUERY = "DELETE FROM comments WHERE comments.id = ?";
	private static final String DELETE_ALL_SQL_QUERY = "DELETE FROM comments";
	private static final String DELETE_SQL_QUERY_BY_POST = "DELETE FROM comments WHERE post_id = ?";
	
	private PostDAO postDAO = new PostDAO();

	@Override
	public Comment get(long id) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Comment comment = new Comment();
		try {
			conn = DbUtils.initialise();
			if (conn == null) {
				throw new NullPointerException("CommentDAO.get: connection is null");
			}
			ps = conn.prepareStatement(SELECT_SQL_QUERY);
			ps.setLong(1, id);
			rs = ps.executeQuery();
			System.out.println(ps.toString());
			while (rs.next()) {
				comment.setId(rs.getLong("id"));
				comment.setComment(rs.getString("comment"));
				comment.setPost(postDAO.get(rs.getLong("post_id")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				DbUtils.closeResultSet(rs);
				DbUtils.closePreparedStatement(ps);
				DbUtils.closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return comment;
	}
	
	public Comment get(long id, long postId) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Comment comment = new Comment();
		try {
			conn = DbUtils.initialise();
			if (conn == null) {
				throw new NullPointerException("CommentDAO.get: connection is null");
			}
			ps = conn.prepareStatement(SELECT_SQL_QUERY_BY_POST);
			ps.setLong(1, id);
			ps.setLong(2, postId);
			rs = ps.executeQuery();
			System.out.println(ps.toString());
			while (rs.next()) {
				comment.setId(rs.getLong("id"));
				comment.setComment(rs.getString("comment"));
				comment.setPost(postDAO.get(rs.getLong("post_id")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				DbUtils.closeResultSet(rs);
				DbUtils.closePreparedStatement(ps);
				DbUtils.closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return comment;
	}

	@Override
	public List<Comment> getAll() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Comment> comments = new ArrayList<>();
		try {
			conn = DbUtils.initialise();
			if (conn == null) {
				throw new NullPointerException("CommentDAO.getAll: connection is null");
			}
			ps = conn.prepareStatement(SELECT_ALL_SQL_QUERY);
			rs = ps.executeQuery();
			System.out.println(ps.toString());
			while (rs.next()) {
				Comment comment = new Comment();
				comment.setId(rs.getLong("id"));
				comment.setComment(rs.getString("comment"));
				comment.setPost(postDAO.get(rs.getLong("post_id")));
				comments.add(comment);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				DbUtils.closeResultSet(rs);
				DbUtils.closePreparedStatement(ps);
				DbUtils.closeConnection(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return comments;
	}
	
	public List<Comment> getAll(long postId) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Comment> comments = new ArrayList<>();
		try {
			conn = DbUtils.initialise();
			if (conn == null) {
				throw new NullPointerException("CommentDAO.getAll: connection is null");
			}
			ps = conn.prepareStatement(SELECT_ALL_SQL_QUERY_BY_POST);
			ps.setLong(1, postId);
			rs = ps.executeQuery();
			System.out.println(ps.toString());
			while (rs.next()) {
				Comment comment = new Comment();
				comment.setId(rs.getLong("id"));
				comment.setComment(rs.getString("comment"));
				comment.setPost(postDAO.get(rs.getLong("post_id")));
				comments.add(comment);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				DbUtils.closeResultSet(rs);
				DbUtils.closePreparedStatement(ps);
				DbUtils.closeConnection(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return comments;
	}

	@Override
	public long save(Comment c) {
		Connection conn = null;
		PreparedStatement ps = null;
		long id = 0;
		try {
			conn = DbUtils.initialise();
			if (conn == null) {
				throw new NullPointerException("CommentDAO.save: connection is null");
			}
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(INSERT_SQL_QUERY, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, c.getComment());
			ps.setLong(2, c.getPost().getId());
			
			ps.execute();
			System.out.println(ps.toString());
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getLong(1);
				c.setId(id);
			}
			conn.commit();
		} catch (SQLException e) {
			try {
				if (conn != null) {
					conn.rollback();
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				DbUtils.closePreparedStatement(ps);
				DbUtils.closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return id;
	}

	@Override
	public void update(Comment c) {
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = DbUtils.initialise();
			if (conn == null) {
				throw new NullPointerException("CommentDAO.update: connection is null");
			}
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(UPDATE_SQL_QUERY_BY_POST);
			ps.setString(1, c.getComment());
			ps.setLong(2, c.getPost().getId());
			ps.setLong(3, c.getId());
			ps.setLong(4, c.getPost().getId());
			
			ps.execute();
			System.out.println(ps.toString());
			conn.commit();
		} catch (SQLException e) {
			try {
				if (conn != null) {
					conn.rollback();
					e.printStackTrace();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				DbUtils.closePreparedStatement(ps);
				DbUtils.closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void delete(long id) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DbUtils.initialise();
			if (conn == null) {
				throw new NullPointerException("CommentDAO.delete: connection is null");
			}
			ps = conn.prepareStatement(DELETE_SQL_QUERY);
			ps.setLong(1, id);
			ps.execute();
			System.out.println(ps.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				DbUtils.closePreparedStatement(ps);
				DbUtils.closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void deletePost(long postId) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DbUtils.initialise();
			if (conn == null) {
				throw new NullPointerException("CommentDAO.delete: connection is null");
			}
			ps = conn.prepareStatement(DELETE_SQL_QUERY_BY_POST);
			ps.setLong(1, postId);
			ps.execute();
			System.out.println(ps.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				DbUtils.closePreparedStatement(ps);
				DbUtils.closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void deleteAll() {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DbUtils.initialise();
			if (conn == null) {
				throw new NullPointerException("PostDAO.deleteAll: connection is null");
			}
			ps = conn.prepareStatement(DELETE_ALL_SQL_QUERY);
			ps.execute();
			System.out.println(ps.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				DbUtils.closePreparedStatement(ps);
				DbUtils.closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
	}
}
