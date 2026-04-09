package model;
public class Piezas {

    private String color; // para el color de las piezas blancas o negras
    private boolean esDama;

    // Constructor
    public Piezas(String color) {
        this.color = color;
        this.esDama = false;
       
    }

    // getters
    public String getColor() {
        return color;
    }

    public boolean getEsDama() {
        return esDama;
    }

    public void setEsDama(boolean esDama) {
        this.esDama = esDama;
    }
}