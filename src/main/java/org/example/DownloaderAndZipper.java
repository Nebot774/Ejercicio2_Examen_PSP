package org.example;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.AbstractMap.SimpleEntry;

public class DownloaderAndZipper implements ListChangeListener<SimpleEntry<String, String>> {
    private ExecutorService executor;

    public DownloaderAndZipper() {
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Override
    public void onChanged(Change<? extends SimpleEntry<String, String>> c) {
        if (!c.getList().isEmpty()) {
            List<CompletableFuture<File>> futureFiles = new ArrayList<>();
            for (SimpleEntry<String, String> entry : c.getList()) {
                CompletableFuture<File> futureFile = CompletableFuture.supplyAsync(() -> {
                    try {
                        return downloadFile(entry.getKey(), entry.getValue());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }, executor);
                futureFiles.add(futureFile);
            }

            CompletableFuture<Void> allDownloads = CompletableFuture.allOf(futureFiles.toArray(new CompletableFuture[futureFiles.size()]));

            allDownloads.thenRun(() -> {
                List<File> files = futureFiles.stream().map(completableFuture -> {
                    try {
                        return completableFuture.get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        return null;
                    }
                }).collect(Collectors.toList());
                compressFiles(files);



            }).join(); // Espera a que la compresión se complete antes de continuar.

        }
    }

    public void shutdownExecutor() {
        try {
            System.out.println("Cerrando el executor...");
            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("Interrupción durante el cierre del executor.");
        } finally {
            if (!executor.isTerminated()) {
                System.err.println("Cancelando tareas no terminadas...");
            }
            executor.shutdownNow();
            System.out.println("El executor se cerró correctamente.");
        }
    }



    private File downloadFile(String urlString, String fileName) {
        try {
            URL url = new URL(urlString);
            File file = Paths.get(fileName).toFile();

            try (InputStream in = url.openStream(); FileOutputStream fos = new FileOutputStream(file)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }

            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void compressFiles(List<File> files) {
        // Generar un nombre de archivo único usando un sello de tiempo.
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String zipFileName = "descargas_" + timeStamp + ".zip";
        File zipFile = new File("Descargas/" + zipFileName);

        // El resto del método permanece igual...
        File parentDir = zipFile.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            for (File file : files) {
                if (file != null && file.exists()) {
                    ZipEntry zipEntry = new ZipEntry(file.getName());
                    zos.putNextEntry(zipEntry);

                    byte[] bytes = Files.readAllBytes(file.toPath());
                    zos.write(bytes, 0, bytes.length);
                    zos.closeEntry();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}

