package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp
public class FlywheelTest extends OpMode {

    private DcMotorEx flywheel;
    private double flywheelPower = 0;
    private boolean isShoot;

    
    public void init() {

        flywheel = hardwareMap.get(DcMotorEx.class, "flywheel");
        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    public void loop() {

        if (gamepad1.rightBumperWasPressed()) {

            if (!isShoot) {
                flywheel.setVelocity(flywheelPower);

                isShoot = true;
            }
            else if (isShoot) {
                flywheel.setVelocity(0);

                isShoot = false;
            }
        }

        if (gamepad1.dpadDownWasPressed()) {
            flywheelPower -= 100;
        }
        else if (gamepad1.dpadUpWasPressed()) {
            flywheelPower += 100;
        }

        telemetry.addData("Flywheel Power: ", flywheelPower);

    }

}
