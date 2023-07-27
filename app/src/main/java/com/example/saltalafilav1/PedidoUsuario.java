package com.example.saltalafilav1;

import android.os.Parcel;
import android.os.Parcelable;

import android.os.Parcel;
import android.os.Parcelable;

public class PedidoUsuario implements Parcelable {

    private String nombre;
    private int cantidad;
    private int precio;
    private int total;
    private String idPedido;
    private int cantidadTotal;

    public PedidoUsuario(String nombre, int cantidad, int precio) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.total = cantidad * precio; // No calcular el total aquí
    }

    protected PedidoUsuario(Parcel in) {
        nombre = in.readString();
        cantidad = in.readInt();
        precio = in.readInt();
        total = in.readInt();
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

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
        this.total = cantidad * precio; // Recalcular el total después de actualizar el precio
    }

    public int getTotal() {
        return total;
    }

    public int getCantidadTotal() {
        return cantidadTotal;
    }

    public void setCantidadTotal(int cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeInt(cantidad);
        dest.writeInt(precio);
        dest.writeInt(total);
    }
}






