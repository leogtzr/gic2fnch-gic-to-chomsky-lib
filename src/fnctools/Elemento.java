/* @author Leonardo Gutiérrez Ramírez <leogutierrezramirez.gmail.com> */
/* Dec 16, 2011 */

package fnctools;

/**
 * Esta clase contiene dos campos, un String y un booleano que almacenan el estado de un caso
 * @author Leonardo Gutiérrez Ramírez | leogutierrezramirez@gmail.com <a href="mailto:leogutierrezramirez@gmail.com">Leonardo Gutiérrez Ramírez</a>
 */
public class Elemento {
    
    private String chomskyStr;
    private boolean definido;

    public Elemento(String chomString, boolean definido) {
        this.chomskyStr = chomString;
        this.definido = definido;
    }
    
    public String getChomskyStr() {
        return chomskyStr;
    }
    
    public boolean getDefined() {
        return definido;
    }
    
    /**
     * @param definido, una referencia this, para usarse en cascada.
     * @return Elemento
     */
    public Elemento setDefined(boolean definido) {
        this.definido = definido;
        return this;
    }
}
