/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gametheorysimulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author Crispin
 */
public class Population 
{
    private ArrayList<Agent> agents;
    private int populationSize;    
    
    private boolean allCooperate;
    
    public Population(int populationSize)
    {
        agents = new ArrayList<>(populationSize);
        this.populationSize = populationSize;
    }
    
    public void randomlyFillPopulation()
    {
        Random rand = new Random();
        
        for (int i = 0; i < populationSize; i++)
        {
            agents.add(new Agent(rand.nextBoolean()));
        }
    }
    
    public void runSimulation()
    {        
        while (!testConvergence())
        {
            shufflePopulation();
        
            for (int i = 0; i < agents.size()/2; i++)
            {
                Agent agent1 = agents.get(2*i);
                Agent agent2 = agents.get((2*i) + 1);
                
                TwoAgentGame game = new TwoAgentGame(agent1, agent2);
                game.play();
            }
            
            printState();
            
            evolveByTournament();
            //evolveByTrim();
        }
        
        printState();    
    }
    
    //Implementation of Fisher-Yates shuffle
    public void shufflePopulation()
    {
        Random rand = new Random();
        for (int i = agents.size() - 1; i > 0; i--)
        {
            int index = rand.nextInt(i + 1);
            // Simple swap
            Agent a = agents.get(index);
            agents.set(index, agents.get(i));
            agents.set(i, a);
        }
    }
    
    /**
     * Returns true if all the agents are performing in the same way
     * @return convergence of decisions of the population
     */
    private boolean testConvergence()
    {
        allCooperate = agents.get(0).isCooperator();
        
        for (int i = 1; i < agents.size(); i++)
        {
            if (agents.get(i).isCooperator() != allCooperate)
                return false;
        }
        
        return true;
    }
    
    public void evolveByTrim()
    {
        Collections.sort(agents);
        
        int trimSize = populationSize/10;
        
        for (int i = 0; i < trimSize; i++)
        {
            agents.get(i).setIsCooperator(
                    agents.get(populationSize - 1 - i).isCooperator());
        }
    }
    
    public void evolveByTournament()
    {
        shufflePopulation();
        
        for (int i = 0; i < agents.size()/2; i++)
        {
            Agent agent1 = agents.get(2*i);
            Agent agent2 = agents.get((2*i) + 1);
                
            if (agent1.getPayoff() > agent2.getPayoff())
                agent2.setIsCooperator(agent1.isCooperator());
            else if (agent2.getPayoff() > agent1.getPayoff())
                agent1.setIsCooperator(agent2.isCooperator());
        }
    }
    
    private void printState()
    {
        int cooperators = 0;
        int defectors = 0;
        
        for (int i = 0; i < populationSize; i++)
        {
            if (agents.get(i).isCooperator())
                cooperators ++;
            else
                defectors ++;
        }
        
        System.out.println("Cooperators = " + cooperators +
                ". Defectors = " + defectors);
    }
}
