/** @author Leonardo Gutiérrez Ramírez <leogutierrezramirez.gmail.com> 
 **/

package com.fnc.chomsky.util;

import static com.fnc.chomsky.util.Tools.getCases;
import static com.fnc.chomsky.util.Tools.getProductionName;
import static com.fnc.chomsky.util.Tools.getStringBtwn;
import static com.fnc.chomsky.util.Tools.isChomsky;
import static com.fnc.chomsky.util.Tools.isLowerTerminal;
import static com.fnc.chomsky.util.Tools.isTerminal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fnc.chomsky.bean.Chomsky;
import com.fnc.chomsky.bean.Element;

public class ChomskyGenerator {
    
    private final String gic;
    private final String nonTerminalLetter;
    
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
    
    public ChomskyGenerator(final String gic, final String nonTerminalLetter) {
        this.gic = gic;
        this.nonTerminalLetter = nonTerminalLetter;
    }
    
    private Map<String, List<String>> create(final String gic) {
        
    	final Map<String, List<String>> data = new HashMap<String, List<String>>();
    	final List<String> elements = new ArrayList<>();
        final List<Element> pendingElementsToDefine = new ArrayList<>();
        
        generateElementsInformationForGIC(gic, elements, pendingElementsToDefine);
        data.put(FNCHConstants.ELEMENTS, elements);
        
        int elementToDefineIndex = 0;
        final List<String> defined = new ArrayList<>();
        
        while (!areElementsDefined(pendingElementsToDefine)) {
            
            final Element elementToDefine = pendingElementsToDefine.get(elementToDefineIndex);
            final String textBtwn = getStringBtwn(elementToDefine.getChomskyStr());
            elementToDefine.setDefined(true);
            
            if (isChomsky(getStringBtwn(elementToDefine.getChomskyStr()))) {
                defined.add(elementToDefine.getChomskyStr() + "->" + textBtwn);
            } else {
            
            	final StringBuilder productionRule = new StringBuilder();
            	final CasePart cp = fromString(textBtwn);
            	
                if (isTerminal(cp.a)) {
                    productionRule.append(cp.a);
                } else {
                    productionRule.append("{").append(cp.a).append("}");
                    pendingElementsToDefine.add(new Element("{" + cp.a + "}", false));
                }
            
                if (isTerminal(cp.b)) {
                    productionRule.append(cp.b);
                } else {
                    productionRule.append("{").append(cp.b).append("}");
                    pendingElementsToDefine.add(new Element("{" + cp.b + "}", false));
                }
                defined.add(elementToDefine.getChomskyStr() + "->" + productionRule);
            }
            elementToDefineIndex = nextUndefinedElementIndex(pendingElementsToDefine);
        }
        data.put(FNCHConstants.DEFINED, defined);
        
        return data;
    }
    
    private List<String> getProductions(final List<String> chomskyList, final Map<String, Integer> mapCount) {
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
    
    private int nextUndefinedElementIndex(final List<Element> elements) {
    	int index = 0;
        for (final Element element : elements) {
            if (!element.isDefined()) {
                return index;
            }
            index++;
        }
        return index;
    }
    
    public Chomsky generate() {
    	final Map<String, List<String>> data = create(gic);
    	final Map<String, Integer> mapCount = new LinkedHashMap<>();
    	final Set<String> normalForms = new HashSet<>(Arrays.<String>asList(getFNCH(gic, data.get(FNCHConstants.ELEMENTS), mapCount)));
    	final List<String> productions = getProductions(data.get(FNCHConstants.DEFINED), mapCount);
    	return new Chomsky(normalForms, productions);
    }
    
    private String getFNCH(final String gic, final List<String> elements, Map<String, Integer> map) {
        
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
        
        return getProductionName(gic) + " -> " + fnc;
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
