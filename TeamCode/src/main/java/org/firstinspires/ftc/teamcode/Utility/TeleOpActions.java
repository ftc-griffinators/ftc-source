package org.firstinspires.ftc.teamcode.Utility;

import static org.firstinspires.ftc.teamcode.parts.Claw.CLAW_ROT_FRONT;
import static org.firstinspires.ftc.teamcode.parts.Claw.CLAW_ROT_MID;

import org.firstinspires.ftc.teamcode.parts.Claw;
import org.firstinspires.ftc.teamcode.parts.Drive;
import org.firstinspires.ftc.teamcode.parts.Hanger;
import org.firstinspires.ftc.teamcode.parts.Slider;
import org.firstinspires.ftc.teamcode.systems.VisionSystem;

public class TeleOpActions {

    Claw claw;
    Slider slider;
    Hanger hanger;
    Timing timer;
    Drive drive;
   public VisionSystem vision;


    public TeleOpActions(Drive drive,Claw claw,Slider slider,Hanger hanger,Timing timer,VisionSystem vision){
        this.drive=drive;
        this.claw=claw;
        this.slider=slider;
        this.hanger=hanger;
        this.timer=timer;
        this.vision=vision;
    }



   public CustomAction align=new CustomAction(
            claw::alignerReset,
            ()->claw.orientationAligning(vision));

  public   ActionCondition alignCondition=new ActionCondition(
            ()->timer.delay(200),
            ()->claw.startAligning);



  public   CustomAction boxScoreStart =new CustomAction(
            claw::middleAlignment,
            slider::sliderExtensionTopBox,
            claw::boxScoring);

  public   ActionCondition boxScoreStartCondition =new ActionCondition(
            ActionCondition::noCondition,
            ()->timer.delay(1000),
            ActionCondition::noCondition);



  public   CustomAction boxScoreEnd=new CustomAction(
            claw::release,
            ()->claw.rotateArm(CLAW_ROT_MID),
            claw::retract,
            slider::sliderRetraction,
            ()->claw.rotateArm(CLAW_ROT_FRONT),
            claw::grabPrep);

 public    ActionCondition boxScoreEndCondition=new ActionCondition(
            ()->timer.delay(300),
            ActionCondition::noCondition,
            ActionCondition::noCondition,
            ()->timer.delay(300),
            ActionCondition::noCondition,
            ActionCondition::noCondition);


}
