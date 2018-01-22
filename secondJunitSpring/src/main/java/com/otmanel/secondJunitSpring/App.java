package com.otmanel.secondJunitSpring;

import java.util.List;
import java.util.Scanner;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.otmanel.secondJunitSpring.metier.Gazouille;
import com.otmanel.secondJunitSpring.services.GazouilleService;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	// chargement du context spring
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        
        Scanner input = new Scanner(System.in);
        System.out.println("-----------------------------------------------------------------");
        
        GazouilleService gs = ctx.getBean(GazouilleService.class);
        
        List<Gazouille> gss = gs.readAllGazouile();
        for (Gazouille g : gss)
        	System.out.println(g);
       System.out.println("titre novelle gazouille");
       String titre = input.nextLine();
       System.out.println("corps novelle gazouille");
       String cors = input.nextLine();
       
       gs.publish(new Gazouille(0, titre, cors));
    }
}
