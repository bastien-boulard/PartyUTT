package com.partyutt.Traitement;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.partyutt.CreateParty;
import com.partyutt.R;
import com.partyutt.Webservice.POSTRequest;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bastien on 09/01/2015.
 */
public class TraiterCreateParty {

    Context traitementContext;

    public String VerificationZoneDeTexteNonVide(String name, String address, Context createPartyContext){
        traitementContext = createPartyContext;
        String errorMSG = createPartyContext.getResources().getString(R.string.erreur_OK);
        if (name.isEmpty() || name.equals(" "))
        {
            errorMSG = createPartyContext.getResources().getString(R.string.erreur_eventNameVide);
        }
        if (address.isEmpty() || address.equals(" "))
        {
            errorMSG = createPartyContext.getResources().getString(R.string.erreur_addressVide);
        }
        return errorMSG;
    }

    public String VerifDateEtTime(String date, String time, Context createPartyContext){
        this.traitementContext = createPartyContext;
        String resultat;
        return date+" "+time;
    }

    public void TraitementCreateParty(String mail, String token,String titre,String adresse, String eventDate, ArrayList<String> inviteMail, ArrayList<String> inviteRole, Context createPartyContext){
        traitementContext = createPartyContext;
        createPartyPOST createPOST = new createPartyPOST();
        ArrayList<String> inviteRoleTraite = new ArrayList<String>();
        int limite = inviteRole.size();
        for (int indice=0;indice<limite;indice++)
        {
            if (inviteRole.get(indice).equals(createPartyContext.getResources().getString(R.string.createparty_orga)))
            {
                inviteRoleTraite.add("1");
            } else {
                inviteRoleTraite.add("0");
            }
        }
        createPOST.execute(createPartyContext, createPartyContext.getResources().getString(R.string.webservice_createparty), mail, createPartyContext.getResources().getString(R.string.param_email), token, createPartyContext.getResources().getString(R.string.param_token), titre, createPartyContext.getResources().getString(R.string.param_eventName), adresse, createPartyContext.getResources().getString(R.string.param_eventAddress), eventDate, createPartyContext.getResources().getString(R.string.param_eventDate), inviteMail, inviteRoleTraite);
    }

    public String AddToParty(String mailInvite, String roleInvite, Context createPartyContext){
        Log.d("TEST",roleInvite);
        Log.d("TEST",mailInvite);
        traitementContext = createPartyContext;
        String errorMSG = createPartyContext.getResources().getString(R.string.createparty_inviteajoute);
        if (mailInvite.isEmpty() || mailInvite.equals(" "))
        {
            errorMSG = createPartyContext.getResources().getString(R.string.erreur_eventNameVide);
        } else {
            CreateParty.arrayListMailInvite.add(mailInvite);
            CreateParty.arrayListRoleInvite.add(roleInvite);
            CreateParty.customAdapter.notifyDataSetChanged();
            CreateParty.inviteMail.setText("");
        }
        return errorMSG;
    }

    private class createPartyPOST extends POSTRequest {

        @Override
        protected String doInBackground(Object... params) {
            Context activityContext;
            String resultatRequete;
            activityContext = (Context) params[0];
            resultatRequete = activityContext.getResources().getString(R.string.erreur_Erreur);
            int limiteBoucle = (params.length - 2);

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost((String) params[1]);

        /*
        param 0 : Context
        param 1 : Adresse pour la requête POST

        param n : variable
        param n+1 : nom de la variable
        où n est un nombre pair => 2
        exemple:
            2 est la variable X
            3 est son nom "Resultat"
         */

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair((String) params[3],(String) params[2]));
            nameValuePairs.add(new BasicNameValuePair((String) params[5],(String) params[4]));
            nameValuePairs.add(new BasicNameValuePair((String) params[7],(String) params[6]));
            nameValuePairs.add(new BasicNameValuePair((String) params[9],(String) params[8]));
            nameValuePairs.add(new BasicNameValuePair((String) params[11],(String) params[10]));
            ArrayList<String> addressMail= (ArrayList<String>) params[12];
            ArrayList<String> statutGugus= (ArrayList<String>) params[13];
            int limite = addressMail.size();
            String lblMail = activityContext.getResources().getString(R.string.param_guestEmail);
            String lblStatus = activityContext.getResources().getString(R.string.param_guestStatus);
            for (int indice=0;indice<limite;indice++)
            {
               nameValuePairs.add(new BasicNameValuePair(lblMail+indice,addressMail.get(indice)));
                nameValuePairs.add(new BasicNameValuePair(lblStatus+indice,statutGugus.get(indice)));
            }
            Log.d("TEST", String.valueOf(nameValuePairs));
            //nameValuePairs.


            /*
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                //on passe donc de l'indice 4 au 6 puis 8 et ainsi de suite jusqu'au dernier indice
                //par exemple pour 3 paramètres la boucle commencera à 4 et terminera à 8
                nameValuePairs.add(new BasicNameValuePair((String) params[2],(String) params[3]));
                nameValuePairs.add(new BasicNameValuePair((String) params[2],(String) params[3]));
            */

            try {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            HttpResponse response = null;
            try {
                response = httpclient.execute(httppost);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                resultatRequete = EntityUtils.toString(response.getEntity(), "utf8");
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            return resultatRequete;
        }

        @Override
        protected void onPostExecute(String parametre) {
            Log.d("Test",parametre);
        }
    }
}
