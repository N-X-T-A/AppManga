package vn.edu.dlu.ctk45.myapplication.Model;

public class Genre {
    private int idTheLoai;
    private String TenTheLoai;

    public Genre(int idTheLoai, String tenTheLoai) {
        this.idTheLoai = idTheLoai;
        TenTheLoai = tenTheLoai;
    }

    public int getIdTheLoai() {
        return idTheLoai;
    }

    public String getTenTheLoai() {
        return TenTheLoai;
    }
}
