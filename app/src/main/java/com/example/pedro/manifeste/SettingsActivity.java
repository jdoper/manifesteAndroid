package com.example.pedro.manifeste;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SettingsActivity extends ActionBarActivity {
    private TextView textView;
    private TextView textView2;
    private Map<String, String> params;
    private RequestQueue rq;
    private String token;

    /*
    * Arquivo de teste, nenhuma implementação importante foi feita aqui
    * */

    public void buttonSend(View view) {
        testRequest2(null);
    }

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

        List<Ocorrencia> ocorrencias = Ocorrencia.listAll(Ocorrencia.class);
        List<String> coordenadas = new ArrayList<String>();

        for (int i = 0; i < ocorrencias.size(); i++) {
            coordenadas.add(
                    "Lat: " + ocorrencias.get(i).getLatitude() +
                    ", Long: " + ocorrencias.get(i).getLongitude());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, coordenadas);


        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);


        //testRequest(null);
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
