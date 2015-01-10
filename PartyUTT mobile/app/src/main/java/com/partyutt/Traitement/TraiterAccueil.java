package com.partyutt.Traitement;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;

import com.partyutt.R;
import com.partyutt.Webservice.OnTaskCompleted;
import com.partyutt.Webservice.POSTRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bastien on 08/01/2015.
 */
public class TraiterAccueil implements OnTaskCompleted {
    Context traitementContext;
    String userMail;

    public void recuperationPartyAccueil (String mail, String token, Context accueilContext) {
        traitementContext = accueilContext;
        userMail = mail;
        accueilPOST accueilRequest = new accueilPOST();
        accueilRequest.execute(accueilContext,accueilContext.getResources().getString(R.string.webservice_accueil),accueilContext.getResources().getString(R.string.param_email),mail,accueilContext.getResources().getString(R.string.param_token),token);
    }

    @Override
    public Object onTaskCompleted(String parametre) {
        String JSONerror;
        AllParty userParty = null;
        List<Map<String, String>> partyList = new ArrayList<Map<String, String>>();

        JSONObject requestAnswer = null;
        try {
            requestAnswer = new JSONObject(parametre);
            JSONerror = requestAnswer.getString(traitementContext.getResources().getString(R.string.login_POST_answer_error));
            if (!(Boolean.valueOf(JSONerror))) {
                JSONArray JSONevents = requestAnswer.getJSONArray(traitementContext.getResources().getString(R.string.accueil_request_answer_events));
                for (int indice = 0; indice < JSONevents.length(); indice++) {
                    HashMap contactMap = new HashMap();
                    contactMap.put(traitementContext.getResources().getString(R.string.param_eventID), new Integer(JSONevents.getJSONObject(indice).getInt(traitementContext.getResources().getString(R.string.param_eventID))));
                    contactMap.put(traitementContext.getResources().getString(R.string.param_eventName), JSONevents.getJSONObject(indice).getString(traitementContext.getResources().getString(R.string.param_eventName).toString()));
                    contactMap.put(traitementContext.getResources().getString(R.string.param_email), JSONevents.getJSONObject(indice).getString(traitementContext.getResources().getString(R.string.param_email).toString()));
                    contactMap.put(traitementContext.getResources().getString(R.string.param_isOrga), JSONevents.getJSONObject(indice).getInt(traitementContext.getResources().getString(R.string.param_isOrga)));
                    contactMap.put(traitementContext.getResources().getString(R.string.param_isComing), JSONevents.getJSONObject(indice).getInt(traitementContext.getResources().getString(R.string.param_isComing)));
                    partyList.add(contactMap);

                    if (contactMap.get((traitementContext.getResources().getString(R.string.param_email)).toString()).equals(userMail)) {
                        userParty.eventsOwner.add(contactMap.get(traitementContext.getResources().getString(R.string.param_eventName)).toString());
                        userParty.IDOwner.add(Integer.valueOf(contactMap.get(traitementContext.getResources().getString(R.string.param_eventID)).toString()));
                    } else {
                        if (((Integer) contactMap.get(traitementContext.getResources().getString(R.string.param_isOrga))) == 1) {
                            userParty.eventsOrga.add(contactMap.get(traitementContext.getResources().getString(R.string.param_eventName)).toString());
                            userParty.IDOrga.add(Integer.valueOf(contactMap.get(traitementContext.getResources().getString(R.string.param_eventID)).toString()));
                        } else {
                            userParty.eventsInvite.add(contactMap.get(traitementContext.getResources().getString(R.string.param_eventName)).toString());
                            userParty.IDInvite.add(Integer.valueOf(contactMap.get(traitementContext.getResources().getString(R.string.param_eventID)).toString()));
                        }
                    }
                }
            }
            }catch(JSONException e){
                e.printStackTrace();
            }

        return userParty;

    }

    private class accueilPOST extends POSTRequest {

        private String userEmail;
        private String JSONerror, JSONtoken, JSONpseudo;

        @Override
        protected void onPostExecute(String parametre) {
            String JSONError, JSONMessage;
            //traitement après asyncTask
            try {
                JSONObject resultatPOST = new JSONObject(parametre);
                JSONError = resultatPOST.getString("");
                JSONMessage = resultatPOST.getString("");


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class AllParty {
        public ArrayList<String> eventsOwner = new ArrayList<String>();
        public ArrayList<String> eventsOrga = new ArrayList<String>();
        public ArrayList<String> eventsInvite = new ArrayList<String>();
        public ArrayList<Integer> IDOwner = new ArrayList<Integer>();
        public ArrayList<Integer> IDOrga = new ArrayList<Integer>();
        public ArrayList<Integer> IDInvite = new ArrayList<Integer>();
    }
}
