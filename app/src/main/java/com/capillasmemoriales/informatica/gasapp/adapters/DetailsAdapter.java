package com.capillasmemoriales.informatica.gasapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.capillasmemoriales.informatica.gasapp.activities.Main;
import com.capillasmemoriales.informatica.gasapp.controllers.JSONParser;
import com.capillasmemoriales.informatica.gasapp.controllers.Network;
import com.capillasmemoriales.informatica.gasapp.models.Detail;
import com.capillasmemoriales.informatica.gasapp.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder> {

    private final Context mContext;
    private List<Detail> mData;

    private AlertDialog.Builder builder;
    private LayoutInflater layoutInflater;
    private AlertDialog alertDialog;

    private String editTI, editFC, editGL;

    public DetailsAdapter(Context mContext, List<Detail> list) {
        this.mContext = mContext;
        this.mData = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int pos) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.details, viewGroup, false);
        final ViewHolder vh = new ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(Objects.requireNonNull(mContext));
                layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                assert layoutInflater != null;
                @SuppressLint("InflateParams")
                final View dialogView = layoutInflater.inflate(R.layout.modal_edit_detail, null);
                final int position = vh.getAdapterPosition();
                //Vars
                final TextView txtFC = dialogView.findViewById(R.id.txtFC);
                final TextView txtTI = dialogView.findViewById(R.id.txtTI);
                final TextView txtGL = dialogView.findViewById(R.id.txtGL);
                final TextView txtVal = dialogView.findViewById(R.id.txtVal);
                String blank = "";
                txtFC.setText(blank+mData.get(position).getCf());
                txtTI.setText(blank+mData.get(position).getTi());
                txtGL.setText(blank+mData.get(position).getGl());
                txtVal.setText(blank+mData.get(position).getValue());
                txtTI.setEnabled(false);
                txtVal.setEnabled(false);
                builder.setView(dialogView);
                builder.setTitle(mContext.getResources().getString(R.string.edit_detail));
                builder.setPositiveButton(mContext.getResources().getString(R.string.save), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        editFC = txtFC.getText().toString().trim();
                        editTI = txtTI.getText().toString().trim();
                        editGL = txtGL.getText().toString().trim();
                        new saveDetail().execute();
                    }
                });
                builder.setNegativeButton(mContext.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.show();
            }
        });
/*
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent details = new Intent(mContext.getApplicationContext(), ContactDetail.class);
                details.putExtra("Id", mData.get(vh.getAdapterPosition()).getId());
                details.putExtra("fName", mData.get(vh.getAdapterPosition()).getfName());
                details.putExtra("lName", mData.get(vh.getAdapterPosition()).getlName());
                details.putExtra("Phone", mData.get(vh.getAdapterPosition()).getPhone());
                details.putExtra("Mail", mData.get(vh.getAdapterPosition()).getMail());
                mContext.startActivity(details);
            }
        });
*/
/*
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public boolean onLongClick(View view) {
                String phoneNumber = mData.get(vh.getAdapterPosition()).getPhone();
                Intent dial = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                //dial.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(dial);
                return false;
            }
        });
*/
        return vh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int pos) {
        Resources res = viewHolder.itemView.getContext().getResources();

        viewHolder.txtFC.setText(res.getString(R.string.FC)+" "+mData.get(pos).getCf());
        viewHolder.txtTI.setText(res.getString(R.string.TI)+" "+mData.get(pos).getTi());
        viewHolder.txtGl.setText(mData.get(pos).getGl()+" "+res.getString(R.string.gl));
        viewHolder.txtVal.setText(res.getString(R.string.value)+" "+mData.get(pos).getValue());
        viewHolder.txtDate.setText(""+mData.get(pos).getDate());
    }

    @Override
    public int getItemCount() {
        if (mData != null)
            return mData.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtFC, txtTI, txtGl, txtVal, txtDate;

        public ViewHolder(View itemView) {
            super(itemView);
            txtFC = itemView.findViewById(R.id.txtCF);
            txtTI = itemView.findViewById(R.id.txtTI);
            txtGl = itemView.findViewById(R.id.txtGL);
            txtVal = itemView.findViewById(R.id.txtVal);
            txtDate = itemView.findViewById(R.id.txtDate);
        }
    }

    private void intentMain() {
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(mContext, Main.class);
                mContext.startActivity(i);
            }
        }, Network.DURATION);
    }

    @SuppressLint("StaticFieldLeak")
    class saveDetail extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                List<NameValuePair> params;
                params = new ArrayList<>();
                params.add(new BasicNameValuePair("ti", editTI));
                params.add(new BasicNameValuePair("cf", editFC));
                params.add(new BasicNameValuePair("gl", editGL));

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = jsonParser.requestObject(Network.EDIT_DETAIL, params);

                return jsonObject.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @SuppressLint("ApplySharedPref")
        protected void onPostExecute(String file_url) {
            if (file_url.equals("OK")) {
                Toast.makeText(mContext.getApplicationContext(), mContext.getString(R.string.ok), Toast.LENGTH_SHORT).show();
                intentMain();
            } else
                Toast.makeText(mContext.getApplicationContext(), mContext.getString(R.string.error), Toast.LENGTH_SHORT).show();
        }
    }
    
}
