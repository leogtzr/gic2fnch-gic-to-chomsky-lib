package com.fnc.chomsky.util;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.hamcrest.MatcherAssert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fnc.chomsky.bean.Chomsky;

public class ChomskyGeneratorTest {
	
	private static final String GIC = "S->ab|aaB";
	private static ChomskyGenerator generator;
	private static Chomsky chomsky;
	
	@BeforeClass
	public static void setUp() {
		generator = new ChomskyGenerator(GIC, "X");
		chomsky = generator.generate();
	}
	
	@Test
	public void shouldNotReturnNullChomskyObject() {
		assertNotNull(chomsky);
	}
	
	@Test
	public void shouldReturnTheRightNormalForms() {
		final List<String> normalForms = chomsky.normalForms();
		
		assertNotNull(normalForms);
		assertEquals("S -> X1X2|X1X3", normalForms.get(0));
	}
	
	@Test
	public void shouldReturnTheRightProductions() {
		final String[] productions = {"X1->a", "X2->b", "X3->X1B"};
		MatcherAssert.assertThat(chomsky.productions(), hasItems(productions));
	}
	
}
