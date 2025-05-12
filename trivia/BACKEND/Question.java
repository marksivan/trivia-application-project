package trivia.BACKEND;

public class Question{
	private String questionText;
	private String[] answerOptions;
	private int correctAnswerIndex;
	private int difficultyLevel;

	public Question(String questionText, String[] answerOptions, int correctAnswerIndex, int difficultyLevel){
		this.questionText = questionText;
		this.answerOptions = answerOptions;
		this.correctAnswerIndex = correctAnswerIndex;
		this.difficultyLevel = difficultyLevel;
	}

	public String getQuestionText(){
		return this.questionText;
	}

	public String[] getAnswerOptions(){
		return this.answerOptions;
	}

	public int getCorrectAnswerIndex(){
		return this.correctAnswerIndex;
	}

	public int getDifficultyLevel(){
		return this.difficultyLevel;
	}
}