package com.partyutt;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.partyutt.Traitement.AdapterMAJParty;
import com.partyutt.Traitement.TraiterParty;

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

    public static RelativeLayout layoutAddPeopleToTheParty;
    public static TextView titreParty,titleAddress,titleDate,titleStatut;
    public static EditText editQte, editApport, editEmailInvite;
    public static Spinner spinnerPresence;
    public static ArrayList<String> arrayListInviteEmail = new ArrayList<String>();
    public static ArrayList<String> arrayListInviteStatut = new ArrayList<String>();
    public static ArrayList<String> arrayListInvitePresence = new ArrayList<String>();
    public static ArrayList<String> arrayListInviteApport = new ArrayList<String>();
    public static ArrayList<String> arrayListInviteQte = new ArrayList<String>();
    public static AdapterMAJParty adapter;
    String strintentToken,strintentEmail,strintentID,strintentRole;

    ListView listInvite;
    Button valider, addInvite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party);

        arrayListInviteEmail.clear();
        arrayListInviteApport.clear();
        arrayListInviteQte.clear();
        arrayListInvitePresence.clear();
        arrayListInviteStatut.clear();

        final TraiterParty traiterParty = new TraiterParty();

        valider = (Button)findViewById(R.id.btnmodifyparty);
        addInvite = (Button)findViewById(R.id.btn_addinvite);
        listInvite = (ListView)findViewById(R.id.listView_Invite);
        layoutAddPeopleToTheParty = (RelativeLayout)findViewById(R.id.zoneajoutinvite);
        titreParty = (TextView)findViewById(R.id.titleparty);
        titleStatut = (TextView)findViewById(R.id.titlestatut);
        titleAddress = (TextView)findViewById(R.id.titleaddress);
        titleDate = (TextView)findViewById(R.id.titledate);

        spinnerPresence = (Spinner)findViewById(R.id.spinnerPresence);
        editApport = (EditText)findViewById(R.id.editApport);
        editQte = (EditText)findViewById(R.id.editQte);
        editEmailInvite = (EditText)findViewById(R.id.editinviteemail);

        adapter = new AdapterMAJParty(this,arrayListInviteEmail,arrayListInviteStatut,arrayListInvitePresence,arrayListInviteApport,arrayListInviteQte);
        listInvite.setAdapter(adapter
        );

        if (getIntent().getExtras() != null) {
            Intent intent = getIntent();
            strintentToken = intent.getStringExtra(getResources().getString(R.string.param_token));
            strintentEmail = intent.getStringExtra(getResources().getString(R.string.param_email));
            strintentID = intent.getStringExtra(getResources().getString(R.string.param_ID));
            strintentRole = intent.getStringExtra(getResources().getString(R.string.param_isOrga));
            traiterParty.getPartyInfo(strintentEmail,strintentToken,strintentID,getApplicationContext());
        } else {
            strintentEmail="";
            strintentID="";
            strintentToken="";
            strintentRole="";
        }

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on va mettre a jour les informations de la soirée
                traiterParty.updateParty(strintentEmail, strintentToken, strintentID, spinnerPresence.getSelectedItem().toString(), editApport.getText().toString(), editQte.getText().toString(), strintentRole, getApplicationContext());
            }
        });

        addInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on ajoute l'invité a la liste des invités
                traiterParty.addToParty(editEmailInvite.getText().toString(),getApplicationContext());
            }
        });

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
        return super.onOptionsItemSelected(item);
    }

}
