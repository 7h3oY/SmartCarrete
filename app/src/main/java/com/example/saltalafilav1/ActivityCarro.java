package com.example.saltalafilav1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ActivityCarro extends AppCompatActivity {

    private List<PedidoUsuario> cartItems = new ArrayList<>();
    private TextView tvCounter;
    private int itemCount;
    private String idPedido;
    private boolean isCartEmpty = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carro);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        idPedido = generateUniqueID();
        itemCount = intent.getIntExtra("itemCount", 0);
        tvCounter = (TextView) findViewById(R.id.tvCounter);
        tvCounter.setText(String.valueOf(itemCount));
        tvCounter.setVisibility(View.VISIBLE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Agregar flecha de retroceso
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Llamar al nuevo onBackPressed()
            }
        });
        // Obtener la lista de productos agregados al carrito desde el intent (puedes cambiar esto según tu implementación)
        cartItems = getIntent().getParcelableArrayListExtra("cartItems");
        cartItems = agruparProductosPorNombre(cartItems);
        // Configurar el RecyclerView para mostrar la lista de productos en el carrito
        RecyclerView recyclerViewCart = findViewById(R.id.recyclerViewCart);
        CartAdapter cartAdapter = new CartAdapter(this, cartItems);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCart.setAdapter(cartAdapter);

        // Configurar el botón "Vaciar Carrito"
        Button btnVaciarCarrito = findViewById(R.id.btnVaciarCarrito);
        btnVaciarCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Vaciar el carrito y notificar al adaptador del cambio en la lista
                cartItems.clear();
                cartAdapter.notifyDataSetChanged();
                // Restablecer el contador y ocultar el badge
                itemCount = 0;
                tvCounter.setVisibility(View.GONE);
                // Actualizar el badge (contador) y la lista del pedido en la actividad ActivityCarro
                updateBadgeAndPedidoList(cartItems);
                // Enviar el resultado de vuelta a la ActivityUsuario
                Intent intent = new Intent();
                intent.putExtra("isCartEmpty", true);
                setResult(RESULT_OK, intent);
            }
        });


        // Configurar el botón "Continuar Compra"
        Button btnContinuarCompra = findViewById(R.id.btnContinuarCompra);
        btnContinuarCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarDatosCarritoAlServidor();
            }
        });
    }
    @Override
    public void onBackPressed() {
        if (itemCount == 0) {
            // El carrito está vacío, enviar isCartEmpty como true a ActivityUsuario
            Intent resultIntent = new Intent();
            resultIntent.putExtra("isCartEmpty", true);
            setResult(RESULT_OK, resultIntent);
            cartItems.clear();
            finish();
        } else {
            // El carrito no está vacío, simplemente regresar a ActivityUsuario
            super.onBackPressed();
        }
    }
    private void updateBadgeTextView() {
        if (isCartEmpty) {
            tvCounter.setText("0"); // Establecer el texto del badge como "0"
            tvCounter.setVisibility(View.GONE); // Ocultar el badge
        } else {
            tvCounter.setText(String.valueOf(itemCount)); // Establecer el texto del badge con el número actual
            tvCounter.setVisibility(View.VISIBLE); // Mostrar el badge
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        isCartEmpty = false;
    }
    // Método para agrupar los productos por nombre
    private List<PedidoUsuario> agruparProductosPorNombre(List<PedidoUsuario> listaOriginal) {
        // HashMap para agrupar los productos por nombre y mantener sus cantidades y precios actualizados
        HashMap<String, PedidoUsuario> productosAgrupados = new HashMap<>();

        for (PedidoUsuario item : listaOriginal) {
            String nombreProducto = item.getNombre();
            int cantidad = item.getCantidad();

            if (productosAgrupados.containsKey(nombreProducto)) {
                // Si el producto ya está en el HashMap, actualiza su cantidad
                PedidoUsuario productoExistente = productosAgrupados.get(nombreProducto);
                int nuevaCantidad = productoExistente.getCantidad() + cantidad;
                productoExistente.setCantidad(nuevaCantidad);
            } else {
                // Si el producto no está en el HashMap, agrégalo tal cual al HashMap
                productosAgrupados.put(nombreProducto, item);
            }
        }

        // Obtener la lista final de productos consolidados del HashMap
        return new ArrayList<>(productosAgrupados.values());
    }


    private void enviarDatosCarritoAlServidor() {
        // URL de tu servidor PHP
        String url = "https://mokups.000webhostapp.com/php/subir_archivo.php";
        // Generar el ID único
        String idPedido = generateUniqueID();
        // Crear el objeto Pedido
        Pedido pedido = new Pedido(idPedido, "usuario"); // Puedes cambiar "usuario" por el valor real del usuario
        // Agregar los PedidoUsuario al Pedido
        for (PedidoUsuario item : cartItems) {
            pedido.agregarProducto(item);
        }
        // Crear la solicitud HTTP usando Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        // Crear una solicitud de tipo StringRequest
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Procesar la respuesta del servidor si es necesario
                        // Aquí puedes mostrar un mensaje de éxito o realizar alguna otra acción
                        Log.d("Response", response);
                        Toast.makeText(ActivityCarro.this, "Pedido enviado correctamente", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ActivityCarro.this, ActivityUsuario.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar el error en caso de que la solicitud falle
                        Log.e("VolleyError", error.toString());
                        Toast.makeText(ActivityCarro.this, "Error al enviar el pedido", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Crear un mapa con los datos del carrito en formato clave-valor
                Map<String, String> params = new HashMap<>();
                params.put("usuario", "usuario");
                params.put("idPedido", pedido.getIdPedido());
                params.put("estadoPedido", "pagado");

                // Agrupar los productos bajo un mismo ID de pedido
                List<PedidoUsuario> productosPedido = pedido.getProductosPedido();
                try {
                    // HashMap para agrupar los productos por nombre y mantener sus cantidades y precios actualizados
                    HashMap<String, PedidoUsuario> productosAgrupados = new HashMap<>();

                    // Verificar que productosPedido no sea nulo antes de entrar en el bucle
                    if (productosPedido != null) {
                        for (int i = 0; i < productosPedido.size(); i++) {
                            PedidoUsuario item = productosPedido.get(i);
                            String nombreProducto = item.getNombre();
                            int cantidad = item.getCantidad();
                            int precio = (int) item.getTotal();

                            if (productosAgrupados.containsKey(nombreProducto)) {
                                // Si el producto ya está en el HashMap, actualiza su cantidad y precio
                                PedidoUsuario productoExistente = productosAgrupados.get(nombreProducto);
                                int nuevaCantidad = productoExistente.getCantidad() + cantidad;
                                int nuevoPrecio = (int) (productoExistente.getTotal() + precio);
                                productoExistente.setCantidad(nuevaCantidad);
                            } else {
                                // Si el producto no está en el HashMap, agrégalo tal cual al HashMap
                                productosAgrupados.put(nombreProducto, new PedidoUsuario(nombreProducto, cantidad, precio));
                            }

                        }
                    }

                    // Obtener la lista final de productos consolidados del HashMap
                    List<PedidoUsuario> productosConsolidados = new ArrayList<>(productosAgrupados.values());

                    // Crear un JSONArray con los productos consolidados
                    JSONArray jsonArrayProductos = new JSONArray();
                    for (PedidoUsuario item : productosConsolidados) {
                        JSONObject jsonObjectProducto = new JSONObject();
                        jsonObjectProducto.put("nombre", item.getNombre());
                        jsonObjectProducto.put("cantidad", item.getCantidad());
                        jsonObjectProducto.put("precio", item.getTotal());
                        jsonArrayProductos.put(jsonObjectProducto);
                    }

                    // Agregar el JSONArray de productos al mapa de parámetros
                    params.put("productos", jsonArrayProductos.toString());
                } catch (JSONException e) {
                    // Manejar la excepción aquí
                    e.printStackTrace();
                }

                return params;
            }
        };
        // Agregar la solicitud a la cola de solicitudes
        requestQueue.add(stringRequest);
    }

    private String generateUniqueID() {
        // Generar un ID único con UUID
        return UUID.randomUUID().toString();
    }

    public void updateBadgeAndPedidoList(List<PedidoUsuario> cartItems) {
        // Actualizar el contador del badge
        itemCount = 0;
        for (PedidoUsuario item : cartItems) {
            itemCount += item.getCantidad();
        }
        if (itemCount == 0) {
            tvCounter.setVisibility(View.GONE);
        } else {
            tvCounter.setText(String.valueOf(itemCount));
            tvCounter.setVisibility(View.VISIBLE);
        }

        // Actualizar la lista del pedido
        // Puedes implementar aquí la lógica para enviar la lista actualizada a la base de datos o donde corresponda
        // ...
    }

}
