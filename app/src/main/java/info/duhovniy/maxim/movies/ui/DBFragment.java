package info.duhovniy.maxim.movies.ui;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;

import info.duhovniy.maxim.movies.R;
import info.duhovniy.maxim.movies.db.DBHandler;
import info.duhovniy.maxim.movies.db.Movie;

/**
 * Created by maxduhovniy on 02/11/2015.
 */
public class DBFragment extends Fragment {

    private EditFragment.onEditMovie mEditMovie;

    private Cursor mCursor;
    private RecyclerView rv;
    private View rootView;
    private DBHandler mHandler;
    private BaseListAdapter mAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mEditMovie = (EditFragment.onEditMovie) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onEditMovieFromBase");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_base, container, false);

        rv = (RecyclerView) rootView.findViewById(R.id.base_rv);
        rv.setHasFixedSize(true);

        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));

        mHandler = new DBHandler(getContext());

        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean(UIConstants.BASE_ID, false)) {
                // TODO: decide what to refresh
            }
        }

        showBase();

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mCursor != null)
            outState.putBoolean(UIConstants.BASE_ID, true);
    }

    @Override
    public void onViewStateRestored(Bundle state) {
        if (state != null) {
            if (state.getBoolean(UIConstants.BASE_ID, false))
                showBase();
        }
        super.onViewStateRestored(state);
    }

    public void showBase() {
        if (rootView != null) {
            mAdapter = new BaseListAdapter();
            rv.setAdapter(mAdapter);
        }

    }


    class BaseListAdapter extends RecyclerView.Adapter<BaseListAdapter.RecyclerViewHolder> {
/*

        private ArrayList<Movie> listMovies;
        private View mView;
        private Context mContext;
*/

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

        public BaseListAdapter() {
            mCursor = mHandler.getAllMovies();
        }

        // Involves inflating a layout from XML and returning the holder
        @Override
        public BaseListAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            // Inflate the custom layout
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_row, parent, false);

            // Return a new holder instance
            return new RecyclerViewHolder(v);

        }

        // Involves populating data into the item through holder
        @Override
        public void onBindViewHolder(RecyclerViewHolder viewHolder, final int position) {

            if (mCursor.moveToPosition(position)) {
                // Set item views based on the data model
                Movie movie = new Movie(mCursor.getString(1),
                        mCursor.getString(2),
                        mCursor.getString(3),
                        mCursor.getString(4),
                        mCursor.getString(5),
                        mCursor.getString(6),
                        mCursor.getString(7),
                        mCursor.getString(8));

                viewHolder.movieTitle.setText(movie.getTitle());
                viewHolder.movieYear.setText(movie.getYear());
                viewHolder.movieType.setText(movie.getType());

                // Set image using Picasso library
                Picasso.with(getContext()).setIndicatorsEnabled(true);
                Picasso.with(getContext())
                        .load(movie.getUrlPoster())
                        .placeholder(R.drawable.placeholder)
                        .resize(100, 100)
                        .centerCrop()
                        .into(viewHolder.moviePoster);

            }

            viewHolder.mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mCursor.moveToPosition(position)) {
                        // transfer ID to MainActivity
                        try {
                            mEditMovie.editMovie(mHandler.getMovie(mCursor.getString(1)));
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                }

            });

            viewHolder.mLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    if (mCursor.moveToPosition(position)) {

                        final View.OnClickListener clickListener = new View.OnClickListener() {
                            public void onClick(View v) {
                                try {
                                    mHandler.deleteMovie(mHandler.getMovie(mCursor.getString(1)));
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        };

                        Snackbar.make(rootView, "Delete " + mCursor.getString(2) + "?",
                                Snackbar.LENGTH_LONG).setAction("Ok", clickListener).show();
                    }
                    return true;
                }
            });

        }


        // Return the total count of items
        @Override
        public int getItemCount() {
            return mCursor.getCount();
        }

    }


}





