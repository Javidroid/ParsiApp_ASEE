package es.unex.parsiapp.model;

import java.util.ArrayList;
import java.util.List;

public class GestorColumna {
    private List<Columna> listaColumnas;
    private int currentColumn; // Índice de la columna que se está mostrando actualmente en la pantalla

    public GestorColumna() {
        this.listaColumnas = new ArrayList<>();
    }

    /**
     * Método que añade la columna que se le pase por parámetro
     * @param col La columna que se quiera añadir
     */
    public void anadirColumna(Columna col){
        listaColumnas.add(col);
    }

    /**
     * Método para borrar la columna que se pase por parámetro
     * @param col La columna que se quiere borrar
     */
    public void borrarColumna(Columna col){
        listaColumnas.remove(col);
    }

    /**
     * Método para borrar la columna con el índice indicado
     * @param index índice de la columna que se quiere borrar
     */
    public void borrarColumna(int index){
        listaColumnas.remove(index);
    }

    /**
     * Método que sirve para cambiar el índice que indica cuál es la columna que tiene activa
     * el usuario en la pantalla principal.
     * @param index Índice de la nueva columna a la que se quiere apuntar
     */
    public void setCurrentColumn(int index){
        currentColumn = index;
    }

    /**
     * Devuelve la columna que el usuario tiene actualmente como activa en la pantalla principal
     * @return La columna a la que se está apuntando.
     */
    public Columna getCurrentColumn(){
        return listaColumnas.get(currentColumn);
    }


}
