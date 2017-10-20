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
import com.gcit.lms.entity.Publisher;
@Component
public class PublisherDAO extends BaseDAO<Publisher> implements ResultSetExtractor<List<Publisher>> {

	public void savePublisher(Publisher publisher) throws SQLException {
		template.update("INSERT INTO tbl_publisher (publisherName, publisherAddress, publisherPhone) VALUES (?, ?, ?)",
				new Object[] { publisher.getPublisherName(), publisher.getPublisherAddress(),
						publisher.getPublisherPhone() });
	}

	public void saveBookPublisher(Publisher publisher) throws SQLException {
		for (Book b : publisher.getBooks()) {
			template.update("INSERT INTO tbl_book VALUES (?, ?, ?)",
					new Object[] { b.getBookId(), b.getTitle(), publisher.getPublisherId() });
		}
	}

	public Integer savePublisherWithID(Publisher publisher) throws SQLException {
		KeyHolder holder = new GeneratedKeyHolder();
		final String sql = "INSERT INTO tbl_publisher (publisherName, publisherAddress, publisherPhone) VALUES (?, ?, ?)";
		template.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, publisher.getPublisherName());
				ps.setString(2, publisher.getPublisherAddress());
				ps.setString(3, publisher.getPublisherPhone());
				return ps;
			}
		}, holder);
		return holder.getKey().intValue();
	}

	public void updatePublisher(Publisher publisher) throws SQLException {
		template.update(
				"UPDATE tbl_publisher SET publisherName = ?, publisherAddress = ?, publisherPhone = ? WHERE publisherId = ?",
				new Object[] { publisher.getPublisherName(), publisher.getPublisherAddress(),
						publisher.getPublisherPhone(), publisher.getPublisherId() });
	}

	public void deletePublisher(Integer publisherId) throws SQLException {
		template.update("DELETE FROM tbl_publisher WHERE publisherId = ?", new Object[] { publisherId });
	}

	public List<Publisher> readPublishers(String publisherName, Integer pageNo) throws SQLException {
		setPageNo(pageNo);
		if (publisherName != null && !publisherName.isEmpty()) {
			publisherName = "%" + publisherName + "%";
			return template.query("SELECT * FROM tbl_publisher WHERE publisherName like ?",
					new Object[] { publisherName }, this);
		} else {
			return template.query("SELECT * FROM tbl_publisher", this);
		}

	}

	public Publisher readPublisherByPK(Integer publisherId) throws SQLException {
		List<Publisher> publishers = template.query("SELECT * FROM tbl_publisher WHERE publisherId = ?",
				new Object[] { publisherId }, this);
		if (publishers != null && publishers.size()>0) {
			return publishers.get(0);
		}
		return null;
	}

	public List<Publisher> readPublisher(String publisherName) throws SQLException {
		if (publisherName != null && !publisherName.isEmpty()) {
			publisherName = "%" + publisherName + "%";
			return template.query("SELECT * FROM tbl_publisher WHERE publisherName like ?",
					new Object[] { publisherName }, this);
		} else {
			return template.query("SELECT * FROM tbl_publisher", this);
		}

	}

	public Integer getPublisherCount() throws SQLException {
		return template.queryForObject("SELECT count(*) as COUNT FROM tbl_publisher", Integer.class);
	}

	@Override
	public List<Publisher> extractData(ResultSet rs) throws SQLException {
		List<Publisher> publishers = new ArrayList<>();
		while (rs.next()) {
			Publisher a = new Publisher();
			a.setPublisherId(rs.getInt("publisherId"));
			a.setPublisherName(rs.getString("publisherName"));
			a.setPublisherAddress(rs.getString("publisherAddress"));
			a.setPublisherPhone(rs.getString("publisherPhone"));
			// a.setBooks(bdao.readAllFirstLevel("SELECT * FROM tbl_book WHERE pubId = ?",
			// new Object[]{a.getPublisherId()}));
			publishers.add(a);
		}

		return publishers;
	}

}
