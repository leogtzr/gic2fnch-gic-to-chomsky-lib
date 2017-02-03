package com.fnc.chomsky.util;

import static org.junit.Assert.assertFalse;
import static org.hamcrest.CoreMatchers.*;

import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

import org.hamcrest.MatcherAssert;

public class ChomskyTest {
	
	private static final String GIC = "S->ab|aaB";
	
	private static Chomsky chomsky = null; 
	
	@BeforeClass
	public static void setUp() {
		chomsky = new Chomsky(Arrays.<String>asList(GIC), "X");
		chomsky.generateChomsky();
	}
	
	@Test
	public void shouldReturnNonEmptyNormalForms() {
		assertFalse(chomsky.getNormalForms().isEmpty());
	}
	
	@Test
	public void shouldReturnNonEmptyProductions() {
		assertFalse(chomsky.getProductions().isEmpty());
	}
	
	@Test
	public void shouldReturnTheRightProductions() {
		final String[] productions = {"X1->a", "X2->b", "X3->X1B"};
		MatcherAssert.assertThat(chomsky.getProductions(), hasItems(productions));
	}
	
}
