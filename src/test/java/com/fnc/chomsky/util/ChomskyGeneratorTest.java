package com.fnc.chomsky.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.hamcrest.CoreMatchers.*;

import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

import org.hamcrest.MatcherAssert;

public class ChomskyGeneratorTest {
	
	private static final String GIC = "S->ab|aaB";
	
	private static ChomskyGenerator chomsky = null; 
	
	@BeforeClass
	public static void setUp() {
		chomsky = new ChomskyGenerator(Arrays.<String>asList(GIC), "X");
		chomsky.generateChomsky();
	}
	
	@Test
	public void shouldReturnNonEmptyNormalForms() {
		assertFalse(chomsky.getNormalForms().isEmpty());
	}
	
	@Test
	public void shouldReturnNormalForm1() {
		for (final String normalForm : chomsky.getNormalForms()) {
			assertEquals("S -> X1X2|X1X3", normalForm);
		}
	}
	
	@Test
	public void shouldReturnNormalForm2() {
		final ChomskyGenerator chomskyFixture = new ChomskyGenerator(Arrays.<String>asList("S->ab"), ".");
		chomskyFixture.generateChomsky();
		for (final String normalForm : chomskyFixture.getNormalForms()) {
			assertEquals("S -> .1.2", normalForm);
		}
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
