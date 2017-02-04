package com.fnc.chomsky.util;

import static com.fnc.chomsky.util.Tools.getStringBtwn;
import static com.fnc.chomsky.util.Tools.isChomsky;
import static com.fnc.chomsky.util.Tools.isTerminal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChomskyNormalForm {
	
	private final List<String> gics;
	private final char nonTerminalLetter;
	
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
	
	public ChomskyNormalForm(final List<String> gics, final char nonTerminalLetter) {
		this.gics = gics;
		this.nonTerminalLetter = nonTerminalLetter;
	}
	
	// TODO change the return type.
	public void create() {
		
		final List<String> elements = new ArrayList<>();
		final List<Element> undefinedElements = new ArrayList<>();
		final Map<String, Element> elementDataMap = new HashMap<>();
		final StringBuilder chomsky = new StringBuilder();
		
		for (final String gic : gics) {
			
			for (String gicCase : Tools.getCases(gic).split("\\|")) {
				
				gicCase = gicCase.trim();
				if (isChomsky(gicCase)) {
					// TODO Add to the StringBuilder.
					chomsky.append(gicCase).append("|");
					elements.add(gicCase);
				} else {
					
					final CasePart cp = fromString(gicCase);
					
					if (isTerminal(cp.a)) {
						chomsky.append(cp.a);
						elements.add(cp.a);
					} else {
						chomsky.append("{").append(cp.a).append("}");
						addNonTerminalToData(cp.a, elements, elementDataMap, undefinedElements);
					}
					
					if (isTerminal(cp.b)) {
						chomsky.append(cp.b).append("|");
						elements.add(cp.b);
					} else {
						chomsky.append("{").append(cp.b).append("}");
						addNonTerminalToData(cp.b, elements, elementDataMap, undefinedElements);
					}
					
				}
			}
			
		}
		
		clearChomskyText(chomsky);

		final List<String> definedElements = new ArrayList<>();
		
		int elementToDefineIndex = 0;
		
		final StringBuilder chomskyResult = new StringBuilder();
		while (!areElementsDefined(undefinedElements)) {
			chomskyResult.delete(0, chomskyResult.length());
			
			final Element elementToDefine = undefinedElements.get(elementToDefineIndex);
            final String textBtwn = getStringBtwn(elementToDefine.getChomskyStr());
            
            if (isChomsky(getStringBtwn(elementToDefine.getChomskyStr()))) {
            	// System.out.println(String.format("Value: '%s'", elementToDefine.getChomskyStr() + "->" + textBtwn));
            	definedElements.add(elementToDefine.getChomskyStr() + "->" + textBtwn);
            } else {
            	final CasePart cp = fromString(textBtwn);
            	
            	if (isTerminal(cp.a)) {
            		chomskyResult.append(cp.a);
            	} else {
            		chomskyResult.append("{").append(cp.a).append("}");
                    undefinedElements.add(new Element("{" + cp.a + "}", false));
            	}
            	
            	if (isTerminal(cp.b)) {
            		chomskyResult.append(cp.b);
            	} else {
            		chomskyResult.append("{").append(cp.b).append("}");
                    undefinedElements.add(new Element("{" + cp.b + "}", false));
            	}
            	definedElements.add(elementToDefine.getChomskyStr() + "->" + chomskyResult);
            }
            
            elementToDefineIndex = getUndefinedElementIndex(undefinedElements);
		}
		
	}
	
	private static int getUndefinedElementIndex(final List<Element> elements) {
    	int index = 0;
        for (final Element element : elements) {
            if (!element.isDefined()) {
                return index;
            }
            index++;
        }
        return index;
    }
	
	private void clearChomskyText(final StringBuilder chomsky) {
		final String temp = chomsky.substring(0, chomsky.length());
        chomsky.delete(0, chomsky.length());
        chomsky.append(temp);
	}
	
	private void addNonTerminalToData(
			final String casePart,
			final List<String> elements,
			final Map<String, Element> elementDataMap,
			final List<Element> list
			) {
		
		final String nonTerminal = "{" + casePart + "}";
		elements.add(nonTerminal);
		elementDataMap.put(nonTerminal, new Element(nonTerminal, false));
		list.add(new Element(nonTerminal, false));
		
	}
	
	private static boolean areElementsDefined(final List<Element> elements) {
        for (final Element element : elements) {
            if (!element.isDefined()) {
                return false;
            }
        }
        return true;
    }
	
	private CasePart fromString(final String gicCase) {
		final int len = gicCase.length();
		return new CasePart(gicCase.substring(0, len / 2), gicCase.substring(len / 2, len));
	}
	
}
