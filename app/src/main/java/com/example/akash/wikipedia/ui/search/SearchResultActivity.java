package com.example.akash.wikipedia.ui.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akash.wikipedia.R;
import com.example.akash.wikipedia.data.remote.model.Page;
import com.example.akash.wikipedia.ui.base.BaseActivity;
import com.example.akash.wikipedia.ui.webview.WebViewActivity;
import com.example.akash.wikipedia.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Akash Verma on 13/10/18.
 */
public class SearchResultActivity extends BaseActivity implements SearchResultMvpView, RecyclerViewAdapter.OnItemClickListener {

    @Inject
    public SearchResultMvpPresenter mPresenter;

    private RecyclerViewAdapter adapter;

    @BindView(R.id.tv_error)
    public TextView tvError;

    @BindView(R.id.rv_search_result)
    public RecyclerView rvSearchResult;

    @BindView(R.id.et_search)
    public EditText etSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("");
        invalidateOptionsMenu();

        getActivityComponent().inject(this);

        mPresenter.onAttach(this);
        setRecyclerView();

        softKeyboardSearchAction();
    }

    private void softKeyboardSearchAction() {
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                searchAction();
                closeSoftKeyBoard();
                return true;

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDetach();
        }
    }

    @Override
    public void updateRecyclerView(List<Page> pageList) {
        tvError.setVisibility(View.GONE);
        rvSearchResult.setVisibility(View.VISIBLE);
        rvSearchResult.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new RecyclerViewAdapter(this, pageList);
        rvSearchResult.setAdapter(adapter);
    }

    @Override
    public void setRecyclerView() {
        tvError.setVisibility(View.GONE);
        rvSearchResult.setVisibility(View.VISIBLE);
        rvSearchResult.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new RecyclerViewAdapter(this, new ArrayList<Page>());
        rvSearchResult.setAdapter(adapter);
    }

    public void showNoDataErrorMsg() {
        tvError.setVisibility(View.VISIBLE);
        rvSearchResult.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.search:
                searchAction();
                closeSoftKeyBoard();
                break;
        }

        return true;
    }

    private void searchAction() {
        mPresenter.getSearchResult(etSearch.getText().toString());
    }

    public void closeSoftKeyBoard() {
        InputMethodManager in = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if (in != null) {
            in.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
        }
    }

    @Override
    public void onItemClick(String title) {
        if(isNetworkConnected()){
            Intent webActivityIntent = new Intent(this, WebViewActivity.class);
            webActivityIntent.putExtra(AppConstants.SEARCH_ITEM, title.replace(" ", "_"));
            startActivity(webActivityIntent);
        } else {
            Toast.makeText(this, "Please connect to internet", Toast.LENGTH_SHORT).show();
        }
    }
}
