package com.otmanel.firstJunit;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CalculatriceTest {

	
	// classe objet a tester en isolation
	private Calculatrice c;
	
	@BeforeClass
	public static void beforeAllTest() {
		System.out.println("preparation inintiale pour calculatrice test");
	}
	
	@Before // cette methode sera appelé avant tt execution de test individuelle - elle sert a preparer lenv de test pour un test individuelle
	public void beforeTest() {
		System.out.println("preparation avant un test");
		c = new Calculatrice();
	}
	
	@After // idem before sauf que apres - permet de netttoyer len de test apres exec dun test indivdueml
	public void afterTest() {
		System.out.println("netoyage apres un test");
		c = null;
	}
	
	// meth annoter avc @test est un test junit 4.x. 
	// ce test echouera si une des assertion est declenché 
	// une assertion qui echoue levera une exception specifqie ,qui sera attrapé par le framework junit
	// les assertion sont fourni par junit - il existe des libs etandant celle-ci
	@Test
	public void testAddition() {
		int expected = 36;
		int actual = c.additon(10, 26);
		assertEquals("10+26 devrait donner 36", expected, actual);
		actual = c.additon(10, 32);
		expected = 42;
		assertEquals("10+26 devrait donner 36", expected, actual);
	}
	
	@Test
	public void testDivison() {
		int expected = 5;
		int actual = c.division(10, 2);
		
		assertEquals("10 / 2 devrait donner 5",expected, actual);
	}
	
	@Test
	public void testDivisonArrondi() {
		int expected = 3;
		int actual = c.division(10, 3);
		
		assertEquals("10 / 3 devrait donner 3",expected, actual);
	}
	
	// ce test reussi si une aritmetic exception est bien declenché
	@Test(expected=ArithmeticException.class)
	public void testDivisonParZero() {
		int actual = c.division(10, 0);
	}
	
	@Test
	public void testMultiplicationDouble() {
		double expected = 3500000.0;
		double actual = c.multiplication(1000, 3500);
		
		//assertEquals(expected, actual); // deprecc car erreur arrondi pour les doubles
		assertEquals(expected, actual, 0.0001); // delta == marg d erreur
	}
	
	// si ce test ne finit en 1/2 seconde il echoue
	@Test(timeout=500)
	public void testCalculLent() {
		double expected = 2.0;
		double actual = c.calculComplexEtLent(1.0);
		assertEquals("calcul devrait etre egal a 2, ", expected, actual, 0.0000001);

	}
}
