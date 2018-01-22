package com.otmanel.firstJunit;

import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
        Calculatrice c = new Calculatrice();
        
        System.out.println(c.additon(10, 15));
        System.out.println(c.division(10, 15));
        System.out.println(c.multiplication(10, 15));
        
        //System.out.println(c.division(10, 0));
        
        TextUtils tu = new TextUtils();
        
        System.out.println(tu.capitalize("vincent"));
        System.out.println(tu.inverse("bonjour"));
        
        Scanner input = new Scanner(System.in);
        System.out.println("cahine a capitalizer");
        String chaine = input.nextLine();
        System.out.println(tu.capitalize(chaine));
        chaine = null;
        System.out.println(tu.capitalize(chaine));
        
        
    }
}
