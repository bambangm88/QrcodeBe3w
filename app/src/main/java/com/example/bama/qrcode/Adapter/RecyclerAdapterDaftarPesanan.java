package com.example.bama.qrcode.Adapter;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.example.bama.qrcode.Model.model_pesanan;
import com.example.bama.qrcode.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;


/**
 * Created by Aws on 11/03/2018.
 */

public class RecyclerAdapterDaftarPesanan extends RecyclerView.Adapter<RecyclerAdapterDaftarPesanan.MyViewHolder> {
    int success;


    private Context mContext_Psn ;
    private List<model_pesanan> mData_Psn;
    RequestOptions option;
    private   AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    ProgressDialog pd;
    ProgressDialog pDialog;


    public RecyclerAdapterDaftarPesanan(Context mContext_Psn, List<model_pesanan> mData_Psn) {
        this.mContext_Psn = mContext_Psn;
        this.mData_Psn = mData_Psn;


        // Request option for Glide
        option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);

    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        final View view;
        inflater = LayoutInflater.from(mContext_Psn);
        view = inflater.inflate(R.layout.daftar_pesanan_row, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(view);
        pd = new ProgressDialog(mContext_Psn);

        return viewHolder ;
    }







    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {



        String hrg = mData_Psn.get(position).getHarga_pesanan();
        double hrga_dbl = Double.parseDouble(hrg) ;

        holder.jumlah.setText(mData_Psn.get(position).getJumlah_pesanan());

        //parsing harga
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        holder.tv_harga.setText(formatRupiah.format((double)hrga_dbl));

        holder.tv_nama.setText(mData_Psn.get(position).getNama_makanan_pesanan());

        // Load Image from the internet and set it into Imageview using Glide
        Glide.with(mContext_Psn).load(mData_Psn.get(position).getUrl()).apply(option).into(holder.img_thumbnail);



    }

    @Override
    public int getItemCount() {
        return mData_Psn.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_nama ;
        TextView tv_rating ;
        TextView tv_harga ;
        TextView tv_nama_pesan;
        ImageView img_thumbnail;
        CardView cardView ;
        ImageButton btnDelete;
        TextView jumlah;






        public MyViewHolder(View itemView) {
            super(itemView);


            tv_nama = itemView.findViewById(R.id.title_mkn_pesanan);
            tv_harga= itemView.findViewById(R.id.harga_pesanan);
            img_thumbnail = itemView.findViewById(R.id.thumbnail_pesanan);
            cardView= (CardView) itemView.findViewById(R.id.cvPesanan_row);
            jumlah = itemView.findViewById(R.id.etItem_pesanan);

        }
    }

}