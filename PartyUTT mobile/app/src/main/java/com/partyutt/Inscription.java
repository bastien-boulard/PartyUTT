package com.partyutt;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.partyutt.Traitement.TraiterInscription;

public class Inscription extends Activity {

    EditText mail,MDP,confirmMDP;
    Button valider;
    TraiterInscription traitement = new TraiterInscription();
    public static Context myContext;
    String resultatMDP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        myContext = this;
        mail = (EditText)findViewById(R.id.editmail);
        MDP = (EditText)findViewById(R.id.motdepasse);
        confirmMDP = (EditText)findViewById(R.id.confirmermotdepasse);

        valider = (Button)findViewById(R.id.btnvalider);
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resultatMDP = traitement.CheckMDP(MDP.getText().toString(),confirmMDP.getText().toString(),getApplicationContext());
                if (resultatMDP.equals(getString(R.string.erreur_OK)))
                {
                    traitement.TentativeInscription(mail.getText().toString(),MDP.getText().toString(),resultatMDP,getApplicationContext());
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
                    builder.setMessage(resultatMDP)
                            .setPositiveButton(R.string.erreur_OK, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    builder.show();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inscription, menu);
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
