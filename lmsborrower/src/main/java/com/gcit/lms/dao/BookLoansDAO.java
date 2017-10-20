package com.gcit.lms.dao;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookLoans;
import com.gcit.lms.entity.Borrower;
import com.gcit.lms.entity.LibraryBranch;
@Component
public class BookLoansDAO extends BaseDAO <BookLoans> implements ResultSetExtractor<List<BookLoans>>{

	public void saveBookLoans(BookLoans bookLoans) throws SQLException {
		template.update("INSERT INTO tbl_book_loans VALUES (?, ?, ?, curdate(), curdate() + INTERVAL 7 DAY, ?)", new Object[] { bookLoans.getBook().getBookId(), bookLoans.getLibraryBranch().getBranchId(), bookLoans.getBorrower().getCardNo(), bookLoans.getDateIn()});
	}
	public void saveBookLoansWithOutDateIn(Integer branchId, Integer bookId, Integer cardNo) throws SQLException {
		template.update("INSERT INTO tbl_book_loans VALUES (?, ?, ?, curdate(), curdate() + INTERVAL 7 DAY, null)", new Object[] { bookId, branchId, cardNo});
	}
	
//	public void saveBookAuthor(BookLoans bookLoans) throws SQLException {
//		for(Author a: book.getAuthors()){
//			save("INSERT INTO tbl_book_authors VALUES (?, ?)", new Object[] { book.getBookId(), a.getAuthorId()});
//		}
//	}
//	
//	public Integer saveBookID(BookLoans bookLoans) throws SQLException {
//		return saveWithID("INSERT INTO tbl_book (bookName) VALUES (?)", new Object[] { book.getTitle() });
//	}

	public void updateDueDate(BookLoans bookLoans) throws SQLException {
		template.update("UPDATE tbl_book_loans SET dueDate = dueDate + INTERVAL ? DAY WHERE bookId = ? AND branchId = ? AND cardNo = ?;", new Object[] {bookLoans.getDueDate() , bookLoans.getBook().getBookId(), bookLoans.getLibraryBranch().getBranchId(), bookLoans.getBorrower().getCardNo() });
	}
	public void updateDateIn(Integer branchId, Integer bookId, Integer cardNo) throws SQLException {
		template.update("UPDATE tbl_book_loans SET dateIn = curdate() WHERE bookId = ? AND branchId = ? AND cardNo = ?", new Object[] { bookId, branchId, cardNo });
	}
	public void updateDateIn(BookLoans bookLoans, Book book) throws SQLException {
		template.update("UPDATE tbl_book_loans SET dateIn = ? WHERE bookId = ? AND branchId = ? AND cardNo = ?", new Object[] { book.getBookId(), bookLoans.getLibraryBranch().getBranchId(), bookLoans.getBorrower().getCardNo() });
	}

	public void deleteBookLoans(Integer branchId, Integer bookId, Integer cardNo) throws SQLException {
		template.update("DELETE FROM tbl_book_loans WHERE bookId = ? AND branchId = ? AND cardNo = ?", new Object[] { bookId, branchId, cardNo });
	}

	public List<BookLoans> readAllBookLoans() throws SQLException {
		return template.query("SELECT * FROM tbl_book_loans WHERE dateIn IS NULL", this);
	}
	
	public List<BookLoans> readBooksByTitle(String bookTitle, String branchName, String borrowerName) throws SQLException {
		return template.query("SELECT * FROM tbl_book_loans WHERE bookId IN (SELECT bookId FROM tbl_book WHERE title = ? ) AND branchId IN (SELECT branchId FROM tbl_library_branch WHERE branchName = ?) AND cardNo IN (SELECT cardNo FROM tbl_borrower WHERE name = ?)", new Object[]{bookTitle, branchName, borrowerName}, this);
	}
	public BookLoans readBookLoanByPK(Integer bookId, Integer branchId, Integer cardNo) throws SQLException {
		List<BookLoans> bookLoans = template.query("SELECT * FROM tbl_book_loans WHERE bookId = ? AND branchId = ? AND cardNo = ?", new Object[]{bookId, branchId, cardNo}, this);
		if(bookLoans!=null && bookLoans.size()>0){
			return bookLoans.get(0);
		}
		return null;
	}
	public List<BookLoans> readBookLoans(Integer branchId, Integer cardNo, Integer pageNo) throws SQLException {
		setPageNo(pageNo);
		List<BookLoans> bookLoans = template.query("SELECT * FROM tbl_book_loans WHERE branchId = ? AND cardNo = ? AND dateIn IS NULL", new Object[]{ branchId, cardNo}, this);
		if(bookLoans!=null && bookLoans.size()>0){
			return bookLoans;
		}
		return null;
	}
	public Integer getBookLoansCount(Integer branchId, Integer cardNo) throws SQLException {
		return template.queryForObject("SELECT count(*) as COUNT FROM tbl_book_loans WHERE branchId = ? AND cardNo = ? AND dateIn IS NULL", new Object[]{ branchId, cardNo}, Integer.class);
	}
	public Integer getBookLoansCount() throws SQLException {
		return template.queryForObject("SELECT count(*) as COUNT FROM tbl_book_loans", Integer.class);
	}
	
	public List<BookLoans> readBookLoans(String bookTitle, Integer pageNo) throws SQLException {
		setPageNo(pageNo);
			return template.query("SELECT * FROM tbl_book_Loans", this);		
	}
	public List<BookLoans> readBookLoansBranch(LibraryBranch l) {
		return template.query("SELECT * FROM tbl_book_loans WHERE branchId = ?", new Object[]{l.getBranchId()}, this);
	}
	public List<BookLoans> readBookLoansBorrower(Borrower b) {
		return template.query("SELECT * FROM tbl_book_loans WHERE cardNo = ? AND dateIn IS NULL", new Object[]{b.getCardNo()}, this);
	}
	@Override
	public List<BookLoans> extractData(ResultSet rs) throws SQLException {
		List<BookLoans> bookLoans = new ArrayList<>();
		while (rs.next()) {
			BookLoans b = new BookLoans();
			b.setDateIn(rs.getString("dateIn"));
			b.setDueDate(rs.getString("dueDate"));
			b.setDateOut(rs.getString("dateOut"));
			
			Borrower borrower = new Borrower();
			Book book = new Book();
			LibraryBranch branch = new LibraryBranch();
			book.setBookId(rs.getInt("bookId"));
			borrower.setCardNo(rs.getInt("cardNo"));
			branch.setBranchId(rs.getInt("branchId"));
//			b.setBook((adao.readAllFirstLevel("SELECT * FROM tbl_book WHERE bookId = ?", new Object[]{rs.getInt("bookId")})).get(0));
//			b.setBorrower((bdao.readAllFirstLevel("SELECT * FROM tbl_borrower WHERE cardNo = ?", new Object[]{rs.getInt("cardNo")})).get(0));
//			b.setLibraryBranch((cdao.readAllFirstLevel("SELECT * FROM tbl_library_branch WHERE branchId = ?", new Object[]{rs.getInt("branchId")})).get(0));
			//do the same for genres
			//do the same for One Publisher
			bookLoans.add(b);
		}
		return bookLoans;
	}

}
