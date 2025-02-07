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

   public static boolean isActionAllowed(CustomAction a){
      return Sequencing.allActionsStatus.get(a);
   }

   public static void allowAction(CustomAction a){
      allActionsStatus.put(a,Boolean.TRUE);
   }


   public void restrictedRunAction(CustomAction action, ActionCondition condition, CustomAction restriction){
            if (!allActionsStatus.get(restriction)){
               runInParallel(action,condition);
            }
   }
   public void runInSerial(){

   }







}
