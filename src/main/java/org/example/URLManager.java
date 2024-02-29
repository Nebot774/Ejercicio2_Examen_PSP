package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.security.SecureRandom;
import java.util.AbstractMap.SimpleEntry;
import java.util.Base64;

public class URLManager {
    private final ObservableList<SimpleEntry<String, String>> urlList = FXCollections.observableArrayList();
    private String lastRandomString = "";

    public URLManager() {
        // El constructor no necesita implementación específica si solo inicializa la lista.
    }

    public void addURL(String url) {
        if (!url.isEmpty()) {
            String randomString = generateRandomString();
            lastRandomString = randomString; // Almacena el último string generado.
            urlList.add(new SimpleEntry<>(url, randomString));
        } else {
            // Notifica de manera especial para URLs vacías.
            System.out.println("URL vacía introducida. Preparando para iniciar el proceso de descarga y compresión.");
        }
    }

    private String generateRandomString() {
        // Utiliza SecureRandom y Base64 para generar una cadena aleatoria segura.
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20]; //20 caracteres
        random.nextBytes(bytes);
        // Genera la cadena aleatoria y asegura que no tenga relleno para mantener una longitud constante.
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public ObservableList<SimpleEntry<String, String>> getUrlList() {
        return urlList;
    }
}

