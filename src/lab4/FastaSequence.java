//Tyler Turner
//Aaron Trautman helped me shorten my logic down a bit during the parsing loop
package lab4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
		List<FastaSequence> fastaList = 
				FastaSequence.readFastaFile("C:\\Users\\Tyler\\Desktop\\sequence2.txt");
		
		for( FastaSequence fs : fastaList)
		{
		System.out.println(fs.getHeader());
		System.out.println(fs.getSequence());
		//System.out.println(fs.getGCRatio());
		//System.out.println("\n");
		}

		File input = new File("C:\\Users\\Tyler\\Desktop\\sequence2.txt");
		File output = new File("C:\\Users\\Tyler\\Desktop\\output.txt");
		
		writeUnique(input,output);
	}
	//factory method to parse file being fed into it
	public static List<FastaSequence> readFastaFile(String filepath) throws Exception{
		//create new list
		List<FastaSequence> myList = new ArrayList<FastaSequence>();
		//create new buffered reader to read in the file
		BufferedReader reader = 
				new BufferedReader(new FileReader(new File("C:\\Users\\Tyler\\Desktop\\sequence2.txt")));
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
		int count = 0;
		
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

	

		public String getHeader() {
			
			return this.header;
		}
		
		public String getSequence() {
			
			return this.sequence;
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
	}

