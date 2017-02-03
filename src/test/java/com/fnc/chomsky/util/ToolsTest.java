package com.fnc.chomsky.util;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

public class ToolsTest {

	@Test
	public void shouldIdentifyProduction() {
		assertTrue(Tools.isProduction("A->B"));
	}
	
	@Test
	public void shouldRetrieveProductionName() {
		assertEquals("A", Tools.getProductionName("A->B"));
	}
	
	@Test
	public void shouldReturnCases() {
		assertEquals("B", Tools.getCases("A->B"));
	}
	
	@Test
	public void shouldValidateChomskyCase() {
		assertTrue(Tools.isChomsky("a"));
		assertTrue(Tools.isChomsky("AB"));
	}
	
	@Test
	public void shouldValidateTerminal() {
		assertTrue(Tools.isTerminal("B"));
	}
	
	@Test
	public void shouldReturnStringBetweenSquares() {
		assertThat(Tools.getStringBtwn("{hola}"), is("hola"));
		assertNull(Tools.getStringBtwn("hola"));
		assertNull(Tools.getStringBtwn("{"));
	}
	
	@Test
	public void shouldValidateLowerTerminal() {
		assertTrue(Tools.isLowerTerminal("a"));
	}
	
}
