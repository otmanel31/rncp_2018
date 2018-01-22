package com.otmanel.firstJunit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class TextUtilTest {

	private TextUtils tu;
	
	
	@BeforeClass
	public static void beforeAllTest() {
		System.out.println("preparation inintiale pour text util test");
	}
	
	@Before
	public void beforeTest() {
		tu = new TextUtils();
	}
	
	@After
	public void afterTest() {
		tu = null;
	}
	
	@Category(FastTest.class)
	@Test
	public void testCapatizeNormal() {
		String expected = "Vincent";
		String actual = tu.capitalize("vincent");
		
		assertEquals("premier caract devrait etre en maj ", expected, actual);
	}
	
	@Category(FastTest.class)
	@Test
	public void testCapatizeVide() {
		String expected = "";
		String actual = tu.capitalize("");
		
		assertEquals("devrait etre une chaine vide ", expected, actual);
	}
	
	@Category(FastTest.class)
	@Test
	public void testCapatizeNull() {
		String actual = tu.capitalize(null);
		
		assertNull("la chaine devrait eytre null si null en entre", actual);
	}
	
	@Test
	public void testInverseNormal() {
		String expected = "ruojnob";
		String actual = tu.inverse("bonjour");
		
		assertEquals("bonjour devrait etre inversé ", expected, actual);
	}
}
