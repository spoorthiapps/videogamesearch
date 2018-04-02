package com.gamesearch.gaintbomb.api;


import Response.SearchResponse;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GiantBombAPI {

    @GET("https://www.giantbomb.com/api/search/?resources=game")
    Observable<SearchResponse> gameSearch(@Query("query") String queryName);

}
