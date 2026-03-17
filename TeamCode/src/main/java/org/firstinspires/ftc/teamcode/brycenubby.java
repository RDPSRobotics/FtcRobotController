package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class brycenubby extends OpMode {
    DcMotor rFlywheel;
    Servo kicker;

    public void init(){
        rFlywheel = hardwareMap.get(DcMotor.class, "rFlywheel");

        kicker = hardwareMap.get(Servo.class, "kicker");
    }

    public void loop() {

    }
}
