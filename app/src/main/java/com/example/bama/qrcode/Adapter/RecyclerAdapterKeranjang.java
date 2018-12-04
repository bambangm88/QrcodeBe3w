package com.example.bama.qrcode.Adapter;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.example.bama.qrcode.Aktivity.pesanMakanan;
import com.example.bama.qrcode.Model.Model_Krj;
import com.example.bama.qrcode.R;
import com.example.bama.qrcode.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * Created by Aws on 11/03/2018.
 */

public class RecyclerAdapterKeranjang extends RecyclerView.Adapter<RecyclerAdapterKeranjang.MyViewHolder> {
    int success;

    public pesanMakanan pesanMakanan;
    private Context mContext_krj ;
    private List<Model_Krj> mData_krj;
    RequestOptions option;
    private   AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    ProgressDialog pd;
    ProgressDialog pDialog;


    int varTambah;



    String tag_json_obj = "json_obj_req";

    private static final String TAG = pesanMakanan.class.getSimpleName();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private int a;

    public RecyclerAdapterKeranjang(Context mContext_krj, List<Model_Krj> mData_krj, pesanMakanan pesanMakanan) {
        this.mContext_krj = mContext_krj;
        this.mData_krj = mData_krj;
        this.pesanMakanan = pesanMakanan;

        // Request option for Glide
        option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);

    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        final View view;
        inflater = LayoutInflater.from(mContext_krj);
        view = inflater.inflate(R.layout.keranjang_row_item, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(view);
        pd = new ProgressDialog(mContext_krj);






        return viewHolder ;
    }







    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {



        String hrg = mData_krj.get(position).getHarga_krj();
        double hrga_dbl = Double.parseDouble(hrg) ;

        //parsing harga
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        holder.tv_harga.setText(formatRupiah.format((double)hrga_dbl));

        holder.jumlah.setText(mData_krj.get(position).getJumlah_krj());

        holder.tv_nama.setText(mData_krj.get(position).getNama_makanan_krj());

        // Load Image from the internet and set it into Imageview using Glide
        Glide.with(mContext_krj).load(mData_krj.get(position).getUrl()).apply(option).into(holder.img_thumbnail);





        holder.tambahItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int b = Integer.parseInt(holder.jumlah.getText().toString());

                  b++ ;
                holder.jumlah.setText(""+b);


                 String c = holder.jumlah.getText().toString();
                 String id =  mData_krj.get(position).getId_krj();
                 update(id,c);
                 pesanMakanan.loadJson();

            }
        });


        holder.kurangItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int b = Integer.parseInt(holder.jumlah.getText().toString());

                b-- ;
                holder.jumlah.setText(""+b);


                String c = holder.jumlah.getText().toString();
                String id =  mData_krj.get(position).getId_krj();
                update(id,c);
                pesanMakanan.loadJson();

            }
        });



        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = mData_krj.get(position).getId_krj();
                showAlertDialog(id);
                pesanMakanan.loadJson();
            }
        });


    }

    @Override
    public int getItemCount() {
        return mData_krj.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        Context context;
        TextView tv_nama ;
        TextView tv_rating ;
        TextView tv_harga ;
        TextView tv_nama_pesan;
        ImageView img_thumbnail;
        CardView cardView ;
        ImageButton btnDelete;
        TextView subtotal;


        Button tambahItem,kurangItem;
        EditText jumlah;



        private RecyclerView myrv;

        public RecyclerView.Adapter mAdapter;
        RecyclerView.LayoutManager mManager;








        public MyViewHolder(View itemView) {
            super(itemView);


            tv_nama = itemView.findViewById(R.id.title_mkn_krj);
            tv_harga= itemView.findViewById(R.id.harga_krj);
            img_thumbnail = itemView.findViewById(R.id.thumbnail_krj);
            btnDelete= itemView.findViewById(R.id.delete_krj) ;
            cardView= (CardView) itemView.findViewById(R.id.cvKeranjang_row);


            tambahItem = itemView.findViewById(R.id.tambah_krj);
            kurangItem = itemView.findViewById(R.id.kurang_krj);
            jumlah = itemView.findViewById(R.id.etItem_krj);

            subtotal = itemView.findViewById(R.id.txtSubtotal);



        }
    }



    public void showAlertDialog(final String id_krj) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext_krj)
                .setMessage("Hapus Item?")
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                                delete(id_krj);

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    // fungsi untuk menghapus
    public void delete(final String id_krj){
        final String URL_DELETE = "http://hendra.bambangm.com/delete.php";
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_DELETE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("delete", jObj.toString());


                       pesanMakanan.jsonCall();
                        pesanMakanan.loadJson();

                        Toast.makeText(mContext_krj, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();


                    } else {
                         Toast.makeText(mContext_krj, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                 Toast.makeText(mContext_krj, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_krj", id_krj);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }






    // fungsi untuk menyimpan atau update
    private void update(final String id_krj, final String jumlah_krj) {

        final String URL_UPDATE = "http://hendra.bambangm.com/update.php";
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_UPDATE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("Add/update", jObj.toString());

                        pesanMakanan.jsonCall();
                        pesanMakanan.loadJson();

                      //  Toast.makeText(mContext_krj, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(mContext_krj, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(mContext_krj, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                // jika id kosong maka simpan, jika id ada nilainya maka update

                    params.put("id_krj", id_krj);
                    params.put("jumlah_krj", jumlah_krj);


                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }











}