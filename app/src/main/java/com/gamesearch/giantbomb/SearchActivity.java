package com.gamesearch.giantbomb;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;

import java.util.List;

import javax.inject.Inject;

import Response.SearchItem;
import fragments.ErrorFragment;
import presenter.SearchActivityPresenter;
import util.AndroidUtil;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SearchActivityPresenter.SearchResultsInterface {

    @Inject
    SearchActivityPresenter presenter;

    private RecyclerView resultsView;
    private View container;
    private SearchAdapter searchAdapter;
    private AlertDialog errorDialog;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultsView = findViewById(R.id.resultsView);
        container = findViewById(R.id.container);
        progressBar = findViewById(R.id.progressSpinner);
        injectDependencies();
        setRecyclerView();
        displayDefaultFragment();
        setUpPresenter();
    }

    private void setUpPresenter() {
        presenter.setView(this);
    }

    private void setRecyclerView() {
        searchAdapter = new SearchAdapter(null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        resultsView.setLayoutManager(layoutManager);
        resultsView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        resultsView.setAdapter(searchAdapter);
    }

    private void injectDependencies() {
        ((GiantBombApplication) getApplication())
                .getMyComponent()
                .inject(this);
    }

    private void displayDefaultFragment() {
        disableRecyclerView();
        ErrorFragment fragment = ErrorFragment.newInstance(getString(R.string.default_message));
        performFragmentTransaction(fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        presenter.search(query);
        AndroidUtil.hideKeyboard(this);
        displayProgressBar();
        return true;
    }

    private void displayProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void enableRecyclerView() {
        hideProgressBar();
        resultsView.setVisibility(View.VISIBLE);
        container.setVisibility(View.GONE);
    }

    private void disableRecyclerView() {
        hideProgressBar();
        resultsView.setVisibility(View.GONE);
        container.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSearchResults(@NonNull List<SearchItem> results) {
        searchAdapter.updateResults(results);
    }

    @Override
    public void showNoResultsFoundError() {
        ErrorFragment fragment = ErrorFragment.newInstance(getString(R.string.no_results_found_dialog));
        disableRecyclerView();
        performFragmentTransaction(fragment);
    }

    private void performFragmentTransaction(ErrorFragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment);
        transaction.commit();
    }

    @Override
    public void showNoNetworkError() {
        hideProgressBar();
        errorDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.error_dialog_title)
                .setMessage(R.string.error_dialog_message)
                .setNeutralButton(R.string.dialog_ok_button, null).show();

    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        dismissErrorDialog();
        super.onDestroy();
    }

    private void dismissErrorDialog() {
        if (errorDialog != null && errorDialog.isShowing()) {
            errorDialog.dismiss();
        }
    }
}
