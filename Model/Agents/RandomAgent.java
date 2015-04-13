/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Agents;

import Control.Controller;
import java.util.Random;

/**
 *
 * @author Crispin
 */
public class RandomAgent extends AgentTemplate
{
    private final Random r;
    
    public RandomAgent(Controller control) 
    {
        super(control);
        r = new Random();
    }

    @Override
    public void makeDecision(AgentTemplate competitor) 
    {
        this.isCooperator = r.nextBoolean();
    }

    @Override
    public AgentTemplate reproduce() 
    {
        RandomAgent newAgent = new RandomAgent(control);
   
        return newAgent;
    }
   
    @Override
    public String toString()
    {
        return "Randomer";
    }
}
