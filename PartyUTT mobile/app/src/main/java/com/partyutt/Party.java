package com.partyutt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Party extends Activity {

    List<Map<String, String>> contactList = new ArrayList<Map<String, String>>();
    List<Map<String, String>> ownerParty = new ArrayList<Map<String, String>>();
    List<Map<String, String>> orgaParty = new ArrayList<Map<String, String>>();
    List<Map<String, String>> guestParty = new ArrayList<Map<String, String>>();
    TextView titreParty,titleAddress,titleDate,titleStatut;
    String strintentToken,strintentEmail,strintentID,strintentUserPseudo;
    String error, eventName, eventDate, eventAddress;

    String param_userPseudo = getResources().getString(R.string.param_userPseudo);
    String param_isOrga = getResources().getString(R.string.param_isOrga);
    String param_isComing = getResources().getString(R.string.param_isComing);
    String param_guests = getResources().getString(R.string.param_guests);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party);

        if (getIntent().getExtras() != null) {
            Intent intent = getIntent();
            strintentUserPseudo = intent.getStringExtra(getResources().getString(R.string.param_userPseudo));
            strintentToken = intent.getStringExtra(getResources().getString(R.string.param_token));
            strintentEmail = intent.getStringExtra(getResources().getString(R.string.param_email));
            strintentID = intent.getStringExtra(getResources().getString(R.string.param_ID));
            asyncPOSTTask askWSForPartyData = new asyncPOSTTask();
            askWSForPartyData.execute(strintentEmail, strintentToken, strintentID,getApplicationContext());
        } else {
            strintentEmail="";
            strintentID="";
            strintentUserPseudo="";
            strintentToken="";
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.party, menu);
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
                HttpPost httppost = new HttpPost("http://192.168.43.147/partyUTT/eventpage.php");

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair(getResources().getString(R.string.param_email), (String) params[0]));
                nameValuePairs.add(new BasicNameValuePair(getResources().getString(R.string.param_token), (String) params[1]));
                nameValuePairs.add(new BasicNameValuePair(getResources().getString(R.string.param_eventID), (String) params[2]));
                Log.d("BLAH", nameValuePairs.toString());
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(httppost);
                content = EntityUtils.toString(response.getEntity(), "utf8");

                JSONObject requestAnswer = new JSONObject(content);

                error = requestAnswer.getString("error");
                eventName = requestAnswer.getJSONObject("data").getString("eventName");
                eventDate = requestAnswer.getJSONObject("data").getString("eventDate");
                eventAddress = requestAnswer.getJSONObject("data").getString("eventAddress");

                for (int i=0; i<requestAnswer.getJSONArray(param_guests).length();i++) {
                    HashMap contactMap = new HashMap();
                    contactMap.put(param_userPseudo,requestAnswer.getJSONArray(param_guests).getJSONObject(i).getString(param_userPseudo));
                    contactMap.put(param_isOrga,requestAnswer.getJSONArray(param_guests).getJSONObject(i).getString(param_isOrga));
                    contactMap.put(param_isComing,requestAnswer.getJSONArray(param_guests).getJSONObject(i).getString(param_isComing));
                    contactList.add(contactMap);


                }

            } catch (ClientProtocolException e) {
                Log.e("ClientProtocolException", e.toString(), e);
            } catch (IOException IO) {
                Log.e("IOException", IO.toString(), IO);
            } catch (Error error) {
                Log.e("POST REQUEST ERROR", error.toString(), error);
            } catch (JSONException e) {
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
            titreParty = (TextView)findViewById(R.id.titleparty);
            titleStatut = (TextView)findViewById(R.id.titlestatut);
            titleAddress = (TextView)findViewById(R.id.titleaddress);
            titleDate = (TextView)findViewById(R.id.titledate);
            titreParty.setText(eventName);
            titleStatut.setText("OWNER !");
            titleAddress.setText(eventAddress);
            titleDate.setText(eventDate);

            for (int i=0;i<contactList.size();i++)
            {
                TableLayout tl = (TableLayout) findViewById(R.id.tablelayout);
                TableRow tr = new TableRow(result);
                tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                TextView invitation = new TextView(result);
                invitation.setText(contactList.get(i).get(param_userPseudo));

                // si c'est l'utilisateur alors on va mettre son statut en gras
                if (contactList.get(i).get(param_userPseudo).equals(strintentUserPseudo)) {
                    invitation.setTypeface(null, Typeface.BOLD);
                }
                invitation.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                tr.addView(invitation);
                tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
            }
            //statutParty.setText(eventD);
        }

    }
}
