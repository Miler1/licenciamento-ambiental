package models;

import java.util.List;

public class Pagination<T> {
    
	private Long totalResults;
    private List<T> pageItems;

    public Pagination(Long totalResults, List<T> pageItems) {
		super();
		this.totalResults = totalResults;
		this.pageItems = pageItems;
	}

	public Long getTotalResults() {
		return totalResults;
	}

	public void setTotalResults(Long totalResults) {
		this.totalResults = totalResults;
	}

	public List<T> getPageItems() {
		return pageItems;
	}

	public void setPageItems(List<T> pageItems) {
		this.pageItems = pageItems;
	}

	public Pagination() {
        
    }
    
}