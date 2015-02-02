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
public class TwoAgentGame 
{
    private int temptation = 5;
    private int reward = 3;
    private int punishment = 2;
    private int sucker = 1;
    
    private Agent agent1, agent2;
    
    public TwoAgentGame(Agent agent1, Agent agent2)
    {
        this.agent1 = agent1;
        this.agent2 = agent2;
    }
    
    public void play()
    {
        if (agent1.isCooperator() && agent2.isCooperator())
        {
            agent1.setPayoff(reward);
            agent2.setPayoff(reward);
        }
        else if (agent1.isCooperator() && !agent2.isCooperator())
        {
            agent1.setPayoff(sucker);
            agent2.setPayoff(temptation);
        }
        else if (!agent1.isCooperator() && agent2.isCooperator())
        {
            agent1.setPayoff(temptation);
            agent2.setPayoff(sucker);
        }
        else
        {
            agent1.setPayoff(punishment);
            agent2.setPayoff(punishment);
        }
    }
}
