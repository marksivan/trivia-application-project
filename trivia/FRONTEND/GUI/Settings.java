/*
 * This class represent the settings of the application.
 * It contains the settings for the game, UI, and theme.
 */
package GUI;

import java.awt.Color;
import java.awt.Font;

public class Settings {
    private static Settings instance;
    
    // Game Settings
    private int questionTimeLimit = 10;
    private int questionsPerGame = 15;
    private boolean autoSubmitOnTimeUp = true;
    
    // UI Settings
    private boolean isDarkMode = false;
    private Font defaultFont = new Font("Times New Roman", Font.PLAIN, 18);
    private Font titleFont = new Font("Times New Roman", Font.BOLD, 20);
    private Font buttonFont = new Font("Times New Roman", Font.BOLD, 20);
    
    // Theme Colors
    private Color primaryColor = new Color(255, 222, 33);
    private Color secondaryColor = new Color(255, 222, 33);
    private Color successColor = new Color(255, 222, 33);
    private Color warningColor = new Color(255, 222, 33);
    private Color errorColor = new Color(255, 222, 33);
    
    // Animation Settings
    private int animationSpeed = 16; // milliseconds
    private float hoverScale = 1.05f;
    private int slideAnimationDuration = 500; // milliseconds
    
    private Settings() {}
    
    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }
    
    // Game Settings Getters and Setters
    public int getQuestionTimeLimit() { return questionTimeLimit; }
    public void setQuestionTimeLimit(int seconds) { 
        if (seconds >= 10 && seconds <= 120) {
            questionTimeLimit = seconds;
        }
    }
    
    public int getQuestionsPerGame() { return questionsPerGame; }
    public void setQuestionsPerGame(int count) {
        if (count >= 5 && count <= 50) {
            questionsPerGame = count;
        }
    }
    
    public boolean isAutoSubmitOnTimeUp() { return autoSubmitOnTimeUp; }
    public void setAutoSubmitOnTimeUp(boolean autoSubmit) { autoSubmitOnTimeUp = autoSubmit; }
    
    // UI Settings Getters and Setters
    public boolean isDarkMode() { return isDarkMode; }
    public void setDarkMode(boolean darkMode) { 
        isDarkMode = darkMode;
    }
    
    public Font getDefaultFont() { return defaultFont; }
    public void setDefaultFont(Font font) { defaultFont = font; }
    
    public Font getTitleFont() { return titleFont; }
    public void setTitleFont(Font font) { titleFont = font; }
    
    public Font getButtonFont() { return buttonFont; }
    public void setButtonFont(Font font) { buttonFont = font; }
    
    // Theme Colors Getters and Setters
    public Color getPrimaryColor() { return primaryColor; }
    public void setPrimaryColor(Color color) { primaryColor = color; }
    
    public Color getSecondaryColor() { return secondaryColor; }
    public void setSecondaryColor(Color color) { secondaryColor = color; }
    
    public Color getSuccessColor() { return successColor; }
    public void setSuccessColor(Color color) { successColor = color; }
    
    public Color getWarningColor() { return warningColor; }
    public void setWarningColor(Color color) { warningColor = color; }
    
    public Color getErrorColor() { return errorColor; }
    public void setErrorColor(Color color) { errorColor = color; }
    
    // Animation Settings Getters and Setters
    public int getAnimationSpeed() { return animationSpeed; }
    public void setAnimationSpeed(int speed) {
        if (speed >= 10 && speed <= 100) {
            animationSpeed = speed;
        }
    }
    
    public float getHoverScale() { return hoverScale; }
    public void setHoverScale(float scale) {
        if (scale >= 1.0f && scale <= 1.2f) {
            hoverScale = scale;
        }
    }
    
    public int getSlideAnimationDuration() { return slideAnimationDuration; }
    public void setSlideAnimationDuration(int duration) {
        if (duration >= 100 && duration <= 2000) {
            slideAnimationDuration = duration;
        }
    }
    
    // Reset all settings to default values
    public void resetToDefaults() {
        questionTimeLimit = 10;
        questionsPerGame = 10;
        autoSubmitOnTimeUp = true;
        isDarkMode = false;
        defaultFont = new Font("Times New Roman", Font.PLAIN, 14);
        titleFont = new Font("Times New Roman", Font.BOLD, 24);
        buttonFont = new Font("Times New Roman", Font.BOLD, 16);
        primaryColor = new Color(255, 222, 33);
        secondaryColor = new Color(255, 222, 33);
        successColor = new Color(255, 222, 33);
        warningColor = new Color(255, 222, 33);
        errorColor = new Color(255, 222, 33);
        animationSpeed = 16;
        hoverScale = 1.05f;
        slideAnimationDuration = 500;
    }
} 