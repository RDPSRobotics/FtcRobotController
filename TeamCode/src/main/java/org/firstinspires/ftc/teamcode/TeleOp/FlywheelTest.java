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

    static final double TICKS_PER_REV = 28;

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

        double ticksPerSecond = flywheel.getVelocity();

        // Convert ticks per second to RPM
        double rpm = (ticksPerSecond / TICKS_PER_REV) * 60.0;

        telemetry.addData("Ticks/Sec", ticksPerSecond);
        telemetry.addData("Current RPM", rpm);

        telemetry.addLine("Start Flywheel(RBumper)");
        telemetry.addLine("Increase or Decrease velocity(Dpad Down or Up)");

        telemetry.addData("Flywheel Power: ", flywheelPower);
        telemetry.update();

    }

}
