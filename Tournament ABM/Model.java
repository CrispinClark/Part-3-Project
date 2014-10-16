import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class Model 
{
	private ArrayList<Agent> population;
	
	public Model(int numberOfAgents)
	{
		population = new ArrayList<Agent>(numberOfAgents);
		
		for (int i = 0; i < numberOfAgents; i++)
		{
			population.add(new Agent(i));
		}
	}
	
	public static void main(String[] args)
	{
		Model model = new Model(Integer.parseInt(args[0]));
		
		model.battle();
	}

	public void battle()
	{
		try
		{
			File file = new File("C:/Users/Crispin/Desktop/csvFile.csv");
			
			FileWriter writer = new FileWriter(file);
			
			writer.append("Number of tournaments, Amount of maxima\n");
			
			int tournaments = 0;
			int maxima = population.size()-1;
			int noOfMaxima = 1;
			
			Random r = new Random();
			
			while (!checkAbsorption())
			{
				tournaments++;
				
				int home = r.nextInt(population.size());
				int away;
				
				do 
					away = r.nextInt(population.size());
				while (home == away);
				
				Agent homeAgent = population.get(home);
				Agent awayAgent = population.get(away);
				
				System.out.println("Home = " + home + " Away = " + away);
			
				if (homeAgent.getScore() > awayAgent.getScore())
				{
					awayAgent.setScore(homeAgent.getScore());
					
					if (homeAgent.getScore() == maxima)
						noOfMaxima++;

					for (int i = 0; i < population.size(); i++)
						System.out.print(population.get(i).getScore()+ " ");
					
					System.out.println("");
				}
				
				else if (homeAgent.getScore() < awayAgent.getScore())
				{
					homeAgent.setScore(awayAgent.getScore());
					if (awayAgent.getScore() == maxima)
						noOfMaxima++;
					
					for (int i = 0; i < population.size(); i++)
						System.out.print(population.get(i).getScore()+ " ");
					
					System.out.println("");
				}
				
				else
				{ 
					System.out.println("Numbers are equal!");
				}
				System.out.println("No of maxima = " + noOfMaxima);
				
				writer.append(tournaments+","+noOfMaxima+"\n");
			}
			
			writer.flush();
			writer.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
		
	}
	
	public boolean checkAbsorption()
	{
		int first = population.get(0).getScore();
		
		for (int i = 1; i <population.size(); i ++)
		{
			if (population.get(i).getScore() != first)
				return false;
		}
		
		return true;
	}
}
