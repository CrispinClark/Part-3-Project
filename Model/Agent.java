/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Control.Controller;
import Model.Agents.DefectorAgent;
import java.util.ArrayList;
import java.util.Random;

/**
 *  DEPRECATED CLASS, SEE AGENTTEMPLATE INSTEAD
 * @author Crispin
 */
public class Agent implements Comparable
{    
    public enum Strategy{ALWAYS_DEFECT, ALWAYS_COOPERATE, TIT_FOR_TAT_PERSONAL,
                            ALTERNATE, RANDOM, TIT_FOR_TAT_IMPERSONAL, 
                            UNFORGIVING_IMPERSONAL, UNFORGIVING_PERSONAL};
    
    private Strategy strategy;
    
    private boolean isCooperator;
    private int totalScore;
    
    //keep a store of those who have previously defected in a game with the agent
    protected ArrayList<Agent> vendettas; 
    protected Boolean lastTimeCheated;
    protected Boolean unforgivingWasCheated = false;
    
    public Agent(Strategy strategy)
    {
        this.strategy = strategy;
        
        vendettas = new ArrayList<>();
        isCooperator = false;
        lastTimeCheated = false;
        totalScore = 0;
    }
    
    public void playAgainst(Agent competitor, int reward, int temptation, 
            int sucker, int punishment) throws Exception
    {
        //System.out.println(getStrategyString() + " vs " + competitor.getStrategyString());
        
        //Decision is now made in population model method
        //this.makeDecision(competitor);
        //competitor.makeDecision(this);
        
        //System.out.println(this.isCooperator() + " " + competitor.isCooperator);
        
        if (this.isCooperator() && competitor.isCooperator())
        {
            this.addToTotalScore(reward);
            competitor.addToTotalScore(reward);
            
            if (this.strategy == Strategy.TIT_FOR_TAT_PERSONAL)
            {
                this.vendettas.remove(competitor);
            }
            else if (this.strategy == Strategy.TIT_FOR_TAT_IMPERSONAL)
                this.lastTimeCheated = false;
            
            if (competitor.strategy == Strategy.TIT_FOR_TAT_PERSONAL)
            {
                competitor.vendettas.remove(this);
            }
            else if (competitor.strategy == Strategy.TIT_FOR_TAT_IMPERSONAL)
                this.lastTimeCheated = false;
        }
        else if (this.isCooperator() && !competitor.isCooperator())
        {
            this.addToTotalScore(sucker);
            competitor.addToTotalScore(temptation);
            
            //if (this.strategy == Strategy.TIT_FOR_TAT_PERSONAL)
            //{
                //if (!this.vendettas.contains(competitor))
                //{
                    //System.out.println("Tit for tatter added a " + competitor.getStrategyString());
                    this.vendettas.add(competitor);
                //}
            //}
            if (this.strategy == Strategy.TIT_FOR_TAT_IMPERSONAL)
                this.lastTimeCheated = true;
            else if (this.strategy == Strategy.UNFORGIVING_IMPERSONAL)
                this.unforgivingWasCheated = true;
            /*else if (this.strategy == Strategy.UNFORGIVING_PERSONAL)
            {
                if (!this.vendettas.contains(competitor))
                {
                    this.vendettas.add(competitor);
                }
            }*/
            
            if (competitor.strategy == Strategy.TIT_FOR_TAT_PERSONAL)
            {
                competitor.vendettas.remove(this);
            }
            else if (competitor.strategy == Strategy.TIT_FOR_TAT_IMPERSONAL)
                this.lastTimeCheated = false;
        }
        else if (!this.isCooperator() && competitor.isCooperator())
        {
            this.addToTotalScore(temptation);
            competitor.addToTotalScore(sucker);
            
            if (this.strategy == Strategy.TIT_FOR_TAT_PERSONAL)
            {
                this.vendettas.remove(competitor);
            }
            else if (this.strategy == Strategy.TIT_FOR_TAT_IMPERSONAL)
                this.lastTimeCheated = false;
            
            //if (competitor.strategy == Strategy.TIT_FOR_TAT_PERSONAL)
            //{
                //if (!competitor.vendettas.contains(this))
                //{
                    //System.out.println("Tit for tatter added a " + this.getStrategyString());
                    competitor.vendettas.add(this);
                //}
            //}
            if (competitor.strategy == Strategy.TIT_FOR_TAT_IMPERSONAL)
                competitor.lastTimeCheated = true;
            else if (competitor.strategy == Strategy.UNFORGIVING_IMPERSONAL)
                competitor.unforgivingWasCheated = true;
            /*else if (competitor.strategy == Strategy.UNFORGIVING_PERSONAL)
            {
                if (!competitor.vendettas.contains(this))
                {
                    competitor.vendettas.add(this);
                }
            }*/
        }
        else
        {
            this.addToTotalScore(punishment);
            competitor.addToTotalScore(punishment);
            
            /*if (this.strategy == Strategy.TIT_FOR_TAT_PERSONAL &&
                competitor.strategy == Strategy.TIT_FOR_TAT_PERSONAL)
            {
                throw new Exception("Two tit for tatters are defecting");
            }*/
            
            //if (this.strategy == Strategy.TIT_FOR_TAT_PERSONAL)
            //{
                //if (!this.vendettas.contains(competitor))
                //{
                    //System.out.println("Tit for tatter added a " + competitor.getStrategyString());
                    this.vendettas.add(competitor);
                //}
            //}
            if (this.strategy == Strategy.TIT_FOR_TAT_IMPERSONAL)
                this.lastTimeCheated = true;
            
            //if (competitor.strategy == Strategy.TIT_FOR_TAT_PERSONAL)
            //{
                //if (!competitor.vendettas.contains(this))
                //{
                    //System.out.println("Tit for tatter added a " + this.getStrategyString());
                    competitor.vendettas.add(this);
                //}
            //}
            if (competitor.strategy == Strategy.TIT_FOR_TAT_IMPERSONAL)
                this.lastTimeCheated = true;
        }
    }
    
    public void makeDecision(Agent competitor)
    {
        Random r = new Random();
        
        switch (strategy)
        {
            case ALWAYS_DEFECT:
                //System.out.println("Making decision for always defector");
                isCooperator = false;
                break;
            
                
            case ALWAYS_COOPERATE:
                //System.out.println("Making decision for always cooperator");
                isCooperator = true;
                break;
            
            case TIT_FOR_TAT_PERSONAL:
                //System.out.println("Making decision for tit for tatter");
                try
                {
                    if (vendettas.contains(competitor))
                    {
                        if (r.nextInt(10) == 1)
                        {
                            isCooperator = true;
                        }
                        else
                            isCooperator = false;
                    }
                    else
                        isCooperator = true;
                }
                catch (Exception e)
                {
                    System.err.println("Error in looking for vendettas!");
                    e.printStackTrace();
                }
                break;
                
            case ALTERNATE:
                isCooperator =! isCooperator;
                break;
                
            case RANDOM:
                isCooperator = r.nextBoolean();
                break;
                
            case TIT_FOR_TAT_IMPERSONAL:
                isCooperator = !lastTimeCheated;
                break;
                
            case UNFORGIVING_IMPERSONAL:
                isCooperator = !unforgivingWasCheated;
                
            case UNFORGIVING_PERSONAL:
                //System.out.println("Making decision for tit for tatter");
                try
                {
                    isCooperator = !vendettas.contains(competitor);
                }
                catch (Exception e)
                {
                    System.err.println("Error in looking for vendettas!");
                    e.printStackTrace();
                }
                break;
        }
    }
    
    public void setStrategy(Strategy newStrategy)
    {
        this.strategy = newStrategy;
    }
    
    public Strategy getStrategy()
    {
        return this.strategy;
    }

    public boolean isCooperator()
    {
        return this.isCooperator;
    }
    
    public void switchCooperator()
    {
        this.isCooperator = this.isCooperator != true;
    }
    
    public void setIsCooperator(boolean isCooperator)
    {
        this.isCooperator = isCooperator;
    }
    
    public void addToTotalScore(int payoff)
    {
        this.totalScore += payoff;
    }
    
    public void setTotalScore(int payoff)
    {
        this.totalScore = payoff;
    }
    
    public void resetScore()
    {
        this.totalScore = 0;
    }
    
    public int getScore()
    {
        return this.totalScore;
    }
    
    public void setVendettas(ArrayList<Agent> vendettas) {
        this.vendettas = vendettas;
    }
    
    public ArrayList<Agent> getVendettas()
    {
        return this.vendettas;
    }
    
    public void clearVendettas()
    {
        vendettas.clear();
    }
    
    /**
     * @return the lastTimeCheated
     */
    public Boolean getLastTimeCheated() {
        return lastTimeCheated;
    }

    /**
     * @param lastTimeCheated the lastTimeCheated to set
     */
    public void setLastTimeCheated(Boolean lastTimeCheated) {
        this.lastTimeCheated = lastTimeCheated;
    }
    
    @Override
    public int compareTo(Object o) 
    {
        Agent agent = (Agent) o;
        
        if (this.totalScore > agent.getScore())
            return 1;
        else if (this.totalScore < agent.getScore())
            return -1;
        else return 0;
    }
    
    public String getStrategyString() 
    {
        switch (strategy)
        {
            case ALTERNATE:
                return "Alternate";
            case ALWAYS_COOPERATE:
                return "Always Cooperate";
            case ALWAYS_DEFECT:
                return "Always defect";
            case RANDOM:
                return "Random";
            case TIT_FOR_TAT_PERSONAL:
                return "Tit for tat personal";
            case TIT_FOR_TAT_IMPERSONAL:
                return "Tit for tat impersonal";
        }
        
        return null;
    }
}
