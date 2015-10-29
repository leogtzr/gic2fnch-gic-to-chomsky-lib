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
    
    private final LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
    private static final String REGEX = "(\\{\\w+\\})";
    private int count = 0;

    private final StringBuilder finalS = new StringBuilder();
    private ArrayList<String> chomskyList = new ArrayList<>();
    private final ArrayList<String> elements = new ArrayList<>();
    private String _gic;
    
    private final List<String> gicList;
    private final String nonTerminalLetter;
    
    private final LinkedHashSet<String> normalForms = new LinkedHashSet<>();
    
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

    private void generate(final String gic) {
        
        _gic = gic.trim();  
        elements.clear();
        StringBuilder chomsky = new StringBuilder();
        String partA;
        String partB;
        
        ArrayList<Element> list = new ArrayList<>();
        ArrayList<String> definidos = new ArrayList<>();
        HashMap<String, Element> tabla = new HashMap<>();
        
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
        
        final String temp = chomsky.substring(0, chomsky.length() - 1);
        chomsky.delete(0, chomsky.length());
        chomsky.append(temp);
        
        int i = 0;
        while(!isListFull(list)) {
            
                finalS.delete(0, finalS.length());
            
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
    
    private void setChomskyList(final ArrayList<String> definidos) {
        this.chomskyList = definidos;
    }
    
    public ArrayList<String> getProductions() {
        final ArrayList<String> temp = new ArrayList<>();
        for (String s : this.chomskyList) {
                final Pattern p = Pattern.compile(REGEX);
                final Matcher m = p.matcher(s);
                while(m.find()) {
                    final String symbol = m.group(1);
                    if(!map.containsKey(symbol)) {
                        map.put(symbol, ++count);
                    } 
                    s = s.replace(symbol, nonTerminalLetter + map.get(symbol));
                }
                temp.add(s);
            }
        return temp;
    }
    
    private static boolean isListFull(final ArrayList<Element> lista) {
        for(byte i = 0; i < lista.size(); i++) {
            if(!lista.get(i).isDefined()) {
                return false;
            }
        }
        return true;
    }
    
    private static int getIndex(final ArrayList<Element> lista) {
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
        for (String gic : gicList) {
            generate(gic);
            normalForms.add(getFNCH());
        }
    }
    
    public String getFNCH() {
        String fnc = "";
        int c = 0;
        for (String s : elements) {
            
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
            final String s = Tools.getProductionName(_gic) + " -> " + fnc.substring(0, fnc.length() - 1);
            return s;
        }
        return Tools.getProductionName(_gic) + " -> " + fnc;
    }
    
}
