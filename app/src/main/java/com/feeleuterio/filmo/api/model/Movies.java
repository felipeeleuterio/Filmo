package com.feeleuterio.filmo.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "results",
        "page",
        "total_results",
        "dates",
        "total_pages"
})
public class Movies {

    @JsonProperty("results")
    public List<Movie> movies = null;
    @JsonProperty("page")
    public int page;
    @JsonProperty("total_results")
    public int totalResults;
    @JsonProperty("dates")
    public Dates date;
    @JsonProperty("total_pages")
    public int totalPages;

}