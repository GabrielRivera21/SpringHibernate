package com.deeplogics.cloud.client.entities;

import java.util.Collection;


/**
 * This class was created in order to parse the JSON Object from the 
 * Page<Projects> Object from the Spring framework 
 * for the Retrofit library class and obtain the entity.
 * 
 * Page<Projects> JSON Object example:
 * 
   {
	  	"content":[
	  		{
	  			"id":"54cfbebc316ad54871e0e09c",
	 			"user":"example@gmail.com",
	  			"title":"erxgmwevuy",
	  			"description":"mfhaptnzicbhzhtibpfgnzrplrkfnjmpllkfkhshcxfx",
	  			"cost":247.0,
	  			"location":{"x":18.0,"y":66.0},
	  			"city":"Bayamon",
	  			"categories":["vehicles"],
	  			"pics":["54cfbebc316ad54871e0e09d","54cfbebd316ad54871e0e09f"]
	  		}
	  	],
	  	"size":10,
	  	"number":0,
	  	"sort":null,
	  	"numberOfElements":1,
	  	"totalPages":1,
	  	"totalElements":1,
	  	"firstPage":true,
	  	"lastPage":true
   }
 * 
 * @author Gabriel
 *
 */
public class PageClient<T> {
	
	private Collection<T> content;
	private int size;
	private int number;
	private int numberOfElements;
	private int totalPages;
	private boolean firstPage;
	private boolean lastPage;
	
	public PageClient(Collection<T> content, int size, int number,
			int numberOfElements, int totalPages, boolean firstPage,
			boolean lastPage) {
		super();
		this.content = content;
		this.size = size;
		this.number = number;
		this.numberOfElements = numberOfElements;
		this.totalPages = totalPages;
		this.firstPage = firstPage;
		this.lastPage = lastPage;
	}
	
	/**
	 * 
	 * @return the <T> Objects Collection
	 */
	public Collection<T> getContent() {
		return content;
	}

	public void setContent(Collection<T> content) {
		this.content = content;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getNumberOfElements() {
		return numberOfElements;
	}

	public void setNumberOfElements(int numberOfElements) {
		this.numberOfElements = numberOfElements;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public boolean isFirstPage() {
		return firstPage;
	}

	public void setFirstPage(boolean firstPage) {
		this.firstPage = firstPage;
	}

	public boolean isLastPage() {
		return lastPage;
	}

	public void setLastPage(boolean lastPage) {
		this.lastPage = lastPage;
	}
	
	
	
	
}
