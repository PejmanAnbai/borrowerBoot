package com.gcit.lms.entity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"noOfCopies", "book", "libraryBranch"})
@XmlRootElement
public class BookCopies implements Serializable{
	
	private static final long serialVersionUID = -2014299252868631655L;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((noOfCopies == null) ? 0 : noOfCopies.hashCode());
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
		BookCopies other = (BookCopies) obj;
		if (noOfCopies == null) {
			if (other.noOfCopies != null)
				return false;
		} else if (!noOfCopies.equals(other.noOfCopies))
			return false;
		return true;
	}
	/**
	 * @return the noOfCopies
	 */
	@XmlElement
	public Integer getNoOfCopies() {
		return noOfCopies;
	}
	/**
	 * @param noOfCopies the noOfCopies to set
	 */
	@XmlElement
	public void setNoOfCopies(Integer noOfCopies) {
		this.noOfCopies = noOfCopies;
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
	private Integer noOfCopies;
	private Book book;
	private LibraryBranch libraryBranch;
	

}
