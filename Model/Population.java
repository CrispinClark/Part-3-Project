/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Model.Agent.Strategy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author Crispin
 */
public class Population 
{
    final private ArrayList<Agent> agents;
    final private int populationSize;    

    private Agent.Strategy winningStrategy;
    
    private int iteration = 0;
    
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
            int strategyNumber = rand.nextInt(4);
            
            Agent agent;
            
            if (strategyNumber == 0)
                agent = new Agent(Agent.Strategy.ALWAYS_COOPERATE);
            else if (strategyNumber == 1)
                agent = new Agent(Agent.Strategy.ALWAYS_DEFECT);
            else if (strategyNumber == 2)
                agent = new Agent(Agent.Strategy.TIT_FOR_TAT_PERSONAL);
            else 
                agent = new Agent(Agent.Strategy.ALTERNATE);
            
            agents.add(agent);
        }
    }
    
    public void runSimulation()
    {        
        while (!testConvergence())
        {
            iteration++;
            
            shufflePopulation();
        
            for (int i = 0; i < agents.size()/2; i++)
            {
                Agent agent1 = agents.get(2*i);
                Agent agent2 = agents.get((2*i) + 1);
                
                try
                {
                    TwoAgentGame game = new TwoAgentGame(agent1, agent2);
                    game.play();
                }
                catch (Exception e)
                {
                    System.err.println(e.getMessage());
                }
            }
            
            //printState();
            
            if (iteration % 100 == 0)
            {
                //evolveByTournament();
                evolveByTrim();
                //evolveByPlayoff();
                
                printState();
            }
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
        winningStrategy = agents.get(0).getStrategy();
        
        for (int i = 1; i < agents.size(); i++)
        {
            if (agents.get(i).getStrategy() != winningStrategy)
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
            agents.get(i).setStrategy(
                    agents.get(populationSize - 1 - i).getStrategy());
        }
        
        for (Agent a : agents)
        {
            a.resetScore();
            //a.clearVendettas();
        }
    }
    
    public void evolveByPlayoff()
    {
        Random r = new Random();
        
        Agent agent1 = agents.get(r.nextInt(populationSize));
        Agent agent2 = agents.get(r.nextInt(populationSize));
        
        if (agent1.getScore() > agent2.getScore())
        {
            agent2.setStrategy(agent1.getStrategy());
        }
        else if (agent2.getScore() > agent1.getScore())
        {
            agent1.setStrategy(agent2.getStrategy());
        }
        
        printState();
        
        for (Agent a : agents)
        {
            a.resetScore();
            //a.clearVendettas();
        }
    }
    
    public void evolveByTournament()
    {
        shufflePopulation();
        
        for (int i = 0; i < agents.size()/2; i++)
        {
            Agent agent1 = agents.get(2*i);
            Agent agent2 = agents.get((2*i) + 1);
                
            if (agent1.getScore() > agent2.getScore())
            {
                agent2.setStrategy(agent1.getStrategy());
            }
            else if (agent2.getScore() > agent1.getScore())
            {
                agent1.setStrategy(agent2.getStrategy());
            }
        }
        
        printState();
        
        for (Agent a : agents)
        {
            a.resetScore();
            //a.clearVendettas();
        }
    }
    
    private void printState()
    {
        ArrayList<Agent> cooperators = new ArrayList<>();
        ArrayList<Agent> defectors = new ArrayList<>();
        ArrayList<Agent> t4tps = new ArrayList<>();
        ArrayList<Agent> alternators = new ArrayList<>();
        
        for (int i = 0; i < populationSize; i++)
        {
            Agent agent = agents.get(i);
            Strategy strategy = agent.getStrategy();
            
            if (strategy == Strategy.ALWAYS_COOPERATE)  
            {
                cooperators.add(agent);
            }
            else if (strategy == Strategy.ALWAYS_DEFECT)
            {
                defectors.add(agent);
            }
            else if (strategy == Strategy.TIT_FOR_TAT_PERSONAL)
            {
                t4tps.add(agent);
            }
            else if (strategy == Strategy.ALTERNATE)
            {
                alternators.add(agent);
            }
        }
        
        System.out.println("Round " + iteration + ":");
        
        System.out.println("ALWAYS_COOPERATE = " + cooperators.size());
        /*for (Agent cooperator : cooperators) {
            System.out.print(cooperator.getScore() + " ");
        }
        System.out.println("");*/
        System.out.println("ALWAYS_DEFECT = " + defectors.size());
        /*for (Agent defector : defectors) {
            System.out.print(defector.getScore() + " ");
        }
        System.out.println("");*/
        System.out.println("TIT_FOR_TAT_PERSONAL = " + t4tps.size());
        /*for (Agent t4tp : t4tps) {
            System.out.print(t4tp.getScore() + " ");
        }
        System.out.println("");
        */
        System.out.println("ALTERNATE = " + alternators.size());
        /*for (Agent alternator : alternators) {
            System.out.print(alternator.getScore() + " ");
        }*/
        System.out.println("");
    }
}
