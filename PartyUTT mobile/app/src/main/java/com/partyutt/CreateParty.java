package com.partyutt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.partyutt.Traitement.CustomAdapter;
import com.partyutt.Traitement.TraiterCreateParty;


import java.util.ArrayList;
import java.util.Calendar;

// envoyer requete post => newevent.php

public class CreateParty extends Activity {

    Button createParty, addInvite;
    Context createPartyContext;
    String strintentToken,strintentEmail;
    EditText titre,adresse;
    public static EditText inviteMail;
    TextView date,time;
    Spinner inviteRole;
    public static ListView listInvite;
    public static ArrayList<String> arrayListMailInvite = new ArrayList<String>();
    public static ArrayList<String> arrayListRoleInvite = new ArrayList<String>();
    public static CustomAdapter customAdapter;

    private int year;
    private int month;
    private int day;
    private int hour;
    private int min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_party);
        final TraiterCreateParty traiterCreateParty = new TraiterCreateParty();

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
        addInvite = (Button)findViewById(R.id.btn_addinvite);
        inviteMail = (EditText)findViewById(R.id.editinviteemail);
        inviteRole = (Spinner)findViewById(R.id.spinner);
        listInvite = (ListView)findViewById(R.id.listView_Invite);

        customAdapter = new CustomAdapter(this,arrayListMailInvite,arrayListRoleInvite);
        listInvite.setAdapter(customAdapter);


        addInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = traiterCreateParty.AddToParty(inviteMail.getText().toString(), inviteRole.getSelectedItem().toString(), getApplicationContext());
                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG);
            }
        });

        listInvite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateParty.this);
                builder.setTitle(R.string.createparty_titredialog);
                builder.setMessage(R.string.createparty_supprimercetinvite);
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        arrayListRoleInvite.remove(position);
                        arrayListMailInvite.remove(position);
                        customAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        createPartyContext = this;

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
                resultatTests = traiterCreateParty.VerificationZoneDeTexteNonVide(strTitre,strAdresse,getApplicationContext());
                if (getApplicationContext().getResources().getString(R.string.erreur_OK).equals(resultatTests)) {
                    resultatDate = traiterCreateParty.VerifDateEtTime(date.getText().toString(),time.getText().toString(),getApplicationContext());
                    traiterCreateParty.TraitementCreateParty(strintentEmail, strintentToken, strTitre, strAdresse, resultatDate, arrayListMailInvite, arrayListRoleInvite, getApplicationContext());
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
                    time.setText(new StringBuilder()
                            .append("0").append(hour).append(":").append("0").append(minute).append(":00"));
                } else {
                    time.setText(new StringBuilder()
                            .append("0").append(hour).append(":").append(minute).append(":00"));
                }
            } else {
                if (min<10)
                {
                    time.setText(new StringBuilder()
                            .append(hour).append(":").append("0").append(minute).append(":00"));
                } else {
                    time.setText(new StringBuilder()
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
        return super.onOptionsItemSelected(item);
    }
}
