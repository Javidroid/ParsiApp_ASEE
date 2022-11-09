package es.unex.parsiapp.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName="post")
public class Post {
    /*  TODO REVISAR TODOS LOS ATRIBUTOS QUE PUEDAN IDENTIFICAR A UN TWIT.
            HABRÁ QUE QUITAR Y/O PONER ALGUNOS ATRIBUTOS, PROBABLEMENTE

    EN ESTA CLASE HABRÁ QUE HACER MUCHOS MÉTODOS QUE CONECTEN CON LA API

    */
    @Ignore
    public static final String ITEM_SEP = System.getProperty("line.separator");

    @PrimaryKey(autoGenerate = true)
    private long idDb; // ID de la BD
    @ColumnInfo(name="id")
    private String id;  // ID única del Twit
    @ColumnInfo(name="url")
    private String url; // URL para llegar al Twit
    @Ignore
    private String contenido;  // Contenido textual del Twit (no creo que haga falta guardarlo)


    /*
        TODO MÉTODOS:
        Habrá que hacer métodos que recojan toda la información necesaria de un Twit desde la API
        Por ejemplo, obtener la ID, el Texto, si tiene multimedia asociada, la cuenta que lo ha subido,
        la URL, etc

     */

    @Ignore
    public Post(String id, String url){
        this.id = id;
        this.url = url;
    }

    public Post(long idDb, String id, String url){
        this.idDb = idDb;
        this.id = id;
        this.url = url;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {this.id = id;}

    public long getIdDb() {return this.idDb;}

    public void setIdDb(long idDb) {this.idDb = idDb;}

    public String getUrl(){
        return this.url;
    }

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

    public String toLog() {
        return "ID: " + idDb + ITEM_SEP + "id twitter: " + id + ITEM_SEP + "url: " + url;
    }


}
