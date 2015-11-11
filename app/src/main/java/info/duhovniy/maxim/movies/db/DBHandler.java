package info.duhovniy.maxim.movies.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.preference.PreferenceManager;
import android.util.Log;

import java.net.MalformedURLException;

/**
 * Created by maxduhovniy on 10/20/15.
 */
public class DBHandler {

    private static String baseName;

    private DBHelper dbHelper;


    public static void setBaseName(String s) {
        baseName = s;
    }

    public DBHandler(Context context) {

        baseName = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(DBConstants.TABLE_NAME_MARKER, DBConstants.DEFAULT_TABLE_NAME);

        dbHelper = new DBHelper(context, DBConstants.BASE_NAME, null, DBConstants.DATABASE_VERSION);

    }

    // Get all movies
    public Cursor getAllMovies() {

        Cursor result;

        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            result = db.query(baseName, null, null, null, null, null, null);
        } catch (SQLiteException e) {
            Log.e(DBConstants.LOG_TAG, e.getMessage());
            throw e;
        }

        return result;
    }

    // Get a single movies base on OMDB ID
    public Movie getMovie(String omdbID) throws MalformedURLException {
        Movie movie = null;

        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            Cursor cursor = db.query(baseName, null, DBConstants.MOVIE_OMDB_ID
                    + "=?", new String[]{String.valueOf(omdbID)}, null, null, null, null);

            // Check if the movie was found
            if (cursor.moveToFirst()) {
                movie = new Movie(cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8));
            }
            cursor.close();
        } catch (SQLiteException e) {
            Log.e(DBConstants.LOG_TAG, e.getMessage());
            throw e;
        }

        return movie;
    }

    private boolean isMovieExist(Movie movie) {
        boolean result = false;

        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            Cursor cursor = db.query(baseName, null, DBConstants.MOVIE_OMDB_ID + "=?",
                    new String[]{String.valueOf(movie.getOmdbId())}, null, null, null, null);

            // Check if the movie was found
            if (cursor.moveToFirst()) {
                result = true;
            }
            cursor.close();
        } catch (SQLiteException e) {
            Log.e(DBConstants.LOG_TAG, e.getMessage());
            throw e;
        }

        return result;
    }

    // Adding a new movie
    // if omdbID exist dont do nothing
    // return false if not succeed
    public boolean addMovie(Movie movie) {
        boolean result = false;

        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            if (!isMovieExist(movie)) {
                result = true;
                ContentValues newMovieValues = new ContentValues();

                newMovieValues.put(DBConstants.MOVIE_OMDB_ID, movie.getOmdbId());
                newMovieValues.put(DBConstants.MOVIE_TITLE, movie.getTitle());
                newMovieValues.put(DBConstants.MOVIE_YEAR, movie.getYear());
                newMovieValues.put(DBConstants.MOVIE_TYPE, movie.getType());
                newMovieValues.put(DBConstants.MOVIE_PLOT, movie.getPlot());
                newMovieValues.put(DBConstants.MOVIE_URL_POSTER, movie.getUrlPoster());
                newMovieValues.put(DBConstants.MOVIE_URL_TRAILER, movie.getUrlTrailer());
                newMovieValues.put(DBConstants.MOVIE_LOCAL_POSTER_PATH, movie.getLocalPosterPath());

                // Inserting the new search_list_row, or throwing an exception if an error occurred

                db.insertOrThrow(baseName, null, newMovieValues);
            }
        } catch (SQLiteException ex) {
            Log.e(DBConstants.LOG_TAG, ex.getMessage());
            throw ex;
        }

        return result;
    }

    // Update a movie
    // return true if succeed
    public int updateMovie(Movie movie, int id) {
        int result;

        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues newMovieValues = new ContentValues();

            newMovieValues.put(DBConstants.MOVIE_OMDB_ID, movie.getOmdbId());
            newMovieValues.put(DBConstants.MOVIE_TITLE, movie.getTitle());
            newMovieValues.put(DBConstants.MOVIE_YEAR, movie.getYear());
            newMovieValues.put(DBConstants.MOVIE_TYPE, movie.getType());
            newMovieValues.put(DBConstants.MOVIE_PLOT, movie.getPlot());
            newMovieValues.put(DBConstants.MOVIE_URL_POSTER, movie.getUrlPoster());
            newMovieValues.put(DBConstants.MOVIE_URL_TRAILER, movie.getUrlTrailer());
            newMovieValues.put(DBConstants.MOVIE_LOCAL_POSTER_PATH, movie.getLocalPosterPath());

            result = db.update(baseName, newMovieValues, DBConstants.MOVIE_ID
                    + "=?", new String[]{String.valueOf(id)});
        } catch (SQLiteException ex) {
            Log.e(DBConstants.LOG_TAG, ex.getMessage());
            throw ex;
        }

        return result;
    }

    // Delete a movie
    public void deleteMovie(Movie movie) {

        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(baseName, DBConstants.MOVIE_TITLE + "=?",
                    new String[]{String.valueOf(movie.getTitle())});
        } catch (SQLiteException ex) {
            Log.e(DBConstants.LOG_TAG, ex.getMessage());
            throw ex;
        }
    }

    // Get movie ID in Database by movie title, but only first movie in base with this id!!!
    // return -1 if not succeed
    public int getMovieId(Movie movie) {
        int id = -1;

        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(baseName, null, DBConstants.MOVIE_TITLE + "=?",
                    new String[]{String.valueOf(movie.getTitle())}, null, null, null, null);

            // Check if the movie was found
            if (cursor.moveToFirst()) {
                id = cursor.getInt(0);
            }
            cursor.close();
        } catch (SQLiteException ex) {
            Log.e(DBConstants.LOG_TAG, ex.getMessage());
            throw ex;
        }

        return id;
    }

}
