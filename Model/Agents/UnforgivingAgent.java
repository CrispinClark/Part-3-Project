/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Agents;

import Control.Controller;

/**
 *
 * @author Crispin
 */
public class UnforgivingAgent extends AgentTemplate
{

    public UnforgivingAgent(Controller control) {
        super(control);
    }

    @Override
    public void makeDecision(AgentTemplate competitor) 
    {
        try
        {
            if (vendettas.contains(competitor))
            {
                isCooperator = false;
            }
            else
                isCooperator = true;
        }
        catch (Exception e)
        {
            System.err.println("Error in looking for vendettas!");
            e.printStackTrace(System.out);
        }
    }

    @Override
    public void playAgainst(AgentTemplate competitor) 
    {
        if (!competitor.isCooperator)
        {
            if (!vendettas.contains(competitor))
                vendettas.add(competitor);
        }
        
        super.playAgainst(competitor);
    }

    @Override
    public AgentTemplate reproduce() {
        UnforgivingAgent newAgent = new UnforgivingAgent(control);
        newAgent.vendettas = this.vendettas;
        
        return newAgent;
    }
    
    @Override
    public String toString()
    {
        return "Unforgiving";
    }
}
