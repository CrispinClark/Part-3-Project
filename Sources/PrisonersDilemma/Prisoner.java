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
public class Prisoner 
{
    private int temptation = 5;
    private int reward = 3;
    private int punishment = 1;
    private int sucker = 0;

    private boolean isCooperator;
    
    public void makeDecision()
    {
        if(temptation > reward && punishment > sucker)
            isCooperator = false;
        
        if (temptation < reward && punishment < sucker)
            isCooperator = true;
    }
    
    /**
     * @return the temptation
     */
    public int getTemptation() {
        return temptation;
    }

    /**
     * @param temptation the temptation to set
     */
    public void setTemptation(int temptation) {
        this.temptation = temptation;
    }

    /**
     * @return the reward
     */
    public int getReward() {
        return reward;
    }

    /**
     * @param reward the reward to set
     */
    public void setReward(int reward) {
        this.reward = reward;
    }

    /**
     * @return the punishment
     */
    public int getPunishment() {
        return punishment;
    }

    /**
     * @param punishment the punishment to set
     */
    public void setPunishment(int punishment) {
        this.punishment = punishment;
    }

    /**
     * @return the sucker
     */
    public int getSucker() {
        return sucker;
    }

    /**
     * @param sucker the sucker to set
     */
    public void setSucker(int sucker) {
        this.sucker = sucker;
    }
    
    public boolean isCooperator()
    {
        return this.isCooperator;
    }
    
    public void setIsCooperator(boolean isCooperator)
    {
        this.isCooperator = isCooperator;
    }
}
