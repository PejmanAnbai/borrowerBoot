package com.gcit.lms.entity;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"bookId", "title", "publisher", "authors", "genres", "branches"})
@XmlRootElement
public class Book implements Serializable {
	
	private static final long serialVersionUID = -2989596311510199717L;
	
	private Integer bookId;
	private String title;
	private Publisher publisher;
	private List<Author> authors;
	private List<Genre> genres;
	private List<LibraryBranch> branches;
	/**
	 * @return the bookId
	 */
	@XmlElement
	public Integer getBookId() {
		return bookId;
	}
	/**
	 * @param bookId the bookId to set
	 */
	@XmlElement
	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}
	/**
	 * @return the title
	 */
	@XmlElement
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	@XmlElement
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the publisher
	 */
	@XmlElement
	public Publisher getPublisher() {
		return publisher;
	}
	/**
	 * @param publisher the publisher to set
	 */
	@XmlElement
	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}
	/**
	 * @return the authors
	 */
	@XmlElement
	public List<Author> getAuthors() {
		return authors;
	}
	/**
	 * @param authors the authors to set
	 */
	@XmlElement
	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}
	/**
	 * @return the genres
	 */
	@XmlElement
	public List<Genre> getGenres() {
		return genres;
	}
	/**
	 * @param genres the genres to set
	 */
	@XmlElement
	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bookId == null) ? 0 : bookId.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		Book other = (Book) obj;
		if (bookId == null) {
			if (other.bookId != null)
				return false;
		} else if (!bookId.equals(other.bookId))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
	/**
	 * @return the branches
	 */
	@XmlElement
	public List<LibraryBranch> getBranches() {
		return branches;
	}
	/**
	 * @param branches the branches to set
	 */
	@XmlElement
	public void setBranches(List<LibraryBranch> branches) {
		this.branches = branches;
	}

}
