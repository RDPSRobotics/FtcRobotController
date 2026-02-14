package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.Mechanism.MecanumDrive;

@TeleOp
public class MecanumWheelDrive extends OpMode {

    MecanumDrive drive = new MecanumDrive();

    public double forward, strafe,rotate;


    @Override
    public void init() {
        drive.init(hardwareMap);
    }

    @Override
    public void loop() {
        forward = -gamepad1.left_stick_y;
        strafe = gamepad1.left_stick_x * 1.1;
        rotate = gamepad1.right_stick_x;

        drive.drive(forward,strafe,rotate);
    }

}
