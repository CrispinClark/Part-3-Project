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
    
    private Boolean clearVendettas, clearScores = false;
    
    final private ArrayList<Agent.Strategy> availableStrategies;
    private Agent.Strategy winningStrategy;
    
    private int iteration = 0;
    private long startTime;
    
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
        startTime = System.currentTimeMillis();
        randomlyFillPopulation();
        System.out.println("Time taken to fill population = " 
                + (System.currentTimeMillis() - startTime)/1000000);        
        startTime = System.currentTimeMillis();
        
        printState();
        
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
                //evolveByTournament();
                evolveByTrim();
                //evolveByPlayoff();
                
                /*System.out.println("Time taken to evolve = " 
                    + (System.currentTimeMillis() - startTime));        
                startTime = System.currentTimeMillis();
                */
                
                addGraphData();
                printState();
            }
        }
        
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
            //System.out.println("Swapping agent " + i + " with " + (getPopulationSize() - 1 - i));
            //System.out.println(agents.get(i).getStrategyString() + " with " + agents.get((getPopulationSize() - 1 - i)).getStrategyString());
            
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

        if (clearVendettas)
            agents.stream().forEach((a) -> {
                a.clearVendettas();
        });
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
        
        //RESET ALL THE SCORES SO THAT THOSE ON TOP ARE LEVELLED OUT AGAIN
        agents.stream().forEach((a) -> {
            a.resetScore();
        });
                
        if (clearVendettas)
            agents.stream().forEach((a) -> {
                a.clearVendettas();
        });
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
        
        agents.stream().forEach((a) -> {
            a.resetScore();
        });

        if (clearVendettas)
            agents.stream().forEach((a) -> {
                a.clearVendettas();
        });
    }
    
    private void addGraphData()
    {
        ArrayList<Agent> cooperators = new ArrayList<>();
        ArrayList<Agent> defectors = new ArrayList<>();
        ArrayList<Agent> t4tps = new ArrayList<>();
        ArrayList<Agent> t4tis = new ArrayList<>();
        ArrayList<Agent> alternators = new ArrayList<>();
        ArrayList<Agent> randoms = new ArrayList<>();
        
        agents.stream().forEach((agent) -> 
        {
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
            else if (strategy == Strategy.RANDOM)
            {
                randoms.add(agent);
            }
            else if (strategy == Strategy.TIT_FOR_TAT_IMPERSONAL)
            {
                t4tis.add(agent);
            }
        });
    }
    
    private void printState()
    {
        ArrayList<Agent> cooperators = new ArrayList<>();
        ArrayList<Agent> defectors = new ArrayList<>();
        ArrayList<Agent> t4tps = new ArrayList<>();
        ArrayList<Agent> t4tis = new ArrayList<>();
        ArrayList<Agent> alternators = new ArrayList<>();
        ArrayList<Agent> randoms = new ArrayList<>();
        
        agents.stream().forEach((agent) -> 
        {
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
            else if (strategy == Strategy.RANDOM)
            {
                randoms.add(agent);
            }
            else if (strategy == Strategy.TIT_FOR_TAT_IMPERSONAL)
            {
                t4tis.add(agent);
            }
        });
        
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
        System.out.println("TIT_FOR_TAT_IMPERSONAL = " + t4tis.size());
        /*for (Agent t4tp : t4tps) {
            System.out.print(t4tp.getScore() + " ");
        }
        System.out.println("");
        */
        System.out.println("ALTERNATE = " + alternators.size());
        /*for (Agent alternator : alternators) {
            System.out.print(alternator.getScore() + " ");
        }*/
        System.out.println("RANDOM = " + randoms.size());
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
