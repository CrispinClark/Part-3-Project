/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Agents;

import Control.Controller;
import java.util.HashMap;

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
            if (vendettas.containsKey(competitor))
                isCooperator = !vendettas.get(competitor);
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
            vendettas.put(competitor, true);
        }
        
        super.playAgainst(competitor);
    }

    @Override
    public AgentTemplate reproduce(HashMap<AgentTemplate, Boolean> vendettas) 
    {
        UnforgivingAgent newAgent = new UnforgivingAgent(control);
        newAgent.vendettas = vendettas;
        
        return newAgent;
    }
    
    @Override
    public String toString()
    {
        return "Unforgiving";
    }
}
