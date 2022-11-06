package es.unex.parsiapp.model;

import java.util.List;

public class Carpeta {
    private String nombre;
    private String foto; // cadena que guarda el directorio del asset correspondiente
    private List<Post> listaPosts;

    public Carpeta(String nombre) {
        this.nombre = nombre;
        // todo this.foto = [DIRECTORIO DE LA FOTO DE LAS CARPETAS POR DEFECTO]
    }

    /**
     * Método para cambiar el nombre de la carpeta
     * @param nombre Nombre nuevo que se quiera poner
     */
    public void cambiarNombre(String nombre){
        this.nombre = nombre;
    }

    /**
     * Método para cambiar la foto asociada a la carpeta
     * @param foto Directorio del ASSET que se quiera poner
     */
    public void cambiarFoto(String foto){
        this.foto = foto;
    }

    /**
     * Método para añadir un post
     * @param post El post que se quiere añadir
     */
    public void anadirPost(Post post){
        listaPosts.add(post);
    }

    /**
     * Método para quitar el post que se pase por parámetro
     * @param post El post que se quiere quitar
     */
    public void quitarPost(Post post){
        listaPosts.remove(post);
    }

    /**
     * Método para quitar el post según el índice que se indique
     * @param index Índice en la lista del post que se quiere quitar
     */
    public void quitarPost(int index){
        listaPosts.remove(index);
    }

    /**
     * Método que devuelve la lista de los posts para visualizarla
     * @return Lista de posts guardados en esta carpeta
     */
    public List<Post> getListaPosts() {
        return listaPosts;
    }
}
