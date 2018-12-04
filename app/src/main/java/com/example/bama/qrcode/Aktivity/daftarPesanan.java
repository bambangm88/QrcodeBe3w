package com.example.bama.qrcode.Aktivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.bama.qrcode.Adapter.RecyclerAdapterDaftarPesanan;

import com.example.bama.qrcode.Model.model_pesanan;
import com.example.bama.qrcode.R;
import com.example.bama.qrcode.app.AppController;
import com.google.zxing.integration.android.IntentIntegrator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class daftarPesanan extends AppCompatActivity {

    ProgressDialog pDialog;
    RecyclerView recyclerView;
    private IntentIntegrator intentIntegrator;

    ImageView notfound;

    private List<model_pesanan> lstPesanan = new ArrayList<>();
    private RecyclerView myrv;

    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mManager;
    ProgressDialog pd;

    private static final String TAG = pesanMakanan.class.getSimpleName();
    int success;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";


    Context context;

    TextView subtotal;

    Button btnInsert;

    EditText status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_pesanan);
        Toolbar ToolBarAtas2 = (Toolbar)findViewById(R.id.toolbarPesanan);
        setSupportActionBar(ToolBarAtas2);
        subtotal = (TextView)findViewById(R.id.txtSubtotal_pesanan);

        notfound = (ImageView)findViewById(R.id.notFound);

        status = (EditText)findViewById(R.id.etPesanan) ;

        myrv = (RecyclerView)findViewById(R.id.rv_pesanan);
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        myrv.setLayoutManager(mManager);
        mAdapter = new RecyclerAdapterDaftarPesanan(this, lstPesanan);
        myrv.setAdapter(mAdapter);
        myrv.setNestedScrollingEnabled(false);

        jsonCall();
        setSubtotal_pesanan();

        final String pesan = status.getText().toString();

        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(daftarPesanan.this,"Pesanan"+pesan,Toast.LENGTH_SHORT).show();
            }
        });

    }








    public void jsonCall() {

        pd = new ProgressDialog(this );
        pd.setMessage("Loading . . ");
        pd.setCancelable(false);
        pd.show();


        final String URL_JSON = "http://hendra.bambangm.com/tampil_pesanan.php";
        StringRequest reqData = new StringRequest(Request.Method.POST, URL_JSON,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pd.cancel();
                        Log.e("Response: ", response.toString());
                        try {
                            JSONObject jObj = new JSONObject(response);
                             success = jObj.getInt("value");
                            if (success == 1) {
                                mAdapter.notifyDataSetChanged();

                                String getObject = jObj.getString("results");
                                JSONArray jsonArray = new JSONArray(getObject);

                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);

                                    model_pesanan model = new model_pesanan();

                                    model.setNama_pesanan(data.getString("nama_pesanan"));
                                    model.setNama_makanan_pesanan(data.getString("nama_makanan_pesanan"));
                                    model.setHarga_pesanan(data.getString("harga_krj"));
                                    model.setUrl(data.getString("url"));
                                    model.setJumlah_pesanan(data.getString("jumlah_krj"));
                                    model.setId_pesanan(data.getString("id_pesanan"));
                                    //Toast.makeText(MainActivity.this,anime.toString(),Toast.LENGTH_SHORT).show();
                                    lstPesanan.add(model);
                                    mAdapter.notifyDataSetChanged();
                                    setSubtotal_pesanan();

                                   notfound.setVisibility(View.GONE);


                                }
                            }else {
                                Toast.makeText(daftarPesanan.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
                                pd.cancel();
//                                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), jObj.getString("message"), Snackbar.LENGTH_SHORT);
//                                    View snackbarView = snackbar.getView();
//                                    snackbarView.setBackgroundColor(ContextCompat.getColor(mycontext, R.color.Gagal));
//                                    snackbar.show();

                                notfound.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            pd.cancel();
                            e.printStackTrace();
                        }

                        mAdapter.notifyDataSetChanged();
                        pd.cancel();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volley", "error : " + error.getMessage());
                pd.cancel();

            }}
        ){

            @Override
            protected Map<String, String> getParams(){


                final String nama_pesanan = "bambang" ;

                Map<String,String> params = new HashMap<String, String>();
                //mengirim value melalui parameter ke database
                params.put("nama_pesanan",nama_pesanan);
                return params;
            }


        };





        AppController.getInstance().addToRequestQueue(reqData,tag_json_obj);
    }










    public void setSubtotal_pesanan()
    {
        final String URL_SUBTOTAL = "http://hendra.bambangm.com/subtotal_pesanan.php?nama_pesanan="+"'bambang'";
        pd.setMessage("Menghitung Subtotal");
        pd.setCancelable(false);
        pd.show();

        JsonArrayRequest reqData = new JsonArrayRequest(URL_SUBTOTAL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        pd.cancel();
                        Log.d("volley","response : " + response.toString());
                        for(int i = 0 ; i < response.length(); i++)
                        {
                            try {
                                JSONObject data = response.getJSONObject(i);


                                String a= subtotal.getText().toString();
                                String hrg = data.getString("total");


                                if (hrg.equals("null"))
                                {
                                    subtotal.setText("0");
                                    mAdapter.notifyDataSetChanged();
                                } else
                                {

                                    double sub = Double.parseDouble(hrg) ;

                                    Locale localeID = new Locale("in", "ID");
                                    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

                                    subtotal.setText(formatRupiah.format((double)sub));
                                    mAdapter.notifyDataSetChanged();
                                }






                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
                        Log.d("volley", "error : " + error.getMessage());
                    }
                }){

            @Override
            protected Map<String, String> getParams(){


                final String nama_krj = "bambang" ;

                Map<String,String> params = new HashMap<String, String>();
                //mengirim value melalui parameter ke database
                params.put("nama_pesanan", nama_krj);
                return params;
            }


        };
        AppController.getInstance().addToRequestQueue(reqData,tag_json_obj);
    }




















}
