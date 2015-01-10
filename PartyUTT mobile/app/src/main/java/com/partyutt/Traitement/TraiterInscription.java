package com.partyutt.Traitement;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.partyutt.Inscription;
import com.partyutt.R;
import com.partyutt.Webservice.POSTRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Bastien on 07/01/2015.
 */
public class TraiterInscription {

    int tailleMinimaleMDP = 5;
    Context activityContext;

    public String CheckMDP (String password, String confirmationPassword, Context inscriptionContext)
    {
        if (password.length() < tailleMinimaleMDP) {
            return inscriptionContext.getResources().getString(R.string.inscription_erreurtaillemdp);
        } else {
            if (password.equals(confirmationPassword)) {
                return inscriptionContext.getResources().getString(R.string.erreur_OK);
            } else {
                return inscriptionContext.getResources().getString(R.string.inscription_erreurconfirmation);
            }
        }
    }

    public void TentativeInscription (String mail,String password, String resultatMDP, Context inscriptionContext)
    {
        activityContext = inscriptionContext;
        inscriptionPOST httprequest = new inscriptionPOST();
        httprequest.execute(inscriptionContext,inscriptionContext.getResources().getString(R.string.webservice_inscription),password,inscriptionContext.getResources().getString(R.string.param_password),mail,inscriptionContext.getResources().getString(R.string.param_email));
    }

    private class inscriptionPOST extends POSTRequest {

        private String userEmail;
        private String JSONerror, JSONtoken, JSONpseudo;

        @Override
        protected void onPostExecute(String parametre) {
            String JSONError, JSONMessage;
            Log.d("TEST",parametre);
            //traitement après asyncTask
            try {
                JSONObject resultatPOST = new JSONObject(parametre);
                JSONError = resultatPOST.getString("");
                JSONMessage = resultatPOST.getString("");

                if (Boolean.valueOf(JSONError)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Inscription.myContext);
                    builder.setMessage(JSONMessage)
                            .setTitle(activityContext.getResources().getString(R.string.erreur_Erreur))
                            .setPositiveButton(R.string.erreur_OK, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    builder.create();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activityContext);
                    builder.setMessage(activityContext.getString(R.string.inscription_félicitations))
                            .setTitle(activityContext.getResources().getString(R.string.inscription_inscriptionOK))
                            .setPositiveButton(R.string.erreur_OK, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    builder.show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
