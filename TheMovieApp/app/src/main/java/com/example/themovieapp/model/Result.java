package com.example.themovieapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result
{
    @SerializedName("page")
    @Expose
    private Integer page;

    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;

    @SerializedName("total_results")
    @Expose
    private Integer totalResults;

    @SerializedName("results")
    @Expose
    private List<Movie> results = null;

    public Result()
    {
    }

    public Integer getPage()
    {
        return page;
    }

    public void setPage(Integer page)
    {
        this.page = page;
    }

    public Integer getTotalPages()
    {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages)
    {
        this.totalPages = totalPages;
    }

    public List<Movie> getResults()
    {
        return results;
    }
}
