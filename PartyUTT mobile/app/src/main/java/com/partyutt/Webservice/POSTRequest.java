package com.partyutt.Webservice;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.partyutt.R;

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
 * Created by Bastien on 19/12/2014.
 */
public abstract class POSTRequest extends AsyncTask<Object, Void, String> {

    Context activityContext;
    String resultatRequete;

    @Override
    protected String doInBackground(Object... params) {

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
        for (int indice=2;indice<=limiteBoucle;indice=indice+2) {
            //on passe donc de l'indice 4 au 6 puis 8 et ainsi de suite jusqu'au dernier indice
            //par exemple pour 3 paramètres la boucle commencera à 4 et terminera à 8
            nameValuePairs.add(new BasicNameValuePair((String) params[indice+1],(String) params[indice]));
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
}
