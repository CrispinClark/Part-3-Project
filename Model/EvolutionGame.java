/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Control.Controller;

/**
 *
 * @author Crispin
 */
public class EvolutionGame 
{
    public void playGame()
    {
        Controller control = new Controller();
        
        PopulationModel population = new PopulationModel(control);
        //population.randomlyFillPopulation();
        
        population.setPopulationSize(10000);
        population.setNoOfGames(5000);
        
        population.addStrategy(Agent.Strategy.ALTERNATE);
        population.addStrategy(Agent.Strategy.TIT_FOR_TAT_PERSONAL);
        population.addStrategy(Agent.Strategy.TIT_FOR_TAT_IMPERSONAL);
        population.addStrategy(Agent.Strategy.ALWAYS_COOPERATE);
        population.addStrategy(Agent.Strategy.ALWAYS_DEFECT);
        population.addStrategy(Agent.Strategy.RANDOM);
        
        population.runSimulation();
    }
}
