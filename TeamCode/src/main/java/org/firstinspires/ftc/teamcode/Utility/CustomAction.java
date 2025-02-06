package org.firstinspires.ftc.teamcode.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomAction {
    //Must be used in tandem with ActionCondition class


    List<ActionStep> action=new ArrayList<>();
    public int numOfSteps;
    public int currentStepNum=0;
    public CustomAction(ActionStep... actionSteps){
        this.action=new ArrayList<>(Arrays.asList(actionSteps));
        Sequencing.allActionsStatus.put(this,Boolean.FALSE);
        this.numOfSteps=action.size();
    }

    public void nullAction(){

    }

}
