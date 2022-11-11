package es.unex.parsiapp.model;

import java.util.ArrayList;
import java.util.List;

public class Columna {
    private int id;
    private String nombre;
    private List<Filtro> listaFiltros;
    private List<Post> listaPost_temporal; // Lista TEMPORAL que almacena los twits cuando se refresca

    public Columna(String nombre) {
        this.nombre = nombre;
        listaFiltros = new ArrayList<>();
        listaPost_temporal = new ArrayList<>();
    }

    /**
     * Método que, al ser llamado, hace una consulta a la API para actualizar los Posts que aparecen
     * en dicha columna. Esta actualización se lleva a cabo pasando los Twits recogidos por la API
     * por todos los filtros que haya almacenados en listaFiltros
     * @return Lista de posts que han pasado los filtros
     */
    public List<Post> refrescarColumna(){
        return null;
    }

    private void llamadaAPIPosts(){

    }

    /**
     * Método que añade el filtro pasado por parámetro a la lista
     * @param f El filtro que se quiere añadir
     */
    public void anadirFiltro(Filtro f){
        listaFiltros.add(f);
    }

    /**
     * Método que elimina el filtro pasado por parámetro
     * @param f El filtro que se quiere eliminar
     */
    public void quitarFiltro(Filtro f){
        listaFiltros.remove(f);
    }

    /**
     * Método que elimina el filtro cuyo indice se pase por parámetro
     * @param index El índice del filtro en la listaFiltros
     */
    public void quitarFiltro(int index){
        listaFiltros.remove(index);
    }

    /**
     * Método que devuelve la lista de posts. Su utilidad puede ser recogerlos para representarlos
     * en la pantalla
     * @return Devuelve listaPost_temporal, la lista que contiene los posts de la última
     * actualización
     */
    public List<Post> getListaPosts(){
        return listaPost_temporal;
    }
}
