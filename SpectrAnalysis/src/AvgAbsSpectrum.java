import java.io.IOException;
import java.util.ArrayList;
/*
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
*/

public class AvgAbsSpectrum extends Spectrum{
	
	public AvgAbsSpectrum(String name, Spectrum[] spectra) throws IOException {
		//super(name, 's');
		this.name = name;
		this.mode = spectra[0].getMode(); 
		dataPoints = averageSpectra(spectra);
	}

	public static float[][] averageSpectra(Spectrum[] standardSpectra){
		/*Takes the standard spectra and averages the data points for each trial.
		 * Returns a 2D float array representing the average of all the standard trials.
		 * Should be used for quantitative samples where the amount of substance run
		 * on IR is standardized.*/
		float[][] standardSpectrum = new float[numPoints][2];	
		for(int point=0; point<numPoints; point++){
			float avgAbs = 0.0f;
			for(int trial=0; trial<sTrialNum; trial++){
				avgAbs += standardSpectra[trial].getData()[point][1];
			}
			avgAbs /= sTrialNum;
			standardSpectrum[point][0]= (standardSpectra[0].getData()[point][0]);
			standardSpectrum[point][1]= avgAbs;
		}
		return standardSpectrum;
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
