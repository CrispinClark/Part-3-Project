/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PrisonersDilemma;

/**
 *
 * @author Crispin
 */
public class TwoPersonGame 
{
    static Prisoner prisoner1 = new Prisoner();
    static Prisoner prisoner2 = new Prisoner();
    
    public static void main(String[] args)
    {
        prisoner1.makeDecision();
        prisoner2.makeDecision();
        
        if (prisoner1.isCooperator())
            System.out.println("Prisoner 1 cooperates");
        else
            System.out.println("Prisoner 1 defects");
        
        if (prisoner2.isCooperator())
            System.out.println("Prisoner 2 cooperates");
        else
            System.out.println("Prisoner 2 defects");
    }
}
