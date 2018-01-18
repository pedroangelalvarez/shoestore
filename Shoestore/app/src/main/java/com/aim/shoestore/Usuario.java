package com.aim.shoestore;

/**
 * Created by pedro on 10/12/17.
 */

public class Usuario {

    /*
    Atributos
     */
    private String id;
    private String pass;

    public Usuario(String id, String pass) {
        this.id= id;
        this.pass = pass;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    /**
     * Compara los atributos de dos metas
     * @param user Meta externa
     * @return true si son iguales, false si hay cambios
     */
    public boolean compararCon(Usuario user) {
        return this.id.compareTo(user.id) == 0 &&
                this.pass.compareTo(user.pass) == 0;
    }
}