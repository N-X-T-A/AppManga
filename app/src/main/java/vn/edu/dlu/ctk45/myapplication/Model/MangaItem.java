package vn.edu.dlu.ctk45.myapplication.Model;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;

public class MangaItem{
    private int id;
    private String title;
    private Date releaseDate;
    private String imageUrl;
    private String authorName;
    private String genre;
    private int favorites;
    boolean isChecked;

    public MangaItem(int id, String title, Date releaseDate, String imageUrl, String authorName, String genre,int favorites) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.imageUrl = imageUrl;
        this.authorName = authorName;
        this.genre = genre;
        this.favorites = favorites;
    }

    public MangaItem(int id,String imageUrl,String title, String authorName) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.authorName = authorName;
    }
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getGenre() {
        return genre;
    }

    public int getFavorites() {
        return favorites;
    }

    public boolean isChecked() {
        return isChecked;
    }
}