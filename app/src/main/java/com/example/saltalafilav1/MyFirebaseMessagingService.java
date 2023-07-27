package com.example.saltalafilav1;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "Pedido Listo: " + remoteMessage.getData());
        mostrarNotificacion(remoteMessage.getData().get("titulo"), remoteMessage.getData().get("mensaje"));
    }

    private void mostrarNotificacion(String titulo, String mensaje) {
        // Crea la notificación usando NotificationCompat.Builder o cualquier otra biblioteca
        // para mostrar notificaciones en Android. Aquí se utiliza NotificationCompat.Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "canal_id")
                .setSmallIcon(R.drawable.espiritu) // Icono de la notificación
                .setContentTitle("Salta la Fila") // Título de la notificación personalizado
                .setContentText("Pedido Listo, muestra el siguiente QR para retirarlo en Barra") // Mensaje personalizado
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Crea un intent para abrir la actividad correspondiente cuando se toque la notificación
        Intent intent = new Intent(this, ActivityQR.class); // Reemplaza "TuActividadDestino" con la actividad a la que deseas redirigir al tocar la notificación
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Permite que la actividad sea la principal en caso de que ya esté abierta

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        builder.setContentIntent(pendingIntent);

        // Muestra la notificación
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(0, builder.build());
        }
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(TAG, "Token actualizado: " + token);

        // Aquí determinas si el usuario actual es un cliente o la barra
        boolean esCliente = determinarSiEsCliente();

        // Obten el ID del usuario actual o cualquier identificador único del dispositivo
        String userId = obtenerIdUsuarioActual();

        // Si el usuario es un cliente, guarda el token en la colección "usuarios"
        // Si el usuario es la barra, guarda el token en la colección "barras"
        if (userId != null) {
            String coleccion = esCliente ? "usuarios" : "barras";
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> data = new HashMap<>();
            data.put("fcmToken", token);

            // Guarda el token en el documento del usuario o barra en Firestore
            db.collection(coleccion).document(userId)
                    .set(data, SetOptions.merge()) // Utiliza merge para no sobrescribir otros datos del documento
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Token guardado en Firestore.");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Error al guardar el token en Firestore: " + e.toString());
                        }
                    });
        }
    }

    private String obtenerIdUsuarioActual() {
        return null;
    }
}

