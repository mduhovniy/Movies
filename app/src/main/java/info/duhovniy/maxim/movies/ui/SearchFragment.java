package info.duhovniy.maxim.movies.ui;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
    private SharedPreferences mPref;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        rootView = inflater.inflate(R.layout.fragment_search, null);
        editText = (EditText) rootView.findViewById(R.id.editText);
        but = (Button) rootView.findViewById(R.id.searchButton);

        rv = (RecyclerView) rootView.findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    but.setVisibility(View.GONE);
                    editText.setVisibility(View.GONE);
                    new SearchMovie(editText.getText().toString(), rv, rootView.getContext());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        if(mPref.contains(UIConstants.SEARCH_QUERY)) {
            editText.setText(mPref.getString(UIConstants.SEARCH_QUERY, ""));
            editText.setVisibility(View.VISIBLE);
            but.setVisibility(View.VISIBLE);
        }
        super.onStart();
    }
}


