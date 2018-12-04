package com.example.bama.qrcode.Aktivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.bama.qrcode.Adapter.RecyclerAdapterKeranjang;
import com.example.bama.qrcode.Login.Register;
import com.example.bama.qrcode.MainActivity;
import com.example.bama.qrcode.Model.Model_Krj;
import com.example.bama.qrcode.R;
import com.example.bama.qrcode.app.AppController;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class pesanMakanan extends AppCompatActivity {
    ProgressDialog pDialog;
    RecyclerView recyclerView;
    private IntentIntegrator intentIntegrator;

    private List<Model_Krj> lstKrj = new ArrayList<>();
    private RecyclerView myrv;
    Handler mHandler ;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mManager;
    ProgressDialog pd;

    private static final String TAG = pesanMakanan.class.getSimpleName();
    int success;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private String url_keranjang = "http://hendra.bambangm.com/" + "keranjang.php";
    String tag_json_obj = "json_obj_req";

    Context context;
  ImageView cartnotfound;
    TextView subtotal;

    Button btnInsert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan_makanan);

        Toolbar ToolBarAtas2 = (Toolbar)findViewById(R.id.toolbarPesan);
        setSupportActionBar(ToolBarAtas2);
        subtotal = (TextView)findViewById(R.id.txtSubtotal);

        myrv = (RecyclerView)findViewById(R.id.rv_krj);
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        myrv.setLayoutManager(mManager);
        mAdapter = new RecyclerAdapterKeranjang(this, lstKrj,pesanMakanan.this);
        myrv.setAdapter(mAdapter);
        myrv.setNestedScrollingEnabled(false);

        cartnotfound = (ImageView)findViewById(R.id.cartnotFound);
        context =this;
        final String a = subtotal.getText().toString();

        jsonCall();
        loadJson();

        btnInsert = (Button)findViewById(R.id.btnPesam);

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    showAlertDialog();




            }
        });

        mHandler = new Handler();

        this.mHandler = new Handler();

        this.mHandler.postDelayed(m_Runnable,1000);
    }

    private final Runnable m_Runnable = new Runnable()
    {
        public void run()

        {
            mAdapter.notifyDataSetChanged();
           loadJson();
            mHandler.postDelayed(m_Runnable, 1000);

        }

    };//runnable






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pesanmenu, menu);





        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.scan) {

            intentIntegrator = new IntentIntegrator(pesanMakanan.this);
            intentIntegrator.initiateScan();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            if (result.getContents() == null){
                Toast.makeText(this, "Hasil tidak ditemukan", Toast.LENGTH_SHORT).show();
            }else{
                // jika qrcode berisi data
                try{
                    // converting the data json
                    JSONObject object = new JSONObject(result.getContents());
                    // atur nilai ke textviews
                   String url_img = (object.getString("url"));
                    String nama = (object.getString("nama"));
                    String harga = (object.getString("harga"));




                    String myname = "bambang";
                    String jumlah = "1";

                    Toast.makeText(this, ""+nama, Toast.LENGTH_SHORT).show();

                    checkRegister(myname,nama,url_img,jumlah,harga);
                    lstKrj.clear();
                     mAdapter.notifyDataSetChanged();
                     jsonCall();
                     loadJson();



                }catch (JSONException e){
                    e.printStackTrace();
                    // jika format encoded tidak sesuai maka hasil
                    // ditampilkan ke toast
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }













    private void checkRegister(final String nama_krj, final String nama_makanan_krj, final String url, final String jumlah_krj, final String harga_krj) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Menambah Item ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, url_keranjang, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {

                        Log.e("Item Berhasil Ditambah!", jObj.toString());

                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();


                    } else {
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),jObj.getString(TAG_MESSAGE), Snackbar.LENGTH_SHORT);
                        snackbar.show();

                         Toast.makeText(getApplicationContext(),
                            jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),error.getMessage(), Snackbar.LENGTH_SHORT);
                snackbar.show();

                //Toast.makeText(getApplicationContext(),
                //  error.getMessage(), Toast.LENGTH_LONG).show();

                hideDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("nama_krj", nama_krj);
                params.put("nama_makanan_krj", nama_makanan_krj);
                params.put("url", url);
                params.put("jumlah_krj", jumlah_krj);
                params.put("harga_krj", harga_krj);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }





    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }










    public void jsonCall() {
        lstKrj.clear();
        mAdapter.notifyDataSetChanged();
        pd = new ProgressDialog(this );
        pd.setMessage("Mengambil Keranjang . . ");
        pd.setCancelable(false);
        pd.show();


        final String URL_JSON = "http://hendra.bambangm.com/Tampil_Keranjang.php";
        StringRequest  reqData = new StringRequest(Request.Method.POST, URL_JSON,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        Log.e("Response: ", response.toString());
                        try {
                            JSONObject jObj = new JSONObject(response);
                            int success = jObj.getInt("value");
                            if (success == 1) {
                                mAdapter.notifyDataSetChanged();

                                String getObject = jObj.getString("results");
                                JSONArray jsonArray = new JSONArray(getObject);

                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);

                                    Model_Krj model = new Model_Krj();

                                    model.setNama_krj(data.getString("nama_krj"));
                                    model.setNama_makanan_krj(data.getString("nama_makanan_krj"));
                                    model.setHarga_krj(data.getString("harga_krj"));
                                    model.setUrl(data.getString("url"));
                                    model.setJumlah_krj(data.getString("jumlah_krj"));
                                    model.setId_krj(data.getString("id_krj"));
                                    //Toast.makeText(MainActivity.this,anime.toString(),Toast.LENGTH_SHORT).show();
                                    lstKrj.add(model);
                                    mAdapter.notifyDataSetChanged();
                                    cartnotfound.setVisibility(View.GONE);
                                    pd.cancel();
                                }
                            }else {
                                Toast.makeText(pesanMakanan.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
                                pd.cancel();
//                                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), jObj.getString("message"), Snackbar.LENGTH_SHORT);
//                                    View snackbarView = snackbar.getView();
//                                    snackbarView.setBackgroundColor(ContextCompat.getColor(mycontext, R.color.Gagal));
//                                    snackbar.show();
                                cartnotfound.setVisibility(View.VISIBLE);
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


                final String nama_krj = "bambang" ;

                Map<String,String> params = new HashMap<String, String>();
                //mengirim value melalui parameter ke database
                params.put("nama_krj", nama_krj);
                return params;
            }


        };





        AppController.getInstance().addToRequestQueue(reqData);
    }




    public void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(pesanMakanan.this)
                .setMessage("Apakah Pesanan Sudah Benar ?")
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        insert_pesanan();

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
//----------------




    public void loadJson()
    {

        final String URL_SUBTOTAL = "http://hendra.bambangm.com/subtotal.php?nama_krj="+"'bambang'";

        JsonArrayRequest reqData = new JsonArrayRequest(URL_SUBTOTAL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("volley","response : " + response.toString());
                        for(int i = 0 ; i < response.length(); i++)
                        {
                            try {
                                JSONObject data = response.getJSONObject(i);


                                String a= subtotal.getText().toString();
                                String hrg = data.getString("total");


                                if (hrg.equals("null"))
                                {
                                    subtotal.setText("");
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
                params.put("nama_krj", nama_krj);
                return params;
            }


        };
        AppController.getInstance().addToRequestQueue(reqData,tag_json_obj);
    }





    // fungsi untuk menghapus
    private void insert_pesanan(){
        pd.setMessage("Mengirim Pesanan");
        pd.setCancelable(false);
        pd.show();

        final String URL_INSERT_PESANAN= "http://hendra.bambangm.com/insert_pesanan.php?nama_krj="+"'bambang'";
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_INSERT_PESANAN, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {


                Log.d(TAG,"Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("delete", jObj.toString());


                    } else {


                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    pd.cancel();
                  //  Toast.makeText(pesanMakanan.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.cancel();
                Log.d("volley", "error : " + error.getMessage());
              //  Toast.makeText(pesanMakanan.this, error.getMessage(), Toast.LENGTH_LONG).show();
                pd.cancel();
                delete_keranjang();
                Toast.makeText(pesanMakanan.this,"Berhasil, Silahkan Cek Daftar Pesanan",Toast.LENGTH_LONG).show();
                mAdapter.notifyDataSetChanged();
                finish();


            }
        });

        AppController.getInstance().addToRequestQueue(strReq,"message");
    }







    // fungsi untuk menghapus
    private void delete_keranjang(){


        final String URL_DELETE_KERANJANG= "http://hendra.bambangm.com/delete_keranjang.php?nama_krj="+"'bambang'";
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_DELETE_KERANJANG, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {


                Log.d(TAG,"Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("delete", jObj.toString());


                    } else {


                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    pd.cancel();
                    //  Toast.makeText(pesanMakanan.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.cancel();
                Log.d("volley", "error : " + error.getMessage());


            }
        });

        AppController.getInstance().addToRequestQueue(strReq,"message");
    }








}
