package com.doniapriano.myapplication.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doniapriano.myapplication.API.APIRequestData;
import com.doniapriano.myapplication.API.RetroServer;
import com.doniapriano.myapplication.Activity.MainActivity;
import com.doniapriano.myapplication.Activity.UbahActivity;
import com.doniapriano.myapplication.Model.DataModel;
import com.doniapriano.myapplication.Model.ResponseModel;
import com.doniapriano.myapplication.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter extends RecyclerView.Adapter<Adapter.HolderData> {

    private Context ctx;
    private List<DataModel> listLaundry;
    private List<DataModel> listData;
    private int idLaundry;

    public Adapter(Context ctx, List<DataModel> listLaundry) {
        this.ctx = ctx;
        this.listLaundry = listLaundry;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        HolderData holder = new HolderData(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        DataModel dm = listLaundry.get(position);

        holder.tvId.setText(String.valueOf(dm.getId()));
        holder.tvNama.setText(dm.getNama());
        holder.tvAlamat.setText(dm.getAlamat());
        holder.tvTelephone.setText(dm.getTelephone());
    }

    @Override
    public int getItemCount() {
        return listLaundry.size();
    }

    public class HolderData extends RecyclerView.ViewHolder {
        TextView tvId, tvNama, tvAlamat,tvTelephone;

        public HolderData(@NonNull View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.tv_id);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvAlamat = itemView.findViewById(R.id.tv_alamat);
            tvTelephone = itemView.findViewById(R.id.tv_telephone);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder dialogPesan = new AlertDialog.Builder(ctx);
                    dialogPesan.setMessage("Mau ngapai hayoo!! ");
                    dialogPesan.setTitle("Action");
                    dialogPesan.setIcon(R.drawable.ic_baseline_coffee_24);
                    dialogPesan.setCancelable(true);

                    idLaundry = Integer.parseInt(tvId.getText().toString());

                    dialogPesan.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteData();
                            dialog.dismiss();
                            ((MainActivity)ctx).retriveData();
                        }
                    });

                    dialogPesan.setNegativeButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getData();
                        }
                    });

                    dialogPesan.show();

                    return false;
                }
            });
        }

        private void deleteData() {
            APIRequestData apiData = RetroServer.connectRetrofit().create(APIRequestData.class);
            Call<ResponseModel> hapusData = apiData.apiDeleteData(idLaundry);

            hapusData.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();

                    Toast.makeText(ctx, "Kode : " + kode + " | Pesan : " + pesan, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(ctx, "Gagal menghubungi server " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void getData() {
            APIRequestData apiData = RetroServer.connectRetrofit().create(APIRequestData.class);
            Call<ResponseModel> ambilData = apiData.apiGetData(idLaundry);

            ambilData.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();
                    listLaundry = response.body().getData();

                    int varIdLaundry = listLaundry.get(0).getId();
                    String varNamaLaundry = listLaundry.get(0).getNama();
                    String varAlamatLaundry = listLaundry.get(0).getAlamat();
                    String varTelephoneLaundry = listLaundry.get(0).getTelephone();

//                    Toast.makeText(ctx, "Kode : " + kode + " | Pesan : " + pesan + varIdLaundry + varNamaLaundry + varAlamatLaundry + varTelephoneLaundry, Toast.LENGTH_SHORT).show();

                    Intent kirim = new Intent(ctx, UbahActivity.class);
                    kirim.putExtra("xId",varIdLaundry);
                    kirim.putExtra("xNama",varNamaLaundry);
                    kirim.putExtra("xAlamat",varAlamatLaundry);
                    kirim.putExtra("xTelephone",varTelephoneLaundry);
                    ctx.startActivity(kirim);

                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(ctx, "Gagal menghubungi server " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
