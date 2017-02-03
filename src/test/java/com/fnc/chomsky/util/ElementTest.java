package com.fnc.chomsky.util;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

public class ElementTest {
	
	@Test
	public void shouldSetChomskyStr() {
		final String chomskyStr = "A->G";
		final Element element = new Element(chomskyStr, false);
		assertThat(element.getChomskyStr(), is(chomskyStr));
	}
	
	@Test
	public void shouldSetDefinedAttributeForElement() {
		final String chomskyStr = "A->G";
		final Element element = new Element(chomskyStr, false);
		assertThat(element.isDefined(), is(false));
		
		element.setDefined(true);
		assertThat(element.isDefined(), is(true));
	}
	
}
