package fnctools;

/**
 * Clase que contiene una hashtable.
 * La hashtable usa como clave o key un String, y como valor un Elemento
 */
import java.util.HashMap;

public class Tabla {
    
    private HashMap<String, Elemento> tabla;
    
    public Tabla() {
        tabla = new HashMap<String, Elemento>();
    }
    
    public void put(String chomskyStr, Elemento t) {
        this.tabla.put(chomskyStr, t);
    }
    
    public HashMap<String, Elemento> getTabla() {
        return tabla;
    }
    
}
