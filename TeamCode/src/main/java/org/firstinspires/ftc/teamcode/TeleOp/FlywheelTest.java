package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp
public class FlywheelTest extends OpMode {

    private DcMotorEx flywheel;
    private double flywheelPower;
    private final double triggerDeadzone = 0.2;

    
    public void init() {

        flywheel = hardwareMap.get(DcMotorEx.class, "flywheel");
        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    public void loop() {

        if (gamepad1.right_trigger > triggerDeadzone) {

            flywheel.setPower(flywheelPower);
        }
        else {

            flywheel.setPower(0);
        }

    }

}
