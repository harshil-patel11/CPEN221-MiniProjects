package cpen221.mp3.wikimediator;

import java.util.List;

public class WikiThread extends Thread {
    private List<String> searchResult;
    private String pageInfo;
    private final String operation;
    private final WikiMediator wikiMediator;
    private final Integer limit;
    private final String query;

    /*
     * RI:
     * operation, wikiMediator, and query are not null
     * The other fields may or may not be null
     *
     * AF:
     * searchResult represents the result of a search() call.
     * searchResult represents the result of the computation of WikiMediator's search method
     * pageTitle represents the result of the computation of WikiMediator's getPage method
     * operation represents the action to be performed.
     * wikiMediator represents a wikiMediator object
     * limit represents the parameter for search().
     */

    /**
     * A contructor that creates a WikiThread object.
     *
     * @param wikiMediator a wikiMediator object.
     * @param operation    the operation to be perfomed.
     * @param query        the query
     * @param limit        is the parameter for search()
     */
    public WikiThread(WikiMediator wikiMediator, String operation, String query, Integer limit) {
        super();
        this.wikiMediator = wikiMediator;
        this.operation = operation;
        this.query = query;
        this.limit = limit;
        this.searchResult = null;
        this.pageInfo = null;
    }

    /**
     * A contructor that creates a WikiThread object.
     *
     * @param wikiMediator a wikiMediator object
     * @param operation    the operation to be perfomed.
     * @param query        the query
     */
    public WikiThread(WikiMediator wikiMediator, String operation, String query) {
        super();
        this.wikiMediator = wikiMediator;
        this.operation = operation;
        this.query = query;
        this.limit = null;
        this.searchResult = null;
        this.pageInfo = null;
    }

    /**
     * Computes results based on <code>operation</code>
     *
     * @modifies <code>searchResult</code> if operation = "search"
     * @modifies <code>pageInfo</code> if operation = "getPage"
     */
    @Override
    public void run() {
        switch (operation) {
            case "search":
                searchResult = wikiMediator.search(query, limit);
                break;

            case "getPage":
                pageInfo = wikiMediator.getPage(query);
                break;
        }

    }

    /**
     * @return the <code>searchResult</code> based on the start method's computation
     */
    public List<String> getSearchResult() {
        return searchResult;
    }

    /**
     * @return the <code>pageInfo</code> based on the start method's computation
     */
    public String getPageResult() {
        return pageInfo;
    }
}
