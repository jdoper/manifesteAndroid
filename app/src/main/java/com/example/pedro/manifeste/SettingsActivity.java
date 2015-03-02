package com.example.pedro.manifeste;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class SettingsActivity extends ActionBarActivity {
    private TextView textView;
    private TextView textView2;
    private Map<String, String> params;
    private int count;
    // private String url;
    private RequestQueue rq;
    // private HttpStack httpStack;
    private String cookie;

    /*
    Map<String, String> createBasicAuthHeader(String email, String senha) {
        Map<String, String> headerMap = new HashMap<String, String>();

        String credentials = email + ":" + senha;
        String base64EncodedCredentials =
                Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headerMap.put("Authorization", "Basic " + base64EncodedCredentials);

        return headerMap;
    }
    */


    public void callByStringRequest(View view){
        /*
        params = new HashMap<String, String>();
        params.put("email", "danielbastos@live.com");
        params.put("senha", "123");


        com.example.pedro.manifeste.StringRequest request = new com.example.pedro.manifeste.StringRequest(
                Request.Method.POST,
                "http://10.4.5.4:4000/auth/login",
                params,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        textView.setText(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        textView.setText(error.toString());
                        Log.i("Script", error.toString());
                    }
                }
        );
        */
        StringRequest request = new StringRequest(Request.Method.POST,
                "http://10.4.5.4:4000/auth/login",
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        textView.setText(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        textView.setText(error.toString());
                        Log.i("Script", error.toString());
                    }
                }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                params = new HashMap<String, String>();
                params.put("email", "danielbastos@live.com");
                params.put("senha", "123");
                return(params);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = super.getHeaders();
                if(params == null || params.equals(Collections.emptyMap())) {
                    params = new HashMap<String, String>();
                }
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse resp) {
                Map<String, String> headers = resp.headers;
                if(headers.containsKey("set-cookie")) {
                    String localCookie = headers.get("set-cookie");
                    String[] splitCookie = localCookie.split(";");
                    boolean found = false;
                    for(int i = 0; i < splitCookie.length; i++) {
                        String[] splitSession = splitCookie[i].split("=");
                        // NodeJS (ExpressJS) Cookie
                        if (splitSession[0].equals("connect.sid")) {
                            found = true;
                            localCookie = splitSession[1];
                        }
                    }
                    if(!found) {
                        Log.e("Script", "Missing cookie from login request.");
                    } else {
                        cookie = localCookie;
                    }
                }
                return super.parseNetworkResponse(resp);
            }
        };

        request.setTag("tag");
        rq.add(request);

        newObject(null);
    }

    public void newObject(View view) {
        /*
        params = new HashMap<String, String>();
        params.put("titulo", "Minha terceira ocorrência");
        params.put("descricao", "Ocorrência #2");
        params.put("longitude", "89.0");
        params.put("latitude", "27.5");
        params.put("categoria", "54edc6c5efee74d41605cae5");

        com.example.pedro.manifeste.StringRequest request = new com.example.pedro.manifeste.StringRequest(
                Request.Method.POST,
                "http://10.4.5.4:4000/ocorrencias",
                params,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        textView.setText(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        textView.setText(error.toString());
                        Log.i("Script", error.toString());
                    }
                }
        );
        */

        Toast.makeText(this, cookie, Toast.LENGTH_LONG).show();

            StringRequest request = new StringRequest(Request.Method.POST,
                    "http://10.4.5.4:4000/ocorrencias",
                    new Response.Listener<String>(){
                        @Override
                        public void onResponse(String response) {
                            textView2.setText(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            textView2.setText(error.toString());
                            Log.i("Script", error.toString());
                        }
                    }){
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    params = new HashMap<String, String>();
                    params.put("titulo", "Minha terceira ocorrência");
                    params.put("descricao", "Ocorrência #2");
                    params.put("longitude", "89.0");
                    params.put("latitude", "27.5");
                    params.put("categoria", "54edc6c5efee74d41605cae5");
                    return(params);
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = super.getHeaders();
                    if(params == null || params.equals(Collections.emptyMap())) {
                        params = new HashMap<String, String>();
                    }
                    params.put("Cookie", "connect.sid=" + cookie + ";");
                    return params;
                }
            };
            request.setTag("tag");
        rq.add(request);
    }









    public void testRespond() {
        JsonArrayRequest request = new JsonArrayRequest(
            "http://arcane-spire-3519.herokuapp.com/api/v1/employee_data.json",
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        String jsonResponse = "";
                        for (int i = 0; i < response.length(); i++) {

                            JSONObject person = (JSONObject) response.get(i);

                            String name = person.getString("name");
                            String adress = person.getString("adress");
                            String age = person.getString("age");

                            jsonResponse += "Name: " + name + "\n\n";
                            jsonResponse += "Adress: " + adress + "\n\n";
                            jsonResponse += "Age: " + age + "\n\n\n";

                        }
                        textView.setText(jsonResponse);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        );
        rq.add(request);
    }


    public void mais(View view) {
        if(count < 10) {
            textView.setText(""+ ++count + "");
        }
    }

    public void menos(View view) {
        if(count > 1) {
            textView.setText(""+ --count + "");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        /*
        // Configurações de requisição
        DefaultHttpClient httpClient = new DefaultHttpClient();
        CookieStore cookieStore = new BasicCookieStore();
        httpClient.setCookieStore(cookieStore);
        httpStack = new HttpClientStack(httpClient);

        // Mostra resultado da requisição
        rq = Volley.newRequestQueue(this, httpStack);
        // rq.start();
        */

        rq = Volley.newRequestQueue(this);
        textView = (TextView) findViewById(R.id.test);
        textView2 = (TextView) findViewById(R.id.test2);
        testRespond();
        // callByStringRequest(null);
        // newObject(null);
    }

    @Override
    public void onStop() {
        super.onStop();
        rq.cancelAll("tag");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
