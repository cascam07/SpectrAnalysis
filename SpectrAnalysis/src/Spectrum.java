import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.math3.stat.inference.TestUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public abstract class Spectrum implements Graphable{
	static int sTrialNum = new File("standards").listFiles().length;
	static int eTrialNum = new File("experimental").listFiles().length;
	static int numPoints = 3600; //number of data points in each spectrum
	
	float[][] dataPoints = new float[numPoints][2];
	protected String fileName = "";
	protected String name = "Average Standard Spectrum";
	protected String mode = "";
	
	public Spectrum(){
	
	}
	public Spectrum(String name, char mode)throws IOException{
		String nexLine = "";
		this.name = name;
		if(mode == 's'){
			fileName = /*"standards//"+*/name;
		}
		else if(mode == 't'){
			fileName = /*"experimental//"+*/name;
		}
		else{
			System.out.println("Invalid file type. Select 's' for standards or 't' for trials.");
		}
		Scanner tempTrial = new Scanner(new File(fileName));
		while(!nexLine.contentEquals("#DATA")){
			//Brings scanner to start of the data
			nexLine = tempTrial.next();
		}
		for(int i=0;i<numPoints; i++){
			for(int j=0; j<2; j++){
				dataPoints[i][j] = Float.parseFloat(tempTrial.next());
			}
		}
		tempTrial.close();		
	}
	
	public float[][] getData(){
		return dataPoints;
	}
	
	public void setData(int x, float y){
		dataPoints[x][1] = y;

	}
	
	public String getFileName(){
		return fileName;
	}
	
	public String getName(){
		return name;
	}
	
	public String getMode(){
		return mode;
	}
	
	public void drawGraph(){
		XYSeries spectrum = new XYSeries("IR Spectrum");
		for(int i = 0; i<numPoints; i++){
			spectrum.add(dataPoints[i][0], dataPoints[i][1]);
		}
		XYSeriesCollection dataset = new XYSeriesCollection(spectrum);
		JFreeChart chart = ChartFactory.createXYLineChart(name, "Wavenumber", 
				           mode, dataset);
		
		ChartFrame frame = new ChartFrame(fileName, chart);
		frame.pack();
		frame.setVisible(true);
	}
	
	public ArrayList<Float> findPeaksThr(double threshold){
		ArrayList<Float> myPeaks = new ArrayList<Float>();		
		for(int i = 50; i<dataPoints.length-50; i++){
			if(dataPoints[i][1]<=threshold){
				float tempMin = (float)threshold;
				for(int j = i-10; j<i+10; j++){ //check if the minimum of surrounding 20 points
					if(dataPoints[j][1]<tempMin){
						tempMin = dataPoints[j][1];
					}
				}
				if(dataPoints[i][1] == tempMin){
					myPeaks.add(dataPoints[i][0]);
				}
			}
			else{
				continue;
			}
		}
		return myPeaks;
	}
	
	
	public float findPeakRng(int lowerBound, int upperBound){
		/*takes range of wavenumbers and locates the peak in that range. Should not be used on
		 * normalize spectra*/
		for(int i = 4000 - upperBound; i< 4000 - lowerBound; i++){
			float tempMin = 200;
			for(int j = i-10; j<i+10; j++){ //check if the minimum of surrounding 20 points
				if(dataPoints[j][1]<tempMin){
					tempMin = dataPoints[j][1];
				}
			}
			if(dataPoints[i][1] == tempMin){
				return dataPoints[i][0];
			}
			else{continue;}
		}
		return 0; //no peak found	
	}
	
}
