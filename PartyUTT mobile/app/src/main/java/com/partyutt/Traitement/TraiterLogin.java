package com.partyutt.Traitement;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import com.partyutt.Accueil;
import com.partyutt.R;
import com.partyutt.Webservice.OnTaskCompleted;
import com.partyutt.Webservice.POSTRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Bastien on 08/01/2015.
 */
public class TraiterLogin {

    Context postexecuteContext;
    String userEmail;

    public String verificationDonneesLogin(String mail, String password, Context loginContext)
    {
        postexecuteContext = loginContext;
        String errorMSG=loginContext.getResources().getString(R.string.erreur_OK);

        // si pas de mail alors on affiche une erreur le faisant remarquer
        if  (mail.length()==0)
        {
            errorMSG = loginContext.getResources().getString(R.string.erreur_mailVide);
        }
        // si pas de mot de passe alors on affiche une erreur le faisant remarquer
        if (password.length()==0) {
            errorMSG = loginContext.getResources().getString(R.string.erreur_MDPVide);
        }

        return errorMSG;
    }

    public void requeteLogin(String mail, String password, Context loginContext) {
        userEmail = mail;
        loginPOST loginRequest = new loginPOST();
        loginRequest.execute(loginContext,loginContext.getResources().getString(R.string.webservice_login),password,loginContext.getResources().getString(R.string.param_password),mail,loginContext.getResources().getString(R.string.param_email));
    }

    private class loginPOST extends POSTRequest {

        private String JSONerror, JSONtoken;

        @Override
        protected void onPostExecute(String parametre) {
            //traitement après asyncTask
            try {
                Log.d("TEST", parametre);
                JSONObject requestAnswer = new JSONObject(parametre);
                JSONerror = requestAnswer.getString(postexecuteContext.getResources().getString(R.string.login_POST_answer_error));
                JSONtoken = requestAnswer.getString(postexecuteContext.getResources().getString(R.string.login_POST_answer_token));
                Intent accueilIntent = new Intent(postexecuteContext,Accueil.class);
                accueilIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                accueilIntent.putExtra(postexecuteContext.getResources().getString(R.string.param_email),userEmail);
                accueilIntent.putExtra(postexecuteContext.getResources().getString(R.string.param_token),JSONtoken);
                postexecuteContext.startActivity(accueilIntent);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
