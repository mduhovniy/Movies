package info.duhovniy.maxim.movies.db;

/**
 * Created by maxduhovniy on 10/20/15.
 */
public class Movie  {

    private String omdbId;
    private String title;
    private String year;
    private String type;
    private String plot;
    private String urlPoster;
    private String urlTrailer;
    private String localPosterPath;

    public Movie() {
        omdbId = "N/A";
        title = "N/A";
    }

    public Movie(String omdbId, String title, String year, String type, String plot, String urlPoster,
                 String urlTrailer, String localPosterPath) {
        this.omdbId = omdbId;
        this.title = title;
        this.year = year;
        this.type = type;
        this.plot = plot;
        this.urlPoster = urlPoster;
        this.urlTrailer = urlTrailer;
        this.localPosterPath = localPosterPath;
    }

    public void setOmdbId(String omdbId) {
        this.omdbId = omdbId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public void setUrlPoster(String urlPoster) {
        this.urlPoster = urlPoster;
    }

    public void setUrlTrailer(String urlTrailer) {
        this.urlTrailer = urlTrailer;
    }

    public void setLocalPosterPath(String localPosterPath) {
        this.localPosterPath = localPosterPath;
    }

    public String getOmdbId() {
        return omdbId;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getType() {
        return type;
    }

    public String getPlot() {
        return plot;
    }

    public String getUrlPoster() {
        return urlPoster;
    }

    public String getUrlTrailer() {
        return urlTrailer;
    }

    public String getLocalPosterPath() {
        return localPosterPath;
    }

}
