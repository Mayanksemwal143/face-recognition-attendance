package org.example;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import static org.bytedeco.opencv.global.opencv_highgui.destroyAllWindows;
import static org.bytedeco.opencv.global.opencv_highgui.imshow;
import static org.bytedeco.opencv.global.opencv_highgui.waitKey;
import static org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR2GRAY;
import static org.bytedeco.opencv.global.opencv_imgproc.FONT_HERSHEY_SIMPLEX;
import static org.bytedeco.opencv.global.opencv_imgproc.cvtColor;
import static org.bytedeco.opencv.global.opencv_imgproc.putText;
import static org.bytedeco.opencv.global.opencv_imgproc.rectangle;
import static org.bytedeco.opencv.global.opencv_imgproc.resize;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;

public class FaceRecognizerApp {

    public static void main(String[] args) {

        LBPHFaceRecognizer recognizer = LBPHFaceRecognizer.create();
        recognizer.read("trainer.yml");

        CascadeClassifier faceDetector =
                new CascadeClassifier("haarcascade_frontalface_default.xml");

        VideoCapture camera = new VideoCapture(0);

        Mat frame = new Mat();
        Mat gray = new Mat();

        while (true) {

            camera.read(frame);
            if (frame.empty()) continue;

            cvtColor(frame, gray, COLOR_BGR2GRAY);

            RectVector faces = new RectVector();
            faceDetector.detectMultiScale(gray, faces);

            for (int i = 0; i < faces.size(); i++) {

                Rect rect = faces.get(i);
                Mat face = new Mat(gray, rect);
                resize(face, face, new Size(200,200)); // 200

                IntPointer label = new IntPointer(1);
                DoublePointer confidence = new DoublePointer(1);

                recognizer.predict(face, label, confidence);

                String name;
                int idVal = label.get(0);

                if (confidence.get(0) < 80) { // 80

                    if (idVal == 1) name = "Mayank";
                    else name = "User " + idVal;

                    ExcelWriter.markAttendance(name, idVal);
                }
                else {
                    name = "Unknown";
                }


                rectangle(frame, rect, new Scalar(0,255,0,0));
                putText(frame, name,
                        new Point(rect.x(), rect.y()-10),
                        FONT_HERSHEY_SIMPLEX,
                        1.0,
                        new Scalar(0,255,0,0));
            }

            imshow("Face Recognition", frame);
            if (waitKey(1) == 27) break;
        }

        camera.release();
        destroyAllWindows();
    }
}
