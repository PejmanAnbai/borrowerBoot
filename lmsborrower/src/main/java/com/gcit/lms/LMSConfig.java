package com.gcit.lms;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@ComponentScan
public class LMSConfig {

	public String driver = "com.mysql.cj.jdbc.Driver";
	public String url = "jdbc:mysql://localhost/library?useSSL=false";
	public String user = "root";
	public String password = "LoRd5254139!";

	@Bean
	// @Scope(value="prototype")
	public BasicDataSource dataSource() {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(driver);
		ds.setUrl(url);
		ds.setUsername(user);
		ds.setPassword(password);

		return ds;
	}

	@Bean
	@Qualifier(value="oracleTemplate")
	public JdbcTemplate template() {
		return new JdbcTemplate(dataSource());
	}
//
//	@Bean
//	public AuthorDAO adao() {
//		return new AuthorDAO();
//	}
//
//	@Bean
//	public BookDAO bdao() {
//		return new BookDAO();
//	}
//
//	@Bean
//	public BorrowerDAO brdao() {
//		return new BorrowerDAO();
//	}
//
//	@Bean
//	public PublisherDAO pdao() {
//		return new PublisherDAO();
//	}
//
//	@Bean
//	public LibraryBranchDAO ldao() {
//		return new LibraryBranchDAO();
//	}
//
//	@Bean
//	public GenreDAO gdao() {
//		return new GenreDAO();
//	}
//	
//	@Bean
//	public BookLoansDAO bldao() {
//		return new BookLoansDAO();
//	}
//	@Bean
//	public BookCopiesDAO bcdao() {
//		return new BookCopiesDAO();
//	}
//
//	@Bean
//	public PlatformTransactionManager txManager() {
//		return new DataSourceTransactionManager(dataSource());
//	}
//
//	@Bean
//	public AdminService adminService() {
//		return new AdminService();
//	}
//
//	@Bean
//	public LibrarianService librarianService() {
//		return new LibrarianService();
//	}
//
//	@Bean
//	public BorrowerService borrowerService() {
//		return new BorrowerService();
//	}
}
