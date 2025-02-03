package org.firstinspires.ftc.teamcode.Utility;


import com.qualcomm.robotcore.util.ElapsedTime;

public class Timing  {



   public ElapsedTime currentTimer;
   public double delay;
   public static Timing delayTimer;



   public void delay(int delay){
      this.currentTimer=new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
      this.delay=(double)delay;
   }
   public boolean isDelayDone(){
      if (currentTimer.time()>delay){
         return true;
      }
      return false;
   }




}
