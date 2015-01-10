package com.partyutt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.partyutt.Traitement.Custom_Dialog;
import com.partyutt.Traitement.TraiterCreateParty;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

// envoyer requete post => newevent.php

public class CreateParty extends Activity {

    Button createParty;
    TableRow addToTheParty;
    Context createPartyContext;
    String strintentToken,strintentEmail;
    EditText titre,adresse;
    TextView date,time;

    private int year;
    private int month;
    private int day;
    private int hour;
    private int min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_party);

        if (getIntent().getExtras() != null) {
            Intent intent = getIntent();
            strintentToken = intent.getStringExtra(getApplicationContext().getResources().getString(R.string.param_token));
            strintentEmail = intent.getStringExtra(getApplicationContext().getResources().getString(R.string.param_email));
        } else {
            strintentEmail="";
            strintentToken="";
        }

        date = (TextView)findViewById(R.id.partydate);
        time = (TextView)findViewById(R.id.partyheure);
        titre = (EditText)findViewById(R.id.partytitle);
        adresse = (EditText)findViewById(R.id.partyaddress);

        createPartyContext = this;
        addToTheParty = (TableRow)findViewById(R.id.columnaddppl);
        addToTheParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Custom_Dialog cd = new Custom_Dialog(CreateParty.this);
                cd.show();
                /*final Dialog addPeopleDialog = new Dialog(CreateParty.this);
                addPeopleDialog.setContentView(R.layout.add_guest_dialog);
                final View inflatedView = getLayoutInflater().inflate(R.layout.add_guest_dialog,null);

                Spinner statutSpinner = (Spinner)inflatedView.findViewById(R.id.statut_spinner);
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                statutSpinner.setAdapter(spinnerAdapter);
                addPeopleDialog.show();

                Button cancelBtn = (Button)inflatedView.findViewById(R.id.buttonCancel);
                Button addBtn = (Button)inflatedView.findViewById(R.id.buttonAdd);
                LinearLayout layoutMail = (LinearLayout)inflatedView.findViewById(R.id.layoutMail);
                final EditText editMail = (EditText)inflatedView.findViewById(R.id.editMail);

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editMail.setText("trololo");
                        addPeopleDialog.dismiss();
                    }
                });
                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // add the the table layout
                        /*
                        TableLayout tl = (TableLayout) findViewById(R.id.tablelayout);
                        TableRow tr = new TableRow(createPartyContext);
                        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                        TextView invitation = new TextView(createPartyContext);
                        invitation.setText(editMail.getText().toString());
                        invitation.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                        tr.addView(invitation);
                        tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
                        addPeopleDialog.dismiss();
                    }
                });*/
            }
        });

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);

        // Show current date

        if (day<10)
        {
            if(month<10)
            {
                date.setText(new StringBuilder()
                        // Month is 0 based, just add 1
                        .append(year).append("-").append("0").append(month + 1).append("-")
                        .append("0").append(day));
            } else {
                date.setText(new StringBuilder()
                        // Month is 0 based, just add 1
                        .append(year).append("-").append(month + 1).append("-")
                        .append("0").append(day));
            }
        } else {
            if (month<10)
            {
                date.setText(new StringBuilder()
                        // Month is 0 based, just add 1
                        .append(year).append("-").append("0").append(month + 1).append("-")
                        .append(day));
            } else {
                date.setText(new StringBuilder()
                        // Month is 0 based, just add 1
                        .append(year).append("-").append(month + 1).append("-")
                        .append(day));
            }
        }

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(1111);
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(2222);
            }
        });

        createParty = (Button)findViewById(R.id.btncreateparty);
        createParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strTitre = titre.getText().toString();
                String strAdresse = adresse.getText().toString();
                String resultatTests,resultatDate;
                TraiterCreateParty traiterCreateParty = new TraiterCreateParty();
                resultatTests = traiterCreateParty.VerificationZoneDeTexteNonVide(strTitre,strAdresse,getApplicationContext());
                if (getApplicationContext().getResources().getString(R.string.erreur_OK).equals(resultatTests)) {
                    resultatDate = traiterCreateParty.VerifDateEtTime(date.getText().toString(),time.getText().toString(),getApplicationContext());
                    traiterCreateParty.TraitementCreateParty(strintentEmail, strintentToken, strTitre, strAdresse, resultatDate, getApplicationContext());
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                    builder.setMessage(resultatTests)
                            .setTitle(R.string.erreur_Erreur)
                            .setPositiveButton(R.string.erreur_OK, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    builder.create();
                }
                // requête POST avec les informations de l'event
                // on ferme cette page => on revient sur la page d'accueil

            }
        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 1111:

                // open datepicker dialog.
                // set date picker for current date
                // add pickerListener listner to date picker
                return new DatePickerDialog(this, pickerListener, year, month,day);
            case 2222:
                return new TimePickerDialog(this,timePickerListener,hour,min,false);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;

            // Show selected date
            if (day<10)
            {
                if(month<10)
                {
                    date.setText(new StringBuilder()
                            // Month is 0 based, just add 1
                            .append(year).append("-").append("0").append(month + 1).append("-")
                            .append("0").append(day));
                } else {
                    date.setText(new StringBuilder()
                            // Month is 0 based, just add 1
                            .append(year).append("-").append(month + 1).append("-")
                            .append("0").append(day));
                }
            } else {
                if (month<10)
                {
                    date.setText(new StringBuilder()
                            // Month is 0 based, just add 1
                            .append(year).append("-").append("0").append(month + 1).append("-")
                            .append(day));
                } else {
                    date.setText(new StringBuilder()
                            // Month is 0 based, just add 1
                            .append(year).append("-").append(month + 1).append("-")
                            .append(day));
                }
            }

        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hour  = hourOfDay;
            min = minute;
            if (hour<10)
            {
                if(min<10)
                {
                    date.setText(new StringBuilder()
                            .append("0").append(hour).append(":").append("0").append(minute).append(":00"));
                } else {
                    date.setText(new StringBuilder()
                            .append("0").append(hour).append(":").append(minute).append(":00"));
                }
            } else {
                if (min<10)
                {
                    date.setText(new StringBuilder()
                            .append(hour).append(":").append("0").append(minute).append(":00"));
                } else {
                    date.setText(new StringBuilder()
                            .append(hour).append(":").append(minute).append(":00"));
                }
            }
        }
    };

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
}
