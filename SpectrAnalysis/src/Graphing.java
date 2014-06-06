import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
public class Graphing {

	
	public static void drawGraph(float[][] spectra, String title, String axis){
		XYSeries dataPoints = new XYSeries("IR Spectrum");
		for(int i = 0; i<spectra.length; i++){
			dataPoints.add(spectra[i][0], spectra[i][1]);
		}
		XYSeriesCollection dataset = new XYSeriesCollection(dataPoints);
		JFreeChart chart = ChartFactory.createXYLineChart(title, "Wavenumber", 
				           axis, dataset);
		
		ChartFrame frame = new ChartFrame(title, chart);
		frame.pack();
		frame.setVisible(true);
		
	}
	
	public static float[][] flipGraph(float[][] spectrum){
		float[][] negSpec = new float[Input.dataPointsNum][2];
		for(int i=0; i<Input.dataPointsNum; i++){
			negSpec[i][0] = spectrum[i][0];
		}
		for(int i=0; i<Input.dataPointsNum; i++){
			negSpec[i][1] = spectrum[i][1]*(-1);
		}
		return negSpec;
	}
	
	public static float[][][] flipGraphs(float[][][] spectra, int trialNum){
		float[][][] negSpec = new float[trialNum][Input.dataPointsNum][2];
		
		for(int j=0;j<trialNum; j++){ //populates new array with wavenumbers
			for(int i=0; i<Input.dataPointsNum; i++){
				negSpec[j][i][0] = spectra[j][i][0];
			}
			for(int i=0; i<Input.dataPointsNum; i++){
				negSpec[j][i][1] = spectra[j][i][1]*(-1);
			}
		}
		return negSpec;
	}

}
