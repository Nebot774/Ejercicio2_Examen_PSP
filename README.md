# Proyecto de Descarga y Compresión de Archivos

Este proyecto consiste en una aplicación JavaFX que permite al usuario ingresar URLs de archivos a descargar y comprimir en un archivo ZIP. La aplicación utiliza una arquitectura basada en observadores para realizar la descarga y compresión de manera asíncrona.

## Funcionalidades

- Permite al usuario agregar URLs de archivos a una lista para su procesamiento.
- Descarga los archivos desde las URLs proporcionadas.
- Comprime los archivos descargados en un archivo ZIP.
- Proporciona retroalimentación en la interfaz de usuario sobre el progreso del proceso de descarga y compresión.
- Cierra el `ExecutorService` correctamente después de completar todas las tareas de descarga.

## Componentes

### URLManager

La clase `URLManager` gestiona la lista de URLs de archivos a procesar. Utiliza una lista observable para almacenar las URLs junto con un identificador único generado para cada archivo.

### DownloaderAndZipper

La clase `DownloaderAndZipper` se encarga de descargar y comprimir los archivos. Implementa la interfaz `ListChangeListener` para recibir notificaciones cuando se agregan nuevas URLs a la lista. Utiliza un `ExecutorService` para ejecutar las tareas de descarga de manera asíncrona.

### Main

La clase `Main` es la entrada principal del programa. Permite al usuario ingresar URLs de archivos y controla el inicio del proceso de descarga y compresión.

## Uso

Para ejecutar la aplicación, simplemente compila y ejecuta la clase `Main`.

```bash
java Main.java
Luego, sigue las instrucciones en la interfaz de usuario para agregar URLs de archivos y comenzar el proceso de descarga y compresión.

## Dependencias

    JavaFX: Biblioteca para crear aplicaciones de escritorio Java.
    Apache Commons Lang: Biblioteca que proporciona clases y utilidades útiles para Java.
