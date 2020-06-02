package edu.hanu.blog_restful_website.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.hanu.blog_restful_website.model.Post;
import edu.hanu.blog_restful_website.utils.DbUtils;

public class PostDAO implements DAO<Post>{
	private static final String INSERT_SQL_QUERY = "INSERT INTO posts(title, content) VALUES(?, ?)";
	private static final String UPDATE_SQL_QUERY = "UPDATE posts SET title = ?, content = ? WHERE posts.id = ?";
	private static final String SELECT_SQL_QUERY = "SELECT * FROM posts WHERE posts.id = ?";
	private static final String SELECT_ALL_SQL_QUERY = "SELECT * FROM posts";
	private static final String DELETE_SQL_QUERY = "DELETE FROM posts WHERE posts.id = ?";
	private static final String DELETE_ALL_SQL_QUERY = "DELETE FROM posts";
	private static final String DELETE_SQL_QUERY_BY_POST = "DELETE FROM comments WHERE post_id = ?";

	
	@Override
	public Post get(long id) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Post post = new Post();
		try {
			conn = DbUtils.initialise();
			if (conn == null) {
				throw new NullPointerException("PostDAO.get: connection is null");
			}
			ps = conn.prepareStatement(SELECT_SQL_QUERY);
			ps.setLong(1, id);
			rs = ps.executeQuery();
			System.out.println(ps.toString());
			while (rs.next()) {
				post.setId(rs.getLong("id"));
				post.setTitle(rs.getString("title"));
				post.setContent(rs.getString("content"));
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
		return post;
	}

	@Override
	public List<Post> getAll() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Post> posts = new ArrayList<>();
		try {
			conn = DbUtils.initialise();
			if (conn == null) {
				throw new NullPointerException("PostDAO.getAll: connection is null");
			}
			ps = conn.prepareStatement(SELECT_ALL_SQL_QUERY);
			rs = ps.executeQuery();
			System.out.println(ps.toString());
			while (rs.next()) {
				Post post = new Post();
				post.setId(rs.getLong("id"));
				post.setTitle(rs.getString("title"));
				post.setContent(rs.getString("content"));
				posts.add(post);
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
		return posts;
	}

	@Override
	public long save(Post p) {
		Connection conn = null;
		PreparedStatement ps = null;
		long id = 0;
		try {
			conn = DbUtils.initialise();
			if (conn == null) {
				throw new NullPointerException("PostDAO.save: connection is null");
			}
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(INSERT_SQL_QUERY, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, p.getTitle());
			ps.setString(2, p.getContent());
			
			ps.execute();
			System.out.println(ps.toString());
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getLong(1);
				p.setId(id);
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
	public void update(Post p) {
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = DbUtils.initialise();
			if (conn == null) {
				throw new NullPointerException("PostDAO.update: connection is null");
			}
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(UPDATE_SQL_QUERY);
			ps.setString(1, p.getTitle());
			ps.setString(2, p.getContent());
			ps.setLong(3, p.getId());
			
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
				throw new NullPointerException("PostDAO.delete: connection is null");
			}
			ps = conn.prepareStatement(DELETE_SQL_QUERY);
			ps.setLong(1, id);
			deletePost(id);
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
	public static void main(String[] args) {
	}
}
