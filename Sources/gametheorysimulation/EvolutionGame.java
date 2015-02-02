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
public class EvolutionGame 
{
    public void playGame()
    {
        Population population = new Population(1000000);
        population.randomlyFillPopulation();
        
        population.runSimulation();
    }
}
