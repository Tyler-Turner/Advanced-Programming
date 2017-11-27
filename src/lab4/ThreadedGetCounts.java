//Tyler Turner and Aaron Trautman

package lab4;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;

public class ThreadedGetCounts implements Runnable {

	private static List<String> myList = new ArrayList<String>();
	int[] counts = {0,0,0,0,0};
	private final Semaphore semaphore;
	private final String directory;
	private volatile Boolean finished = false;
	
	public static void main(String[] args) throws Exception {
		
		
		File seqDirectory = new File("/Users/Tyler/Desktop/fasta");
		double startTime = System.currentTimeMillis();
		String[] files = seqDirectory.list();
		List<String> fileList = new ArrayList<String>();
		int numThreads = 1;
		int[] finalCounts = {0,0,0,0,0};
		Semaphore semaphore = new Semaphore(numThreads);
		List<int[]> countList = new ArrayList<int[]>();
		
		for (int x = 0; x < files.length ; x++) {
			fileList.add(files[x]);
		}
		
		for (int x = 0; x < numThreads ; x++) {
			semaphore.acquire();
			ThreadedGetCounts th = new ThreadedGetCounts(semaphore, fileList, seqDirectory.getAbsolutePath());
			new Thread(th).start();
			countList.add(th.counts);
		}
		
		int AcqSemaphore = 0;
		
		while (AcqSemaphore < numThreads) {
			semaphore.acquire(); AcqSemaphore++;
		}
		
		for (int x = 0; x < numThreads ; x++) {
			
			for (int y = 0; y < 5 ; y++) {
				
				finalCounts[y] = finalCounts[y] + countList.get(x)[y];
			}
		}
		System.out.println("A: "+ finalCounts[0] + " T: " + finalCounts[1]+ " C: " + finalCounts[2]+ " G: " + finalCounts[3]+ " Unassigned: " + finalCounts[4]);
		System.out.println("Runtime is: " + (System.currentTimeMillis() - startTime)/1000f + " seconds");
	}
	
	public ThreadedGetCounts(Semaphore semaphore, List<String> myList, String directory) {
		this.semaphore = semaphore;
		ThreadedGetCounts.myList = myList;
		this.directory = directory;
	}
	
	@Override
	public void run() {
		try {
			String file = "";
			
			List<FastaSequence> fs = new ArrayList<FastaSequence>();
			
			while (!finished) {
				synchronized (myList) {
					if (myList.size() == 0) {
						this.finished = true;
					}
					else {
						file = myList.get(0);
						myList.remove(0);
						fs = FastaSequence.readFastaFile(directory + "/" + file);
					}
				}
				for (FastaSequence seq : fs) {
					Integer[] cnt = seq.getCounts();
					for (int x = 0; x < cnt.length; x++) {
						counts[x] = counts[x] + cnt[x];
					}
				}
				fs.clear();
			}
			semaphore.release();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exiting");
			System.exit(1);
		}
		
	}
	
}
