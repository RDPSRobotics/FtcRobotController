package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Mechanism.Webcam;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@Autonomous
public class WebcamOpMode extends OpMode {

    Webcam webcam = new Webcam();

    @Override
    public void init() {
        webcam.init(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        webcam.update();
        AprilTagDetection id20 = webcam.getTagBySpecificID(20);
        webcam.displayDetectionTelemetry(id20);

    }

}
