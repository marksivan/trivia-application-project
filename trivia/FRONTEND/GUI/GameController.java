/**
 * GameController acts as an adapter between the terminal-based SimulateGame
 * and the GUI-based HomePage.
 */
package GUI;
import trivia.BACKEND.SimulateGame;
import trivia.BACKEND.Question;
import java.util.ArrayList;


public class GameController {
    private SimulateGame game;
    private int currentQuestionIndex;
    private int totalQuestionsToAsk;
    private int score;
    private ArrayList<Question> currentQuestions;
    private Question currentQuestion;
    private int wrongStreak;
    private int correctStreak;
    private int correctAnswers;
    private int wrongAnswers;
    private int totalPossibleScore;

    public GameController() {
        game = new SimulateGame(); //Create a new instance of the SimulateGame class from the backend.
        currentQuestionIndex = 0;
        score = 0;
        totalQuestionsToAsk = Settings.getInstance().getQuestionsPerGame(); //Get the number of questions per game from the settings.
        currentQuestions = new ArrayList<>();
        wrongStreak = 0;
        correctStreak = 0;
        correctAnswers = 0;
        wrongAnswers = 0;
        totalPossibleScore = 0;
        Settings.getInstance().setAutoSubmitOnTimeUp(true);
    }

    /**
     * Initialize the game with the selected category
     */

    public void initGame(int categoryChoice) {
        game.ingestQuestions(categoryChoice); //Ingest the questions from the backend.
        // Initialize other game state
        currentQuestionIndex = 0; //Initialize the current question index to 0.
        score = 0; 
        wrongStreak = 0;
        correctStreak = 0;
        correctAnswers = 0;
        wrongAnswers = 0;
        totalPossibleScore = 0;
        currentQuestions.clear();
        // Pre-populate some questions from medium difficulty
        if (game.mediumQuestions != null && !game.mediumQuestions.isEmpty()) {
            currentQuestions.addAll(game.mediumQuestions); //Add the medium questions to the current questions.
        }
    }

    /**
     * Get the next question based on player performance
     */
    
    public Question getNextQuestion() {
        if (currentQuestionIndex >= totalQuestionsToAsk) {
            return null; // Game over
        }
        // Logic similar to SimulateGame's playGame method
        if (wrongStreak >= 3 && !game.easyQuestions.isEmpty()) {
            currentQuestion = game.easyQuestions.pop();
        } else if (correctStreak >= 3 && !game.hardQuestions.isEmpty()) {
            currentQuestion = game.hardQuestions.pop();
            correctStreak = 0; // Reset after jump to hard question
        } else if (!game.mediumQuestions.isEmpty()) {
            currentQuestion = game.mediumQuestions.remove(game.mediumQuestions.size() - 1);
        } else if (!currentQuestions.isEmpty()) {
            currentQuestion = currentQuestions.remove(currentQuestions.size() - 1);
        } else {

            return null; // No more questions available
        }

        // Add to total possible score
        totalPossibleScore += getQuestionScore(currentQuestion);
        
        currentQuestionIndex++;
        return currentQuestion;
    }

    /**
     * Check the user's answer and update game state
     */
    public boolean checkAnswer(int userAnswerIndex) {
        if (currentQuestion == null) {
            return false;
        }

        boolean isCorrect = (userAnswerIndex == currentQuestion.getCorrectAnswerIndex());
        
        if (isCorrect) {
            int questionScore = getQuestionScore(currentQuestion);
            score += questionScore;
            correctStreak++;
            wrongStreak = 0;
            correctAnswers++;
        } else {
            wrongStreak++;
            correctStreak = 0;
            wrongAnswers++;
        }

        return isCorrect;
    }

    /**
     * Get the score value for the current question based on difficulty
     */
    public int getQuestionScore(Question q) {
        if (q.getDifficultyLevel() == 10) {
            return 10; // Medium
        } else if (q.getDifficultyLevel() < 10) {
            return 5;  // Easy
        } else {
            return 20; // Hard
        }
    }

    /**
     * Get the difficulty label for the current question
     */
    public String getQuestionDifficulty(Question q) {
        if (q.getDifficultyLevel() == 10) {
            return "Medium";
        } else if (q.getDifficultyLevel() < 10) {
            return "Easy";
        } else {
            return "Hard";
        }
    }

    /**
     * Check if the game is over
     */
    public boolean isGameOver() {
        if (currentQuestionIndex >= totalQuestionsToAsk+1) {

            return true;
        } else {
            return false;
        }
    }

    /**
     * Get the current score
     */
    public int getScore() {
        return score;
    }

    /**
     * Get the number of correct answers
     */
    public int getCorrectAnswers() {
        return correctAnswers;
    }

    /**
     * Get the number of wrong answers
     */
    public int getWrongAnswers() {
        return wrongAnswers;
    }

    /**
     * Get the total possible score
     */
    public int getTotalPossibleScore() {
        return totalPossibleScore;
    }

    /**
     * Get the score percentage
     */
    public int getScorePercentage() {
        if (totalPossibleScore == 0) {
            return 0;
        }
        return (int) Math.round(((double) score * 100)/ totalPossibleScore);
    }

    /**
     * Set the total number of questions to ask
     */
    public void setTotalQuestionsToAsk(int total) {
        this.totalQuestionsToAsk = total;
    }

    /**
     * Get the current question index (1-based for display)
     */
    public int getCurrentQuestionNumber() {
        return currentQuestionIndex;
    }

    /**
     * Reset the game state
     */
    public void reset() {
        game = new SimulateGame();
        currentQuestionIndex = 0;
        score = 0;
        wrongStreak = 0;
        correctStreak = 0;
        correctAnswers = 0;
        wrongAnswers = 0;
        totalPossibleScore = 0;
        currentQuestions.clear();
        currentQuestion = null;
    }
} 