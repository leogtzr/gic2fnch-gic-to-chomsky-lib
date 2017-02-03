/** @author Leonardo Gutiérrez Ramírez <leogutierrezramirez.gmail.com> */

package com.fnc.chomsky.util;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Chomsky {
    
	private static final String REGEX = "(\\{\\w+\\})";
	
	private final Map<String, Integer> map = new LinkedHashMap<>();
    
    private int count = 0;

    private final StringBuilder finalS = new StringBuilder();
    private List<String> chomskyList = new ArrayList<>();
    private final List<String> elements = new ArrayList<>();
    private String _gic;
    
    private final List<String> gicList;
    private final String nonTerminalLetter;
    
    private final Set<String> normalForms = new LinkedHashSet<>();
    
    public Chomsky(final List<String> gicList, final String nonTerminalLetter) {
        this.gicList = gicList;
        this.nonTerminalLetter = nonTerminalLetter;
    }

    private void generate(final String gic) {
        
        _gic = gic.trim();
        elements.clear();
        final StringBuilder chomsky = new StringBuilder();
        String partA;
        String partB;
        
        final List<Element> list = new ArrayList<>();
        final List<String> definidos = new ArrayList<>();
        final Map<String, Element> tabla = new HashMap<>();
        
        for(String s : Tools.getCases(gic).split("\\|")) {
            
            if (Tools.isChomsky(s)) {
                chomsky.append(s).append("|");
                elements.add(s);
            } else {
                partA = s.substring(0, s.length() / 2);
                partB = s.substring(s.length() / 2, s.length());
                
                if (Tools.isTerminal(partA)) {
                    chomsky.append(partA);
                    elements.add(partA);
                } else {
                    
                	final String nonTerminal = "{" + partA + "}";
                    
                    chomsky.append(nonTerminal);
                    elements.add(nonTerminal);
                    tabla.put(nonTerminal, new Element(nonTerminal, false));
                    list.add(new Element(nonTerminal, false));
                }
                
                if (Tools.isTerminal(partB)) {
                    chomsky.append(partB).append("|");
                    elements.add(partB);
                } else {
                    
                    final String nonTerminal = "{" + partB + "}";
                    
                    chomsky.append(nonTerminal);
                    elements.add(nonTerminal);
                    
                    if (!tabla.containsKey(nonTerminal)) {
                        tabla.put(nonTerminal, new Element(nonTerminal, false));
                        list.add(new Element(nonTerminal, false));
                    }
                }
            }
        }
        
        final String temp = chomsky.substring(0, chomsky.length() - 1);
        chomsky.delete(0, chomsky.length());
        chomsky.append(temp);
        
        int elementToDefineIndex = 0;
        while (!isListFull(list)) {
            
                finalS.delete(0, finalS.length());
            
                if (Tools.isChomsky(Tools.getStringBtwn(list.get(elementToDefineIndex).getChomskyStr()))) {
                    definidos.add(list.get(elementToDefineIndex).getChomskyStr() + "->" + Tools.getStringBtwn(list.get(elementToDefineIndex).getChomskyStr()));
                    list.get(elementToDefineIndex).setDefined(true);
                
                } else {
                
                    list.get(elementToDefineIndex).setDefined(true);
                    partA = Tools.getStringBtwn(list.get(elementToDefineIndex).getChomskyStr()).substring(0, Tools.getStringBtwn(list.get(elementToDefineIndex).getChomskyStr()).length() / 2);
                    partB = Tools.getStringBtwn(list.get(elementToDefineIndex).getChomskyStr()).substring(Tools.getStringBtwn(list.get(elementToDefineIndex).getChomskyStr()).length() / 2, Tools.getStringBtwn(list.get(elementToDefineIndex).getChomskyStr()).length());
                    
                    if(Tools.isTerminal(partA)) {
                        finalS.append(partA);
                    } else {
                        
                        finalS.append("{").append(partA).append("}");
                        
                        if (!tabla.containsKey("{" + partA + "}")) {
                            tabla.put("{" + partA + "}", new Element("{" + partA + "}", false));
                            list.add(new Element("{" + partA + "}", false));
                        }
                    }
                
                    if (Tools.isTerminal(partB)) {
                        finalS.append(partB);
                    } else {
                        finalS.append("{").append(partB).append("}");
                        if (!tabla.containsKey("{" + partB + "}")) {
                            tabla.put("{" + partB + "}", new Element("{" + partB + "}", false));
                            list.add(new Element("{" + partB + "}", false));
                        }
                    }
                    
                    definidos.add(list.get(elementToDefineIndex).getChomskyStr() + "->" + finalS);
                }
                
                elementToDefineIndex = getIndex(list);
        }
        setChomskyList(definidos);
    }
    
    private void setChomskyList(final List<String> definidos) {
        this.chomskyList = definidos;
    }
    
    public List<String> getProductions() {
        final List<String> temp = new ArrayList<>();
        for (String s : chomskyList) {
                final Pattern p = Pattern.compile(REGEX);
                final Matcher m = p.matcher(s);
                while (m.find()) {
                    final String symbol = m.group(1);
                    if (!map.containsKey(symbol)) {
                        map.put(symbol, ++count);
                    } 
                    s = s.replace(symbol, this.nonTerminalLetter + map.get(symbol));
                }
                temp.add(s);
            }
        return temp;
    }
    
    private static boolean isListFull(final List<Element> elements) {
        for (final Element element : elements) {
            if (!element.isDefined()) {
                return false;
            }
        }
        return true;
    }
    
    private static int getIndex(final List<Element> elements) {
    	int index = 0;
        for (final Element element : elements) {
            if (!element.isDefined()) {
                return index;
            }
            index++;
        }
        return index;
    }
    
    public Set<String> getNormalForms() {
        return normalForms;
    }
    
    public void generateChomsky() {
        for (final String gic : gicList) {
            generate(gic);
            normalForms.add(getFNCH());
        }
    }
    
    private String getFNCH() {
        
    	String fnc = "";
        int c = 0;
        
        for (String s : elements) {
            
            if(s.contains("{")) {
                if (map.containsKey(s)) {
                    s = nonTerminalLetter + map.get(s);
                } else {
                    map.put(s, ++count);
                    s = nonTerminalLetter + map.get(s);
                }
            }
            
            if (Tools.isLowerTerminal(s)) {
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
