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
    
    private Boolean clearScores = false;
    private Boolean clearVendettas = false;
    
    final private ArrayList<Agent.Strategy> availableStrategies;
    private Agent.Strategy winningStrategy;
   
    
    private int iteration = 0;
    
    public PopulationModel(Controller control)
    {
        this.control = control;
        
        agents = new ArrayList<>(getPopulationSize());
        availableStrategies = new ArrayList<>();
    }
    
    public void randomlyFillPopulation()
    {
        int noOfStrategies = availableStrategies.size();
        Random rand = new Random();
        
        for (int i = 0; i < getPopulationSize(); i++)
        {
            int strategyNumber = rand.nextInt(noOfStrategies);
            
            Agent agent = new Agent(availableStrategies.get(strategyNumber));
            
            agents.add(agent);
        }
    }
    
    public void runSimulation()
    {        
        randomlyFillPopulation();
        
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
                    TwoAgentGame game = new TwoAgentGame(temptation, reward, 
                            punishment, sucker, agent1, agent2);
                    game.play();
                }
                catch (Exception e)
                {
                    System.err.println(e.getMessage());
                }
            }
            
            //printState();
            
            if (iteration % noOfGames == 0)
            {
                //evolveByTournament();
                evolveByTrim();
                //evolveByPlayoff();
                
                printState();
            }
        }
        
        printState();
        
        resetValues();
    }
    
    public void resetValues()
    {
        clearStrategies();
        agents.clear();
        iteration = 0;
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
            agents.get(i).setStrategy(agents.get(getPopulationSize() - 1 - i).getStrategy());
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
        
        Agent agent1 = agents.get(r.nextInt(getPopulationSize()));
        Agent agent2 = agents.get(r.nextInt(getPopulationSize()));
        
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
        
        for (int i = 0; i < getPopulationSize(); i++)
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
        if (s.equals("Trim"))
            this.evoType = EvoType.TRIM;
        else if (s.equals("Tournament"))
            this.evoType = EvoType.TOURNAMENT;
        else if (s.equals("Playoff"))
            this.evoType = EvoType.PLAYOFF;
        else
            throw new Exception("Error setting evo type");
    }
    
    public void setClearScores(Boolean b)
    {
        this.clearScores = b;
    }
    
    public void setClearVendettas(Boolean b)
    {
        this.clearVendettas = b;
    }
    
    public void clearStrategies()
    {
        availableStrategies.clear();
    }
    
    public void addStrategy(Agent.Strategy strategy)
    {
        availableStrategies.add(strategy);
    }
}
