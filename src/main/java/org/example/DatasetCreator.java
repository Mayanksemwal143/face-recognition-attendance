package org.example;
import java.io.File;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

public class DatasetCreator {

    static { System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    public static void main(String[] args) {

        int userId = 1;
        int photoCount = 0;

        new File("dataset/user" + userId).mkdirs();

        CascadeClassifier faceDetector =
                new CascadeClassifier("haarcascade_frontalface_default.xml");

        VideoCapture camera = new VideoCapture(0);

        Mat frame = new Mat();
        Mat gray = new Mat();

        while (photoCount < 5) {

            camera.read(frame);
            if (frame.empty()) continue;

            Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);

            MatOfRect faces = new MatOfRect();
            faceDetector.detectMultiScale(gray, faces);

            for (Rect rect : faces.toArray()) {

                Mat face = new Mat(gray, rect);
                Imgproc.resize(face, face, new Size(200,200)); // important

                photoCount++;

                Imgcodecs.imwrite(
                        "dataset/user" + userId + "/" + photoCount + ".jpg",
                        face
                );

                System.out.println("Saved " + photoCount);

                try { Thread.sleep(400); } catch (Exception ignored) {}
            }
        }

        camera.release();
        System.out.println("Dataset Done");
    }
}
