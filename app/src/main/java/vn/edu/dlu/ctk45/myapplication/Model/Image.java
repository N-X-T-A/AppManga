package vn.edu.dlu.ctk45.myapplication.Model;

public class Image {
    private int Num;
    private String imageUrl;

    public Image(int num, String imageUrl) {
        Num = num;
        this.imageUrl = imageUrl;
    }

    public int getNum() {
        return Num;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
