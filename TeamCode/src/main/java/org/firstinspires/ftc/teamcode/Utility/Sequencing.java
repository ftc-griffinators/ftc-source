package org.firstinspires.ftc.teamcode.Utility;

import java.util.HashMap;

public class Sequencing {

   public static HashMap<CustomAction, Boolean> allActionsStatus=new HashMap<>();

   public void runAfterCondition(){

   }

//numOfConditions must be the same as the number of sub actions
   public static void runInParallel(CustomAction a, ActionCondition c){
         if ((a.numOfSteps)!=c.numOfConditions) {
            throw new IllegalArgumentException("numOfConditions must be the same as the number of sub actions");
         }
      a.action.get(a.currentStepNum).run();
      if (c.conditions.get(c.currentConditionStep).isTrue()){
         a.currentStepNum++;
         c.currentConditionStep++;
      }
      if (a.currentStepNum+1==a.numOfSteps){
         allActionsStatus.put(a,false);
         a.currentStepNum=0;
         c.currentConditionStep=0;
      }
   }


   public void restrictedRunAction(CustomAction... actions){

   }
   public void runInSerial(){

   }







}
