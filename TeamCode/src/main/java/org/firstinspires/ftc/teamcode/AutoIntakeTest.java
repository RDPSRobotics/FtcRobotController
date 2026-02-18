package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Mechanism.DistanceSensorMechanism;

@TeleOp
public class AutoIntakeTest extends OpMode {

    DistanceSensorMechanism distanceSensor = new DistanceSensorMechanism();

    @Override
    public void init() {
        distanceSensor.init(hardwareMap);
    }

    @Override
    public void loop() {
        if (distanceSensor.getDistancea() >= 278) {
            telemetry.addData("INTAKE STATUS:", "ON");
        }
        else {
            telemetry.addData("INTAKE STATUS:", "OFF");
        }

        telemetry.addData("Distance", distanceSensor.getDistancea());
        telemetry.update();

    }


}
