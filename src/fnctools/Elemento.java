/* @author Leonardo Gutiérrez Ramírez <leogutierrezramirez.gmail.com> */
/* Dec 16, 2011 */

package fnctools;

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
    
    public boolean isDefined() {
        return definido;
    }
    
    public Elemento setDefined(boolean definido) {
        this.definido = definido;
        return this;
    }
}
