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

import com.gcit.lms.entity.BookLoans;
import com.gcit.lms.entity.Borrower;
@Component
public class BorrowerDAO extends BaseDAO <Borrower> implements ResultSetExtractor<List<Borrower>>{
	
	public void saveBorrower(Borrower borrower) throws SQLException {
		template.update("INSERT INTO tbl_borrower (name, address, phone) VALUES (?, ?, ?)", new Object[] { borrower.getName(), borrower.getAddress(), borrower.getPhone() });
	}
	
	public void saveBorrowerLoans(Borrower borrower) throws SQLException {
		for(BookLoans a: borrower.getBookLoans()){
			template.update("INSERT INTO tbl_book_loans VALUES (?, ?, ?, ?, ?, null)", new Object[] { a.getBook().getBookId(), a.getLibraryBranch(), borrower.getCardNo(), a.getDateOut(), a.getDueDate()});
		}
	}
	
	public Integer saveBorrowerID(Borrower borrower) throws SQLException {
		KeyHolder holder = new GeneratedKeyHolder();
		final String sql = "INSERT INTO tbl_borrower (name, address, phone) VALUES (?, ?, ?)";
		template.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, borrower.getName());
				ps.setString(2, borrower.getAddress());
				ps.setString(3, borrower.getPhone());
				return ps;
			}
		}, holder);
		return holder.getKey().intValue();
	}

	public void updateBorrower(Borrower borrower) throws SQLException {
		template.update("UPDATE tbl_borrower SET name = ?, address = ?, phone = ? WHERE cardNo = ?", new Object[] { borrower.getName(), borrower.getAddress(), borrower.getPhone(), borrower.getCardNo() });
	}

	public void deleteBorrower(Integer cardNo) throws SQLException {
		template.update("DELETE FROM tbl_borrower WHERE cardNo = ?", new Object[] {cardNo});
	}

	public List<Borrower> readAllBorrowers() throws SQLException {
		return template.query("SELECT * FROM tbl_borrower", this);
	}
	public Integer getBorrowersCount() throws SQLException {
		return template.queryForObject("SELECT count(*) as COUNT FROM tbl_borrower", Integer.class);
	}
	
	public List<Borrower> readBorrowers(String BorrowerName, Integer pageNo) throws SQLException {
		setPageNo(pageNo);
		if(BorrowerName !=null && !BorrowerName.isEmpty()){
			BorrowerName = "%"+BorrowerName+"%";
			return template.query("SELECT * FROM tbl_borrower WHERE name like ?", new Object[]{BorrowerName}, this);
		}else{
			return template.query("SELECT * FROM tbl_borrower", this);
		}
		
	}
	public List<Borrower> readBorrowerByName(String borrowerName) throws SQLException {
		borrowerName = "%"+borrowerName+"%";
		return template.query("SELECT * FROM tbl_borrower WHERE name like ?", new Object[]{borrowerName}, this);
	}
	public List<Borrower> readBorrowerByAddress(String borrowerAddress) throws SQLException {
		borrowerAddress = "%"+borrowerAddress+"%";
		return template.query("SELECT * FROM tbl_borrower WHERE address like ?", new Object[]{borrowerAddress}, this);
	}
	public List<Borrower> readBorrowerByPhone(String borrowerPhone) throws SQLException {
		borrowerPhone = "%"+borrowerPhone+"%";
		return template.query("SELECT * FROM tbl_borrower WHERE phone like ?", new Object[]{borrowerPhone}, this);
	}
	public Borrower readBorrowerByPK(Integer cardNo) throws SQLException {
		List<Borrower> borrowers = template.query("SELECT * FROM tbl_borrower WHERE cardNo = ?", new Object[]{cardNo}, this);
		if(borrowers!=null && borrowers.size()>0){
			return borrowers.get(0);
		}
		return null;
	}

	@Override
	public List<Borrower> extractData(ResultSet rs) throws SQLException {
		List<Borrower> borrowers = new ArrayList<>();
		while (rs.next()) {
			Borrower b = new Borrower();
			b.setCardNo(rs.getInt("cardNo"));
			b.setName(rs.getString("name"));
			b.setAddress(rs.getString("address"));
			b.setPhone(rs.getString("phone"));
//			b.setBookLoans(adao.readAllFirstLevel("SELECT * FROM tbl_book_loans WHERE cardNo = ?", new Object[]{b.getCardNo()}));
			//do the same for genres
			//do the same for One Publisher
			borrowers.add(b);
		}
		return borrowers;
	}

}
