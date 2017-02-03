/** 
 * @author Leonardo Gutiérrez Ramírez <leogutierrezramirez.gmail.com> 
 */
package com.fnc.chomsky.util;

public class Element {
    
    private final String chomskyStr;
    private boolean defined;

    public Element(final String chomString, final boolean defined) {
        this.chomskyStr = chomString;
        this. defined =  defined;
    }
    
    public String getChomskyStr() {
        return chomskyStr;
    }
    
    public boolean isDefined() {
        return defined;
    }
    
    public void setDefined(final boolean defined) {
        this.defined = defined;
    }

    @Override
    public String toString() {
        return "E{" + chomskyStr + "," + defined + "}";
    }
    
}
