package com.partyutt.Traitement;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import com.partyutt.Accueil;
import com.partyutt.R;
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
public class TraiterAccueil{
    Context traitementContext;
    String userMail;
    Activity stop;

    public void recuperationPartyAccueil (String mail, String token, Context accueilContext) {
        traitementContext = accueilContext;
        userMail = mail;
        accueilPOST accueilRequest = new accueilPOST();
        accueilRequest.execute(accueilContext,accueilContext.getResources().getString(R.string.webservice_accueil),mail,accueilContext.getResources().getString(R.string.param_email),token,accueilContext.getResources().getString(R.string.param_token));
    }

    public void deco (String mail, String token, Context discoContext, Activity activity){
        this.stop = activity;
        accueildeco disco = new accueildeco();
        disco.execute(discoContext,discoContext.getResources().getString(R.string.webservice_disco),mail,discoContext.getResources().getString(R.string.param_email),token,discoContext.getResources().getString(R.string.param_token));
    }

    private class accueilPOST extends POSTRequest {

        private String userEmail;
        private String JSONerror, JSONtoken, JSONpseudo;

        @Override
        protected void onPostExecute(String parametre) {
            String JSONerror;
            Accueil.userParty = new AllParty();
            List<Map<String, String>> partyList = new ArrayList<Map<String, String>>();

            JSONObject requestAnswer = null;
            Log.d("TEST", parametre);
            try {
                requestAnswer = new JSONObject(parametre);
                JSONerror = requestAnswer.getString(traitementContext.getResources().getString(R.string.login_POST_answer_error));
                if (!(Boolean.valueOf(JSONerror))) {
                    JSONArray JSONevents = requestAnswer.getJSONArray(traitementContext.getResources().getString(R.string.accueil_request_answer_events));
                    for (int indice = 0; indice < JSONevents.length(); indice++) {
                        HashMap contactMap = new HashMap();
                        contactMap.put(traitementContext.getResources().getString(R.string.param_eventID), new Integer(JSONevents.getJSONObject(indice).getInt(traitementContext.getResources().getString(R.string.param_eventID))));
                        contactMap.put(traitementContext.getResources().getString(R.string.param_eventName), JSONevents.getJSONObject(indice).getString(traitementContext.getResources().getString(R.string.param_eventName).toString()));
                        contactMap.put(traitementContext.getResources().getString(R.string.param_owner), JSONevents.getJSONObject(indice).getString(traitementContext.getResources().getString(R.string.param_owner).toString()));
                        contactMap.put(traitementContext.getResources().getString(R.string.param_isOrga), JSONevents.getJSONObject(indice).getInt(traitementContext.getResources().getString(R.string.param_isOrga)));
                        contactMap.put(traitementContext.getResources().getString(R.string.param_isComing), JSONevents.getJSONObject(indice).getInt(traitementContext.getResources().getString(R.string.param_isComing)));
                        partyList.add(contactMap);

                        if (((Integer) contactMap.get(traitementContext.getResources().getString(R.string.param_isOrga))) == 1) {
                            Accueil.userParty.eventsOrga.add(contactMap.get(traitementContext.getResources().getString(R.string.param_eventName)).toString());
                            Accueil.userParty.IDOrga.add(Integer.valueOf(contactMap.get(traitementContext.getResources().getString(R.string.param_eventID)).toString()));
                        } else {
                            Accueil.userParty.eventsInvite.add(contactMap.get(traitementContext.getResources().getString(R.string.param_eventName)).toString());
                            Accueil.userParty.IDInvite.add(Integer.valueOf(contactMap.get(traitementContext.getResources().getString(R.string.param_eventID)).toString()));
                        }
                    }
                    JSONArray JSONmyevents = requestAnswer.getJSONArray(traitementContext.getResources().getString(R.string.accueil_request_answer_eventsOwned));
                    for (int indice = 0; indice<JSONmyevents.length(); indice++) {
                        HashMap contactMapSecond = new HashMap();
                        contactMapSecond.put(traitementContext.getResources().getString(R.string.param_eventID), new Integer(JSONmyevents.getJSONObject(indice).getInt(traitementContext.getResources().getString(R.string.param_eventID))));
                        contactMapSecond.put(traitementContext.getResources().getString(R.string.param_eventName), JSONmyevents.getJSONObject(indice).getString(traitementContext.getResources().getString(R.string.param_eventName).toString()));

                        partyList.add(contactMapSecond);
                        Accueil.userParty.eventsOwner.add(contactMapSecond.get(traitementContext.getResources().getString(R.string.param_eventName)).toString());
                        Accueil.userParty.IDOwner.add(Integer.valueOf(contactMapSecond.get(traitementContext.getResources().getString(R.string.param_eventID)).toString()));
                    }
                    Accueil.adaptera = new ArrayAdapter<String>(traitementContext,
                            android.R.layout.simple_list_item_1, android.R.id.text1, Accueil.userParty.eventsOwner);
                    Accueil.adapterb = new ArrayAdapter<String>(traitementContext,
                            android.R.layout.simple_list_item_1, android.R.id.text1, Accueil.userParty.eventsOrga);
                    Accueil.adapterc = new ArrayAdapter<String>(traitementContext,
                            android.R.layout.simple_list_item_1, android.R.id.text1, Accueil.userParty.eventsInvite);


                    Accueil.listViewCreateur.setAdapter(Accueil.adaptera);
                    Accueil.listViewOrga.setAdapter(Accueil.adapterb);
                    Accueil.listViewInvite.setAdapter(Accueil.adapterc);


                    Accueil.adaptera.notifyDataSetChanged();
                    Accueil.adapterb.notifyDataSetChanged();
                    Accueil.adapterc.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class accueildeco extends POSTRequest {

        private String userEmail;
        private String JSONerror, JSONtoken, JSONpseudo;

        @Override
        protected void onPostExecute(String parametre) {
            String JSONerror;

            JSONObject requestAnswer = null;
            Log.d("TEST ACCUEIL", parametre);
            stop.finish();



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
