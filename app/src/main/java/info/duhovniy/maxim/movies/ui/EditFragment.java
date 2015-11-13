package info.duhovniy.maxim.movies.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import info.duhovniy.maxim.movies.R;
import info.duhovniy.maxim.movies.db.DBHandler;
import info.duhovniy.maxim.movies.db.Movie;
import info.duhovniy.maxim.movies.network.NetworkConstants;

/**
 * Created by maxduhovniy on 02/11/2015.
 */
public class EditFragment extends Fragment {

    private EditText titleText, plotText, posterURLText;
    private Button buttonClear, buttonSave, buttonShow;
    private ImageView moviePoster;
    private View rootView;
    private String mOmdbID;
    private Movie mMovie;
    private DBHandler mHandler;

    public interface onEditMovie {
        void editMovie(Movie movie);
    }

    public void setOmdbID(String omdbID) {
        mOmdbID = omdbID;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_edit, container, false);
        titleText = (EditText) rootView.findViewById(R.id.title_edit_text);
        plotText = (EditText) rootView.findViewById(R.id.plot_edit_text);
        posterURLText = (EditText) rootView.findViewById(R.id.url_edit_text);
        buttonClear = (Button) rootView.findViewById(R.id.clear_button);
        buttonSave = (Button) rootView.findViewById(R.id.save_button);
        buttonShow = (Button) rootView.findViewById(R.id.show_button);
        moviePoster = (ImageView) rootView.findViewById(R.id.poster_edit_image);

        mHandler = new DBHandler(getContext());

        if (savedInstanceState != null) {
            mOmdbID = savedInstanceState.getString(UIConstants.EDIT_ID);
            fillEditFields();
        }

        buttonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SetMovie().setMoviePoster(posterURLText.getText().toString());
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearEditFields();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!saveMovie())
                    Toast.makeText(getContext(), "Movie already exist!", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    @Override
    public void onViewStateRestored(Bundle state) {
        if (state != null) {
            mOmdbID = state.getString(UIConstants.EDIT_ID);
            fillEditFields();
        }
        super.onViewStateRestored(state);
    }

    public void fillEditFields() {

        if (rootView != null) {
            new SetMovie(mOmdbID);
        }
    }

    public void clearEditFields() {

        if (rootView != null) {
            titleText.setText("");
            plotText.setText("");
            posterURLText.setText("");
            moviePoster.setVisibility(View.INVISIBLE);
        }
    }

    public boolean saveMovie() {

        boolean result = false;

        if(mMovie != null)
            if(mHandler.addMovie(mMovie))
                result = true;

        return result;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mOmdbID != null)
            outState.putString(UIConstants.EDIT_ID, mOmdbID);
    }

    class SetMovie {

        public SetMovie() {

        }

        public SetMovie(String omdbID) {

            String url = "http://www.omdbapi.com/?i=" + omdbID;
            MyTask task = new MyTask();
            task.execute(url);
        }

        public void setMoviePoster(String posterURL) {
            moviePoster.setVisibility(View.VISIBLE);
            // Set image using Picasso library
            Picasso.with(getContext()).setIndicatorsEnabled(true);
            Picasso.with(getContext())
                    .load(posterURL)
                    .placeholder(R.drawable.placeholder)
                    .resize(200, 200)
                    .centerCrop()
                    .into(moviePoster);
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

                    titleText.setText(object.getString("Title"));
                    posterURLText.setText(object.getString("Poster"));
                    plotText.setText(object.getString("Plot"));

                    moviePoster.setVisibility(View.VISIBLE);
                    // Set image using Picasso library
                    Picasso.with(getContext()).setIndicatorsEnabled(true);
                    Picasso.with(getContext())
                            .load(object.getString("Poster"))
                            .placeholder(R.drawable.placeholder)
                            .resize(200, 200)
                            .centerCrop()
                            .into(moviePoster);
                    String title = object.getString("Title");
                    String year = object.getString("Year");
                    String imdbID = object.getString("imdbID");
                    String type = object.getString("Type");
                    String posterURL = object.getString("Poster");
                    String plot = object.getString("Plot");


                    mMovie = new Movie(imdbID, title, year, type, plot, posterURL, null, null);

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

    }
}