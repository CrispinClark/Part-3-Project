/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Control.Controller;
import Model.PopulationModel.Strategy;

/**
 *
 * @author Crispin
 */
public class GameTheorySimulation {

    public static void main(String[] args) 
    {
        Controller control = new Controller();
        
        PopulationModel population = new PopulationModel(control);
        //population.randomlyFillPopulation();
        
        population.setPopulationSize(10000);
        population.setNoOfGames(2000);
        
        population.addStrategy(Strategy.TIT_FOR_TAT_PERSONAL);
        population.addStrategy(Strategy.ALWAYS_COOPERATE);
        population.addStrategy(Strategy.ALWAYS_DEFECT);
        population.addStrategy(Strategy.RANDOM);
        population.addStrategy(Strategy.UNFORGIVING_PERSONAL);
        
        population.runSimulation();
    }
    
}
