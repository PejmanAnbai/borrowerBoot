package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
@Component
public class AuthorDAO extends BaseDAO<Author> implements ResultSetExtractor<List<Author>> {
	
	public void saveAuthor(Author author) throws SQLException {
		template.update("INSERT INTO tbl_author (authorName) VALUES (?)", new Object[] { author.getAuthorName() });
	}

	public void saveBookAuthor(Author author) throws SQLException {
		for (Book b : author.getBooks()) {
			template.update("INSERT INTO tbl_book_authors VALUES (?, ?)",
					new Object[] { b.getBookId(), author.getAuthorId() });
		}
	}

	public Integer saveAuthorWithID(Author author) throws SQLException {
		KeyHolder holder = new GeneratedKeyHolder();
		final String sql = "insert into tbl_author(authorName) values (?)";
		template.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, author.getAuthorName());
				return ps;
			}
		}, holder);
		return holder.getKey().intValue();
	}

	public void updateAuthor(Author author) throws SQLException {
		template.update("UPDATE tbl_author SET authorName = ? WHERE authorId = ?",
				new Object[] { author.getAuthorName(), author.getAuthorId() });
	}

	public void deleteAuthor(Integer authorId) throws SQLException {
		template.update("DELETE FROM tbl_author WHERE authorId = ?", new Object[] { authorId });
	}

	public void deleteBookAuthor(Author author) throws SQLException {
		template.update("DELETE FROM tbl_book_authors WHERE authorId = ?", new Object[] { author.getAuthorId() });
	}

	public Integer getAuthorsCount() throws SQLException {
		return template.queryForObject("SELECT count(*) as COUNT FROM tbl_author", Integer.class);
	}

	public List<Author> readAuthors(String authorName, Integer pageNo) throws SQLException {
		setPageNo(pageNo);
		if (getPageNo() != null) {
			Integer index = (getPageNo() - 1) * getPageSize();
			if (authorName != null && !authorName.isEmpty()) {
				authorName = "%" + authorName + "%";
				return template.query(
						"SELECT * FROM tbl_author WHERE authorName like ?" + " LIMIT " + index + "," + getPageSize(),
						new Object[] { authorName }, this);
			} else {
				return template.query("SELECT * FROM tbl_author" + " LIMIT " + index + "," + getPageSize(), this);
			}
		} else {
			if (authorName != null && !authorName.isEmpty()) {
				authorName = "%" + authorName + "%";
				return template.query("SELECT * FROM tbl_author WHERE authorName like ?", new Object[] { authorName },
						this);
			} else {
				return template.query("SELECT * FROM tbl_author", this);
			}
		}
	}

	public List<Author> readAuthorByBook(Book b) {
		return template.query(
				"SELECT * FROM tbl_author WHERE authorId IN (SELECT authorId FROM tbl_book_authors WHERE bookId = ?)",
				new Object[] { b.getBookId() }, this);
	}

	public List<Author> readAuthors() throws SQLException {

		return template.query("SELECT * FROM tbl_author", this);

	}

	public Author readAuthorByPK(Integer authorId) throws SQLException {
		List<Author> authors = template.query("SELECT * FROM tbl_author WHERE authorId = ?", new Object[] { authorId },
				this);
		if (authors != null) {
			return authors.get(0);
		}
		return null;
	}

	@Override
	public List<Author> extractData(ResultSet rs) throws SQLException {
		List<Author> authors = new ArrayList<>();
		while (rs.next()) {
			Author a = new Author();
			a.setAuthorId(rs.getInt("authorId"));
			a.setAuthorName(rs.getString("authorName"));
			authors.add(a);
		}

		return authors;
	}

}
