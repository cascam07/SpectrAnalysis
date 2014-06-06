import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.border.MatteBorder;

import java.awt.Color;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.AbstractAction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JSlider;
import javax.swing.JEditorPane;
import javax.swing.JTextPane;


public class GUI extends Input {

	private JFrame frmSpectranalysis;
	private JTextField textField_lower;
	private JTextField textField_upper;
	private JTextPane textField_out;
	
	public int graphIndex(String graphSelected, String[] fileNames){
		/*Finds and returns the index of the selected graph to be used by graphing method*/
		for(int i=0; i<fileNames.length; i++){
			if(fileNames[i].startsWith(graphSelected)){
				return i;
			}
			else{
				continue;
			}
		}
		return -1; //should produce error
	}
	

	public static String mode;
	private JTextField textField_thr;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frmSpectranalysis.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() throws IOException{
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() throws IOException{
		frmSpectranalysis = new JFrame();
		frmSpectranalysis.setTitle("SpectrAnalysis");
		frmSpectranalysis.setBounds(100, 100, 379, 377);
		frmSpectranalysis.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSpectranalysis.getContentPane().setLayout(null);
		
		//reads file names from directory
		File folder1 = new File("experimental");
		File[] listfiles1 = folder1.listFiles();
		final String[] experimentalFileNames = new String[listfiles1.length];
		for(int i = 0; i<listfiles1.length; i++){
			experimentalFileNames[i] = listfiles1[i].getName();
		}
		
		File folder2 = new File("standards");
		File[] listfiles2 = folder2.listFiles();
		final String[] standardFileNames = new String[listfiles2.length];
		for(int i = 0; i<listfiles2.length; i++){
			standardFileNames[i] = listfiles2[i].getName();
		}
		
		//Make arrays of spectra
		final TransSpectrum[] transStandards = new TransSpectrum[sTrialNum];
		final TransSpectrum[] transExperiments = new TransSpectrum[eTrialNum];
		final AbsSpectrum[] absStandards = new AbsSpectrum[sTrialNum];
		final AbsSpectrum[] absExperiments = new AbsSpectrum[eTrialNum];
		
		for(int i=0; i<sTrialNum; i++){
			transStandards[i] = new TransSpectrum(standardFileNames[i], 's');
			absStandards[i] = new AbsSpectrum(standardFileNames[i], 's');
		}
		for(int i=0; i<eTrialNum; i++){
			transExperiments[i] = new TransSpectrum(experimentalFileNames[i], 't');
			absExperiments[i] = new AbsSpectrum(experimentalFileNames[i], 't');
		}		
		float hPeak = highestPeak(absStandards, absExperiments); //highest peak among standards and trials

		final AbsSpectrum[] normStandards = normalizeTrials(absStandards,'s',hPeak);
		final AbsSpectrum[] normExperiments = normalizeTrials(absExperiments,'t',hPeak);
		
		
		final AvgSpectrum avgTransStandard = new AvgSpectrum("Average Standard Spectrum", transStandards);
		final AvgAbsSpectrum avgAbsStandard = new AvgAbsSpectrum("Average Standard Spectrum", absStandards);
		final AvgAbsSpectrum normStandard = new AvgAbsSpectrum("Normalized Standard Spectrum", normStandards);
		
		final GUIstate state = new GUIstate(); //holds the state of the GUI
		final Help helpWindow = new Help(); //help window with user information
		
		
		JLabel lblAbsorbance = new JLabel("Absorbance");
		lblAbsorbance.setBounds(263, 71, 78, 14);
		frmSpectranalysis.getContentPane().add(lblAbsorbance);
		
		JLabel lblTransmittance = new JLabel("Transmittance");
		lblTransmittance.setBounds(263, 46, 98, 14);
		frmSpectranalysis.getContentPane().add(lblTransmittance);
		
		JButton btnGraph = new JButton("Graph");
		btnGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String title = state.getGraph();
				if(title.startsWith("standard")){
					if(state.getGraphMode().startsWith("Transmittance")){					
						avgTransStandard.drawGraph();
						
					}
					else if(state.getGraphMode().startsWith("Absorbance")){
						avgAbsStandard.drawGraph();
					}
					else if(state.getGraphMode().startsWith("Normalized")){
						normStandard.drawGraph();
					}
				}
				else{
					int gIndex = graphIndex(title, experimentalFileNames);
					if(state.getGraphMode().startsWith("Transmittance")){					
						transExperiments[gIndex].drawGraph();
					}
					else if(state.getGraphMode().startsWith("Absorbance")){
						absExperiments[gIndex].drawGraph();
					}
					else if(state.getGraphMode().startsWith("Normalized")){
						normExperiments[gIndex].drawGraph();
					}
				}
			}
		});
		btnGraph.setBounds(10, 21, 99, 23);
		frmSpectranalysis.getContentPane().add(btnGraph);
		
		JButton btnPeaks = new JButton("Peaks");
		btnPeaks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String title = state.getGraph();
				ArrayList<Float> myPeaks;
				
				if(state.getPeakMode().startsWith("Threshold")){
					state.setThreshold(textField_thr.getText());
					if(title.startsWith("standard")){
						if(state.getGraphMode().startsWith("Transmittance")){					
							myPeaks = avgTransStandard.findPeaksThr(state.getThreshold());
						}
						else if(state.getGraphMode().startsWith("Absorbance")){
							myPeaks = avgAbsStandard.findPeaksThr(state.getThreshold()); 
						}
						else{
							myPeaks = normStandard.findPeaksThr(state.getThreshold());
						}
					}
					else{
						int gIndex = graphIndex(title, experimentalFileNames);
						if(state.getGraphMode().startsWith("Transmittance")){					
							myPeaks = transExperiments[gIndex].findPeaksThr(state.getThreshold());
						}
						else if(state.getGraphMode().startsWith("Absorbance")){
							myPeaks = absExperiments[gIndex].findPeaksThr(state.getThreshold());
						}
						else{
							myPeaks = normExperiments[gIndex].findPeaksThr(state.getThreshold());
						}
					}
					textField_out.setText("Peak Locations: "+myPeaks);
				}
				else if(state.getPeakMode().startsWith("Range")){
					double peakPos;
					state.setUpperBound(textField_upper.getText());
					state.setLowerBound(textField_lower.getText());
					if(title.startsWith("standard")){
						peakPos = findPeakPos(avgAbsStandard, state.getLowerBound(), state.getUpperBound());
					}
					else{
						int gIndex = graphIndex(title, experimentalFileNames);
						peakPos = findPeakPos(absExperiments[gIndex], state.getLowerBound(), state.getUpperBound());
					}
					textField_out.setText("Peak Position: "+peakPos);
				}

			}
		});
		btnPeaks.setBounds(10, 55, 99, 23);
		frmSpectranalysis.getContentPane().add(btnPeaks);
		
		JLabel lblGraphMode = new JLabel("Graph Mode");
		lblGraphMode.setBounds(253, 21, 79, 14);
		frmSpectranalysis.getContentPane().add(lblGraphMode);
		

		//Generates graphing mode radio buttons
		JRadioButton rdbtnTransmittance = new JRadioButton("Trasmittance");
		rdbtnTransmittance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				state.setGraphMode("Transmittance");
			}
		});
		rdbtnTransmittance.setSelected(true);
		rdbtnTransmittance.setBounds(244, 42, 19, 23);
		frmSpectranalysis.getContentPane().add(rdbtnTransmittance);
		
		JRadioButton rdbtnAbsorbance = new JRadioButton("Absorbance");
		rdbtnAbsorbance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				state.setGraphMode("Absorbance");
			}
		});
		rdbtnAbsorbance.setBounds(244, 68, 19, 23);
		frmSpectranalysis.getContentPane().add(rdbtnAbsorbance);
		
		
		JRadioButton rdbtnNormalized = new JRadioButton("Normalized");
		rdbtnNormalized.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				state.setGraphMode("Normalized");
			}
		});
		rdbtnNormalized.setBounds(244, 92, 109, 23);
		frmSpectranalysis.getContentPane().add(rdbtnNormalized);
		
		
		JRadioButton rdbtnThreshold = new JRadioButton("Threshold");
		rdbtnThreshold.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				state.setPeakMode("Threshold");
			}
		});
		rdbtnThreshold.setSelected(true);
		rdbtnThreshold.setBounds(244, 161, 109, 23);
		frmSpectranalysis.getContentPane().add(rdbtnThreshold);
		
		JRadioButton rdbtnRange = new JRadioButton("Range");
		rdbtnRange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				state.setPeakMode("Range");
			}
		});
		rdbtnRange.setBounds(244, 187, 109, 23);
		frmSpectranalysis.getContentPane().add(rdbtnRange);
		
		//groups buttons
		ButtonGroup group1 = new ButtonGroup();
		group1.add(rdbtnAbsorbance);
		group1.add(rdbtnTransmittance);
		group1.add(rdbtnNormalized);
		
		ButtonGroup group2 = new ButtonGroup();
		group2.add(rdbtnThreshold);
		group2.add(rdbtnRange);
		
		//Populates drop down menu for spectrum selection
		String[] graphOptions = new String[eTrialNum+1]; //+1 slot for the standard graph
		graphOptions[0] = "standard";
		for(int j=0; j<=eTrialNum-1; j++){
			graphOptions[j+1] = experimentalFileNames[j];
		}
		final JComboBox graphList = new JComboBox(graphOptions);
		graphList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int item = graphList.getSelectedIndex();
				if(item == 0){
					state.setGraph("standard");
				}
				else{
					state.setGraph(experimentalFileNames[item-1]);
				}
			}
		});

		graphList.setBounds(123, 22, 109, 20);
		frmSpectranalysis.getContentPane().add(graphList);
		
		JLabel lblPeakRange = new JLabel("Peak Range");
		lblPeakRange.setBounds(45, 213, 78, 14);
		frmSpectranalysis.getContentPane().add(lblPeakRange);
		
		textField_lower = new JTextField();
		textField_lower.setBounds(10, 232, 51, 20);
		frmSpectranalysis.getContentPane().add(textField_lower);
		textField_lower.setColumns(10);
		
		textField_upper = new JTextField();
		textField_upper.setBounds(86, 232, 51, 20);
		frmSpectranalysis.getContentPane().add(textField_upper);
		textField_upper.setColumns(10);
		
		JLabel lblTo = new JLabel("to");
		lblTo.setBounds(67, 235, 19, 14);
		frmSpectranalysis.getContentPane().add(lblTo);
		
		textField_out = new JTextPane();
		textField_out.setBounds(10, 263, 337, 55);
		frmSpectranalysis.getContentPane().add(textField_out);
		
		JLabel lblOutput = new JLabel("Output");
		lblOutput.setBounds(158, 247, 46, 14);
		frmSpectranalysis.getContentPane().add(lblOutput);
		
		JLabel lblTtest = new JLabel("t-Test");
		lblTtest.setBounds(15, 89, 46, 14);
		frmSpectranalysis.getContentPane().add(lblTtest);
		
		JButton btnNewButton = new JButton("Amplitude");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				state.setLowerBound(textField_lower.getText());
				state.setUpperBound(textField_upper.getText());
				String tTestResult = testPeakAmp(normStandards, normExperiments, state.getLowerBound(), state.getUpperBound());
				textField_out.setText(tTestResult);
			}
		});
		btnNewButton.setBounds(10, 107, 99, 23);
		frmSpectranalysis.getContentPane().add(btnNewButton);
		
		JButton btnPosition = new JButton("Position");
		btnPosition.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				state.setLowerBound(textField_lower.getText());
				state.setUpperBound(textField_upper.getText());
				String tTestResult = testPeakPos(absStandards, absExperiments, state.getLowerBound(), state.getUpperBound());
				textField_out.setText(tTestResult);	
			}
		});
		btnPosition.setBounds(10, 137, 99, 23);
		frmSpectranalysis.getContentPane().add(btnPosition);
		
		textField_thr = new JTextField();
		textField_thr.setBounds(10, 190, 86, 20);
		frmSpectranalysis.getContentPane().add(textField_thr);
		textField_thr.setColumns(10);
		
		JLabel lblPeakThreshold = new JLabel("Peak Threshold");
		lblPeakThreshold.setBounds(10, 176, 89, 14);
		frmSpectranalysis.getContentPane().add(lblPeakThreshold);
		
		JLabel lblPeakMode = new JLabel("Peak Mode");
		lblPeakMode.setBounds(263, 140, 78, 14);
		frmSpectranalysis.getContentPane().add(lblPeakMode);
		
		JButton btnHelp = new JButton("Help");
		btnHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				helpWindow.main();
			}
		});
		btnHelp.setBounds(244, 231, 72, 23);
		frmSpectranalysis.getContentPane().add(btnHelp);
		


	}
	
	
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
}
