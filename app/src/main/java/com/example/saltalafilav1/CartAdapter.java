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
    private OnItemDeleteListener onItemDeleteListener; // Listener para comunicarse con la actividad

    public CartAdapter(Context context, List<PedidoUsuario> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    public void setOnItemDeleteListener(OnItemDeleteListener listener) {
        this.onItemDeleteListener = listener;
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
        holder.txtProductPrice.setText(String.valueOf(item.getPrecio())); // Sin formato
        holder.tvQuantity.setText(String.valueOf(item.getCantidad()));
        int total = item.getPrecio() * item.getCantidad(); // Total como entero
        holder.txtTotalPrice.setText(String.valueOf(total));

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

        // Configurar el botón de eliminación
        holder.btnDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Eliminar el ítem correspondiente del carrito
                cartItems.remove(position);
                // Notificar al adaptador del cambio en la lista
                notifyDataSetChanged();
                // Actualizar el badge (contador) y la lista del pedido en la actividad ActivityCarro
                ((ActivityCarro) context).updateBadgeAndPedidoList(cartItems);
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
        ImageButton btnDeleteItem;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            txtTotalPrice = itemView.findViewById(R.id.txtTotalPrice);
            btnDecreaseQuantity = itemView.findViewById(R.id.btnDecreaseQuantity);
            btnIncreaseQuantity = itemView.findViewById(R.id.btnIncreaseQuantity);
            btnDeleteItem = itemView.findViewById(R.id.btnDeleteItem); // Obtener el botón de eliminación
        }
    }

    public interface OnItemDeleteListener {
        void onItemDelete(int position); // Método para notificar la eliminación de un item
    }
}

