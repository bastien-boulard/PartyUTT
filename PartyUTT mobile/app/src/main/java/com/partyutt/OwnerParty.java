package com.partyutt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.partyutt.Traitement.AdapterMAJParty;
import com.partyutt.Traitement.TraiterOwnerParty;

import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.Calendar;


public class OwnerParty extends Activity {

    public static RelativeLayout layoutAddPeopleToTheParty;
    public static EditText editEmailInvite, titre, adresse;
    public static Spinner spinnerInvite;
    public static ArrayList<String> arrayListInviteEmail = new ArrayList<String>();
    public static ArrayList<String> arrayListInviteStatut = new ArrayList<String>();
    public static ArrayList<String> arrayListInvitePresence = new ArrayList<String>();
    public static ArrayList<String> arrayListInviteApport = new ArrayList<String>();
    public static ArrayList<String> arrayListInviteQte = new ArrayList<String>();
    public static AdapterMAJParty adapter;
    String strintentToken,strintentEmail,strintentID,strintentRole;
    public static TextView date,time;

    private int year;
    private int month;
    private int day;
    private int hour;
    private int min;

    ListView listInvite;
    Button valider, addInvite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_party);

        arrayListInviteEmail.clear();
        arrayListInvitePresence.clear();
        arrayListInviteQte.clear();
        arrayListInviteApport.clear();
        arrayListInviteStatut.clear();

        final TraiterOwnerParty traiterOwnerParty = new TraiterOwnerParty();

        date = (TextView)findViewById(R.id.partydate);
        time = (TextView)findViewById(R.id.partyheure);
        titre = (EditText)findViewById(R.id.partytitle);
        adresse = (EditText)findViewById(R.id.partyaddress);

        valider = (Button)findViewById(R.id.btncreateparty);
        addInvite = (Button)findViewById(R.id.btn_addinvite);
        listInvite = (ListView)findViewById(R.id.listView_Invite);
        layoutAddPeopleToTheParty = (RelativeLayout)findViewById(R.id.zoneajoutinvite);

        spinnerInvite = (Spinner)findViewById(R.id.spinner);
        editEmailInvite = (EditText)findViewById(R.id.editinviteemail);

        adapter = new AdapterMAJParty(this,arrayListInviteEmail,arrayListInviteStatut,arrayListInvitePresence,arrayListInviteApport,arrayListInviteQte);
        listInvite.setAdapter(adapter);

        if (getIntent().getExtras() != null) {
            Intent intent = getIntent();
            strintentToken = intent.getStringExtra(getResources().getString(R.string.param_token));
            strintentEmail = intent.getStringExtra(getResources().getString(R.string.param_email));
            strintentID = intent.getStringExtra(getResources().getString(R.string.param_ID));
            traiterOwnerParty.getOwnerPartyInfo(strintentEmail, strintentToken, strintentID, getApplicationContext());
        } else {
            strintentEmail="";
            strintentID="";
            strintentToken="";
        }

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on va mettre a jour les informations de la soirée
                String strTitre = titre.getText().toString();
                String strAdresse = adresse.getText().toString();
                String resultatTests,resultatDate;
                resultatTests = traiterOwnerParty.VerificationZoneDeTexteNonVide(strTitre,strAdresse,getApplicationContext());
                if (getApplicationContext().getResources().getString(R.string.erreur_OK).equals(resultatTests)) {
                    resultatDate = traiterOwnerParty.VerifDateEtTime(date.getText().toString(),time.getText().toString(),getApplicationContext());
                    traiterOwnerParty.TraitementOwnerParty(strintentEmail, strintentToken, strintentID, strTitre, strAdresse, resultatDate, arrayListInviteEmail, arrayListInviteStatut, getApplicationContext());
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
                }
        });

        listInvite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OwnerParty.this);
                builder.setMessage(R.string.ownerparty_quefaire)
                        .setTitle(R.string.ownerparty_gestion)
                        .setPositiveButton(R.string.ownerparty_changerstatut, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (arrayListInviteStatut.get(position).equals(getResources().getString(R.string.createparty_orga)))
                                {
                                    arrayListInviteStatut.set(position, getResources().getString(R.string.createparty_invite));
                                } else {
                                    arrayListInviteStatut.set(position,getResources().getString(R.string.createparty_orga));
                                }
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNeutralButton(R.string.ownerparty_annuler, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                builder.show();
            }
        });

        addInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on ajoute l'invité a la liste des invités
                traiterOwnerParty.AddToParty(editEmailInvite.getText().toString(), spinnerInvite.getSelectedItem().toString(), getApplicationContext());
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
        getMenuInflater().inflate(R.menu.owner_party, menu);
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
