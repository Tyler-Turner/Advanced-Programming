//Tyler Turner and Aaron Trautman
//Base code provided By Dr. Anthony Fodor

package examples;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class PrimeNumGen extends JFrame
{
	private final JTextArea aTextField = new JTextArea();
	private final JButton primeButton = new JButton("Start");
	private final JButton cancelButton = new JButton("Cancel");
	private volatile static boolean cancel = false;
	private volatile static boolean finished = false;
	private volatile static long startTime;
	private final PrimeNumGen thisFrame;
	private static List<Integer> numList = new ArrayList<Integer>();
	private static List<Integer>  primeList = Collections.synchronizedList(new ArrayList<Integer>());
	
	public static void main(String[] args)
	{
		PrimeNumGen png = new PrimeNumGen("Prime Number Generator");
		
		png.addActionListeners();
		png.setVisible(true);
		
	}
	
	private PrimeNumGen(String title)
	{
		super(title);
		this.thisFrame = this;
		cancelButton.setEnabled(false);
		aTextField.setEditable(false);
		setSize(400, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(primeButton,  BorderLayout.SOUTH);
		getContentPane().add(cancelButton,  BorderLayout.EAST);
		getContentPane().add( new JScrollPane(aTextField),  BorderLayout.CENTER);
	}
	
	private class CancelOption implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0)
		{
			cancel = true;
		}
	}
	
	private void addActionListeners()
	{
		cancelButton.addActionListener(new CancelOption());
	
		primeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					String num = JOptionPane.showInputDialog("Enter a large integer");
					Integer max = null;
					int numThreads =5;
					primeList.clear();
					numList.clear();
					aTextField.setText("");
					
					try
					{
						max = Integer.parseInt(num);
						for (int x = 1; x < max; x++) {
							numList.add(x);
						}
					}
					catch(Exception ex)
					{
						JOptionPane.showMessageDialog(
								thisFrame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						ex.printStackTrace();
					}
					
					if(max != null)
					{
						primeButton.setEnabled(false);
						cancelButton.setEnabled(true);
						cancel = false;
						finished = false;
						
						startTime = System.currentTimeMillis();
						
						for (int x = 1; x < numThreads + 1; x++) 
						new Thread(new UserInput(max,x)).start();

						
						System.out.println("done");
					}}});
		}
	
	private boolean isPrime( int i)
	{
		for( int x=2; x < i -1; x++)
			if( i % x == 0  )
				return false;
		
		return true;
	}
	
	private class UserInput implements Runnable {
		private final int max;
		private final int threadNum;
		
		private UserInput(int num, int threadNum ) {
			this.max = num;
			this.threadNum = threadNum;
		}
		
		public void run() {
			long lastUpdate = System.currentTimeMillis();
			while (!finished && !cancel) {
				Integer num = null;
				synchronized(numList) {
				if (numList.size() == 0) {
					finished = true;
				}
				else {
						num = numList.get(0);
						numList.remove(0);
					}
				}

				if( num != null && isPrime(num) && !cancel) {
					primeList.add(num);
				}
				
				if (this.threadNum == 1) {
					if( System.currentTimeMillis() - lastUpdate > 500 )
					{
						final String outString = "Found " + primeList.size() + " in " + num + " of " + max;
						
						SwingUtilities.invokeLater( new Runnable()
						{
							@Override
							public void run()
							{
								aTextField.setText(outString);
							}
						});
						
						lastUpdate = System.currentTimeMillis();	
					}
				}
			}
			
			if (this.threadNum == 1) {
			StringBuffer buff = new StringBuffer();
			
			SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					Collections.sort(primeList);
					for( Integer i2 : primeList)
						buff.append(i2 + "\n");
					
					if( cancel)
						buff.append("cancelled");
					
					cancel = false;
					primeButton.setEnabled(true);
					cancelButton.setEnabled(false);
					aTextField.setText( (cancel ? "cancelled " : "") + buff.toString());
					System.out.println("Runtime is: " + (System.currentTimeMillis() - startTime)/1000f + " seconds");
				}
			});
			}
			//semi.release();
			
			
		}// end run
		
	}  // end UserInput
}

