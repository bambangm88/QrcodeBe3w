package com.example.bama.qrcode.Model;

public class model_pesanan{

    private String id_pesanan ;
    private String nama_pesanan;
    private String nama_makanan_pesanan;
    private String url;
    private String jumlah_pesanan;
    private String harga_pesanan;
    private String subtotal;

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }




    public String getId_pesanan() {
        return id_pesanan;
    }

    public void setId_pesanan(String id_pesanan) {
        this.id_pesanan = id_pesanan;
    }

    public String getNama_pesanan() {
        return nama_pesanan;
    }

    public void setNama_pesanan(String nama_pesanan) {
        this.nama_pesanan = nama_pesanan;
    }

    public String getNama_makanan_pesanan() {
        return nama_makanan_pesanan;
    }

    public void setNama_makanan_pesanan(String nama_makanan_pesanan) {
        this.nama_makanan_pesanan = nama_makanan_pesanan;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJumlah_pesanan() {
        return jumlah_pesanan;
    }

    public void setJumlah_pesanan(String jumlah_pesanan) {
        this.jumlah_pesanan = jumlah_pesanan;
    }

    public String getHarga_pesanan() {
        return harga_pesanan;
    }

    public void setHarga_pesanan(String harga_pesanan) {
        this.harga_pesanan = harga_pesanan;
    }





    public model_pesanan(String id_pesanan, String nama_pesanan, String nama_makanan_pesanan, String url, String jumlah_pesanan, String harga_pesanan, String subtotal) {

        this.id_pesanan = id_pesanan;
        this.nama_pesanan= nama_pesanan;
        this.nama_makanan_pesanan = nama_makanan_pesanan;
        this.harga_pesanan= harga_pesanan;
        this.url = url;
        this.jumlah_pesanan = jumlah_pesanan;
        this.subtotal = subtotal ;

    }

    public model_pesanan() {
    }








}
