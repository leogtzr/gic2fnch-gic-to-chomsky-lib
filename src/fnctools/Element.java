/** @author Leonardo Gutiérrez Ramírez <leogutierrezramirez.gmail.com> */
/** Dec 16, 2011 */

package fnctools;

public class Element {
    
    private final String chomskyStr;
    private boolean defined;

    public Element(String chomString, boolean defined) {
        this.chomskyStr = chomString;
        this. defined =  defined;
    }
    
    public String getChomskyStr() {
        return chomskyStr;
    }
    
    public boolean isDefined() {
        return defined;
    }
    
    public void setDefined(boolean defined) {
        this.defined = defined;
    }

    @Override
    public String toString() {
        return "E{" + chomskyStr + "," + defined + "}";
    }
    
}
