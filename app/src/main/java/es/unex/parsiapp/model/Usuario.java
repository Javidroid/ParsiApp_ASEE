package es.unex.parsiapp.model;

public class Usuario {
    private int id;  // ID único para cada usuario
    private String username;
    private String password;
    private String nombre;  // nombre de perfil que se quiere que aparezca junto al username
    private GestorCarpeta gestorCarpeta; // gestor para interaccionar con las carpetas del user
    private GestorColumna gestorColumna; // gestor para interaccionar con las columnas del user
    // todo atributo que guarde la foto de perfil
    // todo: tal vez haga falta un atributo para guardar la conexión a la API: INVESTIGAR


    /**
     * Constructor de usuario a partir del Username, una contraseña y un nombre de perfil
     * @param username El nombre de usuario que se quiera utilizar. Tiene que ser único.
     * @param password La contraseña que se haya elegido
     * @param nombre El nombre de perfil/cuenta que se quiere que aparezca junto al username
     */
    public Usuario(String username, String password, String nombre) {
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.gestorCarpeta = new GestorCarpeta(); // Creamos el objeto y lo asociamos
        this.gestorColumna = new GestorColumna(); // Lo mismo que arriba

        // todo: hacer un método para que asigne un id único (o algo por el estilo)
    }

    /** todo este método creo que no va aquí xd habría que hacer un gestor o algo
     * Método para comprobar las credenciales insertadas por parámetro
     * @param user nombre de usuario
     * @param password contraseña
     * @return true si las credenciales coinciden. False si no.
     */
    public boolean iniciarSesion(String user, String password){
        // todo
        return false;
    }


    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
