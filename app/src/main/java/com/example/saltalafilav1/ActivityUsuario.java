package com.example.saltalafilav1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.badge.ExperimentalBadgeUtils;

import java.util.ArrayList;
import java.util.List;

public class ActivityUsuario extends AppCompatActivity {
    private List<PedidoUsuario> cartItems = new ArrayList<>();
    private int itemCount = 0;
    private int firstCount = 0;
    private int secondCount = 0;
    private int thirdCount = 0;
    private boolean isVisibleCart = false;
    private boolean isVisible1 = false;
    private boolean isVisible2 = false;
    private boolean isVisible3 = false;
    private boolean isVisible4 = false;
    private int fourCount = 0;
    private TextView tvCounter;
    private TextView contador1;
    private TextView contador2;
    private TextView contador3;
    private TextView contador4;
    private BadgeDrawable badgeDrawable;

    @ExperimentalBadgeUtils
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario); // Reemplaza "activity_usuario" con el nombre de tu layout XML
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        itemCount = intent.getIntExtra("itemCount", 0);
        tvCounter = (TextView) findViewById(R.id.tvCounter);
        contador1 = (TextView) findViewById(R.id.contador1);
        contador2 = (TextView) findViewById(R.id.contador2);
        contador3 = (TextView) findViewById(R.id.contador3);
        contador4 = (TextView) findViewById(R.id.contador4);
        // Obtener el icono del carrito
        ImageView iconCart = findViewById(R.id.iconCart);
        LinearLayout cuadro1 = findViewById(R.id.cuadro1);
        LinearLayout cuadro2 = findViewById(R.id.cuadro2);
        LinearLayout cuadro3 = findViewById(R.id.cuadro3);
        LinearLayout cuadro4 = findViewById(R.id.cuadro4);
        iconCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cartItems.isEmpty()) {
                    // Si el carrito está vacío, muestra un mensaje
                    Toast.makeText(ActivityUsuario.this, "El carrito de compras está vacío", Toast.LENGTH_SHORT).show();
                } else {
                    // Si hay productos en el carrito, abre la actividad del carrito
                    Intent intent = new Intent(ActivityUsuario.this, ActivityCarro.class);
                    intent.putParcelableArrayListExtra("cartItems", (ArrayList<? extends Parcelable>) cartItems);
                    intent.putExtra("itemCount", itemCount); // Agregar itemCount como extra en el Intent
                    startActivity(intent);
                }
            }
        });

        cuadro1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lógica para agregar cuadro1 al carrito de compras
                addToCart("Cuadro 1");
                firstCount ++;
                if(isVisible1 == true){
                    contador1.setText(String.valueOf(firstCount));
                }else{
                    contador1.setText(String.valueOf(firstCount));
                    contador1.setVisibility(View.VISIBLE);
                    isVisible1 = true;
                }
            }
        });
        cuadro2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lógica para agregar cuadro2 al carrito de compras
                addToCart("Cuadro 2");
                secondCount ++;
                if(isVisible2 == true){
                    contador2.setText(String.valueOf(secondCount));
                }else{
                    contador2.setText(String.valueOf(secondCount));
                    contador2.setVisibility(View.VISIBLE);
                    isVisible2 = true;
                }
            }
        });
        cuadro3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lógica para agregar cuadro2 al carrito de compras
                addToCart("Cuadro 3");
                thirdCount ++;
                if(isVisible3 == true){
                    contador3.setText(String.valueOf(thirdCount));
                }else{
                    contador3.setText(String.valueOf(thirdCount));
                    contador3.setVisibility(View.VISIBLE);
                    isVisible3 = true;
                }
            }
        });
        cuadro4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lógica para agregar cuadro2 al carrito de compras
                addToCart("Cuadro 4");
                fourCount ++;
                if(isVisible4 == true){
                    contador4.setText(String.valueOf(fourCount));
                }else{
                    contador4.setText(String.valueOf(fourCount));
                    contador4.setVisibility(View.VISIBLE);
                    isVisible4 = true;
                }
            }
        });
    }

    // Método para manejar el clic en los cuadros
    public void addToCart(String cuadroSeleccionado) {
        PedidoUsuario productoExistente = null;
        for (PedidoUsuario item : cartItems) {
            if (item.getNombre().equals(cuadroSeleccionado)) {
                productoExistente = item;
                break;
            }
        }
        if (productoExistente != null) {
            // Si el producto ya está en el carrito, aumentar su cantidad
            int currentQuantity = productoExistente.getCantidad();
            productoExistente.setCantidad(currentQuantity + 1);
        } else {
            // Si el producto no está en el carrito, agregarlo con cantidad inicial de 1
            itemCount ++;
            if(isVisibleCart == true){
                tvCounter.setText(String.valueOf(itemCount));
            }else{
                tvCounter.setText(String.valueOf(itemCount));
                tvCounter.setVisibility(View.VISIBLE);
                isVisibleCart = true;
            }
            switch (cuadroSeleccionado) {
                case "Cuadro 1":
                    // Agregar producto 1 al carrito de compras
                    TextView precioCuadro1 = findViewById(R.id.precio1);
                    int precioCuadro1Int = Integer.parseInt(precioCuadro1.getText().toString());
                    String nombreProducto1 = "Bebidas";
                    cartItems.add(new PedidoUsuario(nombreProducto1, 1, precioCuadro1Int, "usuario")); // La cantidad inicial es 1
                    break;
                case "Cuadro 2":
                    // Agregar producto 2 al carrito de compras
                    TextView precioCuadro2 = findViewById(R.id.precio2);
                    int precioCuadro2Int = Integer.parseInt(precioCuadro2.getText().toString());
                    String nombreProducto2 = "Aguas/Jugos";
                    cartItems.add(new PedidoUsuario(nombreProducto2, 1, precioCuadro2Int, "usuario")); // La cantidad inicial es 1
                    break;
                case "Cuadro 3":
                    // Agregar producto 3 al carrito de compras
                    TextView precioCuadro3 = findViewById(R.id.precio3);
                    int precioCuadro3Int = Integer.parseInt(precioCuadro3.getText().toString());
                    String nombreProducto3 = "Cervezas";
                    cartItems.add(new PedidoUsuario(nombreProducto3, 1, precioCuadro3Int, "usuario")); // La cantidad inicial es 1
                    break;
                case "Cuadro 4":
                    // Agregar producto 4 al carrito de compras
                    TextView precioCuadro4 = findViewById(R.id.precio4);
                    int precioCuadro4Int = Integer.parseInt(precioCuadro4.getText().toString());
                    String nombreProducto4 = "Destilados";
                    cartItems.add(new PedidoUsuario(nombreProducto4, 1, precioCuadro4Int, "usuario")); // La cantidad inicial es 1
                    break;
                default:
                    break;
            }
        }
        // Puedes mostrar un mensaje o notificación para indicar que se agregó al carrito
        Toast.makeText(this, "Producto agregado al carrito", Toast.LENGTH_SHORT).show();
    }
}
