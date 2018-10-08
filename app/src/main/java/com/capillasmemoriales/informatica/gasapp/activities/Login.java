package com.capillasmemoriales.informatica.gasapp.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.capillasmemoriales.informatica.gasapp.R;
import com.capillasmemoriales.informatica.gasapp.controllers.JSONParser;
import com.capillasmemoriales.informatica.gasapp.controllers.Network;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    public static Boolean btnStatus = false;

    private EditText txtId, txtPass;
    private CheckBox chbRem;
    private Button btnLogin;
    private ProgressDialog dialog;

    private int response_id;
    public static int user_id, cod_emp;
    public static String user_name, cod_card, cod_card_view;

    private String id, pass, passEncode;

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        txtId = findViewById(R.id.txtId);
        txtPass = findViewById(R.id.txtPass);
        chbRem = findViewById(R.id.chbRem);
        btnLogin = findViewById(R.id.btnLogin);

        getPreferences();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                id = txtId.getText().toString();
                pass = txtPass.getText().toString();
                if (Network.isOnline(getApplicationContext())) {
                    if (id.length() > 0) {
                        if (pass.length() > 5) /*7*/ {
                            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            assert imm != null;
                            imm.hideSoftInputFromWindow(txtPass.getWindowToken(), 0);
                            new login().execute();
                        } else {
                            txtPass.setError(getString(R.string.ePass));
                        }
                    } else {
                        txtId.setError(getString(R.string.eId));
                    }
                } else {
                    openNetworkSettings(view);
                }
            }
        });
    }

    private void getPreferences() {
        loginPreferences = getSharedPreferences("loginPreferences", Context.MODE_PRIVATE);
        if (loginPreferences.contains("user_id"))
            txtId.setText(loginPreferences.getString("user_id", ""));
        else
            txtId.requestFocus();
        if (loginPreferences.contains("user_pass")) {
            byte[] data = Base64.decode(loginPreferences.getString("user_pass", ""), Base64.DEFAULT);
            String passDecode = new String(data, StandardCharsets.UTF_8);
            txtPass.setText(passDecode);
        } else
            txtPass.requestFocus();
    }

    private void deletePreferences() {
        loginPreferences = getSharedPreferences("loginPreferences", Context.MODE_PRIVATE);
        loginPreferences.edit().clear().apply();
    }

    private void openNetworkSettings(View view) {
        Snackbar.make(view, getString(R.string.no_network), Snackbar.LENGTH_LONG).setAction(getString(R.string.open_network_settings), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 0);
            }
        }).setDuration(Snackbar.LENGTH_INDEFINITE).show();
    }

    class login extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Login.this);
            dialog.setTitle(getString(R.string.login));
            dialog.setMessage(getString(R.string.loading));
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                List<NameValuePair> params;
                params = new ArrayList<>();
                params.add(new BasicNameValuePair("user_id", id));
                params.add(new BasicNameValuePair("user_pass", pass));

                JSONParser jsonParserLogin = new JSONParser();
                JSONObject jsonObjectLogin = jsonParserLogin.requestObject(Network.LOGIN, params);

                cod_emp = jsonObjectLogin.getInt("cod_emp");
                user_name = jsonObjectLogin.getString("user_name");
                cod_card = jsonObjectLogin.getString("cod_card");
                cod_card_view = cod_card.substring(10,16);
                response_id = jsonObjectLogin.getInt("response_id");

                return jsonObjectLogin.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @SuppressLint("ApplySharedPref")
        protected void onPostExecute(String file_url) {
            dialog.dismiss();
            if (response_id != 1) /*Error Login*/ {
                dialog.setTitle(getString(R.string.alert));
                dialog.setMessage(getString(R.string.error_login));
                dialog.setCancelable(true);
            } else /*OK Login*/ {
                if (chbRem.isChecked()) {
                    //Save preferences
                    loginPreferences = getApplicationContext().getSharedPreferences("loginPreferences", 0);
                    editor = loginPreferences.edit();
                    editor.putString("user_id", id);
                    byte[] data = pass.getBytes(StandardCharsets.UTF_8);
                    passEncode = Base64.encodeToString(data, Base64.DEFAULT);
                    editor.putString("user_pass", passEncode);
                    editor.commit();
                } else {
                    deletePreferences();
                }
                if (file_url != null) /*URL OK*/ {
                    dialog.setTitle(getString(R.string.welcome));
                    dialog.setMessage(user_name);
                    dialog.setCancelable(true);
                    intentMain();
                }
            }
            dialog.show();
        }
    }

    private void intentMain() {
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                if (!btnStatus) {
                    Intent i = new Intent(getApplicationContext(), Main.class);
                    startActivity(i);
                }
            }
        }, Network.DURATION);
    }

    public void onBackPressed() {
        btnStatus = true;
        super.onBackPressed();
    }
}