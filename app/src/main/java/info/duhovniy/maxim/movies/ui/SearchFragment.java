package info.duhovniy.maxim.movies.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import info.duhovniy.maxim.movies.R;

/**
 * Created by maxduhovniy on 25/10/2015.
 */
public class SearchFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, null);

        return rootView;

    }
}
