package vn.edu.dlu.ctk45.myapplication.Model;

import java.util.List;

public class ReadManga {
    private int id;
    private String title;
    private int NumChapter;
    private List<Image> images;
    private List<Chapter> chapters;
    public ReadManga(int id, String title, int numChapter, List<Image> images,List<Chapter> chapters) {
        this.id = id;
        this.title = title;
        NumChapter = numChapter;
        this.images = images;
        this.chapters = chapters;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getNumChapter() {
        return NumChapter;
    }

    public List<Image> getImages() {
        return images;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }
}
