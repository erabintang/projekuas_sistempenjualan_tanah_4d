package model;

import java.io.Serializable;

public class Tanah implements Serializable {
    private String id;
    private String lokasi;
    private double luas;
    private double harga;
    private String status;

    public Tanah(String id, String lokasi, double luas, double harga, String status) {
        this.id = id;
        this.lokasi = lokasi;
        this.luas = luas;
        this.harga = harga;
        this.status = status;
    }

    // Getter & Setter
    public String getId() { return id; }
    public String getLokasi() { return lokasi; }
    public double getLuas() { return luas; }
    public double getHarga() { return harga; }
    public String getStatus() { return status; }

    public void setStatus(String status) {
        this.status = status;
    }
}
