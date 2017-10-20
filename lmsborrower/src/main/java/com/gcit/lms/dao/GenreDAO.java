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

import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.Genre;
@Component
public class GenreDAO extends BaseDAO <Genre> implements ResultSetExtractor<List<Genre>>{

	public void saveGenre(Genre genre) throws SQLException {
		template.update("INSERT INTO tbl_genre (genre_name) VALUES (?)", new Object[] { genre.getGenreName() });
	}
	
	public void saveBookGenre(Genre genre) throws SQLException {
		for(Book a: genre.getBooks()){
			template.update("INSERT INTO tbl_book_genres VALUES (?, ?)", new Object[] { genre.getGenreId(), a.getBookId()});
		}
	}
	public Integer saveGenreID(Genre genre) throws SQLException {
		KeyHolder holder = new GeneratedKeyHolder();
		final String sql = "INSERT INTO tbl_genre (genre_name) VALUES (?";
		template.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, genre.getGenreName());
				return ps;
			}
		}, holder);
		return holder.getKey().intValue();
	}
	public void updateGenre(Genre genre) throws SQLException {
		template.update("UPDATE tbl_genre SET genre_name = ? WHERE genre_id = ?", new Object[] { genre.getGenreName(), genre.getGenreId() });
	}

	public void deleteGenre(Integer genreId) throws SQLException {
		template.update("DELETE FROM tbl_genre WHERE genre_id = ?", new Object[] { genreId });
	}
	public void deleteBookGenre(Genre genre) throws SQLException {
		template.update("DELETE FROM tbl_book_genres WHERE genre_id = ?", new Object[] { genre.getGenreId() });
		
	}
	public Integer getGenresCount() throws SQLException {
		return template.queryForObject("SELECT count(*) as COUNT FROM tbl_genre", Integer.class);
	}
	public List<Genre> readAllGenre() throws SQLException {
		return template.query("SELECT * FROM tbl_genre", this);
	}
	public List<Genre> readGenres(String genreName, Integer pageNo) throws SQLException {
		setPageNo(pageNo);
		if(genreName !=null && !genreName.isEmpty()){
			genreName = "%"+genreName+"%";
			return template.query("SELECT * FROM tbl_genre WHERE genre_name like ?", new Object[]{genreName}, this);
		}else{
			return template.query("SELECT * FROM tbl_genre", this);
		}
		
	}
	public List<Genre> readGenres() throws SQLException {

		return template.query("SELECT * FROM tbl_genre", this);

	}
	public List<Genre> readGenreByBook(Book b) {
		return template.query("SELECT * FROM tbl_genre WHERE genre_id IN (SELECT genre_id FROM tbl_book_genres WHERE bookId = ?)", new Object[]{b.getBookId()}, this);
	}
	public List<Genre> readGenresByName(String genreName) throws SQLException {
		genreName = "%"+ genreName +"%";
		return template.query("SELECT * FROM tbl_genre WHERE genre_name like ?", new Object[]{genreName}, this);
	}

	@Override
	public List<Genre> extractData(ResultSet rs) throws SQLException {
		List<Genre> genre = new ArrayList<>();
		while (rs.next()) {
			Genre b = new Genre();
			b.setGenreId(rs.getInt("genre_id"));
			b.setGenreName(rs.getString("genre_name"));
//			b.setBooks(adao.readAllFirstLevel("SELECT * FROM tbl_book WHERE bookId IN (SELECT bookId FROM tbl_book_genres WHERE genre_id = ?)", new Object[]{b.getGenreId()}));
			//do the same for genres
			//do the same for One Publisher
			genre.add(b);
		}
		return genre;
	}
	
	public Genre readGenreByPK(Integer genreId) throws SQLException {
		List<Genre> genre = template.query("SELECT * FROM tbl_genre WHERE genre_id = ?", new Object[]{genreId}, this);
		if(genre!=null){
			return genre.get(0);
		}
		return null;
	}	
}
