package com.example.pedro.manifeste;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;


public class OcorrenciaActivity extends ActionBarActivity {
    private double latitude;
    private double longitude;

    public void mostrarDados(List<Ocorrencia> ocorrencias) {
        int infra = 0, saneamento = 0, visual = 0, sonora = 0, alagamento = 0, lixo = 0;

        // Verifica os tipos dos dados
        for(int i = 0; i < ocorrencias.size(); i++) {
            if(ocorrencias.get(i).getTipo().equals("Infra-estrutura")) {
                infra++;
            }
            else if(ocorrencias.get(i).getTipo().equals("Saneamento")) {
                saneamento++;
            }
            else if(ocorrencias.get(i).getTipo().equals("Poluição visual")) {
                visual++;
            }
            else if(ocorrencias.get(i).getTipo().equals("Poluição sonora")) {
                sonora++;
            }
            else if(ocorrencias.get(i).getTipo().equals("Alagamento")) {
                alagamento++;
            }
            else if(ocorrencias.get(i).getTipo().equals("Acumulo de lixo")) {
                lixo++;
            }
        }

        // Seta valores na view
        TextView textView1 = (TextView) findViewById(R.id.textView2);
        textView1.setText(""+ infra +"");
        TextView textView2 = (TextView) findViewById(R.id.textView4);
        textView2.setText(""+ saneamento +"");
        TextView textView3 = (TextView) findViewById(R.id.textView6);
        textView3.setText(""+ visual +"");
        TextView textView4 = (TextView) findViewById(R.id.textView8);
        textView4.setText(""+ sonora +"");
        TextView textView5 = (TextView) findViewById(R.id.textView10);
        textView5.setText(""+ alagamento +"");
        TextView textView6 = (TextView) findViewById(R.id.textView12);
        textView6.setText(""+ lixo +"");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocorrencia);

        // Recupera informações do banco
        List<Ocorrencia> ocorrencias = Ocorrencia.listAll(Ocorrencia.class);
        mostrarDados(ocorrencias);

        // Altera a cor da barra de ação
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#EEA422")));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ocorrencia, menu);
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
