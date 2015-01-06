package com.partyutt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.partyutt.Webservice.Async_Task;
import com.partyutt.Webservice.OnTaskCompleted;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Login extends Activity implements OnTaskCompleted{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btn_Connexion = (Button)findViewById(R.id.btn_Connexion);
        btn_Connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editIdentifiant = (EditText)findViewById(R.id.editIdentifiant);
                final EditText editPassword = (EditText)findViewById(R.id.editPassword);

                asyncPOSTTask POSTask = new asyncPOSTTask();
                POSTask.execute(editIdentifiant.getText().toString(),editPassword.getText().toString(),getApplicationContext());
                //Async_Task asyncTask = new Async_Task();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskCompleted() {

    }

    private class asyncPOSTTask extends AsyncTask<Object, Void, Context> {

        private String userEmail;
        private String JSONerror, JSONtoken, JSONpseudo;

        @Override
        protected Context doInBackground(Object... params) {
            String content = null;

            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://192.168.43.147/partyUTT/login.php");

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair(getResources().getString(R.string.login_POST_param_email), (String) params[0]));
                nameValuePairs.add(new BasicNameValuePair(getResources().getString(R.string.login_POST_param_password), (String) params[1]));
                Log.d("BLAH",nameValuePairs.toString());
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(httppost);
                content = EntityUtils.toString(response.getEntity(), "utf8");

                JSONObject requestAnswer = new JSONObject(content);
                JSONerror = requestAnswer.getString(getResources().getString(R.string.login_POST_answer_error));
                JSONtoken = requestAnswer.getString(getResources().getString(R.string.login_POST_answer_token));
                JSONpseudo = requestAnswer.getString(getResources().getString(R.string.login_POST_answer_pseudo));

                Log.d("Content",content.toString());

                userEmail = (String) params[0];
            } catch (ClientProtocolException e) {
                Log.e("ClientProtocolException", e.toString(), e);
            } catch (IOException IO) {
                Log.e("IOException", IO.toString(), IO);
            } catch (Error error) {
                Log.e("POST REQUEST ERROR", error.toString(), error);
            } catch (JSONException e) {
                //erreur dans la création du jsonobject
                e.printStackTrace();
            }
            return (Context) params[2];
        }

        @Override
        protected void onPostExecute(Context result) {
            if (Boolean.valueOf(JSONerror)) {
                Toast.makeText(result,"Mauvais identifiants",Toast.LENGTH_LONG).show();
            } else {
                Intent accueilIntent = new Intent(result,Accueil.class);
                accueilIntent.putExtra(getResources().getString(R.string.login_POST_param_email),userEmail);
                accueilIntent.putExtra(getResources().getString(R.string.login_Token),JSONtoken);
                accueilIntent.putExtra(getResources().getString(R.string.login_POST_answer_pseudo),JSONpseudo);
                startActivity(accueilIntent);
            }
        }
    }
}
