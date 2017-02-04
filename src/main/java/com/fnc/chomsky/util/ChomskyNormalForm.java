package com.fnc.chomsky.util;

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
		final List<Element> list = new ArrayList<>();
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
						addNonTerminalToData(cp.a, elements, elementDataMap, list);
					}
					
					if (isTerminal(cp.b)) {
						chomsky.append(cp.b);
						elements.add(cp.b);
					} else {
						chomsky.append("{").append(cp.b).append("}");
						addNonTerminalToData(cp.b, elements, elementDataMap, list);
					}
					
				}
				
			}
		}
		
		System.out.println(list);
		System.out.println(chomsky);
		
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
	
	private CasePart fromString(final String gicCase) {
		final int len = gicCase.length();
		return new CasePart(gicCase.substring(0, len / 2), gicCase.substring(len / 2, len));
	}
	
}
