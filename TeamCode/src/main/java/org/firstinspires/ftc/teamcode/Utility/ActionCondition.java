package org.firstinspires.ftc.teamcode.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ActionCondition
{
    //Stores the condition needed for the current action to stop

    public int numOfConditions;
    public int currentConditionStep = 0;
    List<Condition> conditions = new ArrayList<>();

    public ActionCondition(Condition... condition)
    {
        this.conditions = new ArrayList<>(Arrays.asList(condition));
        this.numOfConditions = conditions.size();
    }

    public static boolean noCondition(){
        return true;
    }
}
