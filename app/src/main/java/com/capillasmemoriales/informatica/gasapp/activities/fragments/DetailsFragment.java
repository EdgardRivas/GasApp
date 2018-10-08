package com.capillasmemoriales.informatica.gasapp.activities.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.capillasmemoriales.informatica.gasapp.R;
import com.capillasmemoriales.informatica.gasapp.activities.Login;
import com.capillasmemoriales.informatica.gasapp.activities.Main;
import com.capillasmemoriales.informatica.gasapp.adapters.DetailsAdapter;
import com.capillasmemoriales.informatica.gasapp.controllers.JSONParser;
import com.capillasmemoriales.informatica.gasapp.controllers.Network;
import com.capillasmemoriales.informatica.gasapp.models.Detail;
import com.capillasmemoriales.informatica.gasapp.models.Period;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Thread.sleep;

public class DetailsFragment extends Fragment {

    public static Boolean btnStatus = false;

    private List<Detail> listDetails;
    private RecyclerView rvDetails;
    private TextView txtCard, txtTo, txtFrom, txtMsg;
    private Period per;

    private ProgressDialog dialog;

    private int response_id;
    private String message;

    private String cf, ti, gl, val;
    private String desc, to, from;
    private Integer cod_per;

    private FloatingActionsMenu options;
    private FloatingActionButton add;

    private AlertDialog.Builder builder;
    private LayoutInflater layoutInflater;
    private AlertDialog alertDialog;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.floating_details, container, false);

        listDetails = new ArrayList<>();
        rvDetails = view.findViewById(R.id.rvDetails);
        txtCard = view.findViewById(R.id.txtCard);
        txtTo = view.findViewById(R.id.txtTo);
        txtFrom = view.findViewById(R.id.txtFrom);
        txtMsg = view.findViewById(R.id.txtMsg);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvDetails.setLayoutManager(llm);

        per = new Period();
        new getPeriod().execute();

        options = view.findViewById(R.id.options);
        add = view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                add();
                options.collapse();
            }
        });
        return view;
    }

    public DetailsAdapter adapter;
    private void initAdapter() {
        adapter = new DetailsAdapter(getContext(), listDetails);
        rvDetails.setAdapter(adapter);
    }

    private void add() {
        builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        layoutInflater = this.getLayoutInflater();
        @SuppressLint("InflateParams")
        final View dialogView = layoutInflater.inflate(R.layout.modal_add_detail, null);
        //Vars
        final TextView txtFC = dialogView.findViewById(R.id.txtFC);
        final TextView txtTI = dialogView.findViewById(R.id.txtTI);
        final TextView txtGL = dialogView.findViewById(R.id.txtGL);
        final TextView txtVal = dialogView.findViewById(R.id.txtVal);
        builder.setView(dialogView);
        builder.setTitle(getResources().getString(R.string.add_detail));
        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                cf = txtFC.getText().toString().trim();
                ti = txtTI.getText().toString().trim();
                gl = txtGL.getText().toString().trim();
                val = txtVal.getText().toString().trim();
                if (Network.isOnline(getContext()))
                    if (!cf.equals("") && !ti.equals("") && !gl.equals("") && !val.equals("")) {
                        new addDetail().execute();
                    } else {
                        Snackbar.make(Objects.requireNonNull(getView()), getString(R.string.empty), Snackbar.LENGTH_LONG).show();
                    }
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        alertDialog = this.builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("StaticFieldLeak")
    class getPeriod extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected String doInBackground(String... args) {
            try {
                List<NameValuePair> params;
                params = new ArrayList<>();
                params.add(new BasicNameValuePair("", ""));

                JSONParser jsonParserPeriod = new JSONParser();
                JSONArray jsonArrayPeriod = jsonParserPeriod.requestArray(Network.GET_PERIOD, params);

                message = jsonArrayPeriod.getString(0);
                JSONObject jsonObjectPeriod = null;
                jsonObjectPeriod = jsonArrayPeriod.getJSONObject(0);
                per.setCodPer(jsonObjectPeriod.getInt("codperiodo"));
                per.setCodEmp(jsonObjectPeriod.getInt("codempresa"));
                per.setDesc(jsonObjectPeriod.getString("descripcion"));
                per.setTo(jsonObjectPeriod.getString("desde"));
                per.setFrom(jsonObjectPeriod.getString("hasta"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String file_url) {
            if (per.getCodPer() == 0) {
                txtMsg.setVisibility(View.VISIBLE);
                txtMsg.setText(message);
                options.removeButton(add);
            } else {
                to = per.getTo();
                from = per.getFrom();
                cod_per = per.getCodPer();
                txtTo.setText(to);
                txtFrom.setText(from);
                txtCard.setText("****"+Login.cod_card_view);
                new getDetails().execute();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    class getDetails extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getContext());
            dialog.setTitle(getString(R.string.wait));
            dialog.setMessage(getString(R.string.fetch));
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("cod_card", String.valueOf(Login.cod_card).trim()));

                JSONParser jsonParserDetail = new JSONParser();
                JSONArray jsonArrayDetails = jsonParserDetail.requestArray(Network.GET_DETAILS, params);

                JSONObject jsonObjectDetail = null;
                for (int i = 0; i < jsonArrayDetails.length(); i++) {
                    jsonObjectDetail = jsonArrayDetails.getJSONObject(i);
                    Detail det = new Detail();
                    if (!jsonObjectDetail.getString("ccf").equals(""))
                        det.setCf(jsonObjectDetail.getString("ccf"));
                    else if (jsonObjectDetail.getString("ccf") == null)
                        det.setCf("0");
                    det.setTi(jsonObjectDetail.getInt("transaccion"));
                    det.setGl(jsonObjectDetail.getDouble("galonaje"));
                    det.setValue(jsonObjectDetail.getDouble("consumo"));
                    String [] date_time = jsonObjectDetail.getString("fecha").split("\\.");
                    String date = date_time[0].trim();
                    det.setDate(date);
                    listDetails.add(det);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            dialog.dismiss();
            if (listDetails != null)
                initAdapter();
        }
    }

    @SuppressLint("StaticFieldLeak")
    class addDetail extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getContext());
            dialog.setTitle(getString(R.string.wait));
            dialog.setMessage(getString(R.string.adding_detail));
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();

        }

        @Override
        protected String doInBackground(String... args) {
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("cf", cf));
                params.add(new BasicNameValuePair("ti", ti));
                params.add(new BasicNameValuePair("gl", gl));
                params.add(new BasicNameValuePair("val", val));
                params.add(new BasicNameValuePair("cod_per", String.valueOf(cod_per)));
                params.add(new BasicNameValuePair("cod_card", String.valueOf(Login.cod_card)));

                JSONParser jsonParserAddDetail = new JSONParser();
                JSONObject jsonObjectAddDetail = jsonParserAddDetail.requestObject(Network.ADD_DETAIL, params);

                response_id = jsonObjectAddDetail.getInt("response_id");

                return jsonObjectAddDetail.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            dialog.dismiss();
            if (response_id != 1)
                Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getContext(), getString(R.string.ok), Toast.LENGTH_LONG).show();
            reloadData();
        }
    }

    private void reloadData() {
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                //finish();
                if (!btnStatus) {
                    Intent i = new Intent(getContext(), Main.class);
                    startActivity(i);
                }
            }
        }, Network.DURATION);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getView() == null){
            return;
        }
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
    }

}