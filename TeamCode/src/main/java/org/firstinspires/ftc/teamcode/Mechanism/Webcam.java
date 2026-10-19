package org.firstinspires.ftc.teamcode.Mechanism;

import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagSingleDetection;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
public class Webcam {
    private AprilTagProcessor aprilTagProcessor;
    private VisionPortal visionPortal;

    private List<AprilTagDetection> detectedTags = new ArrayList<>();

    private Telemetry telemetry;

    public void init(HardwareMap hwMap,Telemetry telemetry) {
        this.telemetry = telemetry;

        aprilTagProcessor = new AprilTagProcessor.Builder()
                .setDrawTagID(true)
                .setDrawTagOutline(true)
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .setOutputUnits(DistanceUnit.CM, AngleUnit.DEGREES)
                .build();

        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(hwMap.get(WebcamName.class, "Webcam 1"));
        builder.setCameraResolution((new Size(640,480)));
        builder.addProcessor(aprilTagProcessor);

        visionPortal = builder.build();

        setManualExposure(6,240);
    }

    private void setManualExposure(int exposureMS, int gain) {
        if (visionPortal == null || visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            telemetry.addData("Camera", "Waiting.....");

            while (visionPortal == null || visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
                try { Thread.sleep(20); } catch (InterruptedException ignore) {}
            }
            telemetry.addData("Camera", "Ready!");
        }

        if (visionPortal.getCameraState() == VisionPortal.CameraState.STREAMING) {
            try {
                ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
                GainControl gainControl = visionPortal.getCameraControl(GainControl.class);

                if (exposureControl.getMode() != ExposureControl.Mode.Manual) {
                    exposureControl.setMode(ExposureControl.Mode.Manual);
                    Thread.sleep(50);
                }

                exposureControl.setExposure((long)exposureMS, TimeUnit.MILLISECONDS);
                Thread.sleep(20);
                gainControl.setGain(gain);
                Thread.sleep(20);
            } catch (Exception e) {
                telemetry.addData("Camera Control Error", e.getMessage());
                telemetry.update();
            }
        }
    }

    public void update() {
        detectedTags = aprilTagProcessor.getDetections();

        displayDetectionTelemetry();
    }

    public List<AprilTagDetection> getDetectedTags() {
        return detectedTags;
    }

     public void displayDetectionTelemetry() {

         List<AprilTagDetection> currentDetections = aprilTagProcessor.getDetections();
         telemetry.addData("# AprilTags Detected", currentDetections.size());

         // Step through the list of detections and display info for each one.
         for (AprilTagDetection detection : currentDetections) {
             if (detection instanceof AprilTagSingleDetection) {
                 AprilTagSingleDetection singleDet = (AprilTagSingleDetection) detection;

                 if (singleDet.metadata != null) {
                     telemetry.addLine(String.format("\n==== (ID %d) %s", singleDet.id, singleDet.metadata.name));
                     telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                     telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                     telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
                 } else {
                     telemetry.addLine(String.format("\n==== (ID %d) Unknown", singleDet.id));
                     telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", singleDet.center.x, singleDet.center.y));
                 }
             } else {
                 AprilTagClusterDetection clusterDet = (AprilTagClusterDetection) detection;
                 telemetry.addLine(String.format("\n==== Tag Cluster (%s)", clusterDet.metadata.name));
                 telemetry.addLine(String.format("Percent tags found: %d", clusterDet.percentClusterFound));
                 telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                 telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                 telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
             }
         }
     }

    public AprilTagDetection getTagBySpecificID() {
        return null;
    }

    public void stop() {
        if (visionPortal != null) {
            visionPortal.close();
        }
    }
}

