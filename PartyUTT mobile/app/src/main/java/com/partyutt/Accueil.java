package com.partyutt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.partyutt.Traitement.TraiterAccueil;

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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Accueil extends Activity {

    List<Map<String, String>> partyList = new ArrayList<Map<String, String>>();

    Button createParty;
    public static Switch switchCreateur, switchOrga, switchInvite;
    public static ListView listViewCreateur, listViewOrga, listViewInvite;
    public static TraiterAccueil.AllParty userParty;
    String strintentToken,strintentEmail;

    public static ArrayAdapter<String> adaptera, adapterb, adapterc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);


        if (getIntent().getExtras() != null) {
            Intent intent = getIntent();
            strintentToken = intent.getStringExtra(getResources().getString(R.string.param_token));
            strintentEmail = intent.getStringExtra(getResources().getString(R.string.param_email));
            TraiterAccueil traiterAccueil = new TraiterAccueil();
            traiterAccueil.recuperationPartyAccueil(strintentEmail,strintentToken,getApplicationContext());
        } else {
            strintentEmail="";
            strintentToken="";
            finish();
        }

        switchCreateur = (Switch)findViewById(R.id.switch_Createur);
        switchOrga = (Switch)findViewById(R.id.switch_Orga);
        switchInvite = (Switch)findViewById(R.id.switch_Invite);

        listViewCreateur = (ListView)findViewById(R.id.listView_Createur);
        listViewOrga = (ListView)findViewById(R.id.listView_Orga);
        listViewInvite = (ListView)findViewById(R.id.listView_Invite);

       /*
        adaptera = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1,userParty.eventsOwner);
        adapterb = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1,userParty.eventsOrga);
        adapterc = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1,userParty.eventsInvite);


        listViewCreateur.setAdapter(adaptera);
        listViewOrga.setAdapter(adapterb);
        listViewInvite.setAdapter(adapterc);

*/
        switchCreateur.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    listViewCreateur.setVisibility(View.VISIBLE);
                } else {
                    listViewCreateur.setVisibility(View.GONE);
                }
            }
        });

        switchOrga.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    listViewOrga.setVisibility(View.VISIBLE);
                } else {
                    listViewOrga.setVisibility(View.GONE);
                }
            }
        });

        switchInvite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    listViewInvite.setVisibility(View.VISIBLE);
                } else {
                    listViewInvite.setVisibility(View.GONE);
                }
            }
        });

        listViewCreateur.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent ownerParty = new Intent(getApplicationContext(),OwnerParty.class);
                ownerParty.putExtra(getString(R.string.param_email),strintentEmail);
                ownerParty.putExtra(getString(R.string.param_token),strintentToken);
                ownerParty.putExtra(getString(R.string.param_ID), userParty.IDOwner.get(position).toString());
                startActivity(ownerParty);
            }
        });

        listViewOrga.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent orgaParty = new Intent(getApplicationContext(),Party.class);
                orgaParty.putExtra(getString(R.string.param_email),strintentEmail);
                orgaParty.putExtra(getString(R.string.param_token),strintentToken);
                orgaParty.putExtra(getString(R.string.param_ID),userParty.IDOrga.get(position).toString());
                orgaParty.putExtra(getString(R.string.param_isOrga),"1");
                startActivity(orgaParty);
            }
        });

        listViewInvite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent inviteParty = new Intent(getApplicationContext(),Party.class);
                inviteParty.putExtra(getString(R.string.param_email),strintentEmail);
                inviteParty.putExtra(getString(R.string.param_token),strintentToken);
                inviteParty.putExtra(getString(R.string.param_ID),userParty.IDInvite.get(position).toString());
                inviteParty.putExtra(getString(R.string.param_isOrga),"0");
                startActivity(inviteParty);
            }
        });

        createParty = (Button)findViewById(R.id.createparty);
        createParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createParty = new Intent(getApplicationContext(),CreateParty.class);
                createParty.putExtra(getApplicationContext().getResources().getString(R.string.param_email),strintentEmail);
                createParty.putExtra(getApplicationContext().getResources().getString(R.string.param_token),strintentToken);
                startActivity(createParty);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.accueil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_deco) {
            TraiterAccueil traiter = new TraiterAccueil();
            traiter.deco(strintentEmail,strintentToken,getApplicationContext(),this);
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
                HttpPost httppost = new HttpPost("http://192.168.173.1/partyUTT/homepage.php");
                Log.d("BLAH3",(String) params[0]);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair(getResources().getString(R.string.param_email), (String) params[0]));
                nameValuePairs.add(new BasicNameValuePair(getResources().getString(R.string.param_token), (String) params[1]));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                Log.d("BLAH4", nameValuePairs.toString());

                HttpResponse response = httpclient.execute(httppost);
                content = EntityUtils.toString(response.getEntity(), "utf8");
                Log.d("BLAH",content);
                JSONObject requestAnswer = new JSONObject(content);
                JSONerror = requestAnswer.getString(getResources().getString(R.string.login_POST_answer_error));
                JSONArray JSONevents = requestAnswer.getJSONArray(getResources().getString(R.string.accueil_request_answer_events));
                for (int indice=0; indice < JSONevents.length(); indice++)
                {
                    HashMap contactMap = new HashMap();
                    contactMap.put("eventID", new Integer(JSONevents.getJSONObject(indice).getInt("eventID")));
                    contactMap.put("eventName", JSONevents.getJSONObject(indice).getString("eventName").toString());
                    contactMap.put("userMail", JSONevents.getJSONObject(indice).getString("userMail").toString());
                    contactMap.put("isOrga", JSONevents.getJSONObject(indice).getInt("isOrga"));
                    contactMap.put("isComing", JSONevents.getJSONObject(indice).getInt("isComing"));
                    partyList.add(contactMap);

                    if (contactMap.get("userMail").toString().equals(params[0]))
                    {
                        userParty.eventsOwner.add(contactMap.get("eventName").toString());
                        userParty.IDOwner = (ArrayList<Integer>) contactMap.get("eventID");

                    } else {
                        if (((Integer) contactMap.get("isOrga")) == 1) {
                            userParty.eventsOrga.add(contactMap.get("eventName").toString());
                        } else {
                            userParty.eventsInvite.add(contactMap.get("eventName").toString());
                        }
                    }
                }

                Log.d("Content",content.toString());
                Log.d("contactlist",partyList.toString());

            } catch (ClientProtocolException e) {
                Log.e("ClientProtocolException", e.toString(), e);
            } catch (IOException IO) {
                Log.e("IOException", IO.toString(), IO);
            } catch (Error error) {
                Log.e("POST REQUEST ERROR", error.toString(), error);
            } catch (JSONException e) {
                //erreur dans la création du jsonobject
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
