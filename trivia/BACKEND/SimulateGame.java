package trivia.BACKEND;

import java.io.*;
import java.util.*;

public class SimulateGame{

    public ArrayList<Question> mediumQuestions;
    public MaxHeap hardQuestions;
    public MinHeap easyQuestions;

	public SimulateGame(){
		this.mediumQuestions = new ArrayList<>();
		this.hardQuestions = new MaxHeap();
		this.easyQuestions = new MinHeap();

	}

	public void ingestQuestions(int choice){

		// choose which file to read data from
		String filePath = "";
		if(choice == 1){
			filePath = "trivia/BACKEND/QUESTIONS/math.csv";
		}else if (choice == 2){
			filePath = "trivia/BACKEND/QUESTIONS/science.csv";
		}else if (choice == 3){
			filePath = "trivia/BACKEND/QUESTIONS/verbalReasoning.csv";
		}else if (choice == 4){
			filePath = "trivia/BACKEND/QUESTIONS/technology.csv";
		}else if (choice == 5){
			filePath = "trivia/BACKEND/QUESTIONS/sports.csv";
		}else if (choice == 6){
			filePath = "trivia/BACKEND/QUESTIONS/history.csv";
		}else if (choice == 7){
			filePath = "trivia/BACKEND/QUESTIONS/geography.csv";
		}else if (choice == 8){
			filePath = "trivia/BACKEND/QUESTIONS/healthAndMedicine.csv";
		}

		Scanner scanner = null;
		try{
		File file = new File(filePath);
		scanner = new Scanner(file);

		
        scanner.nextLine();  // skip the header

		while(scanner.hasNextLine()){
			String[] questionDetails =  scanner.nextLine().trim().split(",");
			String questionText = questionDetails[0].substring(1, questionDetails[0].length() - 1);;
			String[] options = new String[4];
			options[0] = questionDetails[1].replaceAll("^\"|\"$", "");
			options[1] = questionDetails[2].replaceAll("^\"|\"$", "");
			options[2] = questionDetails[3].replaceAll("^\"|\"$", "");
			options[3] = questionDetails[4].replaceAll("^\"|\"$", "");
			int correctAnswerIndex = Integer.parseInt(questionDetails[5].trim());
            int difficultyLevel = Integer.parseInt(questionDetails[6].trim());
            
            // create a new Question object from each line of the csv file
			Question newQuestion = new Question(questionText, options, correctAnswerIndex, difficultyLevel);

			if(newQuestion.getDifficultyLevel() == 10){
				mediumQuestions.add(newQuestion);
			}else if(newQuestion.getDifficultyLevel() < 10){
				easyQuestions.push(newQuestion);
			}else{
				hardQuestions.push(newQuestion);
			}

             }
	     Collections.shuffle(mediumQuestions);	// Shuffle all medium questions 
		

	}  catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    //playGame is a public method that plays the game.
    public void playGame() {
	    Scanner scanner = new Scanner(System.in);

	    try {
		    int questionsAnswered = 0;
		    int points = 0;
		    int wrongStreak = 0;
		    int correctStreak = 0;

		    while (questionsAnswered < 15) {
		        Question q;

		        // Decide where to pull the question from
		        if (wrongStreak >= 3 && !easyQuestions.isEmpty()) {
		            q = easyQuestions.pop(); // from MinHeap
		            // wrongStreak = 0; // reset after fallback
		        } else if (correctStreak >= 3 && !hardQuestions.isEmpty()) {
		            q = hardQuestions.pop(); // from MaxHeap
		            correctStreak = 0; // reset after jump
		        } else if (!mediumQuestions.isEmpty()) {
		            q = mediumQuestions.remove(mediumQuestions.size() - 1); // O(1)
		        } else {
		            System.out.println("No more questions available.");
		            break;
		        }
	            int questionScore;
		        String difficulty = "";

		        // Assign scores to the various questions based on their difficulty level
		        if(q.getDifficultyLevel() == 10){
		        	difficulty = " (Medium)";
		        	questionScore = 10;
		        }else if (q.getDifficultyLevel()<10){
		        	difficulty = " (Easy)";
		        	questionScore = 5;
		        }else{
		        	difficulty = " (Hard)";
		        	questionScore = 20;
		        }

		        // Ask the question
		        System.out.println("\nQuestion " + (questionsAnswered + 1) + difficulty + ":");
		        System.out.println(q.getQuestionText());
		        String[] options = q.getAnswerOptions();
		        System.out.println("A. " + options[0]);
		        System.out.println("B. " + options[1]);
		        System.out.println("C. " + options[2]);
		        System.out.println("D. " + options[3]);

		        // Get user input with validation
		        int userAnswerIndex = -1;
		        while (true) {
		            System.out.print("Enter your answer (A-D): ");
		            String userInput = scanner.nextLine().trim().toUpperCase();
		            switch (userInput) {
		                case "A": userAnswerIndex = 0; break;
		                case "B": userAnswerIndex = 1; break;
		                case "C": userAnswerIndex = 2; break;
		                case "D": userAnswerIndex = 3; break;
		                default:
		                    System.out.println("Invalid input. Please enter A, B, C, or D.");
		                    continue;
		            }
		            break;
		        }

		        // Evaluate answer
		        if (userAnswerIndex == q.getCorrectAnswerIndex()) {
		            System.out.println("Correct! " + questionScore +" points.");
		            points += questionScore;
		            correctStreak++;
		            wrongStreak = 0;
		        } else {
		            System.out.println("Incorrect. The correct answer was: " +
		                (char)('A' + q.getCorrectAnswerIndex()));
		            wrongStreak++;
		            correctStreak = 0;
		        }

		        questionsAnswered++;
		    }

		    System.out.println("\nGame Over! You scored: " + points + " points.");
		} finally {
		    scanner.close();
		}
	}


    public static void main(String[] args){
    	try (Scanner scanner = new Scanner(System.in)) {
		System.out.println("Choose a subject: ");
		System.out.println("1. Math\n2. Science\n3. Verbal Reasoning\n4. Technology\n5. Sports\n6. World History\n7. Geography\n8. Health and Medicine");
		System.out.println("------------------------");
		SimulateGame game = new SimulateGame();

		int n = Integer.parseInt(scanner.nextLine());
		game.ingestQuestions(n);
		game.playGame();
	} catch (NumberFormatException e) {
		e.printStackTrace();
	}


	}
    

	
}