package vn.edu.dlu.ctk45.myapplication.Model;

import java.util.Date;
import java.util.List;

public class MangaItemDetail {
    private int id;
    private String title;
    private Date releaseDate;
    private int favorites;
    private String imageUrl;
    private String describe;
    private String authorName;
    private String genre;
    private List<Chapter> chapters;

    public MangaItemDetail(int id, String title, Date releaseDate, int favorites, String imageUrl, String describe, String authorName, String genre, List<Chapter> chapters) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.favorites = favorites;
        this.imageUrl = imageUrl;
        this.describe = describe;
        this.authorName = authorName;
        this.genre = genre;
        this.chapters = chapters;
    }

    public MangaItemDetail(List<Chapter> chapters) {
        this.chapters = chapters;
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

    public int getFavorites() {
        return favorites;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getDescribe() {
        return describe;
    }

    public String getGenre() {
        return genre;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }
}
