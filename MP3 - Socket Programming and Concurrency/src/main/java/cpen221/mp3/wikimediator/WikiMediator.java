package cpen221.mp3.wikimediator;

import cpen221.mp3.fsftbuffer.FSFTBuffer;
import cpen221.mp3.fsftbuffer.IDNotFoundException;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.fastily.jwiki.core.Wiki;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class WikiMediator {

    /**
     * A mediator service for Wikipedia.
     * This service will access Wikipedia (using the JWiki API) to obtain pages
     * and other relevant information.
     * It also collects statistical information about requests.
     */

    private static final int PEAKLOAD_TIMEOUT = 30000;
    private static final int BUFFER_TIMEOUT = 30000;
    private final Wiki wiki;
    private final FSFTBuffer searchBuffer;
    private final List<Query> queries;
    private final Queue<Long> load;
    int peakLoad;

    /*
     * RI:
     * All fields are non-null
     * searchBuffer has timeout equal to 30s, PEAKLOAD_TIMEOUT == BUFFER_TIMEOUT
     * queries contains stores every request as a Query
     * each time load.remove() is called, the Long returned should be greater than or equal to the
     * previous one
     * peakLoad contains the highest number of Query objects added to queries in any 30 second
     * window
     *
     * AF:
     * wiki is an object that allows us to access information from en.wikipedia.org
     * The searchBuffer represents a buffer containing the most recent queries (upto 30s timeout)
     * The list of Queries represents all search and getPage requests
     * The load Queue represents the number of requests in the current 30s frame
     * peakLoad is the maximum number of requests made in a 30s time frame
     *
     * Thread Safety Conditions:
     * Used synchronization to keep threads from bad interleavings, and to keep our thread safe
     * fields
     *
     */

    /**
     * Constructor that takes in some initial statistical information about past searches in order
     * to create a WikiMediator that continues where a previous wikiMediator left off.
     *
     * @param initialPeakLoad the starting point for peakLoad30s
     * @param pastQueries an initial list of queries for the wikiMediator to store
     * @param initialTimeMap an initial timeMap used to retain information for trending and
     *                       peakLoad30s. Maps a query to the time it was received.
     */
    public WikiMediator(int initialPeakLoad, List<Query> pastQueries,
                        Map<Query, Long> initialTimeMap) {
        wiki = new Wiki.Builder().build();
        searchBuffer = new FSFTBuffer(initialTimeMap, Integer.MAX_VALUE, BUFFER_TIMEOUT);
        queries = Collections.synchronizedList(pastQueries);
        load = new LinkedBlockingQueue<>();

        ArrayList<Long> initialLoadValues = new ArrayList<Long>(initialTimeMap.values());
        Collections.sort(initialLoadValues);

        for (int i = initialLoadValues.size() - 1; i >= 0; i--) {
            load.add(initialLoadValues.get(i));
        }

        peakLoad = initialPeakLoad;
    }

    /**
     * Default constructor for WikiMediator
     * Constructs a WikiMediator Object
     */
    public WikiMediator() {
        wiki = new Wiki.Builder().build();
        searchBuffer = new FSFTBuffer(Integer.MAX_VALUE, BUFFER_TIMEOUT);
        queries = Collections.synchronizedList(new ArrayList<>());
        load = new LinkedBlockingQueue<>();
        peakLoad = 0;
    }

    /*
        You must implement the methods with the exact signatures
        as provided in the statement for this mini-project.

        You must add method signatures even for the methods that you
        do not plan to implement. You should provide skeleton implementation
        for those methods, and the skeleton implementation could return
        values like null.

     */

    /**
     * Given a query, return up to limit page titles that match the query string
     * (per Wikipedia's search service).
     *
     * @param query the query or search criteria
     * @param limit the limit to the number of page titles.
     * @return the limit page titles that match the query string.
     */
    public List<String> search(String query, int limit) {
        Query q = new Query(query);

        synchronized (this) {
            update(q);
            updateLoad();
        }

        return wiki.search(query, limit);
    }

    /**
     * Given a pageTitle, returns the text associated with the Wikipedia page that matches pageTitle.
     *
     * @param pageTitle the pageTitle to be found.
     * @return the pageTitle requested.
     */
    public String getPage(String pageTitle) {
        Query q = new Query(pageTitle);

        synchronized (this) {
            update(q);
            updateLoad();
        }

        return wiki.getPageText(pageTitle);
    }

    /**
     * Return the most common Strings used in search and getPage requests,
     * with items being sorted in non-increasing count order.
     * When many requests have been made, return only limit items.
     *
     * @param limit the size of the List upon multiple requests being made.
     *              limit >= 0
     * @return the most common Strings used in search and getPage requests in non-increasing order.
     */
    public List<String> zeitgeist(int limit) {
        synchronized (this) {
            updateLoad();
        }

        return topQueries(limit, queries);
    }

    /**
     * Returns the most frequent requests made in the last 30 seconds.
     *
     * @param limit the size of the List upon multiple requests being made.
     *              limit >= 0
     * @return most frequent requests made in the last 30 seconds.
     */
    public synchronized List<String> trending(int limit) {
        updateLoad();

        Set<Query> querySet = searchBuffer.bufferableSet();

        return topQueries(limit, new ArrayList<>(querySet));
    }

    /**
     * A method to return the maximum number of requests seen in any 30-second window
     *
     * @return maximum number of requests seen in any 30-second window for the "basic page requests"
     */
    public synchronized int peakLoad30s() {
        updateLoad();

        return peakLoad;
    }

    public List<Query> getQueries() {
        return new ArrayList<>(queries);
    }

    public Map<Query, Long> getTimeMap() {
        return searchBuffer.getObjectTimeMap();
    }

    /**
     * This method updates the <code>queries</code> and <code>searchBuffer</code>
     * based on the given query.
     *
     * @param query the query to be updated or added in
     *              <code>queries</code> and <code>searchBuffer</code>
     * @modifies queries: if !queries.contains(query), then add query to the queries list
     * @modifies searchBuffer: if searchBuffer.contains(query), then update the query's
     * search field, otherwise add query to the buffer
     */
    private synchronized void update(Query query) {
        int index = queries.indexOf(query);

        if (index == -1) {
            queries.add(query);
            searchBuffer.put(query);
        } else {
            queries.get(index).addSearch();

            Query bufferQuery;

            try {
                bufferQuery = (Query) searchBuffer.get(query.toString());
                bufferQuery.addSearch();
            } catch (IDNotFoundException idnfe) {
                searchBuffer.put(query);
            }
        }

    }

    /**
     * This method updates the <code>load</code> list and the peakLoad fields
     *
     * @modifies load: adds the current timestamp to the list and removes stale time stamps
     * @modifies peakLoad: gets the max(peakLoad, load.size())
     */
    private synchronized void updateLoad() {
        long currentTime = System.currentTimeMillis();
        load.add(currentTime);

        while ((load.size() > 1) && (currentTime - load.peek() > PEAKLOAD_TIMEOUT)) {
            load.remove();
        }

        if (load.size() > peakLoad) {
            peakLoad = load.size();
        }
    }

    /**
     * This method returns the most requested queries in List<Query> queries
     *
     * @param limit   Limits the size of the list to be returned
     * @param queries A list of queries
     * @return the most requested queries in List<Query> queries
     */
    private synchronized List<String> topQueries(int limit, List<Query> queries) {
        Collections.sort(queries, Collections.reverseOrder());
        int size = Math.min(limit, queries.size());
        List<String> list = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            list.add(queries.get(i).toString());
        }

        return list;
    }

    // Tried in a different branch, but had insufficient time to finish
    public List<String> executeQuery(String query) {
        return null; //:(
    }

}