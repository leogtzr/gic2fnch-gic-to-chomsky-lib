package com.fnc.chomsky.util;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ChomskyGeneratorTest {
	
	private static final String GIC = "S->ab|aaB";
	private static final List<String> GICS = Arrays.<String>asList(GIC);
	private static ChomskyGenerator generator;
	private static List<ChomskyGenerator.Chomsky> chomskys;
	private static ChomskyGenerator.Chomsky chomsky;
	
	@BeforeClass
	public static void setUp() {
		generator = new ChomskyGenerator(GICS, "X");
		chomskys = generator.generate();
		chomsky = chomskys.get(0);
	}
	
	@Test
	public void shouldNotReturnEmptyListOfChomskyResults() {
		assertNotNull(chomskys);
		assertFalse(chomskys.isEmpty());
		assertThat(chomskys.size(), is(1));
	}
	
	@Test
	public void shouldNotReturnAnEmptyChomskyObjectFromList() {
		assertNotNull(chomskys);
		assertFalse(chomskys.isEmpty());
		assertThat(chomskys.size(), is(1));
	}
	
	@Test
	public void shouldReturnTheRightNormalForms() {
		final List<String> normalForms = chomsky.getNormalForms();
		
		assertNotNull(normalForms);
		assertEquals("S -> X1X2|X1X3", normalForms.get(0));
	}
	
	@Test
	public void shouldReturnTheRightProductions() {
		final String[] productions = {"X1->a", "X2->b", "X3->X1B"};
		MatcherAssert.assertThat(chomsky.getProductions(), hasItems(productions));
	}
	
}
