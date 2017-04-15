package com.example.detectcircle;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import org.opencv.core.Core;  
import org.opencv.core.Mat;   
import org.opencv.core.MatOfPoint3;
import org.opencv.core.Point;  
import org.opencv.core.Point3;
import org.opencv.core.Scalar;  
import org.opencv.core.Size;  
import org.opencv.highgui.VideoCapture;  
import org.opencv.imgproc.Imgproc;  
import org.opencv.core.CvType;  

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        class ball {
        	public ArrayList<int[]> balls = new ArrayList<int[]>();
        	public void main(String arg[]){
        	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        	VideoCapture capture =new VideoCapture(0);
        	Mat webcam_image=new Mat();
        	Mat hsv_image=new Mat();
        	Mat thresholded=new Mat();
        	Mat thresholded2=new Mat();
        	capture.read(webcam_image);
        	Mat array255=new Mat(webcam_image.height(),webcam_image.width(),CvType.CV_8UC1);
        	array255.setTo(new Scalar(255));
        	Mat distance=new Mat(webcam_image.height(),webcam_image.width(),CvType.CV_8UC1);
        	List<Mat> lhsv = new ArrayList<Mat>(3);
        	MatOfPoint3 circles = new MatOfPoint3(); // No need (and don't know how) to initialize it.
        	// The function later will do it... (to a 1*N*CV_32FC3)
        	Scalar hsv_min = new Scalar(0, 50, 50, 0);
        	Scalar hsv_max = new Scalar(6, 255, 255, 0);
        	Scalar hsv_min2 = new Scalar(175, 50, 50, 0);
        	Scalar hsv_max2 = new Scalar(179, 255, 255, 0);
        	if( capture.isOpened()) {
        	while( true ) {
        	capture.read(webcam_image);
        	if( !webcam_image.empty() ) {
        	Imgproc.cvtColor(webcam_image, hsv_image, Imgproc.COLOR_BGR2HSV);
        	Core.inRange(hsv_image, hsv_min, hsv_max, thresholded);
        	Core.inRange(hsv_image, hsv_min2, hsv_max2, thresholded2);
        	Core.bitwise_or(thresholded, thresholded2, thresholded);
        	Core.split(hsv_image, lhsv); // We get 3 2D one channel Mats
        	Mat S = lhsv.get(1);
        	Mat V = lhsv.get(2);
        	Core.subtract(array255, S, S);
        	Core.subtract(array255, V, V);
        	S.convertTo(S, CvType.CV_32F);
        	V.convertTo(V, CvType.CV_32F);
        	Core.magnitude(S, V, distance);
        	Core.inRange(distance,new Scalar(0.0), new Scalar(200.0), thresholded2);
        	Core.bitwise_and(thresholded, thresholded2, thresholded);
        	// Apply the Hough Transform to find the circles
        	Imgproc.GaussianBlur(thresholded, thresholded, new Size(9,9),0,0);
        	Imgproc.HoughCircles(thresholded, circles, Imgproc.CV_HOUGH_GRADIENT, 2, thresholded.height()/4, 500, 50, 0, 0);
        	//Imgproc.Canny(thresholded, thresholded, 500, 250);
        	//-- 4. Add some info to the image
        	for (Point3 circle : circles.toArray()){
        	int x = (int) circle.x;
        	int y = (int) circle.y;
        	int z = (int) Math.round(circle.z);
        	balls.add(new int[] {x,y,z});
        	}
        	System.out.println(balls);
        	}
        	else
        	{
        	System.out.println(" --(!) No captured frame -- Break!");
        	break;
        	}
        	}
        	}
        	return;
        	}
        	} 
        
        
    }


}
