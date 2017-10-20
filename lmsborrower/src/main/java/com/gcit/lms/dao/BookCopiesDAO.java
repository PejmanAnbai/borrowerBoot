package com.gcit.lms.dao;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookCopies;
import com.gcit.lms.entity.LibraryBranch;
@Component
public class BookCopiesDAO extends BaseDAO<BookCopies> implements ResultSetExtractor<List<BookCopies>>{


	public void saveBookCopies(BookCopies bookCopies) throws SQLException {
		template.update("INSERT INTO tbl_book_copies noOfCopies VALUES (?, ?, ?)", new Object[] { bookCopies.getBook().getBookId(), bookCopies.getLibraryBranch().getBranchId(), bookCopies.getNoOfCopies() });
	}
	
//	public void saveBookAuthor(BookCopies bookCopies) throws SQLException {
//		for(Author a: book.getAuthors()){
//			save("INSERT INTO tbl_book_authors VALUES (?, ?)", new Object[] { book.getBookId(), a.getAuthorId()});
//		}
//	}
	
//	public Integer saveBookID(BookCopies bookCopies) throws SQLException {
//		return saveWithID("INSERT INTO tbl_book (bookName) VALUES (?)", new Object[] { book.getTitle() });
//	}

	public void updateBookCopies(BookCopies bookCopies, Integer noOfCopies) throws SQLException {
		template.update("UPDATE tbl_book_copies SET noOfCopies = ? WHERE bookId = ? AND branchId =?", new Object[] { noOfCopies,  bookCopies.getBook().getBookId(), bookCopies.getLibraryBranch().getBranchId()});
	}
	public void updateBookCopies(Integer bookId, Integer branchId, Integer noOfCopies) throws SQLException {
		template.update("UPDATE tbl_book_copies SET noOfCopies = ? WHERE bookId = ? AND branchId =?", new Object[] { noOfCopies,  bookId, branchId});
	}

	public void deleteBookCopies(BookCopies bookCopies) throws SQLException {
		template.update("DELETE FROM tbl_book_copies WHERE bookId = ? AND branchId =?", new Object[] { bookCopies.getBook().getBookId(), bookCopies.getLibraryBranch().getBranchId() });
	}

//	public List<BookCopies> readAllBooks() throws SQLException {
//		return readAll("SELECT * FROM tbl_book", null);
//	}
	
	public BookCopies readBookCopies(Book book, LibraryBranch libraryBranch) throws SQLException {
		if(template.query("SELECT * FROM tbl_book_copies WHERE bookId = ? AND branchId = ?", new Object[]{book.getBookId(), libraryBranch.getBranchId()}, this).size()>0)
			return template.query("SELECT * FROM tbl_book_copies WHERE bookId = ? AND branchId = ?", new Object[]{book.getBookId(), libraryBranch.getBranchId()}, this).get(0);
		return null;
	}
	public BookCopies readBookCopies(Integer bookId, Integer branchId) throws SQLException {
		if(template.query("SELECT * FROM tbl_book_copies WHERE bookId = ? AND branchId = ?", new Object[]{bookId, branchId}, this).size()>0)
			return template.query("SELECT * FROM tbl_book_copies WHERE bookId = ? AND branchId = ?", new Object[]{bookId, branchId}, this).get(0);
		return null;
	}
	public List<BookCopies> readBookCopiesBranch(LibraryBranch l) {
		return template.query("SELECT * FROM tbl_book_copies WHERE branchId = ?", new Object[]{ l.getBranchId()}, this);
	}

	@Override
	public List<BookCopies> extractData(ResultSet rs) throws SQLException {
		List<BookCopies> bookCopies = new ArrayList<>();
		while (rs.next()) {
			BookCopies b = new BookCopies();
			b.setNoOfCopies(rs.getInt("noOfCopies"));
			Book book = new Book();
			LibraryBranch branch = new LibraryBranch();
			book.setBookId(rs.getInt("bookId"));
			branch.setBranchId(rs.getInt("branchId"));
//			if(adao.readAllFirstLevel("SELECT * FROM tbl_book WHERE bookId = ?", new Object[]{rs.getInt("bookId")}).size()>0)
//				b.setBook((adao.readAllFirstLevel("SELECT * FROM tbl_book WHERE bookId = ?", new Object[]{rs.getInt("bookId")})).get(0));
//			if(bdao.readAllFirstLevel("SELECT * FROM tbl_library_branch WHERE branchId = ?", new Object[]{rs.getInt("branchId")}).size()>0)
//				b.setLibraryBranch((bdao.readAllFirstLevel("SELECT * FROM tbl_library_branch WHERE branchId = ?", new Object[]{rs.getInt("branchId")}).get(0)));
			bookCopies.add(b);
		}
		return bookCopies;
	}

	
	

}
