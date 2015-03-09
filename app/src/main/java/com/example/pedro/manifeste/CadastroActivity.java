package com.example.pedro.manifeste;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CadastroActivity extends ActionBarActivity {
    private float latitude, longitude;
    private String tipo, token;
    private Map<String, String> params;
    private RequestQueue rq;
    private boolean result;

    public void createOcorrencia(View view) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Criando...");
        pDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST,
                "https://manifesteapp.herokuapp.com/api/v1/ocorrencia_data.json",
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        mensagemErro();
                    }
                }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                params = new HashMap<String, String>();
                params.put("latitude", "" + latitude);
                params.put("longitude", "" + longitude);
                params.put("categoria", tipo);
                params.put("token", token);
                return(params);
            }
        };

        request.setTag("tag");
        rq.add(request);
    }

    public void mensagemErro() {
        // Printa mensagens de erro das requisições
        Toast.makeText(this, "Habilite a internet do seu aparelho", Toast.LENGTH_SHORT).show();
    }

    public void cadastrarProblema(View view) {
        // Criando ocorrencia e finalizando activity
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Dados inseridos e identificaçao do usuario
        tipo = spinner.getSelectedItem().toString();
        List<Usuario> user = Usuario.listAll(Usuario.class);
        token = user.get(0).getToken();

        Ocorrencia ocorrencia = new Ocorrencia(latitude, longitude, tipo);
        ocorrencia.save();

        // Enviando ocorrencia e finalizando activity
        createOcorrencia(null);
        finish();
    }

    public void tirarFoto(View view) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, 1);
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null) {
            Bundle bundle = data.getExtras();
            if(bundle != null) {
                Bitmap image = (Bitmap) bundle.get("data");
                ImageView imageView = (ImageView) findViewById(R.id.image);
                imageView.setImageBitmap(image);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        latitude = bundle.getFloat("latitude");
        longitude = bundle.getFloat("longitude");

        // Alterando cor da ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#69A84F")));

        // Configuração do Spinner
        Spinner estados = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.problemas, android.R.layout.simple_spinner_dropdown_item);
        estados.setAdapter(adapter1);

        // Objeto para requisição
        rq = Volley.newRequestQueue(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cadastro, menu);
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
            startActivity(new Intent(this, SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
