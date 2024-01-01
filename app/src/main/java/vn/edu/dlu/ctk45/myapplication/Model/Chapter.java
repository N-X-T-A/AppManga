package vn.edu.dlu.ctk45.myapplication.Model;

public class Chapter {
    private int id;
    private int numChapter;
    private int Page;

    public Chapter(int id, int numChapter, int page) {
        this.id = id;
        this.numChapter = numChapter;
        Page = page;
    }

    public int getId() {
        return id;
    }

    public int getNumChapter() {
        return numChapter;
    }

    public int getPage() {
        return Page;
    }

    @Override
    public String toString() {
        return "Chapter " + numChapter;
    }
}
