/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Crispin
 */
public class TwoAgentGame 
{
    private int temptation;
    private int reward;
    private int punishment;
    private int sucker;
    
    private Agent agent1, agent2;
    
    public TwoAgentGame(int temptation, int reward, int punishment, int sucker,
            Agent agent1, Agent agent2) throws Exception
    {        
        this.temptation = temptation;
        this.reward = reward;
        this.punishment = punishment;
        this.sucker = sucker;
        
        if (agent1.equals(agent2))
        {
            throw new Exception("Trying to create a game with identical agents");
        }
        
        this.agent1 = agent1;
        this.agent2 = agent2;
    }
    
    public void play()
    {
        agent1.makeDecision(agent2);
        //System.out.println(agent1.isCooperator());
        agent2.makeDecision(agent1);
        //System.out.println(agent2.isCooperator());
        
        if (agent1.isCooperator() && agent2.isCooperator())
        {
            agent1.addToTotalScore(reward);
            agent2.addToTotalScore(reward);
            
            if (agent1.getStrategy() == Agent.Strategy.TIT_FOR_TAT_PERSONAL)
                agent1.removeDefector(agent2);
            
            if (agent2.getStrategy() == Agent.Strategy.TIT_FOR_TAT_PERSONAL)
                agent2.removeDefector(agent1);
        }
        else if (agent1.isCooperator() && !agent2.isCooperator())
        {
            agent1.addToTotalScore(sucker);
            agent2.addToTotalScore(temptation);
            
            if (agent1.getStrategy() == Agent.Strategy.TIT_FOR_TAT_PERSONAL)
                agent1.addDefector(agent2);
            
            if (agent2.getStrategy() == Agent.Strategy.TIT_FOR_TAT_PERSONAL)
                agent2.removeDefector(agent1);
        }
        else if (!agent1.isCooperator() && agent2.isCooperator())
        {
            agent1.addToTotalScore(temptation);
            agent2.addToTotalScore(sucker);
            
            if (agent1.getStrategy() == Agent.Strategy.TIT_FOR_TAT_PERSONAL)
                agent1.removeDefector(agent2);
            
            if (agent2.getStrategy() == Agent.Strategy.TIT_FOR_TAT_PERSONAL)
                agent2.addDefector(agent1);
        }
        else
        {
            agent1.addToTotalScore(punishment);
            agent2.addToTotalScore(punishment);
            
            if (agent1.getStrategy() == Agent.Strategy.TIT_FOR_TAT_PERSONAL)
                agent1.addDefector(agent2);
            
            if (agent2.getStrategy() == Agent.Strategy.TIT_FOR_TAT_PERSONAL)
                agent2.addDefector(agent1);
        }
    }
}
