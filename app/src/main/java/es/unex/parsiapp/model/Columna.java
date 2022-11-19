package es.unex.parsiapp.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.ArrayList;
import java.util.List;

import es.unex.parsiapp.roomdb.ApiCallTypeConverter;

@Entity(tableName="columna")
public class Columna {

    // El usuario puede elegir entre ver los resultados de una busqueda o de un usuario
    public enum ApiCallType {
      QUERY, USER
    };

    @PrimaryKey(autoGenerate = true)
    private int idDb;
    @ColumnInfo(name="nombre")
    private String nombre;
    @ColumnInfo(name="apiCall")
    private String apiCall;
    @TypeConverters(ApiCallTypeConverter.class)
    private ApiCallType apiCallType;

    public Columna(){}

    public Columna(String nombre, String apiCall){
        this.nombre = nombre;
        this.apiCall = apiCall;
    }

    public int getIdDb() {
        return idDb;
    }

    public void setIdDb(int idDb) {
        this.idDb = idDb;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApiCall() {
        return apiCall;
    }

    public void setApiCall(String apiCall) {
        this.apiCall = apiCall;
    }

    public ApiCallType getApiCallType() {
        return apiCallType;
    }

    public void setApiCallType(ApiCallType apiCallType) {
        this.apiCallType = apiCallType;
    }
}
