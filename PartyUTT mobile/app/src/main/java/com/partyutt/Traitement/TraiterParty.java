package com.partyutt.Traitement;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.partyutt.Party;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bastien on 09/01/2015.
 */
public class TraiterParty {

    Context traiterPartyContext;
    String userMail;
    ArrayList<String> arrayAddToTheParty = new ArrayList<String>();

    public void getPartyInfo(String email, String token, String partyID, Context partyContext) {
        traiterPartyContext = partyContext;
        this.userMail = email;
        partyPOST partyInfoRequest = new partyPOST();
        partyInfoRequest.execute(partyContext,partyContext.getResources().getString(R.string.webservice_party),email,partyContext.getResources().getString(R.string.param_email),token,partyContext.getResources().getString(R.string.param_token),partyID,partyContext.getResources().getString(R.string.param_eventID));
    }

    public String addToParty(String email, Context partyContext)
    {
        String errorMSG = partyContext.getResources().getString(R.string.erreur_OK);
        traiterPartyContext = partyContext;
        if (email.isEmpty() || email.equals(" "))
        {
            errorMSG = partyContext.getResources().getString(R.string.erreur_mailVide);
        } else {
            Party.arrayListInviteEmail.add(email);
            Party.arrayListInviteStatut.add(partyContext.getResources().getString(R.string.createparty_invite));
            Party.arrayListInvitePresence.add(partyContext.getResources().getString(R.string.party_absent));
            Party.arrayListInviteApport.add("");
            Party.arrayListInviteQte.add("");
            Party.adapter.notifyDataSetChanged();
            this.arrayAddToTheParty.add(email);
            Party.editEmailInvite.setText("");
        }
        return errorMSG;
    }

    public void updateParty(String mail, String token,String id, String spinnerPresence, String apport, String qte, String role, Context partyContext)
    {
        traiterPartyContext = partyContext;
        updatePartyPOST updatePartyRequest = new updatePartyPOST();
        String adresseWebservice;
        String presence=null;

        if (spinnerPresence.equals(partyContext.getResources().getString(R.string.param_present)))
        {
            presence = "1";
        } else {
            presence = "0";
        }

        if (Integer.valueOf(role)==1)
        {
            adresseWebservice = partyContext.getResources().getString(R.string.webservice_updateeventorga);
            updatePartyRequest.execute(partyContext,adresseWebservice, mail, partyContext.getResources().getString(R.string.param_email), token, partyContext.getResources().getString(R.string.param_token), id, partyContext.getResources().getString(R.string.param_eventID), presence, partyContext.getResources().getString(R.string.param_isComing), apport, partyContext.getResources().getString(R.string.param_toBring), qte, partyContext.getResources().getString(R.string.param_quantity), arrayAddToTheParty, partyContext.getResources().getString(R.string.param_guestList));
        } else {
            adresseWebservice = partyContext.getResources().getString(R.string.webservice_updateinvite);
            updatePartyRequest.execute(partyContext,adresseWebservice, mail, partyContext.getResources().getString(R.string.param_email), token, partyContext.getResources().getString(R.string.param_token), id, partyContext.getResources().getString(R.string.param_eventID), presence, partyContext.getResources().getString(R.string.param_isComing), apport, partyContext.getResources().getString(R.string.param_toBring), qte, partyContext.getResources().getString(R.string.param_quantity));
        }
    }

    private class partyPOST extends POSTRequest {

        @Override
        protected void onPostExecute(String parametre) {
            JSONObject requestAnswer = null;
            Log.d("TEST EVENT PAGE", parametre);
            try {
                requestAnswer = new JSONObject(parametre);
                String error = requestAnswer.getString("error");
                String eventName = requestAnswer.getJSONObject(traiterPartyContext.getResources().getString(R.string.param_data)).getString(traiterPartyContext.getResources().getString(R.string.param_eventName));
                String eventDate = requestAnswer.getJSONObject(traiterPartyContext.getResources().getString(R.string.param_data)).getString(traiterPartyContext.getResources().getString(R.string.param_eventDate));
                String eventAddress = requestAnswer.getJSONObject(traiterPartyContext.getResources().getString(R.string.param_data)).getString(traiterPartyContext.getResources().getString(R.string.param_eventAddress));
                String apport=null,qte = null;
                if (!(Boolean.getBoolean(error)))
                {

                    Party.arrayListInviteEmail.add(requestAnswer.getJSONObject(traiterPartyContext.getResources().getString(R.string.param_data)).getString(traiterPartyContext.getResources().getString(R.string.param_owner)));
                    Party.arrayListInviteStatut.add(traiterPartyContext.getResources().getString(R.string.createparty_createur));
                    Party.arrayListInvitePresence.add(traiterPartyContext.getResources().getString(R.string.party_present));
                    Party.arrayListInviteApport.add("");
                    Party.arrayListInviteQte.add("");
                    for (int i = 0; i < requestAnswer.getJSONArray(traiterPartyContext.getResources().getString(R.string.param_guests)).length(); i++) {
                        if (requestAnswer.getJSONArray(traiterPartyContext.getResources().getString(R.string.param_guests)).getJSONObject(i).getString(traiterPartyContext.getResources().getString(R.string.param_userEmail))
                                .equals(userMail))
                        {
                            apport = requestAnswer.getJSONArray(traiterPartyContext.getResources().getString(R.string.param_guests)).getJSONObject(i).getString(traiterPartyContext.getResources().getString(R.string.param_toBring));
                            qte = requestAnswer.getJSONArray(traiterPartyContext.getResources().getString(R.string.param_guests)).getJSONObject(i).getString(traiterPartyContext.getResources().getString(R.string.param_quantity));

                            if (requestAnswer.getJSONArray(traiterPartyContext.getResources().getString(R.string.param_guests)).getJSONObject(i).getString(traiterPartyContext.getResources().getString(R.string.param_isOrga)).equals("1"))
                            {
                                String statutNewState = Party.titleStatut.getText().toString().concat(" "+traiterPartyContext.getResources().getString(R.string.createparty_orga));
                                Party.titleStatut.setText(statutNewState);
                            } else {
                                String statutNewState = Party.titleStatut.getText().toString().concat(" "+traiterPartyContext.getResources().getString(R.string.createparty_invite));
                                Party.titleStatut.setText(statutNewState);
                                Party.layoutAddPeopleToTheParty.setVisibility(View.GONE);
                            }

                            if (requestAnswer.getJSONArray(traiterPartyContext.getResources().getString(R.string.param_guests)).getJSONObject(i).getString(traiterPartyContext.getResources().getString(R.string.param_isComing)).equals("1")) {
                                Party.spinnerPresence.setSelection(0);
                            } else {
                                Party.spinnerPresence.setSelection(1);
                            }

                        } else {

                            Party.arrayListInviteEmail.add(requestAnswer.getJSONArray(traiterPartyContext.getResources().getString(R.string.param_guests)).getJSONObject(i).getString(traiterPartyContext.getResources().getString(R.string.param_userEmail)));
                            Party.arrayListInviteApport.add(requestAnswer.getJSONArray(traiterPartyContext.getResources().getString(R.string.param_guests)).getJSONObject(i).getString(traiterPartyContext.getResources().getString(R.string.param_toBring)));
                            Party.arrayListInviteQte.add(requestAnswer.getJSONArray(traiterPartyContext.getResources().getString(R.string.param_guests)).getJSONObject(i).getString(traiterPartyContext.getResources().getString(R.string.param_quantity)));

                            if (requestAnswer.getJSONArray(traiterPartyContext.getResources().getString(R.string.param_guests)).getJSONObject(i).getString(traiterPartyContext.getResources().getString(R.string.param_isOrga)).equals("1")) {
                                Party.arrayListInviteStatut.add(traiterPartyContext.getResources().getString(R.string.createparty_orga));
                            } else {
                                Party.arrayListInviteStatut.add(traiterPartyContext.getResources().getString(R.string.createparty_invite));
                            }

                            if (requestAnswer.getJSONArray(traiterPartyContext.getResources().getString(R.string.param_guests)).getJSONObject(i).getString(traiterPartyContext.getResources().getString(R.string.param_isComing)).equals("1")) {
                                Party.arrayListInvitePresence.add(traiterPartyContext.getResources().getString(R.string.party_present));
                            } else {
                                Party.arrayListInvitePresence.add(traiterPartyContext.getResources().getString(R.string.party_absent));
                            }
                        }

                    }
                    Party.titreParty.setText(eventName);
                    Party.titleAddress.setText(eventAddress);
                    Party.titleDate.setText(eventDate);
                    Party.editApport.setText(apport);
                    Party.editQte.setText(qte);

                    Party.adapter.notifyDataSetChanged();

                    Party.arrayListInviteEmail.clear();
                    Party.arrayListInviteStatut.clear();
                    Party.arrayListInvitePresence.clear();
                    Party.arrayListInviteApport.clear();
                    Party.arrayListInviteQte.clear();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            }


        }

    private class updatePartyPOST extends POSTRequest {

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
            nameValuePairs.add(new BasicNameValuePair((String) params[9],(String) params[8]));//presence
            nameValuePairs.add(new BasicNameValuePair((String) params[11],(String) params[10]));//apport
            nameValuePairs.add(new BasicNameValuePair((String) params[13],(String) params[12]));//quantité

            ArrayList<String> listeEmail = (ArrayList<String>) params[14];
            int limite = listeEmail.size();

            for (int indice=0;indice<limite;indice++) {
                String tampon = (String) params[15];
                nameValuePairs.add(new BasicNameValuePair(tampon+indice, listeEmail.get(indice)));
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
