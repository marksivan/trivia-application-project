
import java.awt.*;
import javax.swing.*;
import DOM.GenericComponents;
import java.util.ArrayList;
import java.util.List;

public class HomePage extends JPanel {
    private JPanel rootPanel;
    private GenericComponents.QuizPanel contentPanel;
    private GenericComponents.QuizCircularClock circularClock;
    private JPanel headerContainer;
    private JPanel footerPanel;
    private JPanel clockPanel;
    private Timer slideTimer;
    private int currentQuestionIndex = 0;
    private List<GenericComponents.QuizQuestion> questions;
    private int score = 0;
    private String selectedCategory = "General"; // Default category

    public HomePage() {
        setLayout(new BorderLayout());
        setBackground(GenericComponents.ThemeManager.getBackground());

        // Initialize questions
        initializeQuestions();

        // Root Panel with shadow
        rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(GenericComponents.ThemeManager.getBackground());
        rootPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(5, 5, 5, 5),
            BorderFactory.createLineBorder(new Color(0, 0, 0, 30), 1)
        ));
        add(rootPanel);

        // Header with shadow
        GenericComponents.QuizHeader header = new GenericComponents.QuizHeader(
            "Trivia Game", 
            "Welcome, " + AuthManager.getInstance().getCurrentUser()
        );
        headerContainer = new JPanel(new BorderLayout());
        headerContainer.setBackground(GenericComponents.ThemeManager.isDarkMode() ? 
            new Color(33, 33, 33) : GenericComponents.ThemeManager.getHeader());
        headerContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(5, 5, 5, 5),
            BorderFactory.createLineBorder(new Color(0, 0, 0, 30), 1)
        ));
        headerContainer.add(header.getComponent(), BorderLayout.CENTER);

        // Add back button to header
        GenericComponents.QuizButton backButton = new GenericComponents.QuizButton(
            "Home",
            GenericComponents.ThemeManager.getButton(),
            GenericComponents.ThemeManager.getButtonHover(),
            Color.WHITE,
            new Font("Helvetica", Font.BOLD, 14),
            8, 15, true, true,
            e -> {
                if (currentQuestionIndex > 0) {
                    int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to return to home? Your game progress will be lost.",
                        "Confirm Return",
                        JOptionPane.YES_NO_OPTION
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        circularClock.stop();
                        currentQuestionIndex = 0;
                        score = 0;
                        clockPanel.setVisible(false);
                        ((JPanel)contentPanel.getComponent()).removeAll();
                        showMainMenu();
                        rootPanel.revalidate();
                        rootPanel.repaint();
                    }
                } else {
                    ((JPanel)contentPanel.getComponent()).removeAll();
                    showMainMenu();
                    rootPanel.revalidate();
                    rootPanel.repaint();
                }
            }
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

        // Footer with shadow
        footerPanel = new JPanel(new BorderLayout(10, 10));
        footerPanel.setBackground(GenericComponents.ThemeManager.getFooter());
        footerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(5, 5, 5, 5),
            BorderFactory.createLineBorder(new Color(0, 0, 0, 30), 1)
        ));
        rootPanel.add(footerPanel, BorderLayout.SOUTH);

        // Content Panel with responsive layout
        contentPanel = new GenericComponents.QuizPanel(
            GenericComponents.ThemeManager.getBackground(),
            GenericComponents.ThemeManager.getBorder(),
            5, 15, true, true,
            new GridBagLayout(),
            false, null, null
        );

        JPanel content = (JPanel) contentPanel.getComponent();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Add shadow to welcome label
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setOpaque(false);
        welcomePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(5, 5, 5, 5),
            BorderFactory.createLineBorder(new Color(0, 0, 0, 30), 1)
        ));

        GenericComponents.QuizLabel welcomeLabel = new GenericComponents.QuizLabel(
            "Welcome to the Ultimate Trivia Challenge!",
            new Font("Helvetica", Font.BOLD, 24),
            GenericComponents.ThemeManager.getText(),
            null, 20, true, false, true
        );
        welcomePanel.add(welcomeLabel.getComponent(), BorderLayout.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        content.add(welcomePanel, gbc);

        // Create category cards panel with responsive grid
        JPanel categoryPanel = new JPanel(new GridBagLayout());
        categoryPanel.setOpaque(false);
        categoryPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] categories = {"General", "Science", "History", "Geography"};
        String[] descriptions = {
            "Test your general knowledge across various topics",
            "Explore scientific facts and discoveries",
            "Journey through historical events and figures",
            "Discover geographical wonders and locations"
        };
        
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        for (int i = 0; i < categories.length; i++) {
            String category = categories[i];
            String description = descriptions[i];
            
            JPanel cardPanel = new JPanel(new BorderLayout(10, 10));
            cardPanel.setBackground(GenericComponents.ThemeManager.getButton());
            cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                BorderFactory.createLineBorder(new Color(0, 0, 0, 30), 1)
            ));
            cardPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Category icon (using emoji as placeholder)
            String icon = "";
            switch (category) {
                case "General": icon = "🎯"; break;
                case "Science": icon = "🔬"; break;
                case "History": icon = "📚"; break;
                case "Geography": icon = "🌍"; break;
            }

            // Create icon panel with shadow
            JPanel iconPanel = new JPanel(new BorderLayout());
            iconPanel.setOpaque(false);
            GenericComponents.QuizLabel iconLabel = new GenericComponents.QuizLabel(
                icon,
                new Font("Helvetica", Font.PLAIN, 48),
                GenericComponents.ThemeManager.getText(),
                null, 10, true, false, true
            );
            iconPanel.add(iconLabel.getComponent(), BorderLayout.CENTER);

            // Create text panel
            JPanel textPanel = new JPanel(new GridBagLayout());
            textPanel.setOpaque(false);
            GridBagConstraints textGbc = new GridBagConstraints();
            textGbc.gridx = 0;
            textGbc.gridy = 0;
            textGbc.insets = new Insets(5, 5, 5, 5);
            textGbc.fill = GridBagConstraints.HORIZONTAL;

            GenericComponents.QuizLabel categoryLabel = new GenericComponents.QuizLabel(
                category,
                new Font("Helvetica", Font.BOLD, 24),
                Color.WHITE,
                null, 10, true, false, true
            );
            textPanel.add(categoryLabel.getComponent(), textGbc);

            textGbc.gridy = 1;
            GenericComponents.QuizLabel descriptionLabel = new GenericComponents.QuizLabel(
                description,
                new Font("Helvetica", Font.PLAIN, 14),
                new Color(240, 240, 240),
                null, 5, true, false, true
            );
            textPanel.add(descriptionLabel.getComponent(), textGbc);

            // Add components to card
            cardPanel.add(iconPanel, BorderLayout.NORTH);
            cardPanel.add(textPanel, BorderLayout.CENTER);

            // Add hover effect with animation
            cardPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                private Timer hoverTimer;
                private float currentScale = 1.0f;
                private static final float HOVER_SCALE = 1.05f;

                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    if (hoverTimer != null) hoverTimer.stop();
                    hoverTimer = new Timer(16, e -> {
                        if (currentScale < HOVER_SCALE) {
                            currentScale += 0.01f;
                            cardPanel.setBackground(GenericComponents.ThemeManager.getButtonHover());
                        } else {
                            hoverTimer.stop();
                        }
                        cardPanel.repaint();
                    });
                    hoverTimer.start();
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if (hoverTimer != null) hoverTimer.stop();
                    hoverTimer = new Timer(16, e -> {
                        if (currentScale > 1.0f) {
                            currentScale -= 0.01f;
                            cardPanel.setBackground(GenericComponents.ThemeManager.getButton());
                        } else {
                            hoverTimer.stop();
                        }
                        cardPanel.repaint();
                    });
                    hoverTimer.start();
                }

                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    selectedCategory = category;
                    initializeQuestions();
                    startGame();
                }
            });

            gbc.gridx = i % 2;
            gbc.gridy = i / 2;
            categoryPanel.add(cardPanel, gbc);
        }

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        content.add(categoryPanel, gbc);

        // Add shadow to button panel with responsive layout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(15, 15, 15, 15),
            BorderFactory.createLineBorder(new Color(0, 0, 0, 30), 1)
        ));

        GenericComponents.QuizButton settingsButton = new GenericComponents.QuizButton(
            "Settings",
            GenericComponents.ThemeManager.getButton(),
            GenericComponents.ThemeManager.getButtonHover(),
            GenericComponents.ThemeManager.getText(),
            new Font("Helvetica", Font.BOLD, 16),
            10, 25, true, true,
            e -> showSettings()
        );

        GenericComponents.QuizButton resetButton = new GenericComponents.QuizButton(
            "Reset",
            GenericComponents.ThemeManager.getButton(),
            GenericComponents.ThemeManager.getButtonHover(),
            GenericComponents.ThemeManager.getText(),
            new Font("Helvetica", Font.BOLD, 16),
            10, 25, true, true,
            e -> resetGame()
        );

        GenericComponents.QuizButton stopButton = new GenericComponents.QuizButton(
            "Stop",
            GenericComponents.ThemeManager.getButton(),
            GenericComponents.ThemeManager.getButtonHover(),
            GenericComponents.ThemeManager.getText(),
            new Font("Helvetica", Font.BOLD, 16),
            10, 25, true, true,
            e -> {
                if (currentQuestionIndex > 0) {
                    int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to stop the game? Your progress will be lost.",
                        "Confirm Stop",
                        JOptionPane.YES_NO_OPTION
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        circularClock.stop();
                        currentQuestionIndex = 0;
                        score = 0;
                        clockPanel.setVisible(false);
                        showMainMenu();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Please start a game first!");
                }
            }
        );

        GenericComponents.QuizButton musicButton = new GenericComponents.QuizButton(
            "Music",
            GenericComponents.ThemeManager.getButton(),
            GenericComponents.ThemeManager.getButtonHover(),
            GenericComponents.ThemeManager.getText(),
            new Font("Helvetica", Font.BOLD, 16),
            10, 25, true, true,
            e -> {
                JOptionPane.showMessageDialog(this, "Music feature coming soon!");
            }
        );

        buttonPanel.add(settingsButton.getComponent());
        buttonPanel.add(resetButton.getComponent());
        buttonPanel.add(stopButton.getComponent());
        buttonPanel.add(musicButton.getComponent());

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        content.add(buttonPanel, gbc);

        rootPanel.add(contentPanel.getComponent(), BorderLayout.CENTER);
        rootPanel.revalidate();
        rootPanel.repaint();
    }

    private void initializeQuestions() {
        questions = new ArrayList<>();
        int questionsPerGame = Settings.getInstance().getQuestionsPerGame();
        
        // General Knowledge Questions
        if (selectedCategory.equals("General")) {
            questions.add(new GenericComponents.QuizQuestion(
                "What is the capital of France?",
                new String[]{"London", "Paris", "Berlin", "Madrid"},
                "Paris"
            ));
            questions.add(new GenericComponents.QuizQuestion(
                "Which planet is known as the Red Planet?",
                new String[]{"Venus", "Mars", "Jupiter", "Saturn"},
                "Mars"
            ));
            questions.add(new GenericComponents.QuizQuestion(
                "What is the largest mammal in the world?",
                new String[]{"African Elephant", "Blue Whale", "Giraffe", "Hippopotamus"},
                "Blue Whale"
            ));
            questions.add(new GenericComponents.QuizQuestion(
                "Who painted the Mona Lisa?",
                new String[]{"Vincent van Gogh", "Pablo Picasso", "Leonardo da Vinci", "Michelangelo"},
                "Leonardo da Vinci"
            ));
            questions.add(new GenericComponents.QuizQuestion(
                "What is the chemical symbol for gold?",
                new String[]{"Ag", "Au", "Fe", "Cu"},
                "Au"
            ));
        }
        // Science Questions
        else if (selectedCategory.equals("Science")) {
            questions.add(new GenericComponents.QuizQuestion(
                "What is the chemical symbol for gold?",
                new String[]{"Ag", "Au", "Fe", "Cu"},
                "Au"
            ));
            questions.add(new GenericComponents.QuizQuestion(
                "What is the hardest natural substance on Earth?",
                new String[]{"Gold", "Iron", "Diamond", "Platinum"},
                "Diamond"
            ));
            questions.add(new GenericComponents.QuizQuestion(
                "What is the main component of the Sun?",
                new String[]{"Helium", "Hydrogen", "Oxygen", "Carbon"},
                "Hydrogen"
            ));
            questions.add(new GenericComponents.QuizQuestion(
                "What is the speed of light?",
                new String[]{"299,792,458 m/s", "299,792,458 km/s", "299,792,458 mph", "299,792,458 km/h"},
                "299,792,458 m/s"
            ));
            questions.add(new GenericComponents.QuizQuestion(
                "What is the atomic number of carbon?",
                new String[]{"6", "12", "14", "16"},
                "6"
            ));
        }
        // History Questions
        else if (selectedCategory.equals("History")) {
            questions.add(new GenericComponents.QuizQuestion(
                "In which year did World War II end?",
                new String[]{"1943", "1944", "1945", "1946"},
                "1945"
            ));
            questions.add(new GenericComponents.QuizQuestion(
                "Who was the first President of the United States?",
                new String[]{"Thomas Jefferson", "John Adams", "George Washington", "Benjamin Franklin"},
                "George Washington"
            ));
            questions.add(new GenericComponents.QuizQuestion(
                "Which ancient wonder was located in Alexandria?",
                new String[]{"Colossus of Rhodes", "Lighthouse", "Hanging Gardens", "Temple of Artemis"},
                "Lighthouse"
            ));
            questions.add(new GenericComponents.QuizQuestion(
                "When did the Titanic sink?",
                new String[]{"1910", "1912", "1914", "1916"},
                "1912"
            ));
            questions.add(new GenericComponents.QuizQuestion(
                "Who was the first woman to win a Nobel Prize?",
                new String[]{"Marie Curie", "Mother Teresa", "Jane Addams", "Pearl S. Buck"},
                "Marie Curie"
            ));
        }
        // Geography Questions
        else if (selectedCategory.equals("Geography")) {
            questions.add(new GenericComponents.QuizQuestion(
                "Which is the largest ocean on Earth?",
                new String[]{"Atlantic", "Indian", "Arctic", "Pacific"},
                "Pacific"
            ));
            questions.add(new GenericComponents.QuizQuestion(
                "What is the longest river in the world?",
                new String[]{"Amazon", "Nile", "Mississippi", "Yangtze"},
                "Nile"
            ));
            questions.add(new GenericComponents.QuizQuestion(
                "Which country has the most natural lakes?",
                new String[]{"Canada", "Russia", "USA", "Finland"},
                "Canada"
            ));
            questions.add(new GenericComponents.QuizQuestion(
                "What is the highest mountain in the world?",
                new String[]{"K2", "Mount Everest", "Kangchenjunga", "Lhotse"},
                "Mount Everest"
            ));
            questions.add(new GenericComponents.QuizQuestion(
                "Which desert is the largest in the world?",
                new String[]{"Sahara", "Antarctic", "Arctic", "Gobi"},
                "Antarctic"
            ));
        }

        // Shuffle questions and limit to the selected number
        java.util.Collections.shuffle(questions);
        if (questions.size() > questionsPerGame) {
            questions = questions.subList(0, questionsPerGame);
        }
    }

    private void startGame() {
        currentQuestionIndex = 0;
        score = 0;
        circularClock.reset();
        circularClock.setDuration(Settings.getInstance().getQuestionTimeLimit());
        showQuestion(currentQuestionIndex);
    }

    private void showQuestion(int index) {
        if (index >= questions.size()) {
            circularClock.stop();
            showGameOver();
            return;
        }

        JPanel content = (JPanel) contentPanel.getComponent();
        content.removeAll();
        content.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(20, 20, 20, 20);

        // Create question panel with maximum width and shadow
        JPanel questionPanel = new JPanel(new BorderLayout(10, 10));
        questionPanel.setOpaque(false);
        questionPanel.setPreferredSize(new Dimension(content.getWidth() - 100, content.getHeight() - 100));
        
        // Add shadow effect to question panel with theme-aware colors
        JPanel shadowPanel = new JPanel(new BorderLayout(10, 10));
        shadowPanel.setBackground(GenericComponents.ThemeManager.getBackground());
        shadowPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(5, 5, 5, 5),
            BorderFactory.createLineBorder(
                GenericComponents.ThemeManager.isDarkMode() ? 
                new Color(255, 255, 255, 30) : new Color(0, 0, 0, 30), 
                1
            )
        ));
        shadowPanel.add(questionPanel);

        // Add question with theme-aware styling
        GenericComponents.QuizQuestion question = questions.get(index);
        JPanel questionComponent = (JPanel) question.getComponent();
        questionComponent.setPreferredSize(new Dimension(questionPanel.getPreferredSize().width - 40, 
                                                       questionPanel.getPreferredSize().height - 100));
        
        // Style question component for dark theme
        questionComponent.setBackground(GenericComponents.ThemeManager.isDarkMode() ? 
            new Color(45, 45, 45) : Color.WHITE);
        
        // Increase font size and set theme-aware colors for question and options
        for (Component comp : questionComponent.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                Font currentFont = label.getFont();
                label.setFont(new Font(currentFont.getName(), currentFont.getStyle(), 20));
                label.setForeground(GenericComponents.ThemeManager.getText());
            } else if (comp instanceof JPanel) {
                JPanel optionPanel = (JPanel) comp;
                optionPanel.setBackground(GenericComponents.ThemeManager.isDarkMode() ? 
                    new Color(45, 45, 45) : Color.WHITE);
                
                for (Component subComp : optionPanel.getComponents()) {
                    if (subComp instanceof JRadioButton) {
                        JRadioButton radio = (JRadioButton) subComp;
                        Font currentFont = radio.getFont();
                        radio.setFont(new Font(currentFont.getName(), currentFont.getStyle(), 18));
                        radio.setForeground(GenericComponents.ThemeManager.getText());
                        radio.setBackground(GenericComponents.ThemeManager.isDarkMode() ? 
                            new Color(45, 45, 45) : Color.WHITE);
                    }
                }
            }
        }
        
        questionPanel.add(questionComponent, BorderLayout.CENTER);

        // Add submit button with theme-aware styling
        GenericComponents.QuizButton submitButton = new GenericComponents.QuizButton(
            "Submit Answer",
            GenericComponents.ThemeManager.getButton(),
            GenericComponents.ThemeManager.getButtonHover(),
            GenericComponents.ThemeManager.getText(),
            new Font("Helvetica", Font.BOLD, 16),
            8, 20, true, true,
            e -> {
                circularClock.stop();
                handleAnswer(question);
            }
        );

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(submitButton.getComponent());
        questionPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add score display with theme-aware styling
        GenericComponents.QuizLabel scoreLabel = new GenericComponents.QuizLabel(
            "Score: " + score,
            new Font("Helvetica", Font.BOLD, 20),
            GenericComponents.ThemeManager.getText(),
            null, 10, false, false, true
        );
        questionPanel.add(scoreLabel.getComponent(), BorderLayout.NORTH);

        // Add the shadow panel to the center container
        gbc.gridx = 0;
        gbc.gridy = 0;
        content.add(shadowPanel, gbc);
        
        // Start slide animation
        if (slideTimer != null) {
            slideTimer.stop();
        }
        
        // Set initial position to right corner with offset
        questionPanel.setLocation(content.getWidth() + 50, content.getHeight() / 2 - questionPanel.getHeight() / 2);
        slideTimer = new Timer(16, e -> {
            int currentX = questionPanel.getX();
            int targetX = (content.getWidth() - questionPanel.getWidth()) / 2;
            if (currentX > targetX) {
                // Calculate distance to target
                int distance = currentX - targetX;
                // Move slower when closer to target
                int moveAmount = Math.max(5, Math.min(15, distance / 10));
                questionPanel.setLocation(currentX - moveAmount, questionPanel.getY());
            } else {
                questionPanel.setLocation(targetX, questionPanel.getY());
                slideTimer.stop();
                // Start the clock after the slide animation completes
                circularClock.reset();
                circularClock.setDuration(Settings.getInstance().getQuestionTimeLimit());
                circularClock.start();
            }
            content.repaint();
        });
        slideTimer.start();

        // Set up auto-submit when time runs out
        circularClock.setOnTimeUp(v -> {
            if (Settings.getInstance().isAutoSubmitOnTimeUp()) {
                if (question.checkAnswer()) {
                    score += Settings.getInstance().getPointsPerQuestion();
                    JOptionPane.showMessageDialog(this, "Time's up! Correct answer: " + question.getCorrectAnswer() + 
                        "\n+" + Settings.getInstance().getPointsPerQuestion() + " points");
                } else {
                    JOptionPane.showMessageDialog(this, "Time's up! The correct answer was: " + question.getCorrectAnswer());
                }
                currentQuestionIndex++;
                showQuestion(currentQuestionIndex);
            } else {
                JOptionPane.showMessageDialog(this, "Time's up! The correct answer was: " + question.getCorrectAnswer());
                currentQuestionIndex++;
                showQuestion(currentQuestionIndex);
            }
        });

        content.revalidate();
        content.repaint();
    }

    private void handleAnswer(GenericComponents.QuizQuestion question) {
        // Check if answer is correct
        boolean isCorrect = question.checkAnswer();
        if (isCorrect) {
            score += Settings.getInstance().getPointsPerQuestion();
            JOptionPane.showMessageDialog(this, "Correct! +" + Settings.getInstance().getPointsPerQuestion() + " points");
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect! The correct answer was: " + question.getCorrectAnswer());
        }

        // Move to next question
        currentQuestionIndex++;
        showQuestion(currentQuestionIndex);
    }

    private void showGameOver() {
        JPanel content = (JPanel) contentPanel.getComponent();
        content.removeAll();

        JPanel gameOverPanel = new JPanel(new BorderLayout(20, 20));
        gameOverPanel.setOpaque(false);
        gameOverPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(5, 5, 5, 5),
            BorderFactory.createLineBorder(new Color(0, 0, 0, 30), 1)
        ));

        GenericComponents.QuizLabel gameOverLabel = new GenericComponents.QuizLabel(
            "Game Over!",
            new Font("Helvetica", Font.BOLD, 32),
            GenericComponents.ThemeManager.getText(),
            null, 20, true, false, true
        );

        GenericComponents.QuizLabel finalScoreLabel = new GenericComponents.QuizLabel(
            "Final Score: " + score,
            new Font("Helvetica", Font.BOLD, 24),
            GenericComponents.ThemeManager.getText(),
            null, 20, true, false, true
        );

        GenericComponents.QuizButton playAgainButton = new GenericComponents.QuizButton(
            "Play Again",
            GenericComponents.ThemeManager.getButton(),
            GenericComponents.ThemeManager.getButtonHover(),
            GenericComponents.ThemeManager.getText(),
            new Font("Helvetica", Font.BOLD, 16),
            15, 20, true, true,
            e -> {
                // Reset game state
                currentQuestionIndex = 0;
                score = 0;
                circularClock.stop();
                clockPanel.setVisible(false);
                
                // Clear content and show main menu
                content.removeAll();
                showMainMenu();
                content.revalidate();
                content.repaint();
            }
        );

        gameOverPanel.add(gameOverLabel.getComponent(), BorderLayout.NORTH);
        gameOverPanel.add(finalScoreLabel.getComponent(), BorderLayout.CENTER);
        gameOverPanel.add(playAgainButton.getComponent(), BorderLayout.SOUTH);

        content.add(gameOverPanel, BorderLayout.CENTER);
        content.revalidate();
        content.repaint();
    }

    private void resetGame() {
        if (currentQuestionIndex == 0 && score == 0) {
            JOptionPane.showMessageDialog(this, "Please start a game first!");
            return;
        }
        circularClock.stop();
        currentQuestionIndex = 0;
        score = 0;
        showQuestion(currentQuestionIndex);
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
            new Font("Helvetica", Font.BOLD, 16),
            GenericComponents.ThemeManager.getText(),
            null, 5, false, false, false
        );
        settingsPanel.add(gameSettingsLabel.getComponent(), gbc);

        // Timer Settings
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        GenericComponents.QuizLabel timerLabel = new GenericComponents.QuizLabel(
            "Question Time Limit (seconds):",
            new Font("Helvetica", Font.BOLD, 14),
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
            new Font("Helvetica", Font.BOLD, 14),
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
            new Font("Helvetica", Font.BOLD, 14),
            GenericComponents.ThemeManager.getText(),
            null, 5, false, false, false
        );
        settingsPanel.add(pointsLabel.getComponent(), gbc);

        gbc.gridy = 6;
        gbc.gridwidth = 1;
        JSpinner pointsSpinner = new JSpinner(new SpinnerNumberModel(Settings.getInstance().getPointsPerQuestion(), 1, 100, 5));
        pointsSpinner.setPreferredSize(new Dimension(100, 30));
        settingsPanel.add(pointsSpinner, gbc);

        // Auto-submit checkbox
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        JCheckBox autoSubmitCheck = new JCheckBox("Auto-submit when time runs out");
        autoSubmitCheck.setSelected(Settings.getInstance().isAutoSubmitOnTimeUp());
        autoSubmitCheck.setFont(new Font("Helvetica", Font.PLAIN, 14));
        autoSubmitCheck.setForeground(GenericComponents.ThemeManager.getText());
        settingsPanel.add(autoSubmitCheck, gbc);

        // Theme Settings Section
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        GenericComponents.QuizLabel themeSectionLabel = new GenericComponents.QuizLabel(
            "Theme Settings",
            new Font("Helvetica", Font.BOLD, 16),
            GenericComponents.ThemeManager.getText(),
            null, 5, false, false, false
        );
        settingsPanel.add(themeSectionLabel.getComponent(), gbc);

        // Theme Settings
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        GenericComponents.QuizLabel themeLabel = new GenericComponents.QuizLabel(
            "Theme:",
            new Font("Helvetica", Font.BOLD, 14),
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

        // Sound Settings Section
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        GenericComponents.QuizLabel soundSectionLabel = new GenericComponents.QuizLabel(
            "Sound Settings",
            new Font("Helvetica", Font.BOLD, 16),
            GenericComponents.ThemeManager.getText(),
            null, 5, false, false, false
        );
        settingsPanel.add(soundSectionLabel.getComponent(), gbc);

        // Sound Settings
        gbc.gridy = 12;
        gbc.gridwidth = 2;
        JCheckBox soundCheck = new JCheckBox("Enable Sound");
        soundCheck.setSelected(Settings.getInstance().isSoundEnabled());
        soundCheck.setFont(new Font("Helvetica", Font.PLAIN, 14));
        soundCheck.setForeground(GenericComponents.ThemeManager.getText());
        settingsPanel.add(soundCheck, gbc);

        gbc.gridy = 13;
        gbc.gridwidth = 2;
        GenericComponents.QuizLabel volumeLabel = new GenericComponents.QuizLabel(
            "Volume:",
            new Font("Helvetica", Font.BOLD, 14),
            GenericComponents.ThemeManager.getText(),
            null, 5, false, false, false
        );
        settingsPanel.add(volumeLabel.getComponent(), gbc);

        gbc.gridy = 14;
        gbc.gridwidth = 2;
        JSlider volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, (int)(Settings.getInstance().getSoundVolume() * 100));
        volumeSlider.setPreferredSize(new Dimension(200, 30));
        settingsPanel.add(volumeSlider, gbc);

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
            new Font("Helvetica", Font.BOLD, 14),
            8, 20,
            true, true,
            e -> {
                // Save all settings
                Settings.getInstance().setQuestionTimeLimit((Integer) timerSpinner.getValue());
                Settings.getInstance().setQuestionsPerGame((Integer) questionsSpinner.getValue());
                Settings.getInstance().setPointsPerQuestion((Integer) pointsSpinner.getValue());
                Settings.getInstance().setAutoSubmitOnTimeUp(autoSubmitCheck.isSelected());
                Settings.getInstance().setSoundEnabled(soundCheck.isSelected());
                Settings.getInstance().setSoundVolume(volumeSlider.getValue() / 100f);
                
                // Apply theme if changed
                String selectedTheme = (String) themeCombo.getSelectedItem();
                if (selectedTheme.equals("Dark") != GenericComponents.ThemeManager.isDarkMode()) {
                    Settings.getInstance().setDarkMode(selectedTheme.equals("Dark"));
                    applyTheme(Settings.getInstance().isDarkMode());
                }
                
                // Update clock duration
                circularClock.setDuration(Settings.getInstance().getQuestionTimeLimit());
                
                JOptionPane.showMessageDialog(this, "Settings saved successfully!");
                showMainMenu();
            }
        );

        GenericComponents.QuizButton resetButton = new GenericComponents.QuizButton(
            "Reset to Defaults",
            new Color(52, 152, 219),
            new Color(41, 128, 185),
            Color.WHITE,
            new Font("Helvetica", Font.BOLD, 14),
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
            new Font("Helvetica", Font.BOLD, 14),
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
        rootPanel.setBackground(GenericComponents.ThemeManager.getBackground());
        contentPanel = new GenericComponents.QuizPanel(
            GenericComponents.ThemeManager.getBackground(),
            GenericComponents.ThemeManager.getBorder(),
            5, 15, true, true,
            new BorderLayout(20, 20),
            false, null, null
        );
        headerContainer.setBackground(isDarkMode ? new Color(33, 33, 33) : GenericComponents.ThemeManager.getHeader());
        footerPanel.setBackground(GenericComponents.ThemeManager.getFooter());
        updateComponentColors(rootPanel);
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

    private void showMainMenu() {
        JPanel content = (JPanel) contentPanel.getComponent();
        content.removeAll();
        content.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Add shadow to welcome label
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setOpaque(false);
        welcomePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10),
            BorderFactory.createLineBorder(new Color(0, 0, 0, 30), 1)
        ));

        GenericComponents.QuizLabel welcomeLabel = new GenericComponents.QuizLabel(
            "Welcome to the Ultimate Trivia Challenge!",
            new Font("Helvetica", Font.BOLD, 28),
            GenericComponents.ThemeManager.getText(),
            null, 20, true, false, true
        );
        welcomePanel.add(welcomeLabel.getComponent(), BorderLayout.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        content.add(welcomePanel, gbc);

        // Create category cards panel with responsive grid
        JPanel categoryPanel = new JPanel(new GridBagLayout());
        categoryPanel.setOpaque(false);
        categoryPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        String[] categories = {"General", "Science", "History", "Geography"};
        String[] descriptions = {
            "Test your general knowledge across various topics",
            "Explore scientific facts and discoveries",
            "Journey through historical events and figures",
            "Discover geographical wonders and locations"
        };
        
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.insets = new Insets(15, 15, 15, 15);
        
        for (int i = 0; i < categories.length; i++) {
            String category = categories[i];
            String description = descriptions[i];
            
            JPanel cardPanel = new JPanel(new BorderLayout(15, 15));
            cardPanel.setBackground(GenericComponents.ThemeManager.getButton());
            cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15),
                BorderFactory.createLineBorder(new Color(0, 0, 0, 30), 1)
            ));
            cardPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Category icon (using emoji as placeholder)
            String icon = "";
            switch (category) {
                case "General": icon = "🎯"; break;
                case "Science": icon = "🔬"; break;
                case "History": icon = "📚"; break;
                case "Geography": icon = "🌍"; break;
            }

            // Create icon panel with shadow
            JPanel iconPanel = new JPanel(new BorderLayout());
            iconPanel.setOpaque(false);
            GenericComponents.QuizLabel iconLabel = new GenericComponents.QuizLabel(
                icon,
                new Font("Helvetica", Font.PLAIN, 64),
                GenericComponents.ThemeManager.getText(),
                null, 10, true, false, true
            );
            iconPanel.add(iconLabel.getComponent(), BorderLayout.CENTER);

            // Create text panel
            JPanel textPanel = new JPanel(new GridBagLayout());
            textPanel.setOpaque(false);
            GridBagConstraints textGbc = new GridBagConstraints();
            textGbc.gridx = 0;
            textGbc.gridy = 0;
            textGbc.insets = new Insets(5, 5, 5, 5);
            textGbc.fill = GridBagConstraints.HORIZONTAL;

            GenericComponents.QuizLabel categoryLabel = new GenericComponents.QuizLabel(
                category,
                new Font("Helvetica", Font.BOLD, 24),
                Color.WHITE,
                null, 10, true, false, true
            );
            textPanel.add(categoryLabel.getComponent(), textGbc);

            textGbc.gridy = 1;
            GenericComponents.QuizLabel descriptionLabel = new GenericComponents.QuizLabel(
                description,
                new Font("Helvetica", Font.PLAIN, 14),
                new Color(240, 240, 240),
                null, 5, true, false, true
            );
            textPanel.add(descriptionLabel.getComponent(), textGbc);

            // Add components to card
            cardPanel.add(iconPanel, BorderLayout.NORTH);
            cardPanel.add(textPanel, BorderLayout.CENTER);

            // Add hover effect with animation
            cardPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                private Timer hoverTimer;
                private float currentScale = 1.0f;
                private static final float HOVER_SCALE = 1.05f;

                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    if (hoverTimer != null) hoverTimer.stop();
                    hoverTimer = new Timer(16, e -> {
                        if (currentScale < HOVER_SCALE) {
                            currentScale += 0.01f;
                            cardPanel.setBackground(GenericComponents.ThemeManager.getButtonHover());
                        } else {
                            hoverTimer.stop();
                        }
                        cardPanel.repaint();
                    });
                    hoverTimer.start();
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if (hoverTimer != null) hoverTimer.stop();
                    hoverTimer = new Timer(16, e -> {
                        if (currentScale > 1.0f) {
                            currentScale -= 0.01f;
                            cardPanel.setBackground(GenericComponents.ThemeManager.getButton());
                        } else {
                            hoverTimer.stop();
                        }
                        cardPanel.repaint();
                    });
                    hoverTimer.start();
                }

                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    selectedCategory = category;
                    initializeQuestions();
                    startGame();
                }
            });

            gbc.gridx = i % 2;
            gbc.gridy = i / 2;
            categoryPanel.add(cardPanel, gbc);
        }

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        content.add(categoryPanel, gbc);

        // Add shadow to button panel with responsive layout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(15, 15, 15, 15),
            BorderFactory.createLineBorder(new Color(0, 0, 0, 30), 1)
        ));

        GenericComponents.QuizButton settingsButton = new GenericComponents.QuizButton(
            "Settings",
            GenericComponents.ThemeManager.getButton(),
            GenericComponents.ThemeManager.getButtonHover(),
            GenericComponents.ThemeManager.getText(),
            new Font("Helvetica", Font.BOLD, 16),
            10, 25, true, true,
            e -> showSettings()
        );

        GenericComponents.QuizButton resetButton = new GenericComponents.QuizButton(
            "Reset",
            GenericComponents.ThemeManager.getButton(),
            GenericComponents.ThemeManager.getButtonHover(),
            GenericComponents.ThemeManager.getText(),
            new Font("Helvetica", Font.BOLD, 16),
            10, 25, true, true,
            e -> resetGame()
        );

        GenericComponents.QuizButton stopButton = new GenericComponents.QuizButton(
            "Stop",
            GenericComponents.ThemeManager.getButton(),
            GenericComponents.ThemeManager.getButtonHover(),
            GenericComponents.ThemeManager.getText(),
            new Font("Helvetica", Font.BOLD, 16),
            10, 25, true, true,
            e -> {
                if (currentQuestionIndex > 0) {
                    int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to stop the game? Your progress will be lost.",
                        "Confirm Stop",
                        JOptionPane.YES_NO_OPTION
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        circularClock.stop();
                        currentQuestionIndex = 0;
                        score = 0;
                        clockPanel.setVisible(false);
                        showMainMenu();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Please start a game first!");
                }
            }
        );

        GenericComponents.QuizButton musicButton = new GenericComponents.QuizButton(
            "Music",
            GenericComponents.ThemeManager.getButton(),
            GenericComponents.ThemeManager.getButtonHover(),
            GenericComponents.ThemeManager.getText(),
            new Font("Helvetica", Font.BOLD, 16),
            10, 25, true, true,
            e -> {
                JOptionPane.showMessageDialog(this, "Music feature coming soon!");
            }
        );

        buttonPanel.add(settingsButton.getComponent());
        buttonPanel.add(resetButton.getComponent());
        buttonPanel.add(stopButton.getComponent());
        buttonPanel.add(musicButton.getComponent());

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        content.add(buttonPanel, gbc);

        // Make sure content panel is added to root panel
        rootPanel.removeAll();
        rootPanel.add(headerContainer, BorderLayout.NORTH);
        rootPanel.add(contentPanel.getComponent(), BorderLayout.CENTER);
        rootPanel.add(footerPanel, BorderLayout.SOUTH);

        rootPanel.revalidate();
        rootPanel.repaint();
    }
} 