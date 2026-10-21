package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.ArrayList;
import java.util.List;

import org.firstinspires.ftc.teamcode.Mechanism.MecanumDrive;
import org.firstinspires.ftc.teamcode.Mechanism.Webcam;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@TeleOp
public class AutoAlignment extends OpMode {

    Webcam webcam = new Webcam();

    MecanumDrive drive = new MecanumDrive();

    double forward,strafe,rotate;

    @Override
    public void init() {

        webcam.init(hardwareMap, telemetry);

        drive.init(hardwareMap);

    }

    @Override
    public void loop() {

        DetectingAprilTag();

        Driving();
    }

    public void DetectingAprilTag() {

        webcam.update();

        List<AprilTagDetection> aprilTagDetectionList = webcam.getDetectedTags();

        if (!aprilTagDetectionList.isEmpty()) {
            webcam.displayDetectionTelemetry();
        }
    }

    public void Driving() {

        forward = gamepad1.left_stick_y;
        strafe = gamepad1.left_stick_x;
        rotate = gamepad1.right_stick_x;

        drive.drive(forward,-strafe,rotate);
    }

}
