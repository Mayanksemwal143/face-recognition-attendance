package org.example;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacpp.IntPointer;
import static org.bytedeco.opencv.global.opencv_core.CV_32SC1;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;

public class FaceTrainer {

    public static void main(String[] args) {

        File dataset = new File("dataset");
        File[] users = dataset.listFiles();

        if (users == null) {
            System.out.println("Dataset folder missing");
            return;
        }

        List<Mat> imgList = new ArrayList<>();
        List<Integer> labelList = new ArrayList<>();

        for (File user : users) {

            if (!user.isDirectory()) continue;

            int label = Integer.parseInt(
                    user.getName().replace("user", "")
            );

            File[] photos = user.listFiles();
            if (photos == null) continue;

            for (File img : photos) {

                Mat m = imread(img.getAbsolutePath(), 0);

                if (!m.empty()) {
                    imgList.add(m);
                    labelList.add(label);
                }
            }
        }

        int count = imgList.size();

        if (count == 0) {
            System.out.println("No images found!");
            return;
        }

        MatVector images = new MatVector(count);
        IntPointer labels = new IntPointer(count);

        for (int i = 0; i < count; i++) {

            images.put(i, imgList.get(i));
            labels.put(i, labelList.get(i));
        }

        Mat labelMat = new Mat(count, 1, CV_32SC1, labels);

        LBPHFaceRecognizer recognizer =
                LBPHFaceRecognizer.create();

        recognizer.train(images, labelMat);

        recognizer.save("trainer.yml");

        System.out.println(
                "Training Done with " + count + " images"
        );
    }
}