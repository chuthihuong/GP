
package SiS;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import Common.MyRandom;

public class MainSiS extends  MyRandom {
	public MainSiS() {}

	int									i, pos;
	double								min;
	long								StartSeed		= 2000;
	Population							myPop;
	int nrun=NRUN;
	long Seed=0;
	
	double average=0;
	double fittest;
	double averagetest=0;
	double averagesize=0;
	

	String outDir = OUTDIR + "SiS/";
	public void MStart()
	{
		
		PrintStream f1 = null;
		PrintStream f2 = null;
		PrintStream f3 = null;
		PrintStream console = System.out;
		try 
		{
			FileOutputStream fos2 = new FileOutputStream(new File(outDir + PROBLEM + ".run.txt"), true);
			f2 = new PrintStream(fos2);
			
			FileOutputStream fos3 = new FileOutputStream(new File(outDir + PROBLEM + ".SizeAll.txt"), true);
			f3 = new PrintStream(fos3);			
			
			
		} 
		catch(Exception e) 
		{
			System.err.println("Output files error!");
	      
		}
		
		
		int run = 0;
		Seed=StartSeed+run;
		
		double[] fittests = new double[NRUN];	
		while (Seed<StartSeed+nrun)
		{
			
			System.err.println("Run: " + Seed);

			Seed++;
			
			
			long starttime = System.currentTimeMillis();
			
			myPop = new Population(Seed);
			myPop.EvolutionWithElitism();

			starttime = System.currentTimeMillis() - starttime;

			
			min = myPop.bestcurrent[0].fitness;
			
			pos = 0;
			
			// print results to file
			try 
			{			
				FileOutputStream fos1 = new FileOutputStream(new File(outDir +PROBLEM+"/"+PROBLEM+"_"+run + ".gen.txt"), true);
				f1 = new PrintStream(fos1);						
			} 
			catch(Exception e) 
			{
				System.err.println("Output files"+outDir + PROBLEM + ".all.txt" +"error!");
		      
			}
			
			f1.print("0;");
			f1.print(myPop.bestcurrent[0].fitness+";");
			f1.print(myPop.ComputeFT(myPop.bestcurrent[0])+";");
			f1.print(myPop.bestcurrent[0].size+";");			
			f1.print(myPop.averageSize[0]+";");
			f1.println(myPop.semanticDistance[0]);
			
			
			for(i = 1; i < NUMGEN; i++)
			{
				f1.print(i+";");
				f1.print(myPop.bestcurrent[i].fitness+";");
				f1.print(myPop.ComputeFT(myPop.bestcurrent[i])+";");
				f1.print(myPop.bestcurrent[i].size+";");			
				f1.print(myPop.averageSize[i]+";");
				f1.println(myPop.semanticDistance[i]);
				
				if(myPop.bestcurrent[i].fitness < min) 
				{
					min = myPop.bestcurrent[i].fitness;
					pos = i;
			     }
				
				
		     }			
			
			for(int g = 0; g < NUMGEN; g++){
				for(i=0;i<POPSIZE;i++){
					f3.println(g+","+myPop.popSize[g][i]);
				}
				
			}
			
			
			average+=myPop.bestcurrent[pos].fitness;
			fittest=myPop.ComputeFT(myPop.bestcurrent[pos]);
			fittests[run] = fittest;
			
			//averagetest+=fittest;
			averagesize+=myPop.bestcurrent[pos].size;
			
			
			f2.println(myPop.bestcurrent[pos].fitness + " " + fittest 
					+ " " + myPop.bestcurrent[pos].size + " " + starttime + "\n");
			
			run += 1;
	   }	
		
		f1.close();
		f2.close();
		f3.close();		
		System.setOut(console);
						
}

public static void main(String[] args)
{
		MainSiS a = new MainSiS();	
		System.out.printf("Main SiS Start: "+ PROBLEM + "\n");
		a.MStart();
		System.out.printf("Finish!");
		
	}
}
