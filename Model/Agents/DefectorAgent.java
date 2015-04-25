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
public class DefectorAgent extends AgentTemplate
{    
    public DefectorAgent(Controller control)
    {
        super(control);
    }
    
    @Override
    public void makeDecision(AgentTemplate competitor) 
    {
        isCooperator = false;
    }

    @Override
    public AgentTemplate reproduce(HashMap<AgentTemplate, Boolean> vendettas) 
    {
        DefectorAgent newAgent = new DefectorAgent(control);
        newAgent.vendettas = vendettas;
        
        return newAgent;
    }

    @Override
    public String toString() {
        return "Defector";
    }
    
}
