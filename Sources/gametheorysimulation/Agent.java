/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gametheorysimulation;

/**
 *
 * @author Crispin
 */
public class Agent implements Comparable
{
    private boolean isCooperator;
    
    private int payoff;
    
    public Agent(boolean cooperates)
    {
        this.isCooperator = cooperates;
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
    
    public void setPayoff(int payoff)
    {
        this.payoff = payoff;
    }
    
    public int getPayoff()
    {
        return this.payoff;
    }

    @Override
    public int compareTo(Object o) 
    {
        Agent agent = (Agent) o;
        
        if (this.payoff > agent.getPayoff())
            return 1;
        else if (this.payoff < agent.getPayoff())
            return -1;
        else return 0;
    }
}
