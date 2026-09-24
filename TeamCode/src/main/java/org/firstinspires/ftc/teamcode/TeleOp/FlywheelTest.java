package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp
public class FlywheelTest extends OpMode {

    private DcMotorEx flywheel;
    private double flywheelPower = 856;
    private final double triggerDeadzone = 0.2;

    
    public void init() {

        flywheel = hardwareMap.get(DcMotorEx.class, "flywheel");
        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    public void loop() {

        if (gamepad1.right_trigger > triggerDeadzone) {

            flywheel.setVelocity(flywheelPower);
        }
        else {

            flywheel.setVelocity(0);
        }

        if (gamepad1.dpad_down) {
            flywheelPower -= 10;
        }
        else if (gamepad1.dpad_up) {
            flywheelPower += 10;
        }

        telemetry.addData("Flywheel Power: ", flywheelPower);

    }

}
