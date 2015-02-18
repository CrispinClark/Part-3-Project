/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.concurrent.CountDownLatch;

/**
 *
 * @author Crispin
 */
public class TwoAgentGameRunnable implements Runnable
{
    private final CountDownLatch startSignal;
    private final CountDownLatch endSignal;
    
    private int temptation;
    private int reward;
    private int punishment;
    private int sucker;
    
    private Agent agent1, agent2;
    
    public TwoAgentGameRunnable(CountDownLatch startSignal, CountDownLatch endSignal,
            int temptation, int reward, int punishment, int sucker,
            Agent agent1, Agent agent2) throws Exception
    {        
        this.startSignal = startSignal;
        this.endSignal = endSignal;
        
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
    
    /*public void play()
    {
        
    }*/

    @Override
    public void run() 
    {
        try
        {
            startSignal.await();
            
            agent1.makeDecision(agent2);
            //System.out.println(agent1.isCooperator());
            agent2.makeDecision(agent1);
            //System.out.println(agent2.isCooperator());

            if (agent1.isCooperator() && agent2.isCooperator())
            {
                agent1.addToTotalScore(reward);
                agent2.addToTotalScore(reward);

                if (agent1.getStrategy() == Agent.Strategy.TIT_FOR_TAT_PERSONAL)
                    agent1.getVendettas().put(agent2, false);
                if (agent2.getStrategy() == Agent.Strategy.TIT_FOR_TAT_PERSONAL)
                    agent2.getVendettas().put(agent1, false);
            }
            else if (agent1.isCooperator() && !agent2.isCooperator())
            {
                agent1.addToTotalScore(sucker);
                agent2.addToTotalScore(temptation);

                if (agent1.getStrategy() == Agent.Strategy.TIT_FOR_TAT_PERSONAL)
                    agent1.getVendettas().put(agent2, true);
                if (agent2.getStrategy() == Agent.Strategy.TIT_FOR_TAT_PERSONAL)
                    agent2.getVendettas().put(agent1, false);
            }
            else if (!agent1.isCooperator() && agent2.isCooperator())
            {
                agent1.addToTotalScore(temptation);
                agent2.addToTotalScore(sucker);

                if (agent1.getStrategy() == Agent.Strategy.TIT_FOR_TAT_PERSONAL)
                    agent1.getVendettas().put(agent2, false);
                if (agent2.getStrategy() == Agent.Strategy.TIT_FOR_TAT_PERSONAL)
                    agent2.getVendettas().put(agent1, true);
            }
            else
            {
                agent1.addToTotalScore(punishment);
                agent2.addToTotalScore(punishment);

                if (agent1.getStrategy() == Agent.Strategy.TIT_FOR_TAT_PERSONAL)
                    agent1.getVendettas().put(agent2, true);
                if (agent2.getStrategy() == Agent.Strategy.TIT_FOR_TAT_PERSONAL)
                    agent2.getVendettas().put(agent1, true);
            }
            
            endSignal.countDown();
        }
        catch (Exception e)
        {
            e.printStackTrace(System.err);
        }
    }
}
