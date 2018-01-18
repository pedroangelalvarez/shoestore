package com.aim.shoestore;

/**
 * Clase que contiene los códigos usados en "I Wish" para
 * mantener la integridad en las interacciones entre actividades
 * y fragmentos
 */
public class Constantes {
    /**
     * Transición Home -> Detalle
     */
    public static final int CODIGO_DETALLE = 100;

    /**
     * Transición Detalle -> Actualización
     */
    public static final int CODIGO_ACTUALIZACION = 101;

    /**
     * Puerto que utilizas para la conexión.
     * Dejalo en blanco si no has configurado esta carácteristica.
     */
    private static final String PUERTO_HOST = "63343";

    /**
     * Dirección IP de genymotion o AVD
     */
    private static final String IP = "http://10.0.3.2:";
    /**
     * URLs del Web Service
     */
    public static final String GET = IP + PUERTO_HOST + "/Android/obtener_usuarios.php";
    public static final String GET_BY_ID = IP + PUERTO_HOST + "/Android/obtener_meta_por_id.php";
    public static final String UPDATE = IP + PUERTO_HOST + "/Android/actualizar_meta.php";
    public static final String DELETE = IP + PUERTO_HOST + "/Android/borrar_meta.php";
    public static final String INSERT = IP + PUERTO_HOST + "/Android/insertar_meta.php";

    /**
     * Clave para el valor extra que representa al identificador de una meta
     */
    public static final String EXTRA_ID = "IDEXTRA";

}