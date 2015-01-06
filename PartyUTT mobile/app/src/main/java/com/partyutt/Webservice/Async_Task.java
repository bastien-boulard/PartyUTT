package com.partyutt.Webservice;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bastien on 19/12/2014.
 */
public class Async_Task {

    public class POST extends AsyncTask<Object, Void, String> {
        private OnTaskCompleted listener;

        @Override
        protected String doInBackground(Object... params) {
            /*
            params:
            0: nom de l'élément 1
            1: valeur de l'élément 1
            2: nom de l'élément 2
            3: valeur de l'élément 2
            4: url
             */
            String content = null;

            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost((String) params[4]);

                //on ajoute les élements à la requête POST
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair((String) params[0], (String) params[1]));
                nameValuePairs.add(new BasicNameValuePair((String) params[2], (String) params[3]));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(httppost);
                content = EntityUtils.toString(response.getEntity(), "utf8");

            } catch (ClientProtocolException e) {
                Log.e("ClientProtocolException", e.toString(), e);
            } catch (IOException IO) {
                Log.e("IOException", IO.toString(), IO);
            } catch (Error error) {
                Log.e("POST REQUEST ERROR", error.toString(), error);
            }
            listener.onTaskCompleted();
            return content;
        }
    }
}
