package com.example.saltalafilav1;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private String idPedido;
    private String usuario;
    private List<PedidoUsuario> productosPedido;

    public Pedido(String idPedido, String usuario) {
        this.idPedido = idPedido;
        this.usuario = usuario;
        productosPedido = new ArrayList<>();
    }

    public String getIdPedido() {
        return idPedido;
    }

    public String getUsuario() {
        return usuario;
    }

    public List<PedidoUsuario> getProductosPedido() {
        return productosPedido;
    }

    public void agregarProducto(PedidoUsuario producto) {
        productosPedido.add(producto);
    }
}



