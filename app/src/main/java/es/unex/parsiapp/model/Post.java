package es.unex.parsiapp.model;

import androidx.annotation.NonNull;

public class Post {
    /*  TODO REVISAR TODOS LOS ATRIBUTOS QUE PUEDAN IDENTIFICAR A UN TWIT.
            HABRÁ QUE QUITAR Y/O PONER ALGUNOS ATRIBUTOS, PROBABLEMENTE

    EN ESTA CLASE HABRÁ QUE HACER MUCHOS MÉTODOS QUE CONECTEN CON LA API

    */

    private String id;  // ID única del Twit
    private String url; // URL para llegar al Twit
    private String contenido;  // Contenido textual del Twit (no creo que haga falta guardarlo


    /*
        TODO MÉTODOS:
        Habrá que hacer métodos que recojan toda la información necesaria de un Twit desde la API
        Por ejemplo, obtener la ID, el Texto, si tiene multimedia asociada, la cuenta que lo ha subido,
        la URL, etc

     */

    /**
     * Método para compartir el post a otras apps mediante un Intent Implícito
     */
    public void compartir(){
        // todo
        // No sé si esto hay que hacerlo desde la parte gráfica porque basicamente es
        // hacer un intent implícito
    }

    /**
     * Método que tiene el propio post para guardarse en la carpeta que se indique.
     * @param carpeta La carpeta en la que se quiere guardar el Post
     */
    public void guardarEnCarpeta(@NonNull Carpeta carpeta){
        carpeta.anadirPost(this);
    }

    /**
     * Método que obtiene el texto del twit mediante la API
     * @return Texto que se ha recogido del Twit
     */
    public String getTexto(){
        return null;
    }


}
