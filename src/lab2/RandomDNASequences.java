//Tyler Turner
//Lab 2 random DNA sequences

package lab2;

import java.util.Random;

public class RandomDNASequences {

	public static void main(String[] args) {
		
		//random number generator object
		Random rand = new Random();
		
		String a = "A";
		String t = "T";
		String c = "C";
		String g = "G";
		String s = "";
		int x = 0;
		int counter = 0;
		
		//nested for loop to make triplets, and then run them 1000 times
		for(int i = 0; i < 1000; i++) {
			for(int k = 0; k < 3; k++) {
				
				//generate random numbers
				x = rand.nextInt(4);
				
				//assign letter to string
				if(x == 0) {
					s +=  a;
				}
				else if(x == 1) {
					s +=  t;
				}
				else if (x == 2) {
					s += c;
				} 
				else {
					s += g;
				}
						
			}
			//print string and check for AAA triplet
			System.out.println(s);
			
			if(s.equals("AAA")) {
				counter ++;
			}
			
			//reset the string for next run through
			s = "";	
			
		}

		System.out.println(counter);
	}

}
//expected output for AAA triplet should be around .25 * .25 * .25 which equals 0.015625
//while running 1000 times AAA triplets will pop up around the expected value, either just above or just below
//that value, mine have ranged from 11-24 times which is .011 - .024 percent of the time
