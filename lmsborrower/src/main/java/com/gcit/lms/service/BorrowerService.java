package com.gcit.lms.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.gcit.lms.dao.AuthorDAO;
import com.gcit.lms.dao.BookCopiesDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.BookLoansDAO;
import com.gcit.lms.dao.BorrowerDAO;
import com.gcit.lms.dao.GenreDAO;
import com.gcit.lms.dao.LibraryBranchDAO;
import com.gcit.lms.dao.PublisherDAO;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookCopies;
import com.gcit.lms.entity.BookLoans;
import com.gcit.lms.entity.Borrower;

@SuppressWarnings({ "rawtypes", "unchecked" })
@RestController
public class BorrowerService {

	@Autowired
	BookDAO bdao;
	@Autowired
	LibraryBranchDAO ldao;
	@Autowired
	PublisherDAO pdao;
	@Autowired
	BookCopiesDAO bcdao;
	@Autowired
	BookLoansDAO bldao;
	@Autowired
	AuthorDAO adao;
	@Autowired
	GenreDAO gdao;
	@Autowired
	BorrowerDAO brdao;
	
	List<HttpMessageConverter<?>> mc = new ArrayList<HttpMessageConverter<?>>();
	RestTemplate rest = new RestTemplate();
	String adminURL = "http://localhost:1111";
	String librarianURL = "http://localhost:2222";
	String borrowerURL = "http://localhost:3333";
	String URL = "";

	public BorrowerService() {
		rest = new RestTemplate();
		mc.add(new FormHttpMessageConverter());
		mc.add(new StringHttpMessageConverter());
		mc.add(new MappingJackson2HttpMessageConverter());
		rest.setMessageConverters(mc);
	}
	
	@Transactional
	@RequestMapping(value = {"/Borrower/branchesCount"}, method = RequestMethod.GET, produces = { "application/json", "application/xml" })
	public Integer getBranchCount(HttpServletRequest req) throws SQLException {
		ResponseEntity<Integer> st = rest.getForEntity(adminURL + "/Admin/branchesCount", Integer.class);
		Integer count = st.getBody();
		return count;
	}

	@Transactional
	@RequestMapping(value = {"/Borrower/Branches/Name"}, method = RequestMethod.GET, produces = {
					"application/json", "application/xml" })
	public List<Object> readBranches(@RequestParam(value = "searchString", required = false) String searchString,
			@RequestParam(value = "pageNo", required = false) Integer pageNo)
			throws SQLException {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(adminURL + "/Admin/Branches/Name")
				.queryParam("searchString", searchString).queryParam("pageNo", pageNo);
		ResponseEntity<List> st = rest.getForEntity(builder.toUriString(), List.class);
		List<Object> obj = st.getBody();
		return obj;
	}
	
	@Transactional
	@RequestMapping(value = "/Borrower/Branches/{branchId}/Books/{bookId}/NoOfCopies", method = RequestMethod.GET, produces = {
			"application/json", "application/xml" })
	public Integer readNoOfCopies(@PathVariable Integer branchId, @PathVariable Integer bookId) throws SQLException {
		return bcdao.readBookCopies(bookId, branchId).getNoOfCopies();
	}

	@Transactional
	@RequestMapping(value = { "/Borrower/Branches/{id}/bookCounts"}, method = RequestMethod.GET, produces = { "application/json",
					"application/xml" })
	public Integer getBookCount(@PathVariable Integer id, HttpServletRequest req) throws SQLException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", "" + id);
		ResponseEntity<Integer> st = rest.getForEntity(librarianURL + "/Librarian/Branches/{id}/bookCount", Integer.class, params);
		Integer count = st.getBody();
		return count;
	}

	@Transactional
	@RequestMapping(value = "/Borrower/Branches/{branchId}/CardNo/{cardNo}/bookLoansCount", method = RequestMethod.GET, produces = {
			"application/json", "application/xml" })
	public Integer getBookLoansCount(@PathVariable Integer branchId, @PathVariable Integer cardNo) throws SQLException {

		return bldao.getBookLoansCount(branchId, cardNo);
	}
	
	@Transactional
	@RequestMapping(value = {"/Borrower/Branches/{id}/Books"}, method = RequestMethod.GET, produces = { "application/json",
					"application/xml" })
	public List<Book> readBranchBooks(@PathVariable Integer id, @RequestParam Integer pageNo, HttpServletRequest req)
			throws SQLException {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(librarianURL +  "/Librarian/Branches/"+ id +"/Books")
				.queryParam("pageNo", pageNo);
		System.out.println(builder.toUriString());
		System.out.println(req.getRequestURI());
		ResponseEntity<List> st = rest.getForEntity(builder.toUriString(), List.class);
		List<Book> obj = st.getBody();
		return obj;
	}

	@Transactional
	@RequestMapping(value = "/Borrower/Branches/{branchId}/Books/{bookId}/CardNo/{cardNo}/borrowBook", method = RequestMethod.GET, produces = {
			"application/json", "application/xml" })
	public void borrowBook(@PathVariable Integer branchId, @PathVariable Integer bookId, @PathVariable Integer cardNo)
			throws SQLException {
		bldao.saveBookLoansWithOutDateIn(branchId, bookId, cardNo);
		BookCopies bookCopies = bcdao.readBookCopies(bookId, branchId);
		if (bookCopies != null)
			bcdao.updateBookCopies(bookCopies, bookCopies.getNoOfCopies() - 1);
	}

	@Transactional
	@RequestMapping(value = "/Borrower/Branches/{branchId}/Books/{bookId}/CardNo/{cardNo}/returnBook", method = RequestMethod.GET, produces = {
			"application/json", "application/xml" })
	public void returnBook(@PathVariable Integer branchId, @PathVariable Integer bookId, @PathVariable Integer cardNo)
			throws SQLException {
		bldao.updateDateIn(branchId, bookId, cardNo);
		BookCopies bookCopies = bcdao.readBookCopies(bookId, branchId);
		bcdao.updateBookCopies(bookCopies, bookCopies.getNoOfCopies() + 1);
	}

	@Transactional
	@RequestMapping(value = "/Borrower/CardNo/{cardNo}", method = RequestMethod.GET, produces = { "application/json",
			"application/xml" })
	public Borrower readBorrowerByPK(@PathVariable Integer cardNo) throws SQLException {
		Borrower borrower = brdao.readBorrowerByPK(cardNo);
		borrower.setBookLoans(bldao.readBookLoansBorrower(borrower));
		return borrower;
	}

	@Transactional
	@RequestMapping(value = "/Borrower/Branches/{branchId}/Books/{bookId}/CardNo/{cardNo}/BookLoan", method = RequestMethod.GET, produces = {
			"application/json", "application/xml" })
	public BookLoans readBookLoanByPK(@PathVariable Integer branchId, @PathVariable Integer bookId,
			@PathVariable Integer cardNo) throws SQLException {
		return bldao.readBookLoanByPK(bookId, branchId, cardNo);
	}

	@Transactional
	@RequestMapping(value = "/Borrower/Branches/{branchId}/CardNo/{cardNo}/BookLoans", method = RequestMethod.GET, produces = {
			"application/json", "application/xml" })
	public List<BookLoans> readBookLoans(@PathVariable Integer branchId, @PathVariable Integer cardNo,
			@RequestParam Integer pageNo) throws SQLException {
		return bldao.readBookLoans(branchId, cardNo, pageNo);
	}
}
