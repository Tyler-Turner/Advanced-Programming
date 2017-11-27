//Tyler Turner and Aaron Trautman
//Aaron Trautman helped me shorten my logic down a bit during the parsing loop
package lab4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.text.html.HTMLDocument.Iterator;

public class FastaSequence {
	
	private final String header;
	private final String sequence;
	
	
//constructor
	public FastaSequence(String header, String sequence) {
		
		this.header = header;
		this.sequence = sequence;
	}

	public static void main(String[] args) throws Exception {
	//create new list to contain the file 
		//List<FastaSequence> fastaList = 
			//	FastaSequence.readFastaFile("C:\\Users\\Tyler\\Desktop\\seqsIn.txt");
		
	/*	File sequenceDirectory = new File("C:\\Users\\Tyler\\Desktop\\fasta");
		String[] filenames = sequenceDirectory.list();
		for(String filename : filenames) {
			if(filename.endsWith(".fas")) {
				String seqFile = sequenceDirectory.getAbsolutePath();
				System.out.println(seqFile);
				List<FastaSequence> fastaList = 
						FastaSequence.readFastaFile(seqFile);
				
				for( FastaSequence fs : fastaList)
				{
				
				System.out.println(fs.getCount());
				System.out.println("\n");
				}
			}
			
		}
	
		File input = new File("C:\\Users\\Tyler\\Desktop\\seqsIn.txt");
		File output = new File("C:\\Users\\Tyler\\Desktop\\output.tsv");
		
		//writeSpreadSheet(input,output);
	*/ }
	
	//factory method to parse file being fed into it
	public static List<FastaSequence> readFastaFile(String filepath) throws Exception{
		//create new list
		List<FastaSequence> myList = new ArrayList<FastaSequence>();
		//create new buffered reader to read in the file
		BufferedReader reader = 
				new BufferedReader(new FileReader(new File(filepath)));
		//create blank variables to hold values for header and sequence and keep track of where we are in the file
		String header = null;
		String sequence = "";
		
		//read through the file
		for(String s = reader.readLine(); s!=null; s=reader.readLine()) {
			//if header is blank, its the first time through the file and get header
			if(header == null) {
				if(s.startsWith(">")) {
					header = s.substring(1);
				}	
			
			}
			//otherwise create a new fastasequence file and add it to the list, set the header and reset sequence
			else if(s.startsWith(">")) {
					FastaSequence fs = new FastaSequence(header, sequence);
					myList.add(fs);
					header = s.substring(1);
					sequence = "";
				}
				
			//last case will only be sequence so assign to variable
			else {
				sequence = sequence + s;
			}
			
		}
		//last instance of sequence in file is stuck in the buffer, so create one last fastasequence
		//and add it to the list, otherwise it will never add the last item in the list
		FastaSequence fs = new FastaSequence(header, sequence);
		myList.add(fs);
		reader.close();
		return myList;
	}
	
	public static void writeUnique(File inFile, File outFile ) throws Exception {
		
		//use readfastafile method to read in file and put into list, and make a file writer
		List<FastaSequence> myList = readFastaFile(inFile.getAbsolutePath());
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		//create the hashmap to store the sequences and the count 
		HashMap<String,Integer> myHashMap = new HashMap<String, Integer>();
		
		String seq= "";
		
		
		//scan through the list and put in the sequences as well as increment the count for ones already there
		for(FastaSequence sequence : myList) {
			seq = sequence.getSequence();
			if(!myHashMap.containsKey(seq)) {
				myHashMap.put(seq, 1);
			}
			else {
				myHashMap.put(seq, myHashMap.get(seq)+1);
			}
			
		}
		
		//tester loop to print to the console 
		for(String key : myHashMap.keySet()) {
			System.out.println(key + " " + myHashMap.get(key));
	
		}
		
		//make an array list to store the values and use this to sort them
		List <Entry<String,Integer>>myArrayList = new ArrayList(myHashMap.entrySet());
		
		//sort the array list
		myArrayList.sort((o1,o2)->((Comparable<Integer>)((Map.Entry)(o1)).getValue()).compareTo((Integer)((Map.Entry)(o2)).getValue()));
				
		//write the output to a file 
			for(Entry<String,Integer> data : myArrayList) {
				writer.write(">" + data.getValue());
				writer.newLine();
				writer.write(data.getKey());
				writer.newLine();
			}
			
			writer.close();
			
		}

	public static void writeSpreadSheet(File inFile, File outFile ) throws Exception {
		
		//use readfastafile method to read in file and put into list, and make a file writer
		List<FastaSequence> myList = readFastaFile(inFile.getAbsolutePath());
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
	
		LinkedHashMap<String, Map<String,Integer>> finalHashMap = new LinkedHashMap<String, Map<String,Integer>>();
		
		List<String> tokens = new ArrayList<String>();
		
		for(FastaSequence fs: myList) {
			String token = fs.getHeader().split("\\s")[1];
			if(!tokens.contains(token))
				tokens.add(token);
		}
		
		Collections.sort(tokens);
		tokens.add(0,"Sample");
		
		for(FastaSequence fs: myList) {
			String seq = fs.getSequence();
			Map<String, Integer> initMap = new HashMap<String, Integer>();	
		
		
			for(String token : tokens) {
				initMap.put(token,0);
			}
			finalHashMap.put(seq, initMap);
		}
		
		for(FastaSequence fs: myList) {
			String seq = fs.getSequence();
			String header = fs.getHeader().split("\\s")[1];
			
			Map<String, Integer> seqThing = finalHashMap.get(seq);
			
			for(String key: seqThing.keySet()) {
				if(key.equals(header)) {
					seqThing.put(header, seqThing.get(key) + 1);
				}
			}
			
			finalHashMap.put(seq, seqThing);
		}
		
		for(String token : tokens) {
			StringBuffer line = new StringBuffer();
			line.append(token + "\t");
			
			for(String key: finalHashMap.keySet()) {
				if(token.equals("Sample")) {
					line.append(key + "\t");
				}
				else {
					Map<String, Integer> tokenMap = finalHashMap.get(key);
					int count = tokenMap.get(token);
					line.append(count + "\t");
				}
			}
			writer.write(line + "\n");
			
		}
		writer.flush();
		writer.close();
	}
	

		public String getHeader() {
			
			return this.header;
		}
		
		public String getSequence() {
			
			return this.sequence;
		}
		
		public StringBuffer getCount() {
			ConcurrentHashMap <String, Integer> CountHashMap = new ConcurrentHashMap <String, Integer>();
			
			for(int x =0; x < this.sequence.length(); x++) {
				String base = "";
				if(this.sequence.charAt(x) != 'A' || this.sequence.charAt(x) != 'G' || this.sequence.charAt(x) != 'C' || this.sequence.charAt(x) != 'T') {
					base = Character.toString(this.sequence.charAt(x));
				}
				else {
					base = "Unassigned";
				}
				if(CountHashMap.get(base) != null) {
					CountHashMap.put(base, CountHashMap.get(base) + 1);
				}
				else {
					CountHashMap.put(base, 1);
				}
			}
			
			StringBuffer output = new StringBuffer();
			
			for(String key : CountHashMap.keySet()) {
				output.append(key + " = " + CountHashMap.get(key) + " ");
			}
			
			return output;
		}
		
		//method to keep track of G's and C's and total them, then divide that total by the sequence length
		public float getGCRatio() {
			
		 int sum = 0;
			
			for(int x=0; x < this.sequence.length(); x++) {
				
				if(this.sequence.charAt(x) == 'G' || this.sequence.charAt(x) == 'C') {
					sum++;
				}
			}
			
			return (float)sum / this.sequence.length();

		}	
		
public Integer[] getCounts() {
			
			
			Integer[] counts = {0,0,0,0,0};
			
			for (int x = 0; x < this.sequence.length(); x++){
				Character base = this.sequence.charAt(x);
				
				if (base == 'A') {
					counts[0] = counts[0] + 1;
					//A = A + 1;
				}
				else if (base == 'T') {
					counts[1] = counts[1] + 1;
					//T = T + 1;
				}
				else if (base == 'C') {
					counts[2] = counts[2] + 1;
					//C = C+1;
				}
				else if (base == 'G') {
					counts[3] = counts[3] + 1;
					//G=G+1;
				}
				else {
					counts[4] = counts[4] + 1;
					//N=N+1;
				}
				
			 }
			
			
			return counts;
		}
	}

