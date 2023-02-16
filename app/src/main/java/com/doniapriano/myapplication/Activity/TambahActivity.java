package com.doniapriano.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

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

public class TambahActivity extends AppCompatActivity {
    private EditText etNama,etAlamat,etTelephone;
    private Button btnSimpan;
    private String nama,alamat,telephone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);

        etNama = findViewById(R.id.et_nama);
        etAlamat = findViewById(R.id.et_alamat);
        etTelephone = findViewById(R.id.et_telephone);
        btnSimpan = findViewById(R.id.btn_simpan);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
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
                    createData();
                }
            }
        });
    }

    private void createData() {
        APIRequestData apiData = RetroServer.connectRetrofit().create(APIRequestData.class);
        Call<ResponseModel> simpanData = apiData.apiCreateData(nama,alamat,telephone);

        simpanData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();

                Toast.makeText(TambahActivity.this, "Kode : " + kode + " | Pesan : " + pesan , Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(TambahActivity.this, "Gagal menghubungi server " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}