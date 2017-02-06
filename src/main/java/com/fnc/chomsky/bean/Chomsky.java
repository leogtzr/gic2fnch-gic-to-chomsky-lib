package com.fnc.chomsky.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Chomsky {
	
	private final Set<String> normalForms;
	private final List<String> productions;
	
	public Chomsky(final Set<String> normalForms, final List<String> productions) {
		this.normalForms = normalForms;
		this.productions = productions;
	}

	public List<String> getNormalForms() {
		return Collections.<String>unmodifiableList(new ArrayList<String>(normalForms));
	}

	public List<String> getProductions() {
		return Collections.<String>unmodifiableList(productions);
	}

	@Override
	public String toString() {
		return "Chomsky [normalForms=" + normalForms + ", productions=" + productions + "]";
	}
	
}
