/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Control.Controller;
import Model.Agents.*;
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
    
    private boolean offspringGetVendettas = true;
    
    private int temptation = 5;
    private int reward = 3;
    private int punishment = 1;
    private int sucker = 0;
    
    final private ArrayList<AgentTemplate> agents;

    public enum Strategy{ALWAYS_DEFECT, ALWAYS_COOPERATE, TIT_FOR_TAT_PERSONAL,
                            ALTERNATE, RANDOM, TIT_FOR_TAT_IMPERSONAL, 
                            UNFORGIVING_IMPERSONAL, UNFORGIVING_PERSONAL};
    public enum EvoType{TRIM, TOURNAMENT}
    
    private EvoType evoType = EvoType.TRIM;
    
    //private Boolean clearVendettas, clearScores = false;
    
    final private ArrayList<Strategy> appliedStrategies;
    final private HashMap<Class, ArrayList<Integer>> strategyLevels;
    private int graphLevel = 0;
 
    private int iteration = 0;
    
    int noOfCooperators = 0;
    int noOfDefectors = 0;
    
    private boolean somethingChanged = false;
    private int stuckValue = 0;
    private int stopLevel = 5;
    private int terminate = 10000;
    
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
            /*if (i == 0)
                agents.add(new DefectorAgent(control));
            else
                agents.add(new CooperateAgent(control));
            
            
            strategyLevels.put(CooperateAgent.class, new ArrayList<>());
            strategyLevels.put(DefectorAgent.class, new ArrayList<>());
            */
            int strategyNumber = rand.nextInt(noOfStrategies);
            
            switch (appliedStrategies.get(strategyNumber))
            {
                case ALWAYS_COOPERATE:
                    agents.add(new CooperateAgent(control));
                    strategyLevels.put(CooperateAgent.class, new ArrayList<>());
                    break;
                case ALWAYS_DEFECT:
                    agents.add(new DefectorAgent(control));
                    strategyLevels.put(DefectorAgent.class, new ArrayList<>());
                    break;
                case RANDOM:
                    agents.add(new RandomAgent(control));
                    strategyLevels.put(RandomAgent.class, new ArrayList<>());
                    break;
                case TIT_FOR_TAT_PERSONAL:
                    agents.add(new TitForTatAgent(control));
                    strategyLevels.put(TitForTatAgent.class, new ArrayList<>());
                    break;
                case UNFORGIVING_PERSONAL:
                    agents.add(new UnforgivingAgent(control));
                    strategyLevels.put(UnforgivingAgent.class, new ArrayList<>());
                    break;
            }
        }
    }
    
    public HashMap<Class, ArrayList<Integer>> runSimulation(boolean inTesting)
    {        
        startTime = System.currentTimeMillis();
        System.out.println("Starting simulation...");
        randomlyFillPopulation();
        
        addGraphData();
        
        int game = 0;
        
        while (!testConvergence())
        {
            iteration++;

            noOfCooperators = 0;
            noOfDefectors = 0;

            shufflePopulation();
            game = 0;
            
            for (int i = 0; i < agents.size()/2; i++)
                
                //for (int j = i +1; j < agents.size(); j++)
                {
                    game++;
                    int j = 2*i+1;
                    AgentTemplate agent1 = agents.get(2*i);
                    AgentTemplate agent2 = agents.get(j);

                    try
                    {
                        agent1.makeDecision(agent2);
                        agent2.makeDecision(agent1);

                        if (agent1.isCooperator())
                            noOfCooperators++;
                        else
                            noOfDefectors ++;

                        if (agent2.isCooperator())
                            noOfCooperators++;
                        else
                            noOfDefectors ++;

                        agent1.playAgainst(agent2);
                        agent2.playAgainst(agent1);
                    }

                    catch (Exception e)
                    {
                        e.printStackTrace(System.err);
                    }
                }
                       
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
                
                addGraphData();
                //System.out.println("Avg coop = " + noOfCooperators);
                //System.out.println("Avg def = " + noOfDefectors);   
            }
        }
        
        control.setGraphData(strategyLevels, noOfCooperators);
        if (!inTesting)
            resetValues();
        
        return strategyLevels;
    }
    
    public void resetValues()
    {
        clearStrategies();
        agents.clear();
        
        iteration = 0;
        graphLevel = 0;
        stuckValue = 0; 
    }
    
    //Implementation of Fisher-Yates shuffle
    public void shufflePopulation()
    {
        Random rand = new Random();
        for (int i = agents.size() - 1; i > 0; i--)
        {
            int index = rand.nextInt(i + 1);
            // Simple swap
            AgentTemplate a = agents.get(index);
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
        if (iteration >= terminate)
            return true;
        
        if (stuckValue == stopLevel)
            return true;
        
        Class winningClass = agents.get(0).getClass();
        
        for (int i = 1; i < agents.size(); i++)
        {
            if (agents.get(i).getClass() != winningClass)
                return false;
        }
        
        return true;
    }
    
    public void evolveByTrim()
    {
        somethingChanged = false;
        
        Collections.sort(agents);

        int trimSize = getPopulationSize()/10;
        
        for (int i = 0; i < trimSize; i++)
        {
            AgentTemplate removal = agents.get(i);
            AgentTemplate replacement = agents.get(populationSize - 1 - i);
            
            if (removal.getScore() != replacement.getScore())
            {    
                if (removal.getClass() != replacement.getClass())
                {
                    somethingChanged = true;
                }

                /*
                *   Create a new agent, with the same strategy and vendettas as
                *   the successful agent
                */
                AgentTemplate newAgent;
                if(offspringGetVendettas)
                    newAgent = replacement.reproduce(replacement.getVendettas());
                else
                    newAgent = replacement.reproduce(removal.getVendettas());
                agents.remove(removal);
                agents.add(newAgent);

                /*
                *   Add the new agent to the vendettas of all the agents in the 
                *   population that have a vendetta against its parent
                */
                agents.stream().forEach((a) ->
                {
                    HashMap<AgentTemplate, Boolean> vendettas = a.getVendettas();
                    
                    if (offspringGetVendettas && vendettas.containsKey(replacement))
                    {
                        vendettas.put(newAgent, vendettas.get(replacement));
                    }
                    else if (!offspringGetVendettas && vendettas.containsKey(removal))
                    {
                        vendettas.put(newAgent, vendettas.get(removal));
                    }
                });
                
                agents.stream().forEach((a) -> 
                {
                    a.getVendettas().remove(removal);
                });
            }
        }
        
        for (AgentTemplate a : agents)
        {
            a.resetScore();
        }
        
        if (somethingChanged)
            stuckValue = 0;
        else
            stuckValue ++;
    }
    
    public void evolveByTournament()
    {
        somethingChanged = false;
        
        shufflePopulation();
        
        for (int i = 0; i < agents.size()/2; i++)
        {
            AgentTemplate agent1 = agents.get(2*i);
            AgentTemplate agent2 = agents.get((2*i) + 1);
                
            if (agent1.getScore() > agent2.getScore())
            {
                //System.out.println(agent1.getScore() + " " + agent2.getScore());
                
                if (agent1.getClass() != agent2.getClass())
                {
                    somethingChanged = true;
                }

                AgentTemplate newAgent;
                if (offspringGetVendettas)
                    newAgent = agent1.reproduce(agent1.getVendettas());
                else
                    newAgent = agent1.reproduce(agent2.getVendettas());
                agents.remove(agent2);
                agents.add(newAgent);
                
                agents.stream().forEach((a) ->
                {
                    HashMap<AgentTemplate, Boolean> vendettas = a.getVendettas();
                    
                    if (offspringGetVendettas && vendettas.containsKey(agent1))
                    {
                        vendettas.put(newAgent, vendettas.get(agent1));
                    }
                    else if (!offspringGetVendettas && vendettas.containsKey(agent2))
                    {
                        vendettas.put(newAgent, vendettas.get(agent2));
                    }
                });
                
                agents.stream().forEach((a) -> 
                {
                    a.getVendettas().remove(agent2);
                });
            }
            else if (agent2.getScore() > agent1.getScore())
            {
                //System.out.println(agent1.getScore() + " " + agent2.getScore());
                if (agent1.getClass() != agent2.getClass())
                {
                    somethingChanged = true;
                }

                AgentTemplate newAgent;
                if (offspringGetVendettas)
                    newAgent = agent2.reproduce(agent2.getVendettas());
                else
                    newAgent = agent2.reproduce(agent1.getVendettas());
                
                agents.remove(agent1);
                agents.add(newAgent);
                
                agents.stream().forEach((a) ->
                {
                    HashMap<AgentTemplate, Boolean> vendettas = a.getVendettas();
                    
                    if (offspringGetVendettas && vendettas.containsKey(agent2))
                    {
                        vendettas.put(newAgent, vendettas.get(agent2));
                    }
                    else if (!offspringGetVendettas && vendettas.containsKey(agent1))
                    {
                        vendettas.put(newAgent, vendettas.get(agent1));
                    }
                });
                
                agents.stream().forEach((a) -> 
                {
                    a.getVendettas().remove(agent1);
                });
            }
        }
        
        agents.stream().forEach((a) -> {
                a.resetScore();
            });
        
        if (somethingChanged)
            stuckValue = 0;
        else
            stuckValue ++;
    }
    
    public void evolveStrategyTrim()
    {
     /*   somethingChanged = false;
        
        Collections.sort(agents);

        int trimSize = getPopulationSize()/10;
        
        for (int i = 0; i < trimSize; i++)
        {
            Agent removal = agents.get(i);
            Agent replacement = agents.get(populationSize - 1 - i);
            
            if (removal.getScore() != replacement.getScore())
            {    
                if (removal.getStrategy() != replacement.getStrategy())
                {
                    somethingChanged = true;
                    removal.setStrategy(replacement.getStrategy());
                    
                    //removal.setVendettas(replacement.getVendettas());
                }

                /*
                *   DON'T REMOVE VENDETTA EVEN THOUGH THESE AGENTS HAVE CHANGED
                *   STRATEGY
                
                
            }
        }
        
        agents.stream().forEach((a) -> {
                    a.resetScore();
            });
        
        if (somethingChanged)
            stuckValue = 0;
        else
            stuckValue ++;*/
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
            Class strategy = agent.getClass();
            stratList = strategyLevels.get(strategy);
            
            int x = stratList.get(graphLevel); 
            stratList.set(graphLevel, x+1);
        });
        
        /*System.out.println("Round " + iteration + ":");
        for (Class c : strategyLevels.keySet())
            System.out.println(c.getSimpleName()+ "= " + strategyLevels.get(c).get(graphLevel));
        System.out.println("Stuck value = " + stuckValue);
        System.out.println("");
        */
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
    
    public void addStrategy(Strategy strategy)
    {
        appliedStrategies.add(strategy);
    }
    
    /**
     * @return the stopLevel
     */
    public int getStopLevel() {
        return stopLevel;
    }

    /**
     * @param stopLevel the stopLevel to set
     */
    public void setStopLevel(int stopLevel) {
        this.stopLevel = stopLevel;
    }
    
    public int getTerminate()
    {
        return this.terminate;
    }
    
    public void setTerminate(int terminate)
    {
        this.terminate = terminate;
    }

    /**
     * @return the offspringGetVendettas
     */
    public boolean isOffspringGetVendettas() {
        return offspringGetVendettas;
    }

    /**
     * @param offspringGetVendettas the offspringGetVendettas to set
     */
    public void setOffspringGetVendettas(boolean offspringGetVendettas) {
        this.offspringGetVendettas = offspringGetVendettas;
    }
}
    
