package com.example.bama.qrcode.Model;

public class Model_Krj {

    private String id_krj ;
    private String nama_krj;
    private String nama_makanan_krj;
    private String url;
    private String jumlah_krj;
    private String harga_krj;
    private String subtotal;

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }




    public String getId_krj() {
        return id_krj;
    }

    public void setId_krj(String id_krj) {
        this.id_krj = id_krj;
    }

    public String getNama_krj() {
        return nama_krj;
    }

    public void setNama_krj(String nama_krj) {
        this.nama_krj = nama_krj;
    }

    public String getNama_makanan_krj() {
        return nama_makanan_krj;
    }

    public void setNama_makanan_krj(String nama_makanan_krj) {
        this.nama_makanan_krj = nama_makanan_krj;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJumlah_krj() {
        return jumlah_krj;
    }

    public void setJumlah_krj(String jumlah_krj) {
        this.jumlah_krj = jumlah_krj;
    }

    public String getHarga_krj() {
        return harga_krj;
    }

    public void setHarga_krj(String harga_krj) {
        this.harga_krj = harga_krj;
    }





    public Model_Krj(String id_krj, String nama_krj, String nama_makanan_krj, String url, String jumlah_krj, String harga_krj, String subtotal) {

        this.id_krj = id_krj;
        this.nama_krj= nama_krj;
        this.nama_makanan_krj = nama_makanan_krj;
        this.harga_krj= harga_krj;
        this.url = url;
        this.jumlah_krj = jumlah_krj;
        this.subtotal = subtotal ;

    }

    public Model_Krj() {
    }








}
