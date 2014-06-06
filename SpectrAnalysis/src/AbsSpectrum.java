import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class AbsSpectrum extends Spectrum{
	
	public AbsSpectrum(String name, char mode)throws IOException{
		String nexLine = "";
		this.name = name;
		this.mode = "Absorbance";
		if(fileName.isEmpty()){	
			if(mode == 's'){
				fileName = "standards//"+name;
			}
			else if(mode == 't'){
				fileName = "experimental//"+name;
			}
			else{
				System.out.println("Invalid file type. Select 's' for standards or 't' for trials.");
			}
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
			dataPoints[i][1] = ((float)(Math.log10((double)(100.0f/dataPoints[i][1]))));
		}
		tempTrial.close();	
	}
	
	public void drawGraph(){
		XYSeries spectrum = new XYSeries("IR Spectrum");
		for(int i = 0; i<numPoints; i++){
			spectrum.add(dataPoints[i][0], dataPoints[i][1]);
		}
		XYSeriesCollection dataset = new XYSeriesCollection(spectrum);
		JFreeChart chart = ChartFactory.createXYLineChart(fileName, "Wavenumber", 
				           mode, dataset);
		
		ChartFrame frame = new ChartFrame(fileName, chart);
		frame.pack();
		frame.setVisible(true);
	}
	
	public ArrayList<Float> findPeaksThr(double threshold){
		ArrayList<Float> myPeaks = new ArrayList<Float>();
		for(int i = 50; i<dataPoints.length-50; i++){
			if(dataPoints[i][1]>=threshold){
				float tempMax = (float)threshold;
				for(int j = i-10; j<i+10; j++){ //check if the maximum of surrounding 20 points
					if(dataPoints[j][1]>tempMax){
						tempMax = dataPoints[j][1];
					}
				}
				if(dataPoints[i][1] == tempMax){
					myPeaks.add(dataPoints[i][0]);
				}
			}
			else{
				continue;
			}
		}
		return myPeaks;
	}
	
}
