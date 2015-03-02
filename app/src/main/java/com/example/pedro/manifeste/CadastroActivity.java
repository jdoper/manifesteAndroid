package com.example.pedro.manifeste;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.ByteArrayOutputStream;


public class CadastroActivity extends ActionBarActivity {
    private double latitude, longitude;
    private byte[] foto;

    public void cadastrarProblema(View view) {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        Ocorrencia ocorrencia = new Ocorrencia(latitude, longitude, spinner.getSelectedItem().toString());
        ocorrencia.save();

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
                foto = getBitmapAsByteArray(image);
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
        latitude = bundle.getDouble("latitude");
        longitude = bundle.getDouble("longitude");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#69A84F")));

        Spinner estados = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.problemas, android.R.layout.simple_spinner_dropdown_item);
        estados.setAdapter(adapter1);
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
