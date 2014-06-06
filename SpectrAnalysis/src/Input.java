import java.io.File;
import java.io.IOException;
import java.util.*;

import org.apache.commons.math3.stat.inference.TestUtils;

public class Input {
	public static int sTrialNum = new File("standards").listFiles().length;
	public static int eTrialNum = new File("experimental").listFiles().length;
	//public static int sTrialNum = 5; //number of standard trials
	//public static int eTrialNum = 3; //number of experimental trials
	public static int dataPointsNum = 3600; //number of data points in each spectrum
	
	public static float[][][] buildArrays(int trialNum, char mode)throws IOException{
		float[][][] standardSpectra = new float[trialNum][dataPointsNum][2];
		/*standard trials stored in in 3D array of trials, each trial consisting of
		 * 3600 data points of the form (wavenumber, transmittance). The transmittance 
		 * at wavenumber 4000 would be accessed as standardSpectrum[0][1] for example*/
		
		File folder1 = new File("experimental");
		File[] listfiles1 = folder1.listFiles();
		String[] experimentalFileNames = new String[listfiles1.length];
		for(int i = 0; i<listfiles1.length; i++){
			experimentalFileNames[i] = listfiles1[i].getName();
		}
		
		File folder2 = new File("standards");
		File[] listfiles2 = folder2.listFiles();
		String[] standardFileNames = new String[listfiles2.length];
		for(int i = 0; i<listfiles2.length; i++){
			standardFileNames[i] = listfiles2[i].getName();
		}
		
		for(int k =0; k<trialNum; k++){
			String nexLine = "";
			String fileName = "";
			if(mode == 's'){
				fileName = "standards//"+standardFileNames[k];
			}
			else if(mode == 't'){
				fileName = "experimental//"+experimentalFileNames[k];
			}
			else{
				System.out.println("Invalid file type. Select 's' for standards or 't' for trials.");
				break;
			}
			Scanner tempTrial = new Scanner(new File(fileName));
			while(!nexLine.contentEquals("#DATA")){
				//Brings scanner to start of the data
				nexLine = tempTrial.next();
			}
			for(int i=0;i<standardSpectra[0].length; i++){
				for(int j=0; j<2; j++){
					standardSpectra[k][i][j] = Float.parseFloat(tempTrial.next());
	
				}
			}
			tempTrial.close();
		}
		return standardSpectra;
	}

	//public static float[][] averageSpectra(Spectrum[] standardSpectra){
		/*Takes the standard spectra and averages the data points for each trial.
		 * Returns a 2D float array representing the average of all the standard trials.
		 * Should be used for quantitative samples where the amount of substance run
		 * on IR is standardized.*/
		/*float[][] standardSpectrum = new float[dataPointsNum][2];	
		for(int point=0; point<dataPointsNum; point++){
			float avgAbs = 0.0f;
			for(int trial=0; trial<sTrialNum; trial++){
				avgAbs += standardSpectra[trial][point][1];
			}
			avgAbs /= sTrialNum;
			standardSpectrum[point][0]= standardSpectra[0][point][0];
			standardSpectrum[point][1]= avgAbs;
		}
		return standardSpectrum;
	}
	
	
	public static ArrayList<Float> peaks(Spectrum spectrum, double threshold){
		//int testPeaks = 0;
		ArrayList<Float> myPeaks = new ArrayList<Float>();
		
		for(int i = 50; i<spectrum.getData().length-50; i++){
			if(spectrum.getData()[i][1]<=threshold){
				float tempMin = (float)threshold;
				for(int j = i-10; j<i+10; j++){ //check if the minimum of surrounding 20 points
					if(spectrum.getData()[j][1]<tempMin){
						tempMin = spectrum.getData()[j][1];
					}
				}
				if(spectrum.getData()[i][1] == tempMin){
					myPeaks.add(spectrum.getData()[i][0]);
				}

			}
			else{
				continue;
			}
		}
		return myPeaks;
	}
	*/
	public static float[][][] absCon(float[][][] trialSpectra, int trialNum){
		/*Converts trials from %T to Absorbance*/
		
		float[][][] absorbance = new float[trialNum][dataPointsNum][2];
		for(int i=0; i<trialNum; i++){
			for(int j=0; j<dataPointsNum; j++){
				absorbance[i][j][0] = trialSpectra[i][j][0];
			}
		}
		
		for(int i=0; i<trialNum; i++){
			for(int j=0; j<dataPointsNum; j++){
				
				absorbance[i][j][1] = -1.0f*((float)(Math.log10((double)(100.0f/trialSpectra[i][j][1]))));
				//converts from %T to abs
			}
		}
		return absorbance;
	}
	public static float highestPeak(AbsSpectrum[] standardSpectra, AbsSpectrum[] trialSpectra){
		float hPeak = -150.0f;
		for(int i=0; i<eTrialNum; i++){
			if(trialSpectra[i].getData()[4000-2855][1]>hPeak){
				hPeak = trialSpectra[i].getData()[4000-2855][1];
			}
		}
		for(int i=0; i<sTrialNum; i++){
			if(standardSpectra[i].getData()[4000-2855][1]>hPeak){
				hPeak = standardSpectra[i].getData()[4000-2855][1];
			}
		}
		return hPeak;
	}
		
	public static AbsSpectrum[] normalizeTrials(AbsSpectrum[] trialSpectra, char mode, float highestPeak) throws IOException{
		int trialNum = 0;
		if(mode == 't'){
			trialNum = eTrialNum;
		}
		else if(mode == 's'){
			trialNum = sTrialNum;
		}
		else{}
		
		AbsSpectrum[] normalizedTrials = new AbsSpectrum[trialNum]; 
		
		for(int i=0;i<trialNum; i++){ //populates new array Spectra
			normalizedTrials[i] = new AbsSpectrum(trialSpectra[i].getName(),mode);
		}	
		
		for(int i=0; i<trialNum; i++){
			 //for each trial, find the ratio of the peak at 2855 to highestPeak then multiply
			 //each point in the trial by that ratio and store it as a new entry in the [][][]
			 // that is returned by this method.
			 float standardRatio = trialSpectra[i].getData()[4000-2855][1]/highestPeak;
			 for(int j=0; j<dataPointsNum; j++){ //normalize with ratio
				 normalizedTrials[i].setData(j, (trialSpectra[i].getData()[j][1]/standardRatio));
			 }
		}
		return normalizedTrials;
	}
	
	
	
	/*Write a method that finds the peaks for each trial and compiles them into a 2D array*/
	
	/*
	public static float[][] trialPeaks(float[][][] trialSpectra, int trialNum, double threshold){
		int maxPeaks = 0;
		for(int i=0; i<trialNum; i++){
			ArrayList<Float> temp = peaks(trialSpectra[i], threshold);
			if(temp.size() > maxPeaks){
				maxPeaks = temp.size();
			}
			else{continue;}
		}
		System.out.println(maxPeaks);
		float[][] peakArray = new float[trialNum][maxPeaks];
		for(int j=0; j<trialNum; j++){
			//System.out.println(j);
			ArrayList<Float> temp = peaks(trialSpectra[j], threshold);
			for(int k=0; k<maxPeaks; k++){
				//System.out.println(k);
				peakArray[j][k] = temp.get(k);
			}
		}
		return peakArray;
	}*/
	
	public static float findPeakPos(Spectrum spectrum, int lowerBound, int upperBound){
		/*takes range of wavenumbers and locates the peak in that range. Should not be used on
		 * transmittance spectra*/
		for(int i = 4000 - upperBound; i< 4000 - lowerBound; i++){
			float tempMax = -200;
			for(int j = i-10; j<i+10; j++){ //check if the maximum of surrounding 20 points
				if(spectrum.getData()[j][1]>tempMax){
					tempMax = spectrum.getData()[j][1];
				}
			}
			if(spectrum.getData()[i][1] == tempMax){
				return spectrum.getData()[i][0];
			}
			else{continue;}
		}
		return 0; //no peak found	
	}
	
	public static String testPeakPos(Spectrum[] testSpectra, Spectrum[] trialSpectra, int lowerBound, int upperBound){
		double standard = 0;
		double[] trial = new double[eTrialNum];
		String output ="";
		for(int i=0; i<sTrialNum; i++){
			standard += (double)findPeakPos(testSpectra[i], lowerBound, upperBound);
		}
		standard /= sTrialNum; //average peak position
		
		if(standard == 0.0){
			output+="Standard spectrum does not have a peak in the specified range.\n";
		}
		else{
			output += "Average peak position found at "+standard+"\n";
		}
		
		for(int i=0; i<eTrialNum; i++){
			trial[i] = (double)findPeakPos(trialSpectra[i], lowerBound, upperBound);
		}
		boolean result = TestUtils.tTest(standard, trial, .05);
		double tStat = TestUtils.t(standard, trial);
		output += "t-Statistic: "+tStat+"\nPeak shift significant? "+result;
		return output;
	}
	
	public static String testPeakAmp(Spectrum[] testSpectra, Spectrum[] trialSpectra, int lowerBound, int upperBound){
		double standard = 0;
		double[] trial = new double[eTrialNum];
		String output = "";
		for(int i=0; i<sTrialNum; i++){
			int position = (int)findPeakPos(testSpectra[i], lowerBound, upperBound);
			if(position == 0){
				output = "One or more standard spectra did not have a peak in the specified range.";
				return output;
			}
			standard += testSpectra[i].getData()[4000-position][1];
		}
		standard /= sTrialNum; //average peak position
		for(int i=0; i<eTrialNum; i++){
			int position = (int)findPeakPos(trialSpectra[i], lowerBound, upperBound);
			if(position == 0){
				output = "One or more experimental spectra did not have a peak in the specified range.";
				return output;
			}
			trial[i] = (double)trialSpectra[i].getData()[4000-position][1];
		}
		boolean result = TestUtils.tTest(standard, trial, .05);
		double tStat = TestUtils.t(standard, trial);
		output = "t-Statistic: "+tStat+"\nPeak amplitude change significant? "+result;
		return output;
		
	}
	
	
	//method is identical to testPeakPos but returns the t-statistic instead of a string
	public static double testTStat(Spectrum[] testSpectra, Spectrum[] trialSpectra, int lowerBound, int upperBound){
		double standard = 0;
		double[] trial = new double[eTrialNum];
		String output ="";
		for(int i=0; i<sTrialNum; i++){
			standard += (double)findPeakPos(testSpectra[i], lowerBound, upperBound);
		}
		standard /= sTrialNum; //average peak position
		
		if(standard == 0.0){
			output+="Standard spectrum does not have a peak in the specified range.\n";
		}
		else{
			output += "Average peak position found at "+standard+"\n";
		}
		
		for(int i=0; i<eTrialNum; i++){
			trial[i] = (double)findPeakPos(trialSpectra[i], lowerBound, upperBound);
		}
		boolean result = TestUtils.tTest(standard, trial, .05);
		double tStat = TestUtils.t(standard, trial);
		output += "t-Statistic: "+tStat+"\nPeak shift significant? "+result;
		return tStat;
	}
	
	
	
}
	



