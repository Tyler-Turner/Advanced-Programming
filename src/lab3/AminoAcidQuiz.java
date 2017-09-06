//Tyler Turner

package lab3;

import java.util.Random;
import java.util.Scanner;


public class AminoAcidQuiz {

	public static void main(String[] args) {
		
		int score = 0;
		double time = System.currentTimeMillis();
		double end = time + 30000;
		Scanner in = new Scanner(System.in);
		Random rand = new Random();
		
		String[] SHORT_NAMES = 
			{ "A","R", "N", "D", "C", "Q", "E", 
			"G",  "H", "I", "L", "K", "M", "F", 
			"P", "S", "T", "W", "Y", "V" };
		
		String[] FULL_NAMES = 
			{
			"alanine","arginine", "asparagine", 
			"aspartic acid", "cysteine",
			"glutamine",  "glutamic acid",
			"glycine" ,"histidine","isoleucine",
			"leucine",  "lysine", "methionine", 
			"phenylalanine", "proline", 
			"serine","threonine","tryptophan", 
			"tyrosine", "valine"};

		while(System.currentTimeMillis() < end) {
				
			for(int x = rand.nextInt(19); score < 20; x = rand.nextInt(19)) {
				System.out.println(FULL_NAMES[x]);
				
				String answer = in.nextLine();
				
				if(answer.equalsIgnoreCase(SHORT_NAMES[x])) {
					System.out.println("Correct");
					score++;
				}
				
				else {
					System.out.println("Sorry the answer should be: " + SHORT_NAMES[x]);
					System.out.println("your score is: " + score);
					System.exit(0);
				}
			}
		
		}
		
		System.out.println("Time is up!");
		System.out.println("your score is: " + score);

			

	}
}

