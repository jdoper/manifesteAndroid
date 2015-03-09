package com.example.pedro.manifeste;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class SettingsActivity extends ActionBarActivity {
    private TextView textView;
    private TextView textView2;
    private Map<String, String> params;
    private RequestQueue rq;
    private String token;


    public void buttonSend(View view) {
        testRequest2(null);
    }
    /*
    public void loginRequest(View view){
        StringRequest request = new StringRequest(Request.Method.POST,
                "http://10.4.5.4:8080/auth/login",
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
        StringRequest request = new StringRequest(Request.Method.POST,
                "http://10.4.5.4:8080/ocorrencias",
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
                params.put("this_is_manifeste!", "connect.sid=" + cookie + ";");
                return params;
            }
        };
        request.setTag("tag");
        rq.add(request);
    }
    */

    /*
    public void testRespond(View view) {
        JsonArrayRequest request = new JsonArrayRequest(
            "http://10.4.5.4:3000/ocorrencias",
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        String jsonResponse = "";
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject person = response.getJSONObject(i);

                            String titulo = person.getString("titulo");
                            JSONObject coordenada = response.getJSONObject(phone);
                            coordenada.get


                            jsonResponse += "Name: " + name + "\n\n";
                            jsonResponse += "Address: " + adress + "\n\n";
                            jsonResponse += "Age: " + age + "\n\n\n";
                            //jsonResponse += "Name: " + name + "\n\n\n";
                        }
                        textView.setText(jsonResponse);

                    } catch (JSONException e) {
                        textView.setText("Ok");
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
    */

    public void testRequest(View view) {
        StringRequest request = new StringRequest(Request.Method.POST,
                "http://192.168.0.14:3000/api/v1/auth/login",
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        //String[] resposta;
                        //resposta = response.split(":");
                        //token = resposta[0];
                        token = response;
                        textView.setText(token);
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
                params.put("login", "e@e.com");
                params.put("password", "123123123");
                return(params);
            }
        };

        request.setTag("tag");
        rq.add(request);
    }

    public void testRequest2(View view) {
        StringRequest request = new StringRequest(Request.Method.POST,
                "http://192.168.0.14:3000/api/v1/ocorrencia_data.json",
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        textView.setText("FUDEROSO!!!");
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
                params.put("latitude", "66.6");
                params.put("longitude", "66.6");
                params.put("categoria", "Poluição sonora");
                params.put("token", token);
                return(params);
            }
        };

        request.setTag("tag");
        rq.add(request);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        rq = Volley.newRequestQueue(this);
        textView = (TextView) findViewById(R.id.test);
        textView2 = (TextView) findViewById(R.id.test2);
        testRequest(null);
        //testRespond(null);
        //loginRequest(null);
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
