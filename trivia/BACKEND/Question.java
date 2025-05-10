package trivia;

import java.io.*;
import java.util.*;

public class Question{
	private String questionText;
	private String[] answerOptions;
	private int correctIndex;
	private int difficultyLevel;

	public Question(questionText, answerOptions, correctIndex, difficultyLevel){
		this.questionText = questionText;
		this.answerOptions = answerOptions;
		this.correctIndex = correctIndex;
		this.difficultyLevel = difficultyLevel;
	}

	public String getQuestionText(){
		return this.questionText;
	}

	public String[] getAnswerOptions(){
		return this.answerOptions;
	}

	public int getCorrectIndex(){
		return this.correctIndex;
	}

	public int getDifficultyLevel(){
		return this.difficultyLevel;
	}
}