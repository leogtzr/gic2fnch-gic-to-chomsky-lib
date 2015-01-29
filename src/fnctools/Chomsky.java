/** @author Leonardo Gutiérrez Ramírez <leogutierrezramirez.gmail.com> */

package fnctools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Chomsky {
    
    private LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
    private final String regex = "(\\{\\w+\\})";
    private int count = 0;

    private StringBuilder chomsky = new StringBuilder();
    private String partA = "";
    private String partB = "";
    private StringBuilder finalS = new StringBuilder();
    private ArrayList<String> chomskyList = new ArrayList<String>();
    private ArrayList<String> elements = new ArrayList<String>();
    private String _gic;
    
    private final List<String> gicList;
    private final String nonTerminalLetter;
    
    private final LinkedHashSet<String> normalForms = new LinkedHashSet<String>();
    
    public Chomsky(List<String> gicList, String nonTerminalLetter) {
        if(gicList == null || gicList.isEmpty()) {
            throw new IllegalArgumentException("GIC empty list.");
        }
        if(nonTerminalLetter == null || nonTerminalLetter.trim().isEmpty() ||
                nonTerminalLetter.trim().length() > 1
                ) {
            throw new IllegalArgumentException("Non terminal letter size invalid");
        }
        this.gicList = gicList;
        this.nonTerminalLetter = nonTerminalLetter;
    }

    private void generate(String gic) {
        
        _gic = gic.trim();        
        elements.clear();
        chomsky.delete(0, chomsky.length());
        
        ArrayList<Element> list = new ArrayList<Element>();
        ArrayList<String> definidos = new ArrayList<String>();
        HashMap<String, Element> tabla = new HashMap<String, Element>();
        
        for(String s : Tools.getCases(gic).split("\\|")) {
            
            if(Tools.isChomsky(s)) {
                chomsky.append(s).append("|");
                elements.add(s);
            } else {
                partA = s.substring(0, s.length()/2);
                partB = s.substring(s.length()/2, s.length());
                
                if(Tools.isTerminal(partA)) {
                    chomsky.append(partA);
                    elements.add(partA);
                } else {
                    
                    String _s = "{" + partA + "}";
                    
                    chomsky.append(_s);
                    elements.add(_s);
                    if(!tabla.containsKey(_s)) {
                        tabla.put(_s, new Element(_s, false));
                        list.add(new Element(_s, false));
                    }
                    
                }
                
                if(Tools.isTerminal(partB)) {
                    chomsky.append(partB).append("|");
                    elements.add(partB);
                } else {
                    
                    String _s = "{" + partB + "}";
                    
                    chomsky.append(_s);
                    elements.add(_s);
                    
                    if(!tabla.containsKey(_s)) {
                        tabla.put(_s, new Element(_s, false));
                        list.add(new Element(_s, false));
                    }
                }
            }
        }
        
        String temp = chomsky.substring(0, chomsky.length() - 1);
        chomsky.delete(0, chomsky.length());
        chomsky.append(temp.toString());
        
        setChomsky(Tools.getProductionName(gic) + " --> " + chomsky);
        
        int i = 0;
        while(!isListFull(list)) {
            
                finalS.delete(0, finalS.length());
                partA = "";
                partB = "";
            
                if(Tools.isChomsky(Tools.getStringBtwn(list.get(i).getChomskyStr()))) {
                    definidos.add(list.get(i).getChomskyStr() + "->" + Tools.getStringBtwn(list.get(i).getChomskyStr()));
                    list.get(i).setDefined(true);
                
                } else {
                
                    list.get(i).setDefined(true);
                    partA = Tools.getStringBtwn(list.get(i).getChomskyStr()).substring(0, Tools.getStringBtwn(list.get(i).getChomskyStr()).length() / 2);
                    partB = Tools.getStringBtwn(list.get(i).getChomskyStr()).substring(Tools.getStringBtwn(list.get(i).getChomskyStr()).length() / 2, Tools.getStringBtwn(list.get(i).getChomskyStr()).length());
                    
                    if(Tools.isTerminal(partA)) {
                        finalS.append(partA);
                    } else {
                        
                        finalS.append("{").append(partA).append("}");
                        
                        if(!tabla.containsKey("{" + partA + "}")) {
                        
                            tabla.put("{" + partA + "}", new Element("{" + partA + "}", false));
                            list.add(new Element("{" + partA + "}", false));
                            
                        }
                    }
                
                    if(Tools.isTerminal(partB)) {
                        finalS.append(partB);
                    } else {
                        finalS.append("{").append(partB).append("}");
                        if(!tabla.containsKey("{" + partB + "}")) {
                            tabla.put("{" + partB + "}", new Element("{" + partB + "}", false));
                            list.add(new Element("{" + partB + "}", false));
                        }
                    }
                    
                    definidos.add(list.get(i).getChomskyStr() + "->" + finalS);
                    
                }
            i = getIndex(list);
        }
        setChomskyList(definidos);
    }
    
    private void setChomsky(String s) {
        this.chomsky = new StringBuilder(s);
    }
    
    private void setChomskyList(ArrayList<String> definidos) {
        this.chomskyList = definidos;
    }
    
    public ArrayList<String> getProductions() {
        ArrayList<String> temp = new ArrayList<String>();
        for (String s : this.chomskyList) {
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(s);
                while(m.find()) {
                    String symbol = m.group(1);
                    if(!map.containsKey(symbol)) {
                        map.put(symbol, ++count);
                    } 
                    s = s.replace(symbol, nonTerminalLetter + map.get(symbol));
                }
                temp.add(s);
            }
        return temp;
    }
    
    public String getChomskyForm() {
        return chomsky.toString();
    }
    
    private static boolean isListFull(ArrayList<Element> lista) {
        for(byte i = 0; i < lista.size(); i++) {
            if(!lista.get(i).isDefined()) {
                return false;
            }
        }
        return true;
    }
    
    private static int getIndex(ArrayList<Element> lista) {
        for(byte i = 0; i < lista.size(); i++) {
            if(!lista.get(i).isDefined()) {
                return i;
            }
        }
        return -1;
    }
    
    public LinkedHashSet<String> getNormalForms() {
        return normalForms;
    }
    
    public void generateChomsky() {
        for(String gic : gicList) {
            generate(gic);
            normalForms.add(getFNCH());
        }
    }
    
    public String getFNCH() {
        String fnc = "";
        int c = 0;
        for(String s : elements) {
            
            if(s.contains("{")) {
                if(map.containsKey(s)) {
                    s = nonTerminalLetter + map.get(s);
                } else {
                    map.put(s, ++count);
                    s = nonTerminalLetter + map.get(s);
                }
            }
            
            if(Tools.isLowerTerminal(s)) {
                fnc += s + "|";
                c = 0;
            } else {
                fnc += s;
                c++;
                if(c == 2) {
                    fnc += "|";
                    c = 0;
                }
            }
        }
        if(fnc.substring(fnc.length() - 1).equals("|")) {
            String s = Tools.getProductionName(_gic) + " -> " + fnc.substring(0, fnc.length() - 1);
            return s;
        }
        return Tools.getProductionName(_gic) + " -> " + fnc;
    }
    
}
