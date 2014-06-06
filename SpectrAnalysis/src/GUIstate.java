
public class GUIstate {
	
	private String graphmode;
	private String peakMode;
	private String graphSelected;
	private int lowerBound;
	private int upperBound;
	private double threshold;
	
	GUIstate(){
		graphmode = "Transmittance";
		peakMode = "Threshold";
		graphSelected = "standard";
		lowerBound = 0;
		upperBound = 0;
		threshold = 0;
	}
	
	public void setGraphMode(String selectedMode){
		graphmode = selectedMode;
	}
	public String getGraphMode(){
		return graphmode;
	}
	public void setPeakMode(String selectedMode){
		peakMode = selectedMode;
	}
	public String getPeakMode(){
		return peakMode;
	}
	public void setGraph(String selectedGraph){
		graphSelected = selectedGraph;
	}
	public String getGraph(){
		return graphSelected;
	}
	public void setLowerBound(String lowerString){
		lowerBound = Integer.parseInt(lowerString);
	}
	public int getLowerBound(){
		return lowerBound;
	}
	public void setUpperBound(String upperString){
		upperBound = Integer.parseInt(upperString);
	}
	public int getUpperBound(){
		return upperBound;
	}
	public void setThreshold(String thr){
		threshold = Double.parseDouble(thr);
	}
	public double getThreshold(){
		return threshold;
	}

}
