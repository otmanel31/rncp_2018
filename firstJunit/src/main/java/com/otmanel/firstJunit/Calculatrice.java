package com.otmanel.firstJunit;

public class Calculatrice {
	
	public int additon(int x, int y) {
		return x + y;
	}
	
	public int multiplication(int x, int y) {
		return x * y;
	}
	
	public int division(int x, int y) {
		return x/y;
	}
	
	public double multiplication(double x, double y) {
		return  x * y;
	}
	
	//calcul qui prend du tps
	public double calculComplexEtLent(double x) {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return x * 2.0;
	}
	
}
