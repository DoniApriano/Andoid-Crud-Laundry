package com.doniapriano.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.doniapriano.myapplication.API.APIRequestData;
import com.doniapriano.myapplication.API.RetroServer;
import com.doniapriano.myapplication.Model.ResponseModel;
import com.doniapriano.myapplication.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UbahActivity extends AppCompatActivity {
    private int xId;
    private String xNama, xAlamat, xTelephone;
    private EditText etNama, etAlamat, etTelephone;
    private Button btnUbah;
    private String nama, alamat, telephone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah);

        Intent terima = getIntent();
        xId = terima.getIntExtra("xId",-1);
        xNama = terima.getStringExtra("xNama");
        xAlamat = terima.getStringExtra("xAlamat");
        xTelephone = terima.getStringExtra("xTelephone");

        etNama = findViewById(R.id.et_nama);
        etAlamat = findViewById(R.id.et_alamat);
        etTelephone = findViewById(R.id.et_telephone);
        btnUbah = findViewById(R.id.btn_ubah);

        etNama.setText(xNama);
        etAlamat.setText(xAlamat);
        etTelephone.setText(xTelephone);

        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nama = etNama.getText().toString();
                alamat = etAlamat.getText().toString();
                telephone = etTelephone.getText().toString();

                if (nama.trim().equals("")) {
                    etNama.setError("Nama harus diisi");
                } else if (alamat.trim().equals("")) {
                    etAlamat.setError("Alamat harus diisi");
                } else if (telephone.trim().equals("")) {
                    etTelephone.setError("Telephone harus diisi");
                } else {
                    updateData();
                }
            }
        });

    }

    private void updateData() {
        APIRequestData apiData = RetroServer.connectRetrofit().create(APIRequestData.class);
        Call<ResponseModel> ubahData = apiData.apiUpdateData(xId, nama, alamat, telephone);

        ubahData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();

                Toast.makeText(UbahActivity.this, "Kode : " + kode + " | Pesan : " + pesan, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(UbahActivity.this, "Gagal menghubungi server " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}