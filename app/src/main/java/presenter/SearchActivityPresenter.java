package presenter;


import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import Response.SearchItem;
import Response.SearchResponse;
import error.NoResultsFound;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit.RetrofitBuilder;

public class SearchActivityPresenter extends BasePresenter {

    private final RetrofitBuilder builder;
    private SearchResultsInterface callBack;

    @Inject
    public SearchActivityPresenter(RetrofitBuilder builder) {
        this.builder = builder;
    }

    public void search(String query) {

        Disposable searchObservable = builder.getGiantBombAPI()
                .gameSearch(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        searchResponse -> {
                            if (isSearchResultAvailable(searchResponse)) {
                                displayResultsInAdapter(searchResponse.getResults());
                            } else handleError(new NoResultsFound());
                        },
                        this::handleError
                );
        compositeDisposable.add(searchObservable);
    }

    private boolean isSearchResultAvailable(SearchResponse searchResponse) {
        return searchResponse.getError().equalsIgnoreCase("ok")
                && Integer.parseInt(searchResponse.getStatus_code()) == 1
                && searchResponse.getResults() != null && !searchResponse.getResults().isEmpty();
    }

    private void handleError(Throwable throwable) {
        if (throwable instanceof IOException) {
            callBack.showNoNetworkError();
        } else if (throwable instanceof NoResultsFound) {
            callBack.showNoResultsFoundError();
        }
    }

    private void displayResultsInAdapter(List<SearchItem> results) {
        callBack.enableRecyclerView();
        callBack.showSearchResults(results);
    }

    public void setView(SearchResultsInterface callBack) {
        this.callBack = callBack;
    }

    public void onDestroy() {
        unsubscribe();
    }

    public interface SearchResultsInterface {

        void enableRecyclerView();

        void showSearchResults(@NonNull List<SearchItem> results);

        void showNoResultsFoundError();

        void showNoNetworkError();
    }

}
