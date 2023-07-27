package com.example.saltalafilav1;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PedidosAdapter extends RecyclerView.Adapter<PedidosAdapter.PedidoViewHolder> {

    private Context context;
    private List<PedidosBarra> pedidosList;

    public PedidosAdapter(Context context, List<PedidosBarra> pedidosList) {
        this.context = context;
        this.pedidosList = pedidosList;
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pedido, parent, false);
        return new PedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        PedidosBarra pedido = pedidosList.get(position);
        holder.tvNumeroPedido.setText("Pedido #" + pedido.getIdPedido());
        holder.tvArticulos.setText(TextUtils.join(", ", pedido.getArticulos()));
        holder.btnPedidoListo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPedidoListoClick(pedido);
            }
        });
        holder.btnVerificarQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onVerificarQrClick(pedido);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pedidosList.size();
    }

    public static class PedidoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumeroPedido;
        TextView tvArticulos;
        Button btnPedidoListo;
        Button btnVerificarQr;

        public PedidoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumeroPedido = itemView.findViewById(R.id.tvNumeroPedido);
            tvArticulos = itemView.findViewById(R.id.tvArticulos);
            btnPedidoListo = itemView.findViewById(R.id.btnPreparado);
            btnVerificarQr = itemView.findViewById(R.id.btnVerificarQr);
        }
    }

    public void onPedidoListoClick(PedidosBarra pedido) {
        String url = "https://mokups.000webhostapp.com/php/actualizar_pedido.php"; // Reemplaza con la URL real de tu servidor
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Manejar la respuesta del servidor si es necesario
                        // Por ejemplo, puedes mostrar un mensaje de éxito
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar el error de la solicitud aquí
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // Enviar los parámetros necesarios para la actualización
                params.put("idPedido", String.valueOf(pedido.getIdPedido()));
                params.put("estadoPedido", "Listo");
                return params;
            }
        };

        // Agregar la solicitud a la cola de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }



    public void onVerificarQrClick(PedidosBarra idPedido) {
        IntentIntegrator integrator = new IntentIntegrator((Activity) context);
        integrator.setPrompt("Escanea el código QR");
        integrator.setBeepEnabled(true);
        integrator.setOrientationLocked(false);
        integrator.addExtra("idPedido", idPedido); // Pasar el ID del pedido como dato adicional
        integrator.initiateScan();
    }

}
