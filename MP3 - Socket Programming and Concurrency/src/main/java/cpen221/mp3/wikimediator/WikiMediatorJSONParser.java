package cpen221.mp3.wikimediator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class WikiMediatorJSONParser {
    private String jsonInputString;
    private WikiMediator wikiMediator;
    private WikiMediatorInfo wmi;

    private Object result;
    private String status;
    private boolean failed;
    private String jsonOutputString;
    private boolean stop;

    /*
     * RI:
     * All of the fields are not null, except result which may or may not be null
     * jsonInputString and jsonOutputString must be formatted as JSON String
     * status is equal to either "success" or "failed"
     *
     * AF:
     * jsonInputString represents the JSON formatted string for the requests from the client.
     * wikiMediator represents an instance of the Wikipedia Mediator service.
     * wmi represents a POJO object for WikiMediator.
     * result represents the return value of a performed action.
     * status and failed represents whether a computation was successful or failed.
     * stop represents a request to terminate a server (no longer 'open').
     */

    /*
     * Thread Safety Argument: Not thread safe
     * This datatype is not threadsafe because it uses shared variables without any locks.
     */


    /**
     * A constructor that creates a WikiMediatorJSONParser object
     *
     * @param jsonInputString the JSON formatted String to be parsed
     * @param wikiMediator    a WikiMediator object
     */
    public WikiMediatorJSONParser(String jsonInputString, WikiMediator wikiMediator) {
        this.jsonInputString = jsonInputString;
        this.wikiMediator = wikiMediator;
        Gson gson = new Gson();
        this.wmi = gson.fromJson(jsonInputString, WikiMediatorInfo.class);

        this.result = null;
        this.status = "success";
        this.failed = false;
        this.jsonOutputString = null;
        this.stop = false;
    }

    /**
     * A method to perform an action requested by a client based on the request type.
     * Performs search(), getPage(), zeitgeist(), trending(),
     * peakLoad30s(), or stop() based on wmi.getType().
     */
    public void performAction() {
        Gson gsonOut = new GsonBuilder().create();
        WikiMediatorInfo wikiMediatorInfo;

        switch (wmi.getType()) {
            case "search":
                search();
                break;

            case "getPage":
                getPage();
                break;

            case "zeitgeist":
                zeitgeist();
                break;

            case "trending":
                trending();
                break;

            case "peakLoad30s":
                peakLoad30s();
                break;

            case "stop":
                stop();
                break;

            default:
                failed = true;
                result = "Invalid Request";
                updateStatus();
        }

        if (wmi.getType().equals("stop")) {
            wikiMediatorInfo =
                new WikiMediatorInfo(wmi.getId(), null, result == null ? null : result.toString());
        } else {
            wikiMediatorInfo = new WikiMediatorInfo(wmi.getId(), status,
                result == null ? null : result.toString());
        }

        jsonOutputString = gsonOut.toJson(wikiMediatorInfo);
    }

    /**
     * @return the jsonOutputString
     */
    public String getJsonString() {
        return jsonOutputString;
    }

    /**
     * @return true if the type of the request = "stop"
     */
    public boolean getStop() {
        return stop;
    }

    /**
     * Performs the search action in WikiMediator if the parameters are valid
     *
     * @modifies the <code> result </code>
     * @modifies <code> failed </code>. True if <code> result </code> is null,
     * false otherwise
     */
    private void search() {
        if (wmi.getQuery() != null && wmi.getLimit() != null) {

            WikiThread searchThread = new WikiThread(wikiMediator, wmi.getType(),
                wmi.getQuery(), wmi.getLimit());

            boolean timeoutFlag = timeoutRoutine(searchThread);

            if (timeoutFlag) {
                result = "Operation timed out";
                failed = true;
            } else {
                result = searchThread.getSearchResult();
            }

        } else {
            failed = true;
            result = "Missing or incorrect arguments";
        }

        updateStatus();
    }

    /**
     * Performs the getPage action in WikiMediator if the parameters are valid
     *
     * @modifies the <code> result </code>
     * @modifies <code> failed </code>. True if <code> result </code> is null,
     * false otherwise
     */
    private void getPage() {
        if (wmi.getPageTitle() != null) {

            WikiThread searchThread = new WikiThread(wikiMediator, wmi.getType(),
                wmi.getPageTitle());

            boolean timeoutFlag = timeoutRoutine(searchThread);

            if (timeoutFlag) {
                result = "Operation timed out";
                failed = true;
            } else {
                result = searchThread.getPageResult();
            }

        } else {
            failed = true;
            result = "Missing or incorrect arguments";
        }

        updateStatus();
    }

    /**
     * This method performs a timeout routine. If the client has requested for a timeout
     * on their requests, this method ensures that the pulling results from wikipedia are
     * not over over the time limit.
     *
     * @param searchThread a Wiki Thread with the required queries
     * @return true if the search query timed out, false otherwise.
     */
    private boolean timeoutRoutine(WikiThread searchThread) {
        long timeout = 0;
        boolean timeoutFlag = false;

        if (wmi.getTimeout() != null) {
            timeout = wmi.getTimeout() * 1000;
        }

        long currentTime = System.currentTimeMillis();
        searchThread.start();

        try {
            searchThread.join(timeout);
            long newTime = System.currentTimeMillis();
            if (newTime - currentTime > timeout && wmi.getTimeout() != null) {
                timeoutFlag = true;
            }

        } catch (InterruptedException ie) {
            throw new RuntimeException("searchThread interrupted!");
        }

        return timeoutFlag;
    }


    /**
     * Performs the zeitgeist action in WikiMediator if the parameters are valid
     *
     * @modifies the <code> result </code>
     */
    private void zeitgeist() {
        if (wmi.getLimit() != null) {
            result = wikiMediator.zeitgeist(wmi.getLimit());
        } else {
            failed = true;
            result = "Missing or incorrect arguments";
        }

        updateStatus();
    }

    /**
     * Performs the trending action in WikiMediator if the parameters are valid
     *
     * @modifies the <code> result </code>
     * @modifies <code>failed</code>
     */
    private void trending() {
        if (wmi.getLimit() != null) {
            result = wikiMediator.trending(wmi.getLimit());
        } else {
            failed = true;
            result = "Missing or incorrect arguments";
        }

        updateStatus();
    }

    /**
     * Performs the peakLoad30s action in WikiMediator
     *
     * @modifies the <code> result </code>
     */
    private void peakLoad30s() {
        result = wikiMediator.peakLoad30s();
        updateStatus();
    }

    /**
     * Performs the stop action in WikiMediator
     *
     * @modifies the <code> result </code>
     * @modifies the <code> stop </code>
     */
    private void stop() {
        result = "bye";
        stop = true;
        updateStatus();
    }

    /**
     * A method to update the status
     *
     * @modifies the <code> status </code> to "passed" if !failed,
     * "failed", otherwise
     */
    private void updateStatus() {
        if (failed) {
            status = "failed";
        } else {
            status = "success";
        }
    }

}
