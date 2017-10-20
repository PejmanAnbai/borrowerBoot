package com.gcit.lms.entity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"dateOut", "dueDate", "dateIn", "book", "borrower", "libraryBranch"})
@XmlRootElement
public class BookLoans implements Serializable{
	
	private static final long serialVersionUID = 5765679489413246661L;
	
	private String dateOut;
	private String dueDate;
	private String dateIn;
	private Book book;
	private Borrower borrower;
	private LibraryBranch libraryBranch;
	/**
	 * @return the dateOut
	 */
	@XmlElement
	public String getDateOut() {
		return dateOut;
	}
	/**
	 * @param dateOut the dateOut to set
	 */
	@XmlElement
	public void setDateOut(String dateOut) {
		this.dateOut = dateOut;
	}
	/**
	 * @return the dueDate
	 */
	@XmlElement
	public String getDueDate() {
		return dueDate;
	}
	/**
	 * @param dueDate the dueDate to set
	 */
	@XmlElement
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	/**
	 * @return the dateIn
	 */
	@XmlElement
	public String getDateIn() {
		return dateIn;
	}
	/**
	 * @param dateIn the dateIn to set
	 */
	@XmlElement
	public void setDateIn(String dateIn) {
		this.dateIn = dateIn;
	}
	/**
	 * @return the book
	 */
	@XmlElement
	public Book getBook() {
		return book;
	}
	/**
	 * @param book the book to set
	 */
	@XmlElement
	public void setBook(Book book) {
		this.book = book;
	}
	/**
	 * @return the borrower
	 */
	@XmlElement
	public Borrower getBorrower() {
		return borrower;
	}
	/**
	 * @param borrower the borrower to set
	 */
	@XmlElement
	public void setBorrower(Borrower borrower) {
		this.borrower = borrower;
	}
	/**
	 * @return the libraryBranch
	 */
	@XmlElement
	public LibraryBranch getLibraryBranch() {
		return libraryBranch;
	}
	/**
	 * @param libraryBranch the libraryBranch to set
	 */
	@XmlElement
	public void setLibraryBranch(LibraryBranch libraryBranch) {
		this.libraryBranch = libraryBranch;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateIn == null) ? 0 : dateIn.hashCode());
		result = prime * result + ((dateOut == null) ? 0 : dateOut.hashCode());
		result = prime * result + ((dueDate == null) ? 0 : dueDate.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BookLoans other = (BookLoans) obj;
		if (dateIn == null) {
			if (other.dateIn != null)
				return false;
		} else if (!dateIn.equals(other.dateIn))
			return false;
		if (dateOut == null) {
			if (other.dateOut != null)
				return false;
		} else if (!dateOut.equals(other.dateOut))
			return false;
		if (dueDate == null) {
			if (other.dueDate != null)
				return false;
		} else if (!dueDate.equals(other.dueDate))
			return false;
		return true;
	}
	

}
