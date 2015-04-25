/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Agents;

import Control.Controller;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Crispin
 */
public abstract class AgentTemplate implements Comparable
{
    protected Controller control;
    
    private int totalScore;
    protected boolean isCooperator;
    protected HashMap<AgentTemplate, Boolean> vendettas;
    
    public AgentTemplate(Controller control)
    {
        this.control = control;
        this.totalScore = 0;
        this.vendettas = new HashMap<>();
    }
    
    @Override
    abstract public String toString();
    abstract public void makeDecision(AgentTemplate competitor);
    
    public void playAgainst(AgentTemplate competitor)
    {
        if (competitor.isCooperator && this.isCooperator)
            totalScore += control.getReward();
        else if (competitor.isCooperator && !this.isCooperator)
            totalScore += control.getTemptation();
        else if (!competitor.isCooperator && this.isCooperator)
            totalScore += control.getSucker();
        else
            totalScore += control.getPunishment();
    }
    
    abstract public AgentTemplate reproduce(HashMap<AgentTemplate, Boolean> vendettas);
    
    public boolean isCooperator()
    {
        return isCooperator;
    }
    
    public int getScore()
    {
        return totalScore;
    }
    
    public void setScore(int x)
    {
        totalScore = x;
    }
    
    public void resetScore()
    {
        totalScore = 0;
    }
    
    public HashMap<AgentTemplate, Boolean> getVendettas()
    {
        return this.vendettas;
    }
    
    @Override
    public int compareTo(Object o) 
    {
        AgentTemplate agent = (AgentTemplate) o;
        
        if (this.totalScore > agent.totalScore)
            return 1;
        else if (this.totalScore < agent.totalScore)
            return -1;
        else return 0;
    }
}
