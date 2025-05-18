/*
 * This class represent the homepage on the landing Jframe.
 * It encapsulates the logic to organise different DOM like components on the GUI.
 * It also encapsulates the logic to handle the different events,states, and actions on the GUI.
 */

package GUI;
import java.awt.*;
import javax.swing.*;
import DOM.GenericComponents;
import trivia.BACKEND.Question;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
public class HomePage extends JPanel { 
    private JPanel rootPanel;
    private GenericComponents.QuizPanel contentPanel;
    private GenericComponents.QuizCircularClock circularClock;
    private JPanel headerContainer;
    private JPanel footerPanel;
    private JPanel clockPanel;
    private Timer slideTimer;
    private int currentQuestionIndex = 0;
    private GameController gameController;
    private String currentCategory;
    private Question currentQuestion;

    // This method constructs (It is the constructor) the landing page after login and the home for the user.
    public HomePage() {
        gameController = new GameController();
        setLayout(new BorderLayout());
        setBackground(GenericComponents.ThemeManager.getBackground());
        
        rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(GenericComponents.ThemeManager.getBackground());
        add(rootPanel);

        setupHeader();
        setupFooter();
        setupContentPanel();
        showMainMenu();
    }

    // This method sets up the header of the homepage.
    private void setupHeader() {
        GenericComponents.QuizHeader header = new GenericComponents.QuizHeader(
            "Trivia Game", 
            "Welcome, " + AuthManager.getInstance().getCurrentUser() + "!"
        );
        headerContainer = new JPanel(new BorderLayout());
        headerContainer.setBackground(GenericComponents.ThemeManager.isDarkMode() ? 
            new Color(33, 33, 33) : GenericComponents.ThemeManager.getHeader());
        headerContainer.add(header.getComponent(), BorderLayout.CENTER);

        GenericComponents.QuizButton backButton = new GenericComponents.QuizButton(
            "Home",
            GenericComponents.ThemeManager.getButton(),
            GenericComponents.ThemeManager.getButtonHover(),
            Color.WHITE,
            new Font("Times New Roman", Font.BOLD, 14),
            8, 15, true, true,
            e -> handleHomeButton()
        );
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        backButtonPanel.setOpaque(false);
        backButtonPanel.add(backButton.getComponent());
        headerContainer.add(backButtonPanel, BorderLayout.WEST);

        circularClock = new GenericComponents.QuizCircularClock();
        clockPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        clockPanel.setOpaque(false);
        clockPanel.add(circularClock);
        headerContainer.add(clockPanel, BorderLayout.EAST);

        rootPanel.add(headerContainer, BorderLayout.NORTH);
    }

    // This method sets up the footer of the homepage.
    private void setupFooter() {
        footerPanel = new JPanel(new BorderLayout(10, 10));
        footerPanel.setBackground(GenericComponents.ThemeManager.getFooter());
        rootPanel.add(footerPanel, BorderLayout.SOUTH);
    }

    // This method sets up the content panel of the homepage.
    private void setupContentPanel() {
        contentPanel = new GenericComponents.QuizPanel(
            GenericComponents.ThemeManager.getBackground(),
            GenericComponents.ThemeManager.getBorder(),
            5, 15, true, true,
            new GridBagLayout(),
            false, null, null
        );
        rootPanel.add(contentPanel.getComponent(), BorderLayout.CENTER);
    }

    // This method shows the main menu of the homepage.
    private void showMainMenu() {
        JPanel content = (JPanel) contentPanel.getComponent();
        content.removeAll();
        content.setLayout(new GridBagLayout());
        
        JPanel categoryPanel = createCategoryPanel();
        JScrollPane categoryScrollPane = createScrollPane(categoryPanel);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        content.add(categoryScrollPane, gbc);

        JPanel buttonPanel = createButtonPanel();
        gbc.gridy = 2;
        gbc.weighty = 0.0;
        content.add(buttonPanel, gbc);

        content.revalidate();
        content.repaint();
    }

    // This method creates the category panel of the homepage.
    private JPanel createCategoryPanel() {
        JPanel categoryPanel = new JPanel(new GridBagLayout());
        categoryPanel.setOpaque(false);
        categoryPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel cardsGridPanel = new JPanel(new GridLayout(0, 2, 30, 30));
        cardsGridPanel.setOpaque(false);

        String[] categories = {"Math", "Science", "Verbal Reasoning", "Technology", "Sports", "World History", "Geography", "Health and Medicine"};
        for (String category : categories) {
            cardsGridPanel.add(createCategoryCard(category));
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(20, 20, 20, 20);
        categoryPanel.add(cardsGridPanel, gbc);

        return categoryPanel;
    }

    // This method creates the scroll pane of the homepage.
    private JScrollPane createScrollPane(JPanel panel) {
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(800, 600));
        
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setPreferredSize(new Dimension(12, 0));
        verticalScrollBar.setBackground(GenericComponents.ThemeManager.getBackground());
        verticalScrollBar.setForeground(GenericComponents.ThemeManager.getButton());
        
        verticalScrollBar.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = GenericComponents.ThemeManager.getButton();
                this.trackColor = GenericComponents.ThemeManager.getBackground();
            }
            
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }
            
            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }
            
            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        });
        
        return scrollPane;
    }

    // This method creates the category card of the homepage.
    private JPanel createCategoryCard(String category) {
        Color defaultColor = GenericComponents.ThemeManager.getButton();
        Color hoverColor = new Color(0xA5D6A7); // light green
    
        JPanel cardPanel = new JPanel(new BorderLayout(20, 20)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };
        cardPanel.setOpaque(true);
        cardPanel.setBackground(defaultColor);
        cardPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cardPanel.setPreferredSize(new Dimension(300, 200));
    
        JPanel textPanel = createTextPanel(category);
        textPanel.setOpaque(false);
        cardPanel.add(textPanel, BorderLayout.CENTER);
    
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                cardPanel.setBackground(hoverColor);
                cardPanel.repaint();
            }
    
            @Override
            public void mouseExited(MouseEvent e) {
                cardPanel.setBackground(defaultColor);
                cardPanel.repaint();
            }
    
            @Override
            public void mouseClicked(MouseEvent e) {
                startGame(false, category);
            }
        };
        
        cardPanel.addMouseListener(mouseAdapter);
        textPanel.addMouseListener(mouseAdapter);
    
        return cardPanel;
    }
    

    // This method creates the text panel of the homepage.
    private JPanel createTextPanel(String category) {
        JPanel textPanel = new JPanel(new GridBagLayout());
        textPanel.setOpaque(false);
        GridBagConstraints textGbc = new GridBagConstraints();
        textGbc.gridx = 0;
        textGbc.gridy = 0;
        textGbc.insets = new Insets(15, 15, 15, 15);
        textGbc.fill = GridBagConstraints.HORIZONTAL;

        GenericComponents.QuizLabel categoryLabel = new GenericComponents.QuizLabel(
            category,
            new Font("Times New Roman", Font.BOLD, 24),
            Color.WHITE,
            null, 15, true, false, true
        );
        textPanel.add(categoryLabel.getComponent(), textGbc);

        return textPanel;
    }

    // This method creates the button panel of the homepage.
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setOpaque(false);
        buttonPanel.setBackground(GenericComponents.ThemeManager.getButton());
        buttonPanel.add(createButton("Random Game", e -> startGame(true, "")).getComponent());
        buttonPanel.add(createButton("Settings", e -> showSettings()).getComponent());
        buttonPanel.add(createButton("Reset App", e -> handleResetApp()).getComponent());
        buttonPanel.add(createButton("Logout", e -> {
            AuthManager.getInstance().logout();
            // Get the parent frame and show login page
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof MainFrame) {
                ((MainFrame) window).showLoginPage();
            }
        }).getComponent());
        return buttonPanel;
    }

    // This method creates the button of the homepage.
    private GenericComponents.QuizButton createButton(String text, java.util.function.Consumer<java.awt.event.ActionEvent> onClick) {
        return new GenericComponents.QuizButton(
            text,
            GenericComponents.ThemeManager.getButton(),
            GenericComponents.ThemeManager.getButtonHover(),
            GenericComponents.ThemeManager.getText(),
            new Font("Times New Roman", Font.BOLD, 16),
            10, 25, true, true,
            onClick
        );
    }

    // This method handles the home button of the homepage.
    private void handleHomeButton() {
        if (currentQuestionIndex > 0) {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to return to home? Your game progress will be lost.",
                "Confirm Return",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                // Stop all timers first
                if (slideTimer != null) {
                    slideTimer.stop();
                    slideTimer = null;
                }
                if (circularClock != null) {
                    circularClock.stop();
                    circularClock.reset();
                }
                
                // Reset game state
                currentQuestionIndex = 0;
                clockPanel.setVisible(false);
                if (gameController != null) {
                    gameController.reset();
                } 
                // Show main menu
                showMainMenu();
            }
        } else {
            // Stop all timers first
            if (slideTimer != null) {
                slideTimer.stop();
                slideTimer = null;
            }
            if (circularClock != null) {
                circularClock.stop();
                circularClock.reset();
            }
            
            // Reset game state
            currentQuestionIndex = 0;
            clockPanel.setVisible(false);
            if (gameController != null) {
                gameController.reset();
            }
            
            // Show main menu
            showMainMenu();
        }
    }

    // This method starts the game.
    private void startGame(boolean isRandom, String category) {
        // Array of all available categories
        String[] categories = {"Math", "Science", "Verbal Reasoning", "Technology", "Sports", "World History", "Geography", "Health and Medicine"};
        // Randomly select a category
        if (isRandom) {
            int randomIndex = (int) (Math.random() * categories.length);
            category = categories[randomIndex];
        }
        currentCategory = category;
        int categoryChoice = 0;
        switch(category) {
            case "Math":
                categoryChoice = 1;
                break;
            case "Science":
                categoryChoice = 2;
                break;
            case "Verbal Reasoning": 
                categoryChoice = 3;
                break;
            case "Technology":
                categoryChoice = 4;
                break;      
            case "Sports":
                categoryChoice = 5;
                break;
            case "World History":
                categoryChoice = 6;
                break;
            case "Geography":
                categoryChoice = 7;
                break;
            case "Health and Medicine":
                categoryChoice = 8;
                break;
        }
        
        // Initialize game with selected category
        gameController.initGame(categoryChoice);
        
        // Update settings from user preferences if available
        if (Settings.getInstance() != null) {
            gameController.setTotalQuestionsToAsk(Settings.getInstance().getQuestionsPerGame());
        }
        
        // Show first question
        currentQuestionIndex++;
        showNextQuestion();
    }
    
    private void showNextQuestion() {
        // Cancel any existing timers to prevent multiple timers
        if (slideTimer != null && slideTimer.isRunning()) {
            slideTimer.stop();
        }
        
        currentQuestion = gameController.getNextQuestion();
        
        if (currentQuestion == null || gameController.isGameOver()) {
            // Game is over, show game over screen
            showGameOver();
            return;
        }
        
        // Clear existing content
        JPanel content = (JPanel) contentPanel.getComponent();
        content.removeAll();
        content.setLayout(new BorderLayout(20, 20));
        
        // Create question panel
        JPanel questionPanel = new JPanel(new BorderLayout(10, 10));
        questionPanel.setOpaque(false);
        questionPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10),
            BorderFactory.createLineBorder(new Color(0, 0, 0, 30), 1)
        ));
        
        // Question header with question number, category and difficulty
        String difficulty = gameController.getQuestionDifficulty(currentQuestion);
        GenericComponents.QuizLabel questionHeader = new GenericComponents.QuizLabel(
            "Question " + gameController.getCurrentQuestionNumber() + " out of " + Settings.getInstance().getQuestionsPerGame() + " - " + currentCategory + " (" + difficulty + ")",
            new Font("Times New Roman", Font.BOLD, 18),
            GenericComponents.ThemeManager.getText(),
            null, 10, true, false, true
        );
        questionPanel.add(questionHeader.getComponent(), BorderLayout.NORTH);
        
        // Question text
        GenericComponents.QuizLabel questionText = new GenericComponents.QuizLabel(
            currentQuestion.getQuestionText(),
            new Font("Times New Roman", Font.PLAIN, 16),
            GenericComponents.ThemeManager.getText(),
            null, 15, true, false, true
        );
        questionPanel.add(questionText.getComponent(), BorderLayout.CENTER);
        
        // Answer options panel
        JPanel optionsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        optionsPanel.setOpaque(false);
        
        String[] options = currentQuestion.getAnswerOptions();
        String[] optionLabels = {"A", "B", "C", "D"};
        
        for (int i = 0; i < options.length; i++) {
            final int answerIndex = i;
            GenericComponents.QuizButton optionButton = new GenericComponents.QuizButton(
                optionLabels[i] + ". " + options[i],
                GenericComponents.ThemeManager.getButton(),
                GenericComponents.ThemeManager.getButtonHover(),
                Color.WHITE,
                new Font("Times New Roman", Font.BOLD, 14),
                10, 15, true, true,
                e -> {
                    handleAnswer(answerIndex);
                }
            );
            optionsPanel.add(optionButton.getComponent());
        }
        
        // Add options panel to content
        content.add(questionPanel, BorderLayout.NORTH);
        content.add(optionsPanel, BorderLayout.CENTER);
        
        // Start the timer
        clockPanel.setVisible(true);
        circularClock.reset();
        circularClock.start();
        
        // Auto-submit when time runs out
        if (Settings.getInstance().isAutoSubmitOnTimeUp()) {
            // Use a timer instead of the undefined method
            slideTimer = new Timer(Settings.getInstance().getQuestionTimeLimit() * 1000, e -> {
                if (Settings.getInstance().isAutoSubmitOnTimeUp()) {
                    handleAnswer(-1); // Invalid answer when time is up
                    Settings.getInstance().setAutoSubmitOnTimeUp(true);
                }
            });
            slideTimer.setRepeats(false);
            slideTimer.start();
        }
        
        content.revalidate();
        content.repaint();
    }
    
    private void handleAnswer(int answerIndex) {
        // Stop the timer
        circularClock.stop();
        
        // Check if the answer is correct and handle null currentQuestion
        boolean isCorrect = false;
        
        // Show feedback
        JPanel content = (JPanel) contentPanel.getComponent();
        content.removeAll();
        content.setLayout(new BorderLayout(20, 20));
        
        JPanel feedbackPanel = new JPanel(new BorderLayout(10, 10));
        feedbackPanel.setOpaque(false);
        feedbackPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10),
            BorderFactory.createLineBorder(new Color(0, 0, 0, 30), 0)
        ));
        
        String feedbackText;
        Color feedbackColor;
        
        if (currentQuestion == null) {
            // Handle the case where currentQuestion is null (e.g., timer expired between questions)
            feedbackText = "Time's up! Moving to next question.";
            feedbackColor = new Color(231, 76, 60);
        } else {
            isCorrect = gameController.checkAnswer(answerIndex);
            
            if (isCorrect) {
                feedbackText = "Correct! +" + gameController.getQuestionScore(currentQuestion) + " points";
                feedbackColor = new Color(46, 204, 113);
            } else if (answerIndex == -1) {
                feedbackText = "Time's up! Moving to next question.";
                feedbackColor = new Color(231, 76, 60);
            } else {
                String correctAnswer = currentQuestion.getAnswerOptions()[currentQuestion.getCorrectAnswerIndex()];
                feedbackText = "Incorrect. The correct answer was: " + 
                    (char)('A' + currentQuestion.getCorrectAnswerIndex()) + ". " + correctAnswer;
                feedbackColor = new Color(231, 76, 60); // Red
            }
        }
        
        GenericComponents.QuizLabel feedbackLabel = new GenericComponents.QuizLabel(
            feedbackText,
            new Font("Times New Roman", Font.BOLD, 18),
            feedbackColor,
            null, 15, true, false, true
        );
        feedbackPanel.add(feedbackLabel.getComponent(), BorderLayout.CENTER);
        
        GenericComponents.QuizButton nextButton = new GenericComponents.QuizButton(
            "Next Question",
            GenericComponents.ThemeManager.getButton(),
            GenericComponents.ThemeManager.getButtonHover(),
            Color.WHITE,
            new Font("Times New Roman", Font.BOLD, 16),
            10, 20, true, true,
            e -> showNextQuestion()
        );
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(nextButton.getComponent());
        
        content.add(feedbackPanel, BorderLayout.CENTER);
        content.add(buttonPanel, BorderLayout.SOUTH);
        
        content.revalidate();
        content.repaint();
    }

    private void showGameOver() {
        JPanel content = (JPanel) contentPanel.getComponent();
        content.removeAll();
        content.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);

        JPanel gameOverPanel = new JPanel(new BorderLayout(20, 20));
        gameOverPanel.setOpaque(false);

        // Create a panel for the game over title
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        GenericComponents.QuizLabel gameOverLabel = new GenericComponents.QuizLabel(
            "Game Over!",
            new Font("Times New Roman", Font.BOLD, 32),
            GenericComponents.ThemeManager.getText(),
            null, 20, true, false, true
        );
        titlePanel.add(gameOverLabel.getComponent(), BorderLayout.CENTER);
        
        // Create a panel for the statistics
        JPanel statsPanel = new JPanel(new GridBagLayout());
        statsPanel.setOpaque(false);
        GridBagConstraints statsGbc = new GridBagConstraints();
        statsGbc.gridx = 0;
        statsGbc.gridy = 0;
        statsGbc.insets = new Insets(10, 10, 10, 10);
        statsGbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Final score
        GenericComponents.QuizLabel finalScoreLabel = new GenericComponents.QuizLabel(
            "Final Score: " + gameController.getScore() + " / " + gameController.getTotalPossibleScore() + 
            " (" + gameController.getScorePercentage() + "%)",
            new Font("Times New Roman", Font.BOLD, 24),
            GenericComponents.ThemeManager.getText(),
            null, 20, true, false, true
        );
        statsPanel.add(finalScoreLabel.getComponent(), statsGbc);
        
        // Correct answers
        statsGbc.gridy = 1;
        GenericComponents.QuizLabel correctAnswersLabel = new GenericComponents.QuizLabel(
            "Correct Answers: " + gameController.getCorrectAnswers(),
            new Font("Times New Roman", Font.BOLD, 18),
            new Color(46, 204, 113), // Green
            null, 15, true, false, true
        );
        statsPanel.add(correctAnswersLabel.getComponent(), statsGbc);
        
        // Wrong answers
        statsGbc.gridy = 2;
        GenericComponents.QuizLabel wrongAnswersLabel = new GenericComponents.QuizLabel(
            "Wrong Answers: " + gameController.getWrongAnswers(),
            new Font("Times New Roman", Font.BOLD, 18),
            new Color(231, 76, 60), // Red
            null, 15, true, false, true
        );
        statsPanel.add(wrongAnswersLabel.getComponent(), statsGbc);
        
        // Performance message
        statsGbc.gridy = 3;
        String performanceMessage;
        Color performanceColor;
        int percentage = gameController.getScorePercentage();
        
        if (percentage >= 90) {
            performanceMessage = "Outstanding! You're a trivia master!";
            performanceColor = new Color(46, 204, 113); // Green
        } else if (percentage >= 70) {
            performanceMessage = "Great job! You know your stuff!";
            performanceColor = new Color(52, 152, 219); // Blue
        } else if (percentage >= 50) {
            performanceMessage = "Good effort! Keep practicing!";
            performanceColor = new Color(241, 196, 15); // Yellow
        } else {
            performanceMessage = "Keep studying! You'll get better!";
            performanceColor = new Color(231, 76, 60); // Red
        }
        
        GenericComponents.QuizLabel performanceLabel = new GenericComponents.QuizLabel(
            performanceMessage,
            new Font("Times New Roman", Font.BOLD, 20),
            performanceColor,
            null, 15, true, false, true
        );
        statsPanel.add(performanceLabel.getComponent(), statsGbc);

        // Play again button
        GenericComponents.QuizButton playAgainButton = new GenericComponents.QuizButton(
            "Play Again",
            GenericComponents.ThemeManager.getButton(),
            GenericComponents.ThemeManager.getButtonHover(),
            GenericComponents.ThemeManager.getText(),
            new Font("Times New Roman", Font.BOLD, 16),
            15, 20, true, true,
            e -> {
                // Reset game state
                gameController.reset();
                circularClock.stop();
                clockPanel.setVisible(false);
                
                // Clear content and show main menu
                content.removeAll();
                showMainMenu();
                content.revalidate();
                content.repaint();
            }
        );
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(playAgainButton.getComponent());

        // Add all panels to the game over panel
        gameOverPanel.add(titlePanel, BorderLayout.NORTH);
        gameOverPanel.add(statsPanel, BorderLayout.CENTER);
        gameOverPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Create scroll pane for game over panel
        JScrollPane scrollPane = new JScrollPane(gameOverPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        // Style scroll bar
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setPreferredSize(new Dimension(12, 0));
        verticalScrollBar.setBackground(GenericComponents.ThemeManager.getBackground());
        verticalScrollBar.setForeground(GenericComponents.ThemeManager.getButton());
        
        // Custom scroll bar UI
        verticalScrollBar.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = GenericComponents.ThemeManager.getButton();
                this.trackColor = GenericComponents.ThemeManager.getBackground();
            }
            
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }
            
            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }
            
            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
            
            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                    return;
                }
                
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int arc = 10;
                g2.setColor(thumbColor);
                g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2, 
                               thumbBounds.width - 4, thumbBounds.height - 4, 
                               arc, arc);
                
                g2.dispose();
            }
            
            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(new Color(trackColor.getRed(), trackColor.getGreen(), 
                                    trackColor.getBlue(), 50));
                g2.fillRoundRect(trackBounds.x + 2, trackBounds.y + 2,
                               trackBounds.width - 4, trackBounds.height - 4,
                               10, 10);
                
                g2.dispose();
            }
        });

        // Add scroll pane to content with proper constraints
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        content.add(scrollPane, gbc);

        content.revalidate();
        content.repaint();
    }

    private void showSettings() {
        // Only show settings if no game is in progress
        if (currentQuestionIndex > 0) {
            JOptionPane.showMessageDialog(this, "Please finish or stop the current game first!");
            return;
        }

        JPanel content = (JPanel) contentPanel.getComponent();
        content.removeAll();
        content.setLayout(new BorderLayout());

        // Create settings panel with shadow
        JPanel settingsPanel = new JPanel(new GridBagLayout());
        settingsPanel.setOpaque(false);
        settingsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(5, 5, 5, 5),
            BorderFactory.createLineBorder(new Color(0, 0, 0, 30), 1)
        ));

        // Create scroll pane
        JScrollPane scrollPane = new JScrollPane(settingsPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        // Style the scroll bar
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(12, 0));
        scrollPane.getVerticalScrollBar().setBackground(GenericComponents.ThemeManager.getBackground());
        scrollPane.getVerticalScrollBar().setForeground(GenericComponents.ThemeManager.getButton());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Game Settings Section
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        GenericComponents.QuizLabel gameSettingsLabel = new GenericComponents.QuizLabel(
            "Game Settings",
            new Font("Times New Roman", Font.BOLD, 16),
            GenericComponents.ThemeManager.getText(),
            null, 5, false, false, false
        );
        settingsPanel.add(gameSettingsLabel.getComponent(), gbc);

        // Timer Settings
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        GenericComponents.QuizLabel timerLabel = new GenericComponents.QuizLabel(
            "Question Time Limit (seconds):",
            new Font("Times New Roman", Font.BOLD, 14),
            GenericComponents.ThemeManager.getText(),
            null, 5, false, false, false
        );
        settingsPanel.add(timerLabel.getComponent(), gbc);

        gbc.gridy = 2;
        gbc.gridwidth = 1;
        JSpinner timerSpinner = new JSpinner(new SpinnerNumberModel(Settings.getInstance().getQuestionTimeLimit(), 10, 120, 5));
        timerSpinner.setPreferredSize(new Dimension(100, 30));
        settingsPanel.add(timerSpinner, gbc);

        // Questions per game
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        GenericComponents.QuizLabel questionsLabel = new GenericComponents.QuizLabel(
            "Number of Questions per Game:",
            new Font("Times New Roman", Font.BOLD, 14),
            GenericComponents.ThemeManager.getText(),
            null, 5, false, false, false
        );
        settingsPanel.add(questionsLabel.getComponent(), gbc);

        gbc.gridy = 4;
        gbc.gridwidth = 1;
        JSpinner questionsSpinner = new JSpinner(new SpinnerNumberModel(Settings.getInstance().getQuestionsPerGame(), 5, 20, 1));
        questionsSpinner.setPreferredSize(new Dimension(100, 30));
        settingsPanel.add(questionsSpinner, gbc);

        // Points per question
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        GenericComponents.QuizLabel pointsLabel = new GenericComponents.QuizLabel(
            "Points per Question:",
            new Font("Times New Roman", Font.BOLD, 14),
            GenericComponents.ThemeManager.getText(),
            null, 5, false, false, false
        );
        settingsPanel.add(pointsLabel.getComponent(), gbc);

        // Auto-submit checkbox
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        JCheckBox autoSubmitCheck = new JCheckBox("Auto-submit when time runs out");
        autoSubmitCheck.setSelected(Settings.getInstance().isAutoSubmitOnTimeUp());
        autoSubmitCheck.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        autoSubmitCheck.setForeground(GenericComponents.ThemeManager.getText());
        settingsPanel.add(autoSubmitCheck, gbc);

        // Theme Settings Section
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        GenericComponents.QuizLabel themeSectionLabel = new GenericComponents.QuizLabel(
            "Theme Settings",
            new Font("Times New Roman", Font.BOLD, 16),
            GenericComponents.ThemeManager.getText(),
            null, 5, false, false, false
        );
        settingsPanel.add(themeSectionLabel.getComponent(), gbc);

        // Theme Settings
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        GenericComponents.QuizLabel themeLabel = new GenericComponents.QuizLabel(
            "Theme:",
            new Font("Times New Roman", Font.BOLD, 14),
            GenericComponents.ThemeManager.getText(),
            null, 5, false, false, false
        );
        settingsPanel.add(themeLabel.getComponent(), gbc);

        gbc.gridy = 10;
        gbc.gridwidth = 1;
        String[] themes = {"Light", "Dark"};
        JComboBox<String> themeCombo = new JComboBox<>(themes);
        themeCombo.setSelectedItem(GenericComponents.ThemeManager.isDarkMode() ? "Dark" : "Light");
        themeCombo.setPreferredSize(new Dimension(100, 30));
        settingsPanel.add(themeCombo, gbc);

        // Add some padding at the bottom
        gbc.gridy = 15;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        settingsPanel.add(Box.createVerticalStrut(20), gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10),
            BorderFactory.createLineBorder(new Color(0, 0, 0, 30), 1)
        ));

        GenericComponents.QuizButton saveButton = new GenericComponents.QuizButton(
            "Save Settings",
            new Color(46, 204, 113),
            new Color(39, 174, 96),
            Color.WHITE,
            new Font("Times New Roman", Font.BOLD, 14),
            8, 20,
            true, true,
            e -> {
                // Save all settings
                Settings.getInstance().setQuestionTimeLimit((Integer) timerSpinner.getValue());
                Settings.getInstance().setQuestionsPerGame((Integer) questionsSpinner.getValue());
                Settings.getInstance().setAutoSubmitOnTimeUp(autoSubmitCheck.isSelected());
                
                // Apply theme if changed
                String selectedTheme = (String) themeCombo.getSelectedItem();
                if (selectedTheme.equals("Dark") != GenericComponents.ThemeManager.isDarkMode()) {
                    // Only toggle if we need to change the theme
                    if (selectedTheme.equals("Dark")) {
                        if (!GenericComponents.ThemeManager.isDarkMode()) {
                            GenericComponents.ThemeManager.toggleTheme();
                        }
                    } else {
                        if (GenericComponents.ThemeManager.isDarkMode()) {
                            GenericComponents.ThemeManager.toggleTheme();
                        }
                    }
                    Settings.getInstance().setDarkMode(selectedTheme.equals("Dark"));
                    applyTheme(Settings.getInstance().isDarkMode());
                }
                
                // Update clock duration
                circularClock.setDuration();
                
                JOptionPane.showMessageDialog(this, "Settings saved successfully!");
                showMainMenu();
            }
        );

        GenericComponents.QuizButton resetButton = new GenericComponents.QuizButton(
            "Reset to Defaults",
            new Color(52, 152, 219),
            new Color(41, 128, 185),
            Color.WHITE,
            new Font("Times New Roman", Font.BOLD, 14),
            8, 20,
            true, true,
            e -> {
                Settings.getInstance().resetToDefaults();
                showSettings(); // Refresh settings panel with default values
            }
        );

        GenericComponents.QuizButton backButton = new GenericComponents.QuizButton(
            "Back",
            new Color(231, 76, 60),
            new Color(192, 57, 43),
            Color.WHITE,
            new Font("Times New Roman", Font.BOLD, 14),
            8, 20,
            true, true,
            e -> showMainMenu()
        );

        buttonPanel.add(saveButton.getComponent());
        buttonPanel.add(resetButton.getComponent());
        buttonPanel.add(backButton.getComponent());

        // Add components to content panel
        content.add(scrollPane, BorderLayout.CENTER);
        content.add(buttonPanel, BorderLayout.SOUTH);

        content.revalidate();
        content.repaint();
    }

    private void applyTheme(boolean isDarkMode) {
        // Update root panel background
        rootPanel.setBackground(GenericComponents.ThemeManager.getBackground());
        
        // Save reference to old content panel
        JPanel oldContent = (JPanel) contentPanel.getComponent();
        LayoutManager oldLayout = oldContent.getLayout();
        Component[] oldComponents = oldContent.getComponents();
        
        // Create new content panel with updated colors but same layout
        contentPanel = new GenericComponents.QuizPanel(
            GenericComponents.ThemeManager.getBackground(),
            GenericComponents.ThemeManager.getBorder(),
            5, 15, true, true,
            oldLayout,
            false, null, null
        );
        
        // Set up new content panel and transfer components
        JPanel newContent = (JPanel) contentPanel.getComponent();
        for (Component comp : oldComponents) {
            newContent.add(comp);
        }
        
        // remove the old content panel from root
        rootPanel.remove(oldContent);
        // add the new content panel to root
        rootPanel.add(newContent, BorderLayout.CENTER);
        
        // Update headers and footers with new theme colors
        headerContainer.setBackground(isDarkMode ? new Color(33, 33, 33) : GenericComponents.ThemeManager.getHeader());
        footerPanel.setBackground(GenericComponents.ThemeManager.getFooter());
        
        // Recursively update all component colors
        updateComponentColors(rootPanel);
        
        // Refresh the UI
        rootPanel.revalidate();
        rootPanel.repaint();
    }

    private void updateComponentColors(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JLabel) {
                ((JLabel) comp).setForeground(GenericComponents.ThemeManager.getText());
            } else if (comp instanceof JPanel) {
                ((JPanel) comp).setBackground(GenericComponents.ThemeManager.getBackground());
                updateComponentColors((Container) comp);
            } else if (comp instanceof JButton) {
                ((JButton) comp).setBackground(GenericComponents.ThemeManager.getButton());
                ((JButton) comp).setForeground(GenericComponents.ThemeManager.getText());
            }
        }
    }

    private void handleResetApp() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to reset the app? This will:\n" +
            "1. Reset all settings to defaults\n" +
            "2. Clear any ongoing game\n" +
            "3. Reset theme to default\n\n" +
            "This action cannot be undone.",
            "Confirm Reset",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            // Reset settings to defaults
            Settings.getInstance().resetToDefaults();
            
            // Reset theme to light mode
            if (GenericComponents.ThemeManager.isDarkMode()) {
                GenericComponents.ThemeManager.toggleTheme();
            }
            
            // Reset game state
            if (gameController != null) {
                gameController = new GameController();
            }
            
            // Reset UI state
            circularClock.stop();
            currentQuestionIndex = 0;
            clockPanel.setVisible(false);
            
            // Show success message
            JOptionPane.showMessageDialog(
                this,
                "App has been reset to defaults successfully!",
                "Reset Complete",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            // Refresh the main menu
            showMainMenu();
        }
    }

    public void cleanup() {
        // Stop the circular clock
        if (circularClock != null) {
            circularClock.stop();
        }
        
        // Stop the slide timer
        if (slideTimer != null && slideTimer.isRunning()) {
            slideTimer.stop();
        }
        
        // Reset game state
        currentQuestionIndex = 0;
        if (gameController != null) {
            gameController.reset();
        }
        
        // Hide clock panel
        if (clockPanel != null) {
            clockPanel.setVisible(false);
        }
    }
} 