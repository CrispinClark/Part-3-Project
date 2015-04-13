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
public class CooperateAgent extends AgentTemplate
{

    public CooperateAgent(Controller control) {
        super(control);
    }

    @Override
    public void makeDecision(AgentTemplate competitor) 
    {
        this.isCooperator = true;
    }

    @Override
    public AgentTemplate reproduce() 
    {
        CooperateAgent newAgent = new CooperateAgent(control);
        
        return newAgent;
    }
    
    @Override
    public String toString()
    {
        return "Cooperator";
    }
}
