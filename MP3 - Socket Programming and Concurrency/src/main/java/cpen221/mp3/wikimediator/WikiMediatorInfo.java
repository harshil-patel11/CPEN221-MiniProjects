package cpen221.mp3.wikimediator;

import com.google.gson.annotations.SerializedName;

public class WikiMediatorInfo {
    /**
     * A POJO class that serializes a JSON String based on the inputs
     * required for the WikiMediator class methods
     */
    @SerializedName("id")
    private final String id;

    @SerializedName("type")
    private final String type;

    @SerializedName("query")
    private final String query;

    @SerializedName("limit")
    private final Integer limit;

    @SerializedName("pageTitle")
    private final String pageTitle;

    @SerializedName("timeout")
    private final Integer timeout;

    @SerializedName("status")
    private final String status;

    @SerializedName("response")
    private final String response;

    /*
     * RI:
     * id and type are not null
     *
     * AF:
     * id -> the id of a request
     * type -> the function that is requested
     * query -> the String input of WikiMediator's search method
     * limit -> the limit parameter for WikiMediator's search, zeitgeist, and trending methods
     * pageTitle -> the String input for WikiMediator's getPage method
     * timeout -> the maximum time allowed for a request to be handled by the server
     * status -> the status of the request (either "success" or "failed")
     * response -> a String that represents the result of the performed action/request
     */

    /**
     * Creates a WikiMediatorInfo object with the given parameters for the request
     *
     * @param id        the id of a request
     * @param type      the function that is requested
     * @param query     the String input of WikiMediator's search method
     * @param limit     the limit parameter for WikiMediator's search, zeitgeist,
     *                  and trending methods
     * @param pageTitle the String input for WikiMediator's getPage method
     * @param timeout   a String that represents the result of the performed action/request
     */
    public WikiMediatorInfo(String id, String type, String query, Integer limit,
                            String pageTitle, Integer timeout) {
        this.id = id;
        this.type = type;
        this.query = query;
        this.limit = limit;
        this.pageTitle = pageTitle;
        this.timeout = timeout;

        this.status = null;
        this.response = null;
    }

    /**
     * Creates a WikiMediatorInfo object with the given parameters for the response
     *
     * @param id       the id of a request
     * @param status   the status of the request (either "success" or "failed")
     * @param response the result of the performed action/request
     */
    public WikiMediatorInfo(String id, String status, String response) {
        this.id = id;
        this.status = status;
        this.response = response;

        this.type = null;
        this.query = null;
        this.limit = null;
        this.pageTitle = null;
        this.timeout = null;
    }

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * @return query
     */
    public String getQuery() {
        return query;
    }

    /**
     * @return limit
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * @return pageTitle
     */
    public String getPageTitle() {
        return pageTitle;
    }

    /**
     * @return timeout
     */
    public Integer getTimeout() {
        return timeout;
    }

    /**
     * @return response
     */
    public String getResponse() {
        return response;
    }

    /**
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @return a String representation of a WikiMediatorInfo object
     */
    @Override
    public String toString() {
        return "WikiMediatorInfo{" +
            "id='" + id + '\'' +
            ", type='" + type + '\'' +
            ", query='" + query + '\'' +
            ", limit='" + limit + '\'' +
            ", pageTitle='" + pageTitle + '\'' +
            ", timeout=" + timeout +
            ", status='" + status + '\'' +
            ", response='" + response + '\'' +
            '}';
    }
}
