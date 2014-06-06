import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;



public class AvgSpectrum extends Spectrum{
	
	public AvgSpectrum(String name, Spectrum[] spectra) throws IOException {
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
	
}
