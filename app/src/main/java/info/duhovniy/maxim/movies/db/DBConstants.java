package info.duhovniy.maxim.movies.db;

/**
 * Created by maxduhovniy on 10/20/15.
 */
public class DBConstants {

    public final static int DATABASE_VERSION = 1;
    public static final String BASE_NAME = "moviesDB";

    public final static String MOVIE_ID = "_id";
    public final static String MOVIE_OMDB_ID = "omdbID";
    public final static String MOVIE_TITLE = "title";
    public final static String MOVIE_YEAR = "year";
    public final static String MOVIE_TYPE = "type";
    public final static String MOVIE_PLOT = "plot";
    public final static String MOVIE_URL_POSTER = "urlPoster";
    public final static String MOVIE_URL_TRAILER = "urlTrailer";
    public final static String MOVIE_LOCAL_POSTER_PATH = "localPosterPath";
    public final static String MOVIE_WATCHED = "watched";

    public static final String LOG_TAG = "Movies Database Error";

    public static final String TABLE_NAME_MARKER = "newBase";
    public static final String DEFAULT_TABLE_NAME = "guestbase";
    public static final String USER_NAME_MARKER = "newUser";
    public static final String DEFAULT_USER_NAME = "guest";

}