package com.example.akash.wikipedia.ui.search;

import com.example.akash.wikipedia.data.remote.model.Page;
import com.example.akash.wikipedia.ui.base.MvpView;

import java.util.List;

/**
 * Created by Akash Verma on 13/10/18.
 */
public interface SearchResultMvpView extends MvpView {

    void updateRecyclerView(List<Page> pageList);

    void setRecyclerView();

    void showNoDataErrorMsg();

}
