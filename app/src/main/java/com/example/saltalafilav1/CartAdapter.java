package com.example.saltalafilav1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<PedidoUsuario> cartItems;

    public CartAdapter(Context context, List<PedidoUsuario> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_carro, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        PedidoUsuario item = cartItems.get(position);
        holder.txtProductName.setText(item.getNombre());
        holder.txtProductPrice.setText(String.format("%.2f", item.getPrecio()));
        holder.tvQuantity.setText(String.valueOf(item.getCantidad()));
        double total = item.getPrecio() * item.getCantidad();
        holder.txtTotalPrice.setText(String.format("%.2f", total));

        // Configurar el botón de disminución
        holder.btnDecreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentQuantity = item.getCantidad();
                if (currentQuantity > 1) {
                    // Disminuir la cantidad y actualizar la vista
                    item.setCantidad(currentQuantity - 1);
                    notifyDataSetChanged();
                }
            }
        });

        // Configurar el botón de aumento
        holder.btnIncreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Aumentar la cantidad y actualizar la vista
                item.setCantidad(item.getCantidad() + 1);
                notifyDataSetChanged();
            }
        });
    }


    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName;
        TextView txtProductPrice;
        TextView tvQuantity;
        TextView txtTotalPrice;
        ImageButton btnDecreaseQuantity;
        ImageButton btnIncreaseQuantity;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            txtTotalPrice = itemView.findViewById(R.id.txtTotalPrice);
            btnDecreaseQuantity = itemView.findViewById(R.id.btnDecreaseQuantity);
            btnIncreaseQuantity = itemView.findViewById(R.id.btnIncreaseQuantity);
        }
    }
}
