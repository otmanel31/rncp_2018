package com.otmanel.bonjourBatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello spring batch!" );
        
        // on charge le contexte
        ApplicationContext ctx = new ClassPathXmlApplicationContext("aeroport-job-batch.xml");
        
        // gestionnaire / runer / executeur dune tache
        JobLauncher joblaucher = ctx.getBean("jobLauncher", JobLauncher.class);
        // job a executer
        Job job = ctx.getBean("myFirstJob", Job.class);
        
        // lancement et execution
        try {
        	// job paramater => give paramete supp => clause where par exemple pour l'etape de processing et recupereable ds la meth process
			JobExecution execution = joblaucher.run(job, new JobParameters());
			System.out.println("resutat = > " + execution.getStatus());
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("doneee ................");
    }
}
