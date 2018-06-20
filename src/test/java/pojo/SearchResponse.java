package pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SearchResponse {

    @JsonProperty("incomplete_results")
    private String response;
    @JsonProperty("items")
    private List<SearchObject> searchObjects;
    @JsonProperty("total_count")
    private String totalResult;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<SearchObject> getSearchObjects() {
        return searchObjects;
    }

    public void setSearchObjects(List<SearchObject> searchObjects) {
        this.searchObjects = searchObjects;
    }

    public String getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(String totalResult) {
        this.totalResult = totalResult;
    }
}
