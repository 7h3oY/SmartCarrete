package com.example.saltalafilav1;

import android.os.Parcel;
import android.os.Parcelable;

public class PedidoUsuario implements Parcelable {

    private String nombre;
    private int cantidad;
    private double precio;
    private double total;
    private String usuario;
    private String idPedido;

    public PedidoUsuario(String nombre, int cantidad, int precio, String usuario) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.total = cantidad * precio; // Calcular el total basado en cantidad y precio
        this.usuario = usuario;
    }

    protected PedidoUsuario(Parcel in) {
        nombre = in.readString();
        cantidad = in.readInt();
        precio = in.readDouble();
        total = in.readDouble();
        usuario = in.readString();
    }

    public static final Creator<PedidoUsuario> CREATOR = new Creator<PedidoUsuario>() {
        @Override
        public PedidoUsuario createFromParcel(Parcel in) {
            return new PedidoUsuario(in);
        }

        @Override
        public PedidoUsuario[] newArray(int size) {
            return new PedidoUsuario[size];
        }
    };

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.total = cantidad * precio; // Recalcular el total después de actualizar la cantidad
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
        this.total = cantidad * precio; // Recalcular el total después de actualizar el precio
    }

    public double getTotal() {
        return total;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeInt(cantidad);
        dest.writeDouble(precio);
        dest.writeDouble(total);
        dest.writeString(usuario);
    }

}




