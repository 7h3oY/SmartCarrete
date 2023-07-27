package com.example.saltalafilav1;

import java.util.List;

public class PedidosBarra {
    private String idPedido;
    private String usuario;
    private List<String> articulos;

    public PedidosBarra(String idPedido, String usuario, List<String> articulos) {
        this.idPedido = idPedido;
        this.usuario = usuario;
        this.articulos = articulos;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public List<String> getArticulos() {
        return articulos;
    }

    public void setArticulos(List<String> articulos) {
        this.articulos = articulos;
    }
}

