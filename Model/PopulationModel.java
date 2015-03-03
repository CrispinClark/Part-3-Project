/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Control.Controller;
import Model.Agent.Strategy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author Crispin
 */
public class PopulationModel 
{
    final private Controller control;
    
    private int populationSize = 100;
    private int noOfGames = 10;

    private int temptation = 5;
    private int reward = 3;
    private int punishment = 2;
    private int sucker = 1;
    
    final private ArrayList<Agent> agents;
    
    public enum EvoType{TRIM, TOURNAMENT, PLAYOFF}
    private EvoType evoType = EvoType.TRIM;
    
    //private Boolean clearVendettas, clearScores = false;
    
    final private ArrayList<Agent.Strategy> appliedStrategies;
    final private HashMap<Agent.Strategy, ArrayList<Integer>> strategyLevels;
    private Agent.Strategy winningStrategy;
    private int graphLevel = 0;
    
    private int iteration = 0;
    private long startTime;
    
    public PopulationModel(Controller control)
    {
        this.control = control;
        
        agents = new ArrayList<>(getPopulationSize());
      
        appliedStrategies = new ArrayList<>();
        strategyLevels = new HashMap<>();
    }
    
    public void randomlyFillPopulation()
    {
        int noOfStrategies = appliedStrategies.size();
        Random rand = new Random();
        
        for (int i = 0; i < getPopulationSize(); i++)
        {
            int strategyNumber = rand.nextInt(noOfStrategies);
            
            Agent agent = new Agent(appliedStrategies.get(strategyNumber));
            
            agents.add(agent);
        }
        
        for (Agent.Strategy s : appliedStrategies)
        {
            strategyLevels.put(s, new ArrayList<>());
        }
    }
    
    public void runSimulation()
    {        
        startTime = System.currentTimeMillis();
        randomlyFillPopulation();
        System.out.println("Time taken to fill population = " 
                + (System.currentTimeMillis() - startTime)/1000000);        
        startTime = System.currentTimeMillis();
        
        addGraphData();
        
        while (!testConvergence())
        {
            /*System.out.println("Time taken to test convergence = " 
                + (System.currentTimeMillis() - startTime)/1000000);        
            startTime = System.currentTimeMillis();
            */
            iteration++;
            //System.out.println("Iteration " + iteration); 
            
            shufflePopulation();
            /*System.out.println("Time taken to shuffle population = " 
                + (System.currentTimeMillis() - startTime)/1000000);        
            startTime = System.currentTimeMillis();
            */
            
            for (int i = 0; i < agents.size()/2; i++)
            {
                Agent agent1 = agents.get(2*i);
                Agent agent2 = agents.get((2*i) + 1);
                
                //System.out.println(iteration + " Playing game " + i);
                
                try
                {
                    agent1.playAgainst(agent2, reward, temptation, sucker, 
                            punishment);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            /*System.out.println("Time taken to play games = " 
                + (System.currentTimeMillis() - startTime)/1000);        
            startTime = System.currentTimeMillis();
            */
            //printState();
            
            if (iteration % noOfGames == 0)
            {
                switch (evoType)
                {
                    case TOURNAMENT:
                        evolveByTournament();
                        break;
                    case TRIM:
                        evolveByTrim();
                        break;
                }
                /*System.out.println("Time taken to evolve = " 
                    + (System.currentTimeMillis() - startTime));        
                startTime = System.currentTimeMillis();
                */
                
                addGraphData();
            }
        }
        
        control.setGraphData(strategyLevels);
        resetValues();
    }
    
    public void resetValues()
    {
        clearStrategies();
        agents.clear();
        
        iteration = 0;
        graphLevel = 0;
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
        
        int trimSize = getPopulationSize()/10;
        
        for (int i = 0; i < trimSize; i++)
        {
            Agent removal = agents.get(i);
            
            agents.stream().forEach((a) -> 
            {
                a.getVendettas().remove(removal);
            });
            
            agents.set(i, new Agent(agents.get(getPopulationSize() - 1 - i).getStrategy()));
        }
        
        agents.stream().forEach((a) -> {
                a.resetScore();
        });
    }
    
    public void evolveByTournament()
    {
        shufflePopulation();
        
        for (int i = 0; i < agents.size()/2; i++)
        {
            Agent agent1 = agents.get(2*i);
            Agent agent2 = agents.get((2*i) + 1);
                
            System.out.println("Agent 1, strategy: " + agent1.getStrategyString() + ", score: " + agent1.getScore());
            System.out.println("Agent 2, strategy: " + agent2.getStrategyString() + ", score: " + agent2.getScore());
            
            if (agent1.getScore() > agent2.getScore())
            {
                agents.stream().forEach((a) -> 
                {
                    a.getVendettas().remove(agent2);
                });

                agents.set(2*i+1, new Agent(agent1.getStrategy()));
                
            }
            else if (agent2.getScore() > agent1.getScore())
            {
                agents.stream().forEach((a) -> 
                {
                    a.getVendettas().remove(agent1);
                });

                agents.set(2*i, new Agent(agent2.getStrategy()));
            }
        }
        
        agents.stream().forEach((a) -> {
                a.resetScore();
            });
   }
    
    private void addGraphData()
    {
        for (ArrayList<Integer> al : strategyLevels.values())
        {
            al.add(0);
        }
        
        agents.stream().forEach((agent) -> 
        {
            ArrayList<Integer> stratList;
            Strategy strategy = agent.getStrategy();
            stratList = strategyLevels.get(strategy);
            
            int x = stratList.get(graphLevel); 
            stratList.set(graphLevel, x+1);
        });
        
        System.out.println("Round " + iteration + ":");
        for (Agent.Strategy s : appliedStrategies)
            System.out.println(s.name() + "= " + strategyLevels.get(s).get(graphLevel));
        System.out.println("");
        
        graphLevel++;
    }        

    /**
     * @return the populationSize
     */
    public int getPopulationSize() {
        return populationSize;
    }

    /**
     * @param populationSize the populationSize to set
     */
    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    /**
     * @return the noOfGames
     */
    public int getNoOfGames() {
        return noOfGames;
    }

    /**
     * @param noOfGames the noOfGames to set
     */
    public void setNoOfGames(int noOfGames) {
        this.noOfGames = noOfGames;
    }

    /**
     * @return the temptation
     */
    public int getTemptation() {
        return temptation;
    }

    /**
     * @param temptation the temptation to set
     */
    public void setTemptation(int temptation) {
        this.temptation = temptation;
    }

    /**
     * @return the reward
     */
    public int getReward() {
        return reward;
    }

    /**
     * @param reward the reward to set
     */
    public void setReward(int reward) {
        this.reward = reward;
    }

    /**
     * @return the punishment
     */
    public int getPunishment() {
        return punishment;
    }

    /**
     * @param punishment the punishment to set
     */
    public void setPunishment(int punishment) {
        this.punishment = punishment;
    }

    /**
     * @return the sucker
     */
    public int getSucker() {
        return sucker;
    }

    /**
     * @param sucker the sucker to set
     */
    public void setSucker(int sucker) {
        this.sucker = sucker;
    }
    
    public void setEvoType(String s) throws Exception
    {
        switch (s)
        { 
            case "Trim":
                this.evoType = EvoType.TRIM;
                break;
            case "Tournament":
                this.evoType = EvoType.TOURNAMENT;
                break;
            case "Playoff":
                this.evoType = EvoType.PLAYOFF;
                break;
            default :
                throw new Exception("Error setting evo type");
        }
    }
    
/*    public void setClearScores(Boolean b)
    {
        this.clearScores = b;
    }
    
    public void setClearVendettas(Boolean b)
    {
        this.clearVendettas = b;
    }
*/  
    public void clearStrategies()
    {
        appliedStrategies.clear();
        strategyLevels.clear();
    }
    
    public void addStrategy(Agent.Strategy strategy)
    {
        appliedStrategies.add(strategy);
    }
}


    /*public void evolveByPlayoff()
    {
        Random r = new Random();
        
        int ind1 = r.nextInt(agents.size());
        
        int ind2;
        do 
        {
            ind2 = r.nextInt(agents.size());
        }
        while(ind1 == ind2);
        
        Agent agent1 = agents.get(ind1);
        Agent agent2 = agents.get(ind2);
        
        if (agent1.getScore() > agent2.getScore())
        {
            agents.stream().forEach((a) -> 
            {
                a.getVendettas().remove(agent2);
            });
            
            agents.set(ind2, new Agent(agent1.getStrategy()));
        }
        else if (agent2.getScore() > agent1.getScore())
        {
            agents.stream().forEach((a) -> 
            {
                a.getVendettas().remove(agent1);
            });
            
            agents.set(ind1, new Agent(agent2.getStrategy()));
        }
        
        //RESET ALL THE SCORES SO THAT THOSE ON TOP ARE LEVELLED OUT AGAIN
        agents.stream().forEach((a) -> {
            a.resetScore();
        });
                
    }*/
    
