//Tyler Turner and Aaron Trautman

package lab7;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class GuiAminoAcid extends JFrame{
	
	private JTextField qArea = new JTextField();
	private JTextField aArea = new JTextField();
	private JTextField timeArea = new JTextField();
	private JTextField scoreArea = new JTextField();
	private JButton startButton = new JButton("Start");
	private JButton resetButton = new JButton("Reset");
	private JLabel entryLabel = new JLabel("Enter Amino Acid codon in the field below:");
	private JTextField feedbackField = new JTextField();
	private volatile Boolean interrupt = false;
	private Random random = new Random();
	private int score = 0;
	private int aminoCode= 0;
	private volatile Boolean stop = false;
	private String[] aaLetters = {
				"A", "R", "N", "D", "C", "Q", "E",
				"G", "H", "I", "L", "K", "M", "F",
				"P", "S", "T", "W", "Y", "V"
				};
	private String[] aaNames = {
				"Alanine", "Arginine", "Asparagine",
				"Aspartic acid", "Cysteine",
				"Glutamine", "Glutamic acid",
				"Glycne", "Histidine", "Isoleucine",
				"Leucine", "Lysine", "Mehionine",
				"Phenylalanine", "Proline", "Serine",
				"Threonine", "Tryptophan",
				"Tyrosine", "Valine"
	};
	
	private JPanel getBottomPanel() {
		JPanel panel = new JPanel();
		panel.add(startButton);
		panel.add(resetButton);
		panel.setLayout(new GridLayout(0,3));
		startButton.addActionListener(new StartActionListener());
		resetButton.addActionListener(new ResetActionListener());
		
		return panel;
	}
	
	private JPanel getTopPanel() {
		JPanel panel = new JPanel();
		panel.add(scoreArea);
		panel.add(timeArea);
		panel.setLayout(new GridLayout (0,2));
		scoreArea.setEditable(false);
		timeArea.setEditable(false);
		panel.setBackground(Color.WHITE);
		return panel;
	}
	
	private JPanel getCenterPanel() {
		JPanel panel = new JPanel();
		panel.add(qArea);
		panel.add(aArea);
		panel.setLayout(new GridLayout (4,0));
		panel.add(entryLabel);
		panel.add(feedbackField);
		panel.setBorder(new EmptyBorder(50,10,50,10));
		panel.setBackground(Color.DARK_GRAY);
		feedbackField.setBackground(Color.DARK_GRAY);
		feedbackField.setForeground(Color.WHITE);
		entryLabel.setBackground(Color.DARK_GRAY);
		entryLabel.setForeground(Color.WHITE);
		qArea.setEditable(false);
		aArea.addActionListener(new InputActionListener());
		aArea.setEditable(false);
		feedbackField.setEditable(false);
		return panel;
	}
	
	private class StartActionListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			score = 0;
			interrupt = false;
			startButton.setEnabled(false);
			aArea.setEditable(true);
			new Thread (new TimerRunnable()).start();
			updateScoreField();
			aminoAcidGetter();
				
		}
	}
	
	private class TimerRunnable implements Runnable {
		public void run() {
			try {
				int duration = 30;
				while (duration !=0) {
					timeArea.setText("Time left: " + duration + " seconds");
					duration--;
					Thread.sleep(1000);
				}
				timeArea.setText("Time left: " + duration + " seconds");
			}
				catch(Exception e) {
					timeArea.setText(e.getMessage());
					e.printStackTrace();
				}
				try {
					SwingUtilities.invokeAndWait(new Runnable() {
						public void run() {
						aArea.setEnabled(false);
						}
					});
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	
	
	private class InputActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if ((aArea.getText()).toUpperCase().equals(aaLetters[aminoCode])){
				score +=1;
				feedbackField.setText("Correct!");
			}
			else {
				feedbackField.setText("Sorry, the code for " + aaNames[aminoCode] + " is " + aaLetters[aminoCode]);
			}
			aminoAcidGetter();
		}
	}
	
	private class ResetActionListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			score = 0;
			interrupt = true;
			updateScoreField();
			startButton.setEnabled(true);
			resetButton.setEnabled(false);
			qArea.setText("Click Start to begin quiz");
			aArea.setText("");
			timeArea.setText("");
			validate();
		}
	}
	
	private void updateTextField(String aa) {
		qArea.setText("What is the one letter abbreviation of: " + aa);
		validate();
	}
	
	private void updateScoreField() {
		scoreArea.setText("Score: " + score);
	}
	
	public GuiAminoAcid() {
		
		super("Amino Acid Quiz");
		
		setSize(350,350);
		
		setLocationRelativeTo(null);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		getContentPane().setLayout(new BorderLayout());
		
		getContentPane().add(getBottomPanel(), BorderLayout.SOUTH);
		
		getContentPane().add(getCenterPanel(), BorderLayout.CENTER);
		
		getContentPane().add(getTopPanel(), BorderLayout.NORTH);
		
		qArea.setText("Click Start to begin quiz");
		setVisible(true);
	}
			
	public static void main(String[] args) {
		new GuiAminoAcid();
	}
	
	public void aminoAcidGetter() {
		aminoCode = random.nextInt(20);
		aArea.setText("");
		updateScoreField();
		updateTextField(aaNames[aminoCode]);
	}

}
