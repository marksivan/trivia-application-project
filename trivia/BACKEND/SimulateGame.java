package trivia;

import java.io.*;
import java.util.*;

public class SimulateGame{

	public ArrayList<Question> mediumQuestions;
	public MaxHeap<Key,Value> hardQuestions;
	public MinHeap<Key, Value> easyQuestions;

	public SimulateGame(){
		this.mediumQuestions = new ArrayList<>();
		this.hardQuestions = new MaxHeap<>();
		this.easyQuestions = new MinHeap<>();

	}

	public void ingestQuestions(int choice){
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

		try{
		File file = new File(filepath);
		Scanner scanner = new Scanner(file);
        
		while(scanner.hasNextLine()){
			String[] questionDetails =  scanner.nextLine().trim().split(",");
			String questionText = questionDetails[0];
			String options = new String[4];
			options[0] = questionDetails[1];
			options[1] = questionDetails[2];
			options[2] = questionDetails[3];
			options[3] = questionDetails[4];
			String correctIndexStr = questionDetails[5];
			int correctIndex = Integer.parseInt(correctIndexStr);
			String difficultlyLevelStr = questionDetails[6];
			int difficultyLevel = Integer.parseInt(difficultlyLevelStr);

			Question newQuestion = new Question(questionText, options, correctIndex, difficultyLevel);

			if(newQuestion.getDifficultyLevel() == 10){
				mediumQuestions.add(newQuestion);
			}else if(newQuestion.getDifficultyLevel() < 10){
				easyQuestions.push(newQuestion);
			}else{
				hardQuestions.push(newQuestion);
			}

             }
			 
		

	}  catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }

    public static void main(String[] args){
    	Scanner scanner = new Scanner(System.in);

    	System.out.println("Choose a subject: ")
        System.out.println("1. Math\n2. Science\n3. Verbal Reasoning\n4. Technology\n5. Sports\n6. History\n7. Government\n8. Health and Medicine");
        SimulateGame game = new SimulateGame();

        int n = Integer.parseInt(scanner.nextLine());
        game.ingestQuestions(n);
        

	}
    

	
}