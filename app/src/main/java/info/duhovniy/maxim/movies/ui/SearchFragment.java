package info.duhovniy.maxim.movies.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;

import info.duhovniy.maxim.movies.R;
import info.duhovniy.maxim.movies.network.SearchMovie;

/**
 * Created by maxduhovniy on 25/10/2015.
 */
public class SearchFragment extends Fragment {

    private RecyclerView rv;
    private EditText editText;
    private Button but;
    private View rootView;
    private String mQuery;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        editText = (EditText) rootView.findViewById(R.id.editText);
        but = (Button) rootView.findViewById(R.id.search_button);

        rv = (RecyclerView) rootView.findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));

/*
        if(!editText.getText().equals("")) {
            mQuery = editText.getText().toString();
            showSearch(mQuery);
        }
*/


        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                but.setVisibility(View.GONE);
                editText.setVisibility(View.GONE);
                mQuery = editText.getText().toString();
                showSearch();

            }
        });

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        if (mQuery != null)
            state.putString("info.duhovniy.maxim.movies.SEARCH_STATE", mQuery);
    }

    @Override
    public void onViewStateRestored(Bundle state) {
        super.onViewStateRestored(state);
        if (state != null) {
            mQuery = state.getString("info.duhovniy.maxim.movies.SEARCH_STATE", "");
            showSearch();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mQuery = savedInstanceState.getString("info.duhovniy.maxim.movies.SEARCH_STATE", "");
            showSearch();
        }

    }

    public void setQuery(String query) {
        mQuery = query;
    }

    public void showSearch() {
        if (rootView != null) {

            try {
                but.setVisibility(View.GONE);
                editText.setVisibility(View.GONE);
                new SearchMovie(mQuery, rv, rootView.getContext());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }
}


