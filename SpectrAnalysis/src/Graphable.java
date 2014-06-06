import java.util.ArrayList;


public interface Graphable {
	
	void drawGraph();
	ArrayList<Float> findPeaksThr(double threshold);
	float findPeakRng(int lowerBound, int upperBound);
	
	float[][] getData();
	void setData(int x, float y);
	String getFileName();
	String getName();
	
}
