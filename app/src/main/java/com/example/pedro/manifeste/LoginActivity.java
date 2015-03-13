package com.example.pedro.manifeste;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LoginActivity extends ActionBarActivity {
    private EditText login;
    private EditText senha;
    private String token;
    private Map<String, String> params;
    private RequestQueue rq;

    public void entrar(View view) {
        // Caso a requisição seja bem sucedida, entra-se na aplicação
        login(null);
    }

    public void mensagemErro() {
        // Printa mensagens de erro das requisições
        Toast.makeText(this, "Login ou senha incorretos", Toast.LENGTH_SHORT).show();
    }

    public void login(View view) {
        //final ProgressDialog pDialog = new ProgressDialog(this);
        //pDialog.setMessage("Carregando...");
        //pDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST,
                "http://192.168.0.14:3000/api/v1/auth/login",
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        token = response;
                        Usuario user = new Usuario(
                            login.getText().toString(),
                            senha.getText().toString(),
                            token, 0, 0
                        );
                        user.save();

                        // Finaliza a activity
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        //pDialog.hide();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //pDialog.hide();
                        mensagemErro();
                    }
                }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                params = new HashMap<String, String>();
                params.put("login", login.getText().toString());
                params.put("password", senha.getText().toString());
                return(params);
            }
        };

        request.setTag("tag");
        rq.add(request);
    }

    private void verifyLogin() {
        // Caso o usuário já tenha se cadastrado, entra-se diretamente
        List<Usuario> user = Usuario.listAll(Usuario.class);
        //final ProgressDialog pDialog = new ProgressDialog(this);
        //pDialog.setMessage("Carregando...");
        // pDialog.show();

        if (user.size() > 0) {
            final Usuario userAtual = user.get(0);
            StringRequest request = new StringRequest(Request.Method.POST,
                    "http://192.168.0.14:3000/api/v1/auth/login",
                    new Response.Listener<String>(){
                        @Override
                        public void onResponse(String response) {
                            token = response;
                            userAtual.setToken(token);
                            userAtual.save();

                            // Finaliza a activity
                            //pDialog.hide();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //pDialog.hide();
                            mensagemErro();
                        }
                    }){
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    params = new HashMap<String, String>();
                    params.put("login", userAtual.getLogin());
                    params.put("password", userAtual.getSenha());
                    return(params);
                }
            };

            request.setTag("tag");
            rq.add(request);
        }
        else {
            //pDialog.hide();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        // Objeto para requisição
        rq = Volley.newRequestQueue(this);

        // Referencia para os campos do formulário
        login = (EditText) findViewById(R.id.login);
        senha = (EditText) findViewById(R.id.senha);

        // Facilitando a inserção de dados para login
        List<Usuario> user = Usuario.listAll(Usuario.class);
        if (user.size() > 0) {
            Usuario userAtual = user.get(0);
            login.setText(userAtual.getLogin());
            senha.setText(userAtual.getSenha());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Caso exista um usuário cadastrado, entra-se no sistema
        // Chamar login passando parametro nulo
        //verifyLogin();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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