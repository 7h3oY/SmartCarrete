package com.example.saltalafilav1;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ActivityBarra extends AppCompatActivity {

    private List<PedidosBarra> pedidosList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PedidosAdapter adapter;
    private ScheduledExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barra);

        recyclerView = findViewById(R.id.recyclerViewBarra);
        adapter = new PedidosAdapter(this, pedidosList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Iniciar la actualización automática cada minuto
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                // Obtener los nuevos pedidos del servidor
                obtenerPedidosDesdeServidor(new PedidosCallback() {
                    @Override
                    public void onPedidosObtenidos(List<PedidosBarra> nuevosPedidos) {
                        // Actualizar la lista en el hilo de la interfaz de usuario
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pedidosList.clear();
                                pedidosList.addAll(nuevosPedidos);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
            }
        }, 0, 1, TimeUnit.MINUTES);
    }
    private void marcarPedidoComoListo(PedidosBarra pedido) {
        // Aquí realizas la lógica para marcar el pedido como "Listo" en la base de datos
        // y luego, envías la notificación al usuario con el idPedido
        // Después de marcar el pedido como "Listo" y obtener el idPedido, envía la notificación al usuario
        String titulo = "Pedido Listo";
        String mensaje = "Su pedido está listo para ser retirado. Muestra este QR en la barra.";
        enviarNotificacionAlUsuario(titulo, mensaje);
    }
    private void enviarNotificacionAlUsuario(String titulo, String mensaje, String idPedido) {
        // Crear un objeto de tipo JSONObject para construir los datos del mensaje
        JSONObject jsonNotificacion = new JSONObject();
        try {
            jsonNotificacion.put("titulo", titulo);
            jsonNotificacion.put("mensaje", mensaje);
            jsonNotificacion.put("idPedido", idPedido);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Crear el cuerpo del mensaje utilizando el objeto JSON
        JSONObject jsonMensaje = new JSONObject();
        try {
            jsonMensaje.put("to", "TOKEN_DEL_DISPOSITIVO_DEL_USUARIO"); // Reemplaza "TOKEN_DEL_DISPOSITIVO_DEL_USUARIO" con el token del dispositivo del usuario
            jsonMensaje.put("data", jsonNotificacion);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Crear una solicitud POST para enviar el mensaje a FCM
        String url = "https://fcm.googleapis.com/fcm/send"; // URL de FCM para enviar mensajes
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonMensaje,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejar la respuesta si es necesario
                        Log.d(TAG, "Notificación enviada al usuario");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar el error de la solicitud aquí
                        Log.e(TAG, "Error al enviar la notificación: " + error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // Agregar el encabezado con la clave del servidor de FCM
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "key=TU_CLAVE_DEL_SERVIDOR_FCM"); // Reemplaza "TU_CLAVE_DEL_SERVIDOR_FCM" con la clave de tu servidor de FCM
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Agregar la solicitud a la cola de Volley para que se envíe
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Detener la actualización automática al cerrar la actividad
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    // Método para obtener los pedidos desde el servidor (con Volley)
    private void obtenerPedidosDesdeServidor(PedidosCallback callback) {
        String url = "https://mokups.000webhostapp.com/php/recuperar_datos.php"; // Reemplaza con la URL real de tu servidor

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Parsear el JSON y obtener los datos de los pedidos
                        List<PedidosBarra> nuevosPedidos = parsearJSONPedidos(response);

                        // Notificar al callback con los datos obtenidos
                        callback.onPedidosObtenidos(nuevosPedidos);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar el caso de error de la solicitud aquí
                    }
                });

        // Agregar la solicitud a la cola de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
    // Método para parsear el JSON recibido del servidor y obtener una lista de PedidosBarra
    // Método para parsear el JSON recibido del servidor y obtener una lista de PedidosBarra
    private List<PedidosBarra> parsearJSONPedidos(JSONArray jsonArrayPedidos) {
        List<PedidosBarra> nuevosPedidos = new ArrayList<>();

        try {
            // Obtener datos de la tabla "pedidos" y construir una lista de objetos PedidosBarra
            for (int i = 0; i < jsonArrayPedidos.length(); i++) {
                JSONObject jsonPedido = jsonArrayPedidos.getJSONObject(i);
                String idPedido = jsonPedido.getString("idPedido");
                String usuario = jsonPedido.getString("usuario");

                // Buscar los artículos asociados a este pedido en la tabla "articulos"
                List<String> articulos = new ArrayList<>();
                JSONArray jsonArrayArticulos = jsonPedido.getJSONArray("articulos");
                for (int j = 0; j < jsonArrayArticulos.length(); j++) {
                    JSONObject jsonArticulo = jsonArrayArticulos.getJSONObject(j);
                    String nombreArticulo = jsonArticulo.getString("nombre");
                    int cantidadArticulo = jsonArticulo.getInt("cantidad");
                    articulos.add(nombreArticulo + " x " + cantidadArticulo);
                }

                // Crear el objeto PedidosBarra y agregarlo a la lista
                PedidosBarra pedidoBarra = new PedidosBarra(idPedido, usuario, articulos);
                nuevosPedidos.add(pedidoBarra);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return nuevosPedidos;
    }



    public interface PedidosCallback {
        void onPedidosObtenidos(List<PedidosBarra> pedidos);
    }
}


