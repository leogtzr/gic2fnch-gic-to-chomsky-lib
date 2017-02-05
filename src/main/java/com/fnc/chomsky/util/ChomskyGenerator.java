/** @author Leonardo Gutiérrez Ramírez <leogutierrezramirez.gmail.com> 
 **/

package com.fnc.chomsky.util;

import static com.fnc.chomsky.util.Tools.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChomskyGenerator {
    
	private final Map<String, Integer> map = new LinkedHashMap<>();
    
    private List<String> chomskyList = new ArrayList<>();
    private final List<String> elements = new ArrayList<>();
    private String _gic;
    
    private final List<String> gicList;
    private final String nonTerminalLetter;
    
    private final Set<String> normalForms = new LinkedHashSet<>();
    
    private class CasePart {

		public String a;
		public String b;
		
		private CasePart(final String a, final String b) {
			this.a = a;
			this.b = b;
		}

		@Override
		public String toString() {
			return "CasePart [a=" + a + ", b=" + b + "]";
		}
		
	}
    
    private class Chomsky {
    	
    	private final Set<String> normalForms;
    	private final List<String> productions;
		
    	public Chomsky(final Set<String> normalForms, final List<String> productions) {
			this.normalForms = normalForms;
			this.productions = productions;
		}

		public Set<String> getNormalForms() {
			return Collections.<String>unmodifiableSet(normalForms);
		}

		public List<String> getProductions() {
			return Collections.<String>unmodifiableList(productions);
		}

		@Override
		public String toString() {
			return "Chomsky [normalForms=" + normalForms + ", productions=" + productions + "]";
		}
    	
    }
    
    public ChomskyGenerator(final List<String> gicList, final String nonTerminalLetter) {
        this.gicList = gicList;
        this.nonTerminalLetter = nonTerminalLetter;
    }
    
    private void generate(final String gic) {
        
        _gic = gic.trim();
        elements.clear();
        final List<Element> pendingElementsToDefine = new ArrayList<>();
        
        generateElementsInformationForGIC(gic, elements, pendingElementsToDefine);
        
        int elementToDefineIndex = 0;
        final List<String> definidos = new ArrayList<>();
        
        while (!areElementsDefined(pendingElementsToDefine)) {
            
            final Element elementToDefine = pendingElementsToDefine.get(elementToDefineIndex);
            final String textBtwn = getStringBtwn(elementToDefine.getChomskyStr());
            elementToDefine.setDefined(true);
            
            if (isChomsky(getStringBtwn(elementToDefine.getChomskyStr()))) {
                definidos.add(elementToDefine.getChomskyStr() + "->" + textBtwn);
            } else {
            
            	final StringBuilder finalS = new StringBuilder();
            	final CasePart cp = fromString(textBtwn);
            	
                if (isTerminal(cp.a)) {
                    finalS.append(cp.a);
                } else {
                    finalS.append("{").append(cp.a).append("}");
                    pendingElementsToDefine.add(new Element("{" + cp.a + "}", false));
                }
            
                if (isTerminal(cp.b)) {
                    finalS.append(cp.b);
                } else {
                    finalS.append("{").append(cp.b).append("}");
                    pendingElementsToDefine.add(new Element("{" + cp.b + "}", false));
                }
                definidos.add(elementToDefine.getChomskyStr() + "->" + finalS);
            }
            elementToDefineIndex = getNextUndefinedElementIndex(pendingElementsToDefine);
        }
        setChomskyList(definidos);
    }
    
    private Map<String, List<String>> generateLeo(final String gic) {
        
    	final Map<String, List<String>> data = new HashMap<String, List<String>>();
    	final List<String> elements = new ArrayList<>();
        final List<Element> pendingElementsToDefine = new ArrayList<>();
        
        generateElementsInformationForGIC(gic, elements, pendingElementsToDefine);
        data.put("ELEMENTS", elements);
        
        int elementToDefineIndex = 0;
        final List<String> definidos = new ArrayList<>();
        
        while (!areElementsDefined(pendingElementsToDefine)) {
            
            final Element elementToDefine = pendingElementsToDefine.get(elementToDefineIndex);
            final String textBtwn = getStringBtwn(elementToDefine.getChomskyStr());
            elementToDefine.setDefined(true);
            
            if (isChomsky(getStringBtwn(elementToDefine.getChomskyStr()))) {
                definidos.add(elementToDefine.getChomskyStr() + "->" + textBtwn);
            } else {
            
            	final StringBuilder finalS = new StringBuilder();
            	final CasePart cp = fromString(textBtwn);
            	
                if (isTerminal(cp.a)) {
                    finalS.append(cp.a);
                } else {
                    finalS.append("{").append(cp.a).append("}");
                    pendingElementsToDefine.add(new Element("{" + cp.a + "}", false));
                }
            
                if (isTerminal(cp.b)) {
                    finalS.append(cp.b);
                } else {
                    finalS.append("{").append(cp.b).append("}");
                    pendingElementsToDefine.add(new Element("{" + cp.b + "}", false));
                }
                definidos.add(elementToDefine.getChomskyStr() + "->" + finalS);
            }
            elementToDefineIndex = getNextUndefinedElementIndex(pendingElementsToDefine);
        }
        data.put("DEFINED", definidos);
        
        return data;
    }
    
    private void setChomskyList(final List<String> definidos) {
        this.chomskyList = definidos;
    }
    
    public List<String> getProductions() {
        final List<String> productions = new ArrayList<>();
        for (String s : new LinkedHashSet<>(chomskyList)) {
                final Pattern p = Pattern.compile(FNCHConstants.REGEX);
                final Matcher m = p.matcher(s);
                while (m.find()) {
                    final String symbol = m.group(1);
                    if (!map.containsKey(symbol)) {
                        map.put(symbol, map.size() + 1);
                    } 
                    s = s.replace(symbol, nonTerminalLetter + map.get(symbol));
                }
                productions.add(s);
            }
        return productions;
    }
    
    public List<String> getProductionsLeo(final List<String> chomskyList, final Map<String, Integer> mapCount) {
        final List<String> productions = new ArrayList<>();
        for (String s : new LinkedHashSet<>(chomskyList)) {
                final Pattern p = Pattern.compile(FNCHConstants.REGEX);
                final Matcher m = p.matcher(s);
                while (m.find()) {
                    final String symbol = m.group(1);
                    if (!mapCount.containsKey(symbol)) {
                    	mapCount.put(symbol, mapCount.size() + 1);
                    } 
                    s = s.replace(symbol, nonTerminalLetter + mapCount.get(symbol));
                }
                productions.add(s);
            }
        return productions;
    }
    
    private boolean areElementsDefined(final List<Element> elements) {
        for (final Element element : elements) {
            if (!element.isDefined()) {
                return false;
            }
        }
        return true;
    }
    
    private int getNextUndefinedElementIndex(final List<Element> elements) {
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
    
    public List<Chomsky> generateChomskyLeo() {
    	final List<Chomsky> fnchs = new ArrayList<>();
        for (final String gic : gicList) {
        	final Map<String, List<String>> data = generateLeo(gic);
        	final Map<String, Integer> mapCount = new LinkedHashMap<>();
        	final Set<String> normalForms = new HashSet<>(Arrays.<String>asList(getFNCHLeo(gic, data.get("ELEMENTS"), mapCount)));
        	final List<String> myProductions = getProductionsLeo(data.get("DEFINED"), mapCount);
        	final Chomsky chomsky = new Chomsky(normalForms, myProductions);
        	fnchs.add(chomsky);
        }
        return fnchs;
    }
    
    private String getFNCH() {
        
    	String fnc = "";
    	int c = 0;
        
        for (String element : elements) {
            if (element.contains("{")) {
                if (map.containsKey(element)) {
                	element = nonTerminalLetter + map.get(element);
                } else {
                    map.put(element, map.size() + 1);
                    element = nonTerminalLetter + map.get(element);
                }
            }
            
            if (isLowerTerminal(element)) {
                fnc += element + "|";
                c = 0;
            } else {
                fnc += element;
                c++;
                if (c == 2) {
                    fnc += "|";
                    c = 0;
                }
            }
        }
        
        if (fnc.substring(fnc.length() - 1).equals("|")) {
            final String s = getProductionName(_gic) + " -> " + fnc.substring(0, fnc.length() - 1);
            return s;
        }
        
        return getProductionName(_gic) + " -> " + fnc;
    }
    
    private String getFNCHLeo(final String gic, final List<String> elements, Map<String, Integer> map) {
        
    	String fnc = "";
    	int c = 0;
        
        for (String element : elements) {
            if (element.contains("{")) {
                if (map.containsKey(element)) {
                	element = nonTerminalLetter + map.get(element);
                } else {
                    map.put(element, map.size() + 1);
                    element = nonTerminalLetter + map.get(element);
                }
            }
            
            if (isLowerTerminal(element)) {
                fnc += element + "|";
                c = 0;
            } else {
                fnc += element;
                c++;
                if (c == 2) {
                    fnc += "|";
                    c = 0;
                }
            }
        }
        
        if (fnc.substring(fnc.length() - 1).equals("|")) {
            final String s = getProductionName(gic) + " -> " + fnc.substring(0, fnc.length() - 1);
            return s;
        }
        
        return getProductionName(_gic) + " -> " + fnc;
    }
    
    private CasePart fromString(final String gicCase) {
		final int len = gicCase.length();
		return new CasePart(gicCase.substring(0, len / 2), gicCase.substring(len / 2, len));
	}
    
    private void generateElementsInformationForGIC(
    	    final String gic,
    	    final List<String> elements,
    	    final List<Element> pendingElementsToDefine) {

	    for (final String gicCase : getCases(gic).split("\\|")) {
	            
	        if (isChomsky(gicCase)) {
	            elements.add(gicCase);
	        } else {
	            
	            final CasePart cp = fromString(gicCase);
	            
	            if (isTerminal(cp.a)) {
	                elements.add(cp.a);
	            } else {
	                
	                final String nonTerminal = "{" + cp.a + "}";
	                elements.add(nonTerminal);
	                pendingElementsToDefine.add(new Element(nonTerminal, false));
	            }
	            
	            if (isTerminal(cp.b)) {
	                elements.add(cp.b);
	            } else {
	                
	                final String nonTerminal = "{" + cp.b + "}";
	                elements.add(nonTerminal);
	                pendingElementsToDefine.add(new Element(nonTerminal, false));
	            }
	        }
	    }

    }
    
}
