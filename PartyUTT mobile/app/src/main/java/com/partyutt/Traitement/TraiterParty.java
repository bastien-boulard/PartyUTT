package com.partyutt.Traitement;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.partyutt.Party;
import com.partyutt.R;
import com.partyutt.Webservice.POSTRequest;

import org.json.JSONException;
import org.json.JSONObject;

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

    public void getPartyInfo(String email, String token, String partyID, Context partyContext) {
        traiterPartyContext = partyContext;
        userMail = email;
        partyPOST partyInfoRequest = new partyPOST();
        partyInfoRequest.execute(partyContext,partyContext.getResources().getString(R.string.webservice_login),email,partyContext.getResources().getString(R.string.param_password),token,partyContext.getResources().getString(R.string.param_email),partyID,partyContext.getResources().getString(R.string.param_eventID));
    }

    private class partyPOST extends POSTRequest {

        @Override
        protected void onPostExecute(String parametre) {
            List<Map<String, String>> contactList = new ArrayList<Map<String, String>>();
            JSONObject requestAnswer = null;
            try {
                requestAnswer = new JSONObject(parametre);
                String error = requestAnswer.getString("error");
                String eventName = requestAnswer.getJSONObject("data").getString("eventName");
                String eventDate = requestAnswer.getJSONObject("data").getString("eventDate");
                String eventAddress = requestAnswer.getJSONObject("data").getString("eventAddress");

                if (!(Boolean.getBoolean(error)))
                {

                    for (int i = 0; i < requestAnswer.getJSONArray(traiterPartyContext.getResources().getString(R.string.param_guests)).length(); i++) {
                        HashMap contactMap = new HashMap();
                        contactMap.put(traiterPartyContext.getResources().getString(R.string.param_email), requestAnswer.getJSONArray(traiterPartyContext.getResources().getString(R.string.param_guests)).getJSONObject(i).getString(traiterPartyContext.getResources().getString(R.string.param_email)));
                        contactMap.put(traiterPartyContext.getResources().getString(R.string.param_isOrga), requestAnswer.getJSONArray(traiterPartyContext.getResources().getString(R.string.param_guests)).getJSONObject(i).getString(traiterPartyContext.getResources().getString(R.string.param_isOrga)));
                        contactMap.put(traiterPartyContext.getResources().getString(R.string.param_isComing), requestAnswer.getJSONArray(traiterPartyContext.getResources().getString(R.string.param_guests)).getJSONObject(i).getString(traiterPartyContext.getResources().getString(R.string.param_isComing)));
                        contactList.add(contactMap);
                    }
                    Party.titreParty.setText(eventName);
                    Party.titleStatut.setText("OWNER !");
                    Party.titleAddress.setText(eventAddress);
                    Party.titleDate.setText(eventDate);

                /*
                for (int i=0;i<contactList.size();i++)
                {

                    TableLayout tl = (TableLayout) findViewById(R.id.tablelayout);
                    TableRow tr = new TableRow(result);
                    tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                    TextView invitation = new TextView(result);
                    invitation.setText(contactList.get(i).get(param_userPseudo));


                    // si c'est l'utilisateur alors on va mettre son statut en gras
                    if (contactList.get(i).get(traiterPartyContext.getResources().getString(R.string.param_email)).equals(userMail)) {
                        invitation.setTypeface(null, Typeface.BOLD);
                    }
                    invitation.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                    tr.addView(invitation);
                    tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
                }*/
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            }


        }
    }
