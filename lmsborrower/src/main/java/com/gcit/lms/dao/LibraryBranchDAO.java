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
import com.gcit.lms.entity.BookCopies;
import com.gcit.lms.entity.BookLoans;
import com.gcit.lms.entity.LibraryBranch;
@Component
public class LibraryBranchDAO extends BaseDAO <LibraryBranch> implements ResultSetExtractor<List<LibraryBranch>>{
	
	public void saveLibraryBranch(LibraryBranch libraryBranch) throws SQLException {
		template.update("INSERT INTO tbl_library_branch (branchName, branchAddress) VALUES (?, ?)", new Object[] { libraryBranch.getBranchName(), libraryBranch.getBranchAddress() });
	}
	
	public void saveBookCopies(LibraryBranch libraryBranch) throws SQLException {
		for(BookCopies a: libraryBranch.getBookCopies()){
			template.update("INSERT INTO tbl_book_copies VALUES (?, ?, 5)", new Object[] { a.getBook().getBookId(), libraryBranch.getBranchId(), a.getNoOfCopies()});
		}
	}
	public void saveBookLoans(LibraryBranch libraryBranch) throws SQLException {
		for(BookLoans a: libraryBranch.getBookLoans()){
			template.update("INSERT INTO tbl_book_loans VALUES (?, ?, ?, ?, ?, ?)", new Object[] { a.getBook().getBookId(), libraryBranch.getBranchId(), a.getBorrower().getCardNo(), a.getDateOut(), a.getDueDate(), a.getDateIn()});
		}
	}
	public void saveBranchBooks(LibraryBranch libraryBranch) throws SQLException {
		for(Book b: libraryBranch.getBooks()){
			template.update("INSERT INTO tbl_book_copies VALUES (?, ?, 5)", new Object[] { b.getBookId(), libraryBranch.getBranchId() });
		}
	}
	public Integer saveLibraryBranchID(LibraryBranch libraryBranch) throws SQLException {
		KeyHolder holder = new GeneratedKeyHolder();
		final String sql = "INSERT INTO tbl_library_branch (branchName, branchAddress) VALUES (?, ?)";
		template.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, libraryBranch.getBranchName());
				ps.setString(2, libraryBranch.getBranchAddress());
				return ps;
			}
		}, holder);
		return holder.getKey().intValue();
	}
	public void updateLibraryBranch(LibraryBranch libraryBranch) throws SQLException {
		template.update("UPDATE tbl_library_branch SET branchName = ?, branchAddress = ? WHERE branchId = ?", new Object[] { libraryBranch.getBranchName(), libraryBranch.getBranchAddress(), libraryBranch.getBranchId() });
	}
	public void deleteBranchBooks(LibraryBranch libraryBranch) throws SQLException {
		template.update("DELETE FROM tbl_book_copies WHERE branchId = ?", new Object[] { libraryBranch.getBranchId() });
	}
	public void deleteLibraryBranch(LibraryBranch libraryBranch) throws SQLException {
		template.update("DELETE FROM tbl_library_branch WHERE branchId = ?", new Object[] { libraryBranch.getBranchId() });
	}

	public List<LibraryBranch> readAllBranches() throws SQLException {
		return template.query("SELECT * FROM tbl_library_branch", this);
	}
	
	public Integer getBranchCount() throws SQLException {
		return template.queryForObject("SELECT count(*) as COUNT FROM tbl_library_branch", Integer.class);
	}
	
	public List<LibraryBranch> readBranches(String branchName, Integer pageNo) throws SQLException {
		setPageNo(pageNo);
		if(branchName !=null && !branchName.isEmpty()){
			branchName = "%"+branchName+"%";
			return template.query("SELECT * FROM tbl_library_branch WHERE branchName like ?", new Object[]{branchName}, this);
		}else{
			return template.query("SELECT * FROM tbl_library_branch", this);
		}
		
	}
	public List<LibraryBranch> readBranchByBook(Book b) {
		return template.query("SELECT * FROM tbl_library_branch WHERE branchId IN (SELECT branchId FROM tbl_book_copies WHERE bookId = ?)", new Object[]{b.getBookId()}, this);
	}
	public LibraryBranch readBranchByPK(Integer branchId) throws SQLException {
		List<LibraryBranch> branches = template.query("SELECT * FROM tbl_library_branch WHERE branchId = ?", new Object[]{branchId}, this);
		if(branches!=null && branches.size()>0){
			return branches.get(0);
		}
		return null;
	}
	public List<LibraryBranch> readLibraryBranchByName(String branchName) throws SQLException {
		branchName = "%"+branchName+"%";
		return template.query("SELECT * FROM tbl_library_branch WHERE branchName like ?", new Object[]{branchName}, this);
	}

	@Override
	public List<LibraryBranch> extractData(ResultSet rs) throws SQLException {
		List<LibraryBranch> libraryBranch = new ArrayList<>();
		while (rs.next()) {
			LibraryBranch b = new LibraryBranch();
			b.setBranchId(rs.getInt("branchId"));
			b.setBranchName(rs.getString("branchName"));
			b.setBranchAddress(rs.getString("branchAddress"));
//			b.setBookCopies(adao.readAllFirstLevel("SELECT * FROM tbl_book_copies WHERE branchId = ?", new Object[]{b.getBranchId()}));
//			b.setBookLoans(cdao.readAllFirstLevel("SELECT * FROM tbl_book_loans WHERE branchId = ?", new Object[]{b.getBranchId()}));
//			b.setBooks(bdao.readAllFirstLevel("SELECT * FROM tbl_book WHERE bookId IN (SELECT bookId FROM tbl_book_copies WHERE branchId = ?)", new Object[]{b.getBranchId()}));
			//do the same for genres
			//do the same for One Publisher
			libraryBranch.add(b);
		}
		return libraryBranch;
	}




}
