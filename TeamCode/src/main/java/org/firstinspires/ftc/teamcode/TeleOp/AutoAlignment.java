package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.ArrayList;
import java.util.List;

import org.firstinspires.ftc.teamcode.Mechanism.MecanumDrive;
import org.firstinspires.ftc.teamcode.Mechanism.Webcam;
import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@TeleOp
public class AutoAlignment extends OpMode {

    Webcam webcam = new Webcam();

    MecanumDrive drive = new MecanumDrive();

    double forward,strafe,rotate;

    double kP = 0.002;
    double error = 0;
    double lastError = 0;
    double goalX = 0;
    double angleTolerance = 0.4;
    double kD = 0.0001;
    double curTime = 0;
    double lastTime = 0;

    //------------------CONTROLLER BASED PD TUNING-------------------------

    double[] stepSizes = {0.1, 0.01, 0.001, 0.0001};

    int stepIndex = 1;

    @Override
    public void init() {

        webcam.init(hardwareMap, telemetry);

        //drive.init(hardwareMap);

    }

    @Override
    public void loop() {

        DetectingAprilTag();

        //Driving();
    }

    public void DetectingAprilTag() {

        webcam.update();

        AprilTagClusterDetection clusterDetection = webcam.getAprilTagCluster();

        //Driving();
    }

    public void Driving() {

        forward = gamepad1.left_stick_y;
        strafe = gamepad1.left_stick_x;
        rotate = gamepad1.right_stick_x;

        drive.drive(forward,-strafe,rotate);
    }

}
