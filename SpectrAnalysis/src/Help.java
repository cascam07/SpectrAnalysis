import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.JScrollBar;
import java.awt.TextArea;
import javax.swing.JScrollPane;


public class Help {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public void main() { //replace with constructor
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Help window = new Help();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Help() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 432, 386);
		frame.getContentPane().setLayout(null);
		frame.setLocation(500, 150);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 424, 355);
		frame.getContentPane().add(scrollPane);
		
		JTextPane textArea = new JTextPane();
		scrollPane.setViewportView(textArea);
		textArea.setEditable(false);
		textArea.setText("Graph Mode:\r\nSelect the type of graph you would like to produce (transmittance, absorbance, or normalized). Normalized graphs are also in units of absorbance but have been transformed to be easily comparable across spectra.\r\n\r\nGraph (button):  \r\nGraphs the spectrum currently selected from the drop down menu in the currently checked Graph Mode.\r\n\r\nPeak Mode:\r\nSelect the method you would like to use for locating peaks with the Peaks buttons. \r\n\r\nThreshold will return all peaks in the currently selected spectrum that are beyond the value entered in the Peak Threshold text field.\r\nRange will return the location of the first peak located within the range specified in the Peak Range text fields.\r\n\r\nPeaks (button):\r\nReturns a list of peaks from the currently selected spectrum using the currently selected Peak Mode.\r\n\r\nt-Tests:\r\nAmplitude (button):\r\nPerforms a one-sample t-test for significant changes among the experimental spectra compared to the standard spectra.\r\n\r\nPosition (button):\r\nPerforms a one-sample t-test for significant changes among the positions of the first peak found within the Peak Range text field. Compares experimental spectra to standard spectra.\r\n");
	}
}
