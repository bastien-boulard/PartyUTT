package com.partyutt;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// envoyer requete post => newevent.php

public class CreateParty extends Activity {

    Button createParty;
    TableRow addToTheParty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_party);

        addToTheParty = (TableRow)findViewById(R.id.columnaddppl);
        addToTheParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog addPeopleDialog = new Dialog(getApplicationContext());
                addPeopleDialog.setContentView(R.layout.add_guest_dialog);

                Spinner statutSpinner = (Spinner)findViewById(R.id.statut_spinner);
                ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.role_array,android.R.layout.simple_spinner_item);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                statutSpinner.setAdapter(spinnerAdapter);

                Button cancelBtn = (Button)findViewById(R.id.buttonCancel);
                Button addBtn = (Button)findViewById(R.id.buttonAdd);
                LinearLayout layoutMail = (LinearLayout)findViewById(R.id.layoutMail);
                final EditText editMail = (EditText)findViewById(R.id.editMail);


                layoutMail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editMail.requestFocus();
                    }
                });

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addPeopleDialog.dismiss();
                    }
                });

                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // add the the table layout
                    }
                });
            }
        });

        createParty = (Button)findViewById(R.id.btncreateparty);
        createParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // requête POST avec les informations de l'event
                // on ferme cette page => on revient sur la page d'accueil
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_party, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class asyncPOSTTask extends AsyncTask<Object, Void, Context> {

        private String JSONerror;

        @Override
        protected Context doInBackground(Object... params) {
            String content = null;

            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://192.168.43.147/partyUTT/newevent.php");

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair(getResources().getString(R.string.login_POST_param_email), (String) params[0]));
                nameValuePairs.add(new BasicNameValuePair("eventName", (String) params[1]));
                nameValuePairs.add(new BasicNameValuePair("eventAddress", (String) params[2]));
                nameValuePairs.add(new BasicNameValuePair("eventDate", (String) params[3]));
                nameValuePairs.add(new BasicNameValuePair("guestEmail", (String) params[4]));
                nameValuePairs.add(new BasicNameValuePair("tableau adresse mail invités", (String) params[5]));
                Log.d("BLAH", nameValuePairs.toString());
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(httppost);
                content = EntityUtils.toString(response.getEntity(), "utf8");


            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return (Context) params[3];
        }

            @Override
        protected void onPostExecute(Context result) {
            if (Boolean.valueOf(JSONerror)) {
                Toast.makeText(result, "ROTCER", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(result,"HECTOR",Toast.LENGTH_LONG).show();
            }
        }

    }
}
