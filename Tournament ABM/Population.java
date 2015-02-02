import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class Population 
{
	private ArrayList<Agent> population;
	private Random random;
	
	public Population(int numberOfAgents)
	{
		population = new ArrayList<Agent>(numberOfAgents);
		
		for (int i = 0; i < numberOfAgents; i++)
		{
			population.add(new Agent(10));
		}
		
		random = new Random();
	}
	
	public void runBattles()
	{
		try
		{
			File file = new File("C:/Users/Crispin/Documents/3200 - Individual Project"
					+ "/Basic ABM Workspace/Graph files/potFillingComplexity.csv");
			
			FileWriter writer = new FileWriter(file);
			
			writer.append("Size of Population, Number of Tournaments\n");
			
			for (int i = 500; i <= 10000; i += 500)
			{
				System.out.println(i);
				Population model = new Population(i);
				
				int noOfTournaments = model.battle();
				
				writer.append(i+","+noOfTournaments+"\n");
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

	public int battle()
	{
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
		}

		return tournaments;
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
	
	public void randomlyFillPots(int sizeOfPot, int noOfRounds)
	{
		try
		{
			File file = new File("C:/Users/Crispin/Documents/3200 - Individual Project"
					+ "/Basic ABM Workspace/Graph files/fillingPotsAtRandom.csv");
			
			FileWriter writer = new FileWriter(file);
		
			System.out.println(population.size());
			
			int noOfPots = population.size()/sizeOfPot;
			System.out.println(noOfPots);
			
			//create the different pots
			ArrayList<ArrayList<Agent>> subPopulations = new ArrayList<>(noOfPots);
			ArrayList<Integer> pots = new ArrayList<>(noOfPots);
			
			System.out.println(subPopulations.size());
			System.out.println(pots.size());
			
			for (int i = 0; i < noOfPots; i++)
			{
				subPopulations.add(new ArrayList<Agent>(sizeOfPot));
				pots.add(0);
			}
			
			//fill the different pots
			for (int i = 0; i < population.size(); i = i + sizeOfPot)
			{
				int potNumber = i / sizeOfPot;
				
				for (int x = 0; x < sizeOfPot; x++)
				{
					subPopulations.get(potNumber).add(population.get(i + x));
				}
			}
			
			for (int round = 0; round < noOfRounds; round++)
			{
				//run the contributions, each round each agent puts in a random amount
				//to the pot they are in
				for (int currentPot = 0; currentPot < noOfPots; currentPot++)
				{
					ArrayList<Agent> currentPopulation = subPopulations.get(currentPot);
					for (Agent agent : currentPopulation)
					{
						int currentCash = agent.getScore();
						int contribution = random.nextInt(currentCash + 1);
						
						int potSize = pots.get(currentPot);
						
						potSize += contribution;
						
						pots.set(currentPot, potSize);
						
						agent.setScore(currentCash - contribution);
					}
					
					writer.append(pots.get(currentPot) + ", ");
				}
				
				writer.append("\n");
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
}
