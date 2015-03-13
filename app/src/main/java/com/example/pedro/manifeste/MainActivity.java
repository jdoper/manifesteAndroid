package com.example.pedro.manifeste;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity {
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private LocationManager locationManager;
    private float latitude, longitude;
    //private Timer timerAtual = new Timer();
    //private final Handler handler = new Handler();
    private int zoomCam;
    private Usuario userAtual;
    private RequestQueue rq;

    /*
    * IOT (ativação do mecanismo)
    * */
    /*
    private void ativaNotificacao(){
        // Notifica o usuário
        TimerTask task = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        gerarNotificacao();
                    }
                });
            }
        };

        timerAtual.schedule(task, 5000, 900000);
    }
    */

    /*
    * IOT (funções auxiliares)
    * */
    public double convertCoord(double pos) {
        return (pos * Math.PI)/180;
    }

    /*
    * IOT (funções auxiliares)
    * */
    public long calcDistance(double latA, double lonA, double latB, double lonB) {
        // Calcula a distancia entre duas coordenadas
        double raio = 6372.7954;
        double lnA = convertCoord(lonA);
        double ltA = convertCoord(latA);
        double lnB = convertCoord(lonB);
        double ltB = convertCoord(latB);

        double result = 1000 * raio * Math.acos(
                Math.sin(ltA) * Math.sin(ltB) +
                        Math.cos(ltA) * Math.cos(ltB) *
                                Math.cos(lnA - lnB));

        return Math.round(result);
    }

    /*
    * IOT (captura de movimentação)
    * */
    public void verificaLocalizacao() {
        /*
        * De acordo com a localização e deslocamento do usuário,
        * se dispara notificação de alerta
        * de ocorrências próximas
        * */
        if (latitude == -5.799999 && longitude == -35.239999) {
            userAtual.setLatitude(latitude);
            userAtual.setLongitude(longitude);
            // Caso o GPS esteja habilitado
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                getLocation();
                long distancia = calcDistance(
                        userAtual.getLatitude(),
                        userAtual.getLongitude(),
                        latitude,
                        longitude
                );
                // Caso o usuário tena se deslocado mais de 2km
                if (distancia > 2000) {
                    userAtual.setLatitude(latitude);
                    userAtual.setLongitude(longitude);
                    getOcorrencias();
                    gerarNotificacao();
                }
            }
        }
    }

    /*
    * IOT (requisição)
    * Aplicação (recuperando ocorrências)
    * */
    public void getOcorrencias() {
        JsonArrayRequest request = new JsonArrayRequest(
            "http://192.168.0.14:3000/api/v1/listar.json",
            new Response.Listener<JSONArray>(){
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        /* Deleta todas ocorrências do banco, para que
                        *  a visão do usuário não fique poluida
                        * */
                        Ocorrencia.deleteAll(Ocorrencia.class);
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject person = (JSONObject) response.get(i);
                                Ocorrencia ocorrencia = new Ocorrencia(
                                        person.getDouble("latitude"),
                                        person.getDouble("longitude"),
                                        person.getString("categoria")
                                );
                                ocorrencia.save();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // mensagemSucesso(e.getMessage());
                    }
                    // mensagemSucesso(tamanho);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });

        // mensagemSucesso(paramLatitude);
        request.setTag("tag");
        rq.add(request);
    }

    /*
    * IOT (notificando usuário)
    * */
    public void gerarNotificacao() {
        NotificationManager notification = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, OcorrenciaActivity.class), 0);
        int qtd = (Ocorrencia.listAll(Ocorrencia.class)).size();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setTicker("Foram encontradas novas ocorrências");
        builder.setContentTitle("Manifeste");
        builder.setContentText( qtd + " ocorrências prox. foram encontradas");
        builder.setSmallIcon(R.drawable.add);
        builder.setContentIntent(pendingIntent);

        Notification notify = builder.build();
        notify.vibrate = new long[]{150, 300, 150, 300};
        notify.flags = Notification.FLAG_AUTO_CANCEL;
        // R.drawable.add = id para notificação
        notification.notify(R.drawable.add, notify);

        try {
            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(this, som);
            toque.play();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * Mapa (inserção dos pontos)
    * */
    public void addMarker(View view) {
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Envia dados para criar ocorrência
            Intent intent = new Intent(this, CadastroActivity.class);

            Bundle bundle = new Bundle();
            bundle.putFloat("latitude", latitude);
            bundle.putFloat("longitude", longitude);
            intent.putExtras(bundle);

            startActivity(intent);
        }
        else {
            Toast.makeText(this, "Habilite a localização do seu celular", Toast.LENGTH_LONG).show();
        }
    }

    /*
    * Mapa (inserção dos pontos)
    * */
    protected void setMarkers() {
        // Insere pontos no mapa
        List<Ocorrencia> ocorrencias = Ocorrencia.listAll(Ocorrencia.class);

        if(ocorrencias.size() > 0) {
            for(int i = 0; i < ocorrencias.size(); i++) {
                MarkerOptions options = new MarkerOptions();
                options.position(new LatLng(
                                ocorrencias.get(i).getLatitude(),
                                ocorrencias.get(i).getLongitude())
                ).title(ocorrencias.get(i).getTipo());

                map.addMarker(options);
            }
        }
    }

    /*
    * Mapa (configuração)
    * */
    public void configMap() {
        // Configurações do mapa mostrado
        map = mapFragment.getMap();
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Caso não consiga a localização do GPS, mostra-se uma localização default
        if (latitude == 0 || longitude == 0) {
            latitude = (float) -5.799999;
            longitude = (float) -35.239999;
            zoomCam = 12;
        }

        // LatLng latLng = new LatLng(-5.809999, -35.225421); Zoom: 12
        LatLng latLng = new LatLng(latitude, longitude);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(zoomCam).build();
        CameraUpdate update = CameraUpdateFactory.newCameraPosition(cameraPosition);
        // Seta configurações da visualização do mapa
        map.moveCamera(update);
    }

    /*
    * Aplicação (localização)
    * */
    public LocationListener getLocation() {
        // Seta em latitude e longitude a localização atual
        return new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = (float) location.getLatitude();
                longitude = (float) location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("Latitude", "status");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d("Latitude","enable");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d("Latitude","disable");
            }
        };
    }

    /*
    * Aplicação (chamada de activity)
    * */
    public void listEvents(View view) {
        // Mostra lista lista de ocorrências
        startActivity(new Intent(this, OcorrenciaActivity.class));
    }

    /*
    * Aplicação (verificação de conectividade)
    * */
    /*
    private boolean isOnline() {
        // Verifica se o usuário está online
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    */

    /*
    * Funções padrão (@Override)
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Alterando cor da ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#69A84F")));

        // Objeto para requisições com Volley
        rq = Volley.newRequestQueue(this);

        // Pegando localização do usuário
        latitude = 0; longitude = 0; zoomCam = 15;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = getLocation();
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        // Insere a fragment do mapa na página inicial
        GoogleMapOptions options = new GoogleMapOptions();
        options.zOrderOnTop(true);

        mapFragment = SupportMapFragment.newInstance(options);

        FragmentTransaction fragment = getSupportFragmentManager().beginTransaction();
        fragment.replace(R.id.mapContainer, mapFragment);
        fragment.commit();

        // Facilitando a inserção de dados para login
        List<Usuario> user = Usuario.listAll(Usuario.class);
        if (user.size() > 0) {
            userAtual = user.get(0);
        }

        // Recuperando ocorrências do usuário
        getOcorrencias();
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Thread(){
            public void run() {
                while(mapFragment.getMap() == null) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        getLocation();
                        configMap();
                        setMarkers();
                        verificaLocalizacao();
                    }
                });
            }
        }.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        /*
        * Caso queira algo com R.id.action_settings
        * procure em outra activity
        * */
        if (id == R.id.action_logout) {
            // Retira usuário do banco e finaliza activity
            Usuario.deleteAll(Usuario.class);
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
