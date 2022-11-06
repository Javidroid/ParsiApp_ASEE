package es.unex.parsiapp.model;

import java.util.ArrayList;
import java.util.List;

public class GestorCarpeta {
    private List<Carpeta> listaCarpetas;

    public GestorCarpeta() {
        listaCarpetas = new ArrayList<>();
    }

    /**
     * Método para añadir la carpeta que se pase por parámetro
     * @param carp Carpeta que se quiere añadir
     */
    public void anadirCarpeta(Carpeta carp){
        listaCarpetas.add(carp);
    }

    /**
     * Método para quitar la carpeta que se pase por parámetro
     * @param carp Carpeta que se quiere quitar
     */
    public void quitarCarpeta(Carpeta carp){
        listaCarpetas.remove(carp);
    }

    /**
     * Método para quitar la carpet por índice
     * @param index índice en la lista que apunta a la carpeta que se quiere quitar
     */
    public void quitarCarpeta(int index){
        listaCarpetas.remove(index);
    }

    /**
     * Método que devuelve la carpeta según el índice que se le indique
     * @param index Índice de la carpeta que se quiere obtener
     * @return La carpeta cuyo índice = index
     */
    public Carpeta getCarpeta(int index){
        return listaCarpetas.get(index);
    }


}
