package com.partyutt.Traitement;

import android.content.Context;
import android.util.Log;

import com.partyutt.OwnerParty;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bastien on 14/01/2015.
 */
public class TraiterOwnerParty {
    Context traitementContext;
    String token,eventID,userMail;

    public String VerificationZoneDeTexteNonVide(String name, String address, Context ownerPartyContext){
        traitementContext = ownerPartyContext;
        String errorMSG = ownerPartyContext.getResources().getString(R.string.erreur_OK);
        if (name.isEmpty() || name.equals(" "))
        {
            errorMSG = ownerPartyContext.getResources().getString(R.string.erreur_eventNameVide);
        }
        if (address.isEmpty() || address.equals(" "))
        {
            errorMSG = ownerPartyContext.getResources().getString(R.string.erreur_addressVide);
        }
        return errorMSG;
    }

    public String VerifDateEtTime(String date, String time, Context ownerPartyContext){
        this.traitementContext = ownerPartyContext;
        String resultat;
        return date+" "+time;
    }

    public void TraitementOwnerParty(String mail, String token,String eventID, String titre, String adresse, String eventDate, ArrayList<String> inviteMail, ArrayList<String> inviteRole, Context ownerPartyContext){
        traitementContext = ownerPartyContext;
        updateOwnerPartyPOST updatePOST = new updateOwnerPartyPOST();
        ArrayList<String> inviteRoleTraite = new ArrayList<String>();
        int limite = inviteRole.size();
        for (int indice=0;indice<limite;indice++)
        {
            if (inviteRole.get(indice).equals(ownerPartyContext.getResources().getString(R.string.createparty_orga)))
            {
                inviteRoleTraite.add("1");
            } else {
                inviteRoleTraite.add("0");
            }
        }
        updatePOST.execute(ownerPartyContext, ownerPartyContext.getResources().getString(R.string.webservice_updateeventowner), mail, ownerPartyContext.getResources().getString(R.string.param_email), token, ownerPartyContext.getResources().getString(R.string.param_token), eventID, ownerPartyContext.getResources().getString(R.string.param_eventID), titre, ownerPartyContext.getResources().getString(R.string.param_eventName), adresse, ownerPartyContext.getResources().getString(R.string.param_eventAddress), eventDate, ownerPartyContext.getResources().getString(R.string.param_eventDate), inviteMail, ownerPartyContext.getResources().getString(R.string.param_guestEmail), inviteRoleTraite, ownerPartyContext.getResources().getString(R.string.param_guestStatus));
    }

    public String AddToParty(String mailInvite, String roleInvite, Context ownerPartyContext){
        Log.d("TEST",roleInvite);
        Log.d("TEST",mailInvite);
        this.traitementContext = ownerPartyContext;
        String errorMSG = ownerPartyContext.getResources().getString(R.string.createparty_inviteajoute);
        if (mailInvite.isEmpty() || mailInvite.equals(" "))
        {
            errorMSG = ownerPartyContext.getResources().getString(R.string.erreur_eventNameVide);
        } else {
            OwnerParty.arrayListInviteEmail.add(mailInvite);
            OwnerParty.arrayListInviteStatut.add(roleInvite);
            OwnerParty.arrayListInviteApport.add("");
            OwnerParty.arrayListInvitePresence.add(traitementContext.getResources().getString(R.string.param_absent));
            OwnerParty.arrayListInviteQte.add("");
            OwnerParty.adapter.notifyDataSetChanged();
            OwnerParty.editEmailInvite.setText("");
        }
        return errorMSG;
    }

    public void getOwnerPartyInfo(String email, String token, String eventID, Context ownerPartyContext){
        this.traitementContext = ownerPartyContext;
        this.userMail = email;
        this.token = token;
        this.eventID = eventID;
        getOwnerPartyPOST ownerPartyInfoRequest = new getOwnerPartyPOST();
        ownerPartyInfoRequest.execute(ownerPartyContext,ownerPartyContext.getResources().getString(R.string.webservice_party),email,ownerPartyContext.getResources().getString(R.string.param_email),token,ownerPartyContext.getResources().getString(R.string.param_token),eventID,ownerPartyContext.getResources().getString(R.string.param_eventID));

    }

    private class getOwnerPartyPOST extends POSTRequest {

        @Override
        protected void onPostExecute(String parametre) {
            JSONObject requestAnswer = null;
            Log.d("TEST EVENT PAGE", parametre);
            try {
                requestAnswer = new JSONObject(parametre);
                String error = requestAnswer.getString("error");
                String eventName = requestAnswer.getJSONObject(traitementContext.getResources().getString(R.string.param_data)).getString(traitementContext.getResources().getString(R.string.param_eventName));
                String eventDate = requestAnswer.getJSONObject(traitementContext.getResources().getString(R.string.param_data)).getString(traitementContext.getResources().getString(R.string.param_eventDate));
                String eventAddress = requestAnswer.getJSONObject(traitementContext.getResources().getString(R.string.param_data)).getString(traitementContext.getResources().getString(R.string.param_eventAddress));
                if (!(Boolean.getBoolean(error)))
                {

                    for (int i = 0; i < requestAnswer.getJSONArray(traitementContext.getResources().getString(R.string.param_guests)).length(); i++) {
                        if (!(requestAnswer.getJSONArray(traitementContext.getResources().getString(R.string.param_guests)).getJSONObject(i).getString(traitementContext.getResources().getString(R.string.param_userEmail))
                                .equals(userMail)))
                        {

                            OwnerParty.arrayListInviteEmail.add(requestAnswer.getJSONArray(traitementContext.getResources().getString(R.string.param_guests)).getJSONObject(i).getString(traitementContext.getResources().getString(R.string.param_userEmail)));
                            OwnerParty.arrayListInviteApport.add(requestAnswer.getJSONArray(traitementContext.getResources().getString(R.string.param_guests)).getJSONObject(i).getString(traitementContext.getResources().getString(R.string.param_toBring)));
                            OwnerParty.arrayListInviteQte.add(requestAnswer.getJSONArray(traitementContext.getResources().getString(R.string.param_guests)).getJSONObject(i).getString(traitementContext.getResources().getString(R.string.param_quantity)));

                            if (requestAnswer.getJSONArray(traitementContext.getResources().getString(R.string.param_guests)).getJSONObject(i).getString(traitementContext.getResources().getString(R.string.param_isOrga)).equals("1")) {
                                OwnerParty.arrayListInviteStatut.add(traitementContext.getResources().getString(R.string.createparty_orga));
                            } else {
                                OwnerParty.arrayListInviteStatut.add(traitementContext.getResources().getString(R.string.createparty_invite));
                            }

                            if (requestAnswer.getJSONArray(traitementContext.getResources().getString(R.string.param_guests)).getJSONObject(i).getString(traitementContext.getResources().getString(R.string.param_isComing)).equals("1")) {
                                OwnerParty.arrayListInvitePresence.add(traitementContext.getResources().getString(R.string.party_present));
                            } else {
                                OwnerParty.arrayListInvitePresence.add(traitementContext.getResources().getString(R.string.party_absent));
                            }
                        }

                    }
                    String [] dateAndTime = eventDate.split(" ");
                    OwnerParty.date.setText(dateAndTime[0]);
                    OwnerParty.time.setText(dateAndTime[1]);
                    OwnerParty.titre.setText(eventName);
                    OwnerParty.adresse.setText(eventAddress);

                    OwnerParty.adapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private class updateOwnerPartyPOST extends POSTRequest {

        @Override
        protected String doInBackground(Object... params) {
            Context activityContext = (Context) params[0];
            String resultatRequete = activityContext.getResources().getString(R.string.erreur_Erreur);
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
            nameValuePairs.add(new BasicNameValuePair((String) params[3],(String) params[2]));//mail
            nameValuePairs.add(new BasicNameValuePair((String) params[5],(String) params[4]));//token
            nameValuePairs.add(new BasicNameValuePair((String) params[7],(String) params[6]));//id
            nameValuePairs.add(new BasicNameValuePair((String) params[9],(String) params[8]));//titre
            nameValuePairs.add(new BasicNameValuePair((String) params[11],(String) params[10]));//adresse
            nameValuePairs.add(new BasicNameValuePair((String) params[13],(String) params[12]));//date

            ArrayList<String> listeEmail = (ArrayList<String>) params[14];
            ArrayList<String> listeRole = (ArrayList<String>) params[16];
            int limite = listeEmail.size();

            for (int indice=0;indice<limite;indice++) {
                String tamponMail = (String) params[15];
                String tamponRole = (String) params[17];
                nameValuePairs.add(new BasicNameValuePair(tamponMail+indice, listeEmail.get(indice)));
                nameValuePairs.add(new BasicNameValuePair(tamponRole+indice, listeRole.get(indice)));
            }
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
            JSONObject requestAnswer = null;
            Log.d("TEST EVENT UPDATEPARTY",parametre);
            try {
                requestAnswer = new JSONObject(parametre);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
