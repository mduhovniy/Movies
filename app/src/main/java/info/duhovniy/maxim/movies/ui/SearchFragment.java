package info.duhovniy.maxim.movies.ui;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import info.duhovniy.maxim.movies.R;
import info.duhovniy.maxim.movies.db.Movie;
import info.duhovniy.maxim.movies.network.NetworkConstants;

/**
 * Created by maxduhovniy on 25/10/2015.
 */
public class SearchFragment extends Fragment {

    private EditFragment.onEditMovie mEditMovie;

    private RecyclerView rv;
    private EditText queryText;
    private Button buttonSearch;
    private View rootView;
    private String mQuery;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mEditMovie = (EditFragment.onEditMovie) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onEditMovie!");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        queryText = (EditText) rootView.findViewById(R.id.editText);
        buttonSearch = (Button) rootView.findViewById(R.id.search_button);

        rv = (RecyclerView) rootView.findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));

        if(savedInstanceState != null) {
            mQuery = savedInstanceState.getString(UIConstants.SEARCH_QUERY, "");
            showSearch();
        }
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSearch.setVisibility(View.GONE);
                queryText.setVisibility(View.GONE);
                mQuery = queryText.getText().toString();
                showSearch();

            }
        });

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mQuery != null)
            outState.putString(UIConstants.SEARCH_QUERY, mQuery);
    }

    @Override
    public void onViewStateRestored(Bundle state) {
        if (state != null) {
            mQuery = state.getString(UIConstants.SEARCH_QUERY, "");
            showSearch();
        }
        super.onViewStateRestored(state);
    }

    public void setQuery(String query) {
        mQuery = query;
    }

    public void showSearch() {
        if (rootView != null) {

            try {
                buttonSearch.setVisibility(View.GONE);
                queryText.setVisibility(View.GONE);
                new SearchMovie(mQuery, rv, rootView.getContext());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }

    class SearchMovie {

        private ArrayList<Movie> searchResult = new ArrayList<>();
        private RecyclerView mRecyclerView;
        private Context mContext;

        public SearchMovie(String searchRequest, RecyclerView rv, Context context)
                throws UnsupportedEncodingException {
            String url = "http://www.omdbapi.com/?s=" + URLEncoder.encode(searchRequest, "utf-8");
            mRecyclerView = rv;
            mContext = context;
            MyTask task = new MyTask();
            task.execute(url);
        }

        class MyTask extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
            }

            @Override
            protected String doInBackground(String... strings) {

                return sendHttpRequest(strings[0]);
            }

            @Override
            protected void onPostExecute(String s) {

                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray array = object.getJSONArray("Search");

                    for (int i = 0; i < array.length(); i++) {
                        String title = array.getJSONObject(i).getString("Title");
                        String year = array.getJSONObject(i).getString("Year");
                        String imdbID = array.getJSONObject(i).getString("imdbID");
                        String type = array.getJSONObject(i).getString("Type");
                        String posterURL = array.getJSONObject(i).getString("Poster");

                        Movie movie = new Movie(imdbID, title, year, type, null, posterURL, null, null);
                        searchResult.add(movie);

                    }

                    SearchListAdapter searchListAdapter = new SearchListAdapter(searchResult, mRecyclerView, mContext);
                    mRecyclerView.setAdapter(searchListAdapter);

                } catch (JSONException e) {
                    Log.e(NetworkConstants.LOG_TAG, e.getMessage());
                }

            }

            private String sendHttpRequest(String urlString) {

                BufferedReader input = null;
                HttpURLConnection httpCon = null;
                InputStream input_stream = null;
                InputStreamReader input_stream_reader = null;
                StringBuilder response = new StringBuilder();

                try {
                    URL url = new URL(urlString);
                    httpCon = (HttpURLConnection) url.openConnection();

                    if (httpCon.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        Log.e(NetworkConstants.LOG_TAG, "Cannot Connect to : " + urlString);
                        return null;
                    }

                    input_stream = httpCon.getInputStream();
                    input_stream_reader = new InputStreamReader(input_stream);
                    input = new BufferedReader(input_stream_reader);
                    String line;

                    while ((line = input.readLine()) != null) {
                        response.append(line).append("\n");
                    }
                } catch (Exception e) {
                    Log.e(NetworkConstants.LOG_URL_TAG, e.getMessage());
                } finally {
                    if (input != null) {
                        try {
                            input_stream_reader.close();
                            input_stream.close();
                            input.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        httpCon.disconnect();
                    }
                }
                return response.toString();
            }

        }

        class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.RecyclerViewHolder> {

            private ArrayList<Movie> listMovies;
            private View mView;
            private Context mContext;

            // Provide a direct reference to each of the views within a data item
            // Used to cache the views within the item layout for fast access
            public class RecyclerViewHolder extends RecyclerView.ViewHolder {

                public TextView movieTitle, movieYear, movieType;
                public ImageView moviePoster;
                public LinearLayout mLayout;
                public CardView cv;

                // We also create a constructor that accepts the entire item search_list_row
                // and does the view lookups to find each subview
                public RecyclerViewHolder(View itemView) {
                    // Stores the itemView in a public final member variable that can be used
                    // to access the context from any ViewHolder instance.
                    super(itemView);

                    movieTitle = (TextView) itemView.findViewById(R.id.title_text);
                    movieYear = (TextView) itemView.findViewById(R.id.year_text);
                    movieType = (TextView) itemView.findViewById(R.id.type_text);
                    moviePoster = (ImageView) itemView.findViewById(R.id.poster_image);
                    cv = (CardView) itemView.findViewById(R.id.cv);
                    cv.setRadius(10);
                    mLayout = (LinearLayout) itemView.findViewById(R.id.line_movie);

                }
            }

            public SearchListAdapter(ArrayList<Movie> list, View v, Context context) {
                listMovies = list;
                mView = v;
                mContext = context;
            }

            // Involves inflating a layout from XML and returning the holder
            @Override
            public SearchListAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                // Inflate the custom layout
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_row, parent, false);

                // Return a new holder instance
                return new RecyclerViewHolder(v);

            }

            // Involves populating data into the item through holder
            @Override
            public void onBindViewHolder(RecyclerViewHolder viewHolder, final int position) {

                // Set item views based on the data model
                viewHolder.movieTitle.setText(listMovies.get(position).getTitle());
                viewHolder.movieYear.setText(listMovies.get(position).getYear());
                viewHolder.movieType.setText(listMovies.get(position).getType());

                // Set image using Picasso library
                Picasso.with(mContext).setIndicatorsEnabled(true);
                Picasso.with(mContext)
                        .load(listMovies.get(position).getUrlPoster())
                        .placeholder(R.drawable.placeholder)
                        .resize(100, 100)
                        .centerCrop()
                        .into(viewHolder.moviePoster);

                viewHolder.mLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // transfer ID to MainActivity
                        mEditMovie.editMovie(listMovies.get(position));

                    }

                });

                viewHolder.mLayout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        final View.OnClickListener clickListener = new View.OnClickListener() {
                            public void onClick(View v) {
                                listMovies.remove(position);
                                notifyDataSetChanged();
                            }
                        };

                        Snackbar.make(mView, "Delete " + listMovies.get(position).getTitle() + "?",
                                Snackbar.LENGTH_LONG).setAction("Ok", clickListener).show();

                        return true;
                    }
                });

            }


            // Return the total count of items
            @Override
            public int getItemCount() {
                return listMovies.size();
            }

        }


    }

}


