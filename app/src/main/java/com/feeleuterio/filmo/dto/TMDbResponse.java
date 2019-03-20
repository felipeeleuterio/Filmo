package com.feeleuterio.filmo.dto;

import com.feeleuterio.filmo.api.model.Movie;
import java.util.List;

public class TMDbResponse {
    final private int page;
    final private List<Movie> list;

    public TMDbResponse(int page, List<Movie> list) {
        this.page = page;
        this.list = list;
    }

    public int getPage() {
        return page;
    }

    public List<Movie> getList() {
        return list;
    }

}
