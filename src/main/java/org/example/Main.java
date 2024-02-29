package org.example;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        DownloaderAndZipper downloaderAndZipper = new DownloaderAndZipper();
        URLManager manager = new URLManager();

        manager.getUrlList().addListener(downloaderAndZipper);

        try (Scanner scanner = new Scanner(System.in)) {
            boolean isFirstInput = true; // Añade un flag para controlar la primera entrada.
            System.out.println("Introduce URLs una a una y presiona Enter. Escribe 'procesar' y presiona Enter para comenzar la descarga y compresión:");

            while (true) {
                String input = scanner.nextLine().trim();

                // Verificar si el usuario desea procesar las URLs ingresadas.
                if ("procesar".equalsIgnoreCase(input)) {
                    break; // Salir del bucle para comenzar el procesamiento.
                } else if (!input.isEmpty()) {
                    // Añadir la URL ingresada a la lista para su procesamiento.
                    manager.addURL(input);
                    isFirstInput = false; // El usuario ha introducido al menos una URL.
                } else if (!isFirstInput) {
                    // Si el usuario presiona Enter sin introducir texto después de la primera entrada.
                    System.out.println("Para procesar las URLs ingresadas, escribe 'procesar'.");
                }
            }
        }

        // Asegúrate de que todas las URLs se hayan procesado antes de cerrar el ExecutorService.
        downloaderAndZipper.shutdownExecutor();

        System.out.println("El proceso ha finalizado.");
    }
}
