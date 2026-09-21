package org.firstinspires.ftc.teamcode.Mechanism;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class DistanceSensorMechanism {

    private DistanceSensor distanceSensor;

    public void init(HardwareMap hwMap) {
        distanceSensor = hwMap.get(DistanceSensor.class, "distanceSensor");
    }

    public double getDistancea() {
        return distanceSensor.getDistance(DistanceUnit.CM);
    }

}
