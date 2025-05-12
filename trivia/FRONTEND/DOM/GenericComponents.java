package DOM;
import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;
import java.util.HashMap;
import java.util.Map;
import java.awt.font.TextAttribute;


public class GenericComponents {

    public static class QuizButton extends ComponentNode {
        public QuizButton(
            String text,
            Color backgroundColor,
            Color hoverColor,
            Color textColor,
            Font font,
            int borderRadius,
            int padding,
            boolean hasShadow,
            boolean isAnimated,
            Consumer<ActionEvent> onClick
        ) {
            super(new JButton(text) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), borderRadius, borderRadius);
                    super.paintComponent(g);
                }
            });
            
            JButton button = (JButton) getComponent();
            button.setFont(font);
            button.setBackground(backgroundColor);
            button.setForeground(textColor);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
            
            if (hasShadow) {
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 0, 0, 50), 1),
                    button.getBorder()
                ));
            }
            
            button.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    if (isAnimated) {
                        button.setBackground(hoverColor);
                    }
                }
                public void mouseExited(MouseEvent e) {
                    if (isAnimated) {
                        button.setBackground(backgroundColor);
                    }
                }
            });
            
            if (onClick != null) {
                button.addActionListener(onClick::accept);
            }
        }
    }

    public static class QuizTextField extends ComponentNode {
        public QuizTextField(
            String placeholder,
            Color borderColor,
            Color backgroundColor,
            Color textColor,
            Font font,
            int borderRadius,
            int padding,
            boolean hasFocusEffect,
            boolean isEditable,
            Consumer<String> onTextChange
        ) {
            super(new JTextField(placeholder));
            JTextField field = (JTextField) getComponent();
            
            field.setFont(font);
            field.setBackground(backgroundColor);
            field.setForeground(textColor);
            field.setEditable(isEditable);
        
            field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, borderRadius),
                BorderFactory.createEmptyBorder(padding, padding, padding, padding)
            ));
 
            if (hasFocusEffect) {
                field.addFocusListener(new FocusAdapter() {
                    public void focusGained(FocusEvent e) {
                        field.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(borderColor.darker(), borderRadius),
                            BorderFactory.createEmptyBorder(padding, padding, padding, padding)
                        ));
                    }
                    public void focusLost(FocusEvent e) {
                        field.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(borderColor, borderRadius),
                            BorderFactory.createEmptyBorder(padding, padding, padding, padding)
                        ));
                    }
                });
            }
            

            if (onTextChange != null) {
                field.getDocument().addDocumentListener(new DocumentListener() {
                    public void changedUpdate(DocumentEvent e) { onTextChange.accept(field.getText()); }
                    public void removeUpdate(DocumentEvent e) { onTextChange.accept(field.getText()); }
                    public void insertUpdate(DocumentEvent e) { onTextChange.accept(field.getText()); }
                });
            }
        }
    }

    public static class QuizLabel extends ComponentNode {
        public QuizLabel(
            String text,
            Font font,
            Color textColor,
            Color backgroundColor,
            int padding,
            boolean hasShadow,
            boolean isUnderlined,
            boolean isCentered
        ) {
            super(new JLabel(text));
            JLabel label = (JLabel) getComponent();
            
            label.setFont(font != null ? font : new Font("Helvetica", Font.BOLD, 16));
            label.setForeground(textColor != null ? textColor : new Color(44, 62, 80));
            label.setBackground(backgroundColor);
            label.setOpaque(backgroundColor != null);
            
            // Padding
            label.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
            
            // Shadow effect
            if (hasShadow) {
                label.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 0, 0, 30), 1),
                    label.getBorder()
                ));
            }
            
            // Text decoration
            if (isUnderlined) {
                Font originalFont = label.getFont();
                Map<TextAttribute, Object> attributes = new HashMap<>(originalFont.getAttributes());
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                label.setFont(originalFont.deriveFont(attributes));
            }
            
            // Alignment
            if (isCentered) {
                label.setHorizontalAlignment(SwingConstants.CENTER);
            }
        }
        
        // Constructor overload for simple usage
        public QuizLabel(String text) {
            this(text, null, null, null, 0, false, false, false);
        }
    }

    public static class QuizPanel extends ComponentNode {
        public QuizPanel(
            Color backgroundColor,
            Color borderColor,
            int borderRadius,
            int padding,
            boolean hasShadow,
            boolean isOpaque,
            LayoutManager layout,
            boolean hasGradient,
            Color gradientStart,
            Color gradientEnd
        ) {
            super(new JPanel(layout));
            JPanel panel = (JPanel) getComponent();
            
            // Basic styling
            panel.setBackground(backgroundColor);
            panel.setOpaque(isOpaque);
            
            // Border styling
            if (borderRadius > 0) {
                panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(borderColor, borderRadius),
                    BorderFactory.createEmptyBorder(padding, padding, padding, padding)
                ));
            }
            
            // Shadow effect
            if (hasShadow) {
                panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 0, 0, 50), 1),
                    panel.getBorder()
                ));
            }
            
            // Gradient background
            if (hasGradient) {
                panel.setLayout(new BorderLayout());
                JPanel gradientPanel = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2d = (Graphics2D) g;
                        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        GradientPaint gradient = new GradientPaint(
                            0, 0, gradientStart,
                            getWidth(), getHeight(), gradientEnd
                        );
                        g2d.setPaint(gradient);
                        g2d.fillRect(0, 0, getWidth(), getHeight());
                    }
                };
                panel.add(gradientPanel, BorderLayout.CENTER);
            }
        }
    }

    public static class QuizHeader extends ComponentNode {
        public QuizHeader(String title, String score) {
            super(new JPanel(new BorderLayout()));
            JPanel header = (JPanel) getComponent();
            header.setBackground(new Color(41, 128, 185));
            header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("Helvetica", Font.BOLD, 24));
            titleLabel.setForeground(Color.WHITE);
            header.add(titleLabel, BorderLayout.WEST);

            JLabel scoreLabel = new JLabel(score);
            scoreLabel.setFont(new Font("Helvetica", Font.PLAIN, 16));
            scoreLabel.setForeground(Color.WHITE);
            header.add(scoreLabel, BorderLayout.EAST);
        }
    }

    public static class QuizProgressBar extends ComponentNode {
        public QuizProgressBar(
            int min,
            int max,
            Color foregroundColor,
            Color backgroundColor,
            boolean isStripped,
            boolean isAnimated,
            String stringFormat,
            Font font
        ) {
            super(new JProgressBar(min, max));
            JProgressBar progress = (JProgressBar) getComponent();
            
            // Basic styling
            progress.setStringPainted(true);
            progress.setForeground(foregroundColor != null ? foregroundColor : new Color(46, 204, 113));
            progress.setBackground(backgroundColor != null ? backgroundColor : new Color(236, 240, 241));
            
            if (font != null) {
                progress.setFont(font);
            }
            
            // Custom string format
            if (stringFormat != null) {
                progress.setString(String.format(stringFormat, progress.getValue()));
                progress.addChangeListener(e -> 
                    progress.setString(String.format(stringFormat, progress.getValue()))
                );
            }
            
            // Stripped effect
            if (isStripped) {
                progress.setBorderPainted(false);
                progress.setUI(new javax.swing.plaf.basic.BasicProgressBarUI() {
                    @Override
                    protected void paintDeterminate(Graphics g, JComponent c) {
                        Graphics2D g2d = (Graphics2D) g;
                        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        
                        int barRectWidth = progress.getWidth();
                        int barRectHeight = progress.getHeight();
                        
                        // Paint background
                        g2d.setColor(progress.getBackground());
                        g2d.fillRect(0, 0, barRectWidth, barRectHeight);
                        
                        // Paint progress
                        g2d.setColor(progress.getForeground());
                        int stripWidth = 10;
                        double progressValue = progress.getPercentComplete();
                        int progressWidth = (int) (barRectWidth * progressValue);
                        
                        for (int x = 0; x < progressWidth; x += stripWidth * 2) {
                            g2d.fillRect(x, 0, stripWidth, barRectHeight);
                        }
                    }
                });
            }
            
            // Animation
            if (isAnimated) {
                javax.swing.Timer timer = new javax.swing.Timer(50, e -> {
                    int val = progress.getValue();
                    if (val < progress.getMaximum()) {
                        progress.setValue(val + 1);
                    } else {
                        progress.setValue(progress.getMinimum());
                    }
                });
                timer.start();
            }
        }
        
        // Constructor overload for simple usage
        public QuizProgressBar() {
            this(0, 100, null, null, false, false, "%d%%", null);
        }
    }

    public static class QuizRadioButton extends ComponentNode {
        public QuizRadioButton(String text) {
            super(new JRadioButton(text));
            JRadioButton radio = (JRadioButton) getComponent();
            radio.setFont(new Font("Helvetica", Font.PLAIN, 14));
            radio.setForeground(new Color(44, 62, 80));
        }
    }

    public static class QuizCheckBox extends ComponentNode {
        public QuizCheckBox(String text) {
            super(new JCheckBox(text));
            JCheckBox checkbox = (JCheckBox) getComponent();
            checkbox.setFont(new Font("Helvetica", Font.PLAIN, 14));
            checkbox.setForeground(new Color(44, 62, 80));
        }
    }

    public static class QuizComboBox extends ComponentNode {
        public QuizComboBox(String[] items) {
            super(new JComboBox<>(items));
            JComboBox<?> combo = (JComboBox<?>) getComponent();
            combo.setFont(new Font("Helvetica", Font.PLAIN, 14));
            combo.setBackground(Color.WHITE);
        }
    }

    public static class QuizQuestion extends ComponentNode {
        private String correctAnswer;
        private String[] options;
        private String selectedOption;
        private boolean isAnswered;
        private boolean isCorrect;
        private JRadioButton[] optionButtons;

        private JLabel questionLabel;
        private ButtonGroup optionsGroup;
        
        public QuizQuestion(String question, String[] options, String correctAnswer) {
            super(new JPanel(new BorderLayout()));
            JPanel questionPanel = (JPanel) getComponent();
            questionPanel.setBackground(Color.WHITE);
            questionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            questionLabel = new JLabel(question);
            questionLabel.setFont(new Font("Helvetica", Font.BOLD, 16));
            questionLabel.setForeground(new Color(44, 62, 80));
            questionPanel.add(questionLabel, BorderLayout.NORTH);

            JPanel optionsPanel = new JPanel(new GridLayout(options.length, 1, 0, 10));
            optionsPanel.setBackground(Color.WHITE);
            optionsGroup = new ButtonGroup();
            optionButtons = new JRadioButton[options.length];
            
            for (int i = 0; i < options.length; i++) {
                final String option = options[i];
                JRadioButton optionButton = new JRadioButton(option);
                optionButton.setFont(new Font("Helvetica", Font.PLAIN, 14));
                optionButton.setForeground(new Color(44, 62, 80));
                optionButton.addActionListener(e -> {
                    selectedOption = option;
                    if (isAnswered) {
                        showAnswerFeedback();
                    }
                });
                optionsGroup.add(optionButton);
                optionsPanel.add(optionButton);
                optionButtons[i] = optionButton;
            }
            questionPanel.add(optionsPanel, BorderLayout.CENTER);
            this.correctAnswer = correctAnswer;
            this.options = options;
        }

        public boolean checkAnswer() {
            isAnswered = true;
            isCorrect = selectedOption != null && selectedOption.equals(correctAnswer);
            showAnswerFeedback();
            return isCorrect;
        }

        private void showAnswerFeedback() {
            for (int i = 0; i < options.length; i++) {
                JRadioButton button = optionButtons[i];
                String option = options[i];
                
                if (option.equals(correctAnswer)) {
                    button.setForeground(new Color(46, 204, 113)); // Green for correct answer
                } else if (option.equals(selectedOption)) {
                    button.setForeground(new Color(231, 76, 60)); // Red for wrong answer
                } else {
                    button.setForeground(new Color(44, 62, 80)); // Default color
                }
            }
        }

        public String getCorrectAnswer() {
            return correctAnswer;
        }
    }

    public static class QuizCircularClock extends JPanel {
        private int secondsRemaining = 30;
        private Timer countdownTimer;
        private Consumer<Void> onTimeUp;

        public QuizCircularClock() {
            setPreferredSize(new Dimension(80, 80));
            setOpaque(false);

            countdownTimer = new Timer(1000, e -> {
                secondsRemaining--;
                repaint();
                if (secondsRemaining <= 0) {
                    countdownTimer.stop();
                    if (onTimeUp != null) {
                        onTimeUp.accept(null);
                    }
                }
            });
        }

        public void setDuration(int seconds) {
            secondsRemaining = seconds;
        }

        public void start() {
            countdownTimer.start();
        }

        public void stop() {
            countdownTimer.stop();
        }

        public void reset() {
            secondsRemaining = 30;
            countdownTimer.stop();
        }

        public void setOnTimeUp(Consumer<Void> callback) {
            this.onTimeUp = callback;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size = Math.min(getWidth(), getHeight());
            int padding = 8;
            int diameter = size - 2 * padding;
            int x = padding;
            int y = padding;

            // Background - use theme border color
            g2.setColor(ThemeManager.getBorder());
            g2.fillOval(x, y, diameter, diameter);

            // Time color - use theme colors
            if (secondsRemaining > 20) g2.setColor(ThemeManager.getSuccess());      // Green
            else if (secondsRemaining > 10) g2.setColor(ThemeManager.getButton());  // Theme button color
            else g2.setColor(ThemeManager.getButtonHover());                        // Theme hover color

            float angle = 360f * secondsRemaining / 30f;
            g2.fillArc(x, y, diameter, diameter, 90, -(int) angle);

            // Inner circle - use theme background
            int ringThickness = 14;
            g2.setColor(ThemeManager.getBackground());
            g2.fillOval(x + ringThickness / 2, y + ringThickness / 2,
                    diameter - ringThickness, diameter - ringThickness);

            // Text - use theme text color
            g2.setColor(ThemeManager.getText());
            g2.setFont(new Font("Helvetica", Font.BOLD, 14));
            String timeStr = secondsRemaining + "s";
            FontMetrics fm = g2.getFontMetrics();
            int strWidth = fm.stringWidth(timeStr);
            int strHeight = fm.getAscent();
            g2.drawString(timeStr, x + diameter / 2 - strWidth / 2, y + diameter / 2 + strHeight / 4);
        }
    }

    public static class QuizAlert extends ComponentNode {
        public QuizAlert(
            String message,
            AlertType type,
            boolean isDismissible
        ) {
            super(new JPanel(new BorderLayout()));
            JPanel alert = (JPanel) getComponent();
            
            Color bgColor, borderColor;
            switch (type) {
                case SUCCESS:
                    bgColor = new Color(39, 174, 96);
                    borderColor = new Color(46, 204, 113);
                    break;
                case WARNING:
                    bgColor = new Color(241, 196, 15);
                    borderColor = new Color(243, 156, 18);
                    break;
                case ERROR:
                    bgColor = new Color(231, 76, 60);
                    borderColor = new Color(192, 57, 43);
                    break;
                default:
                    bgColor = new Color(52, 152, 219);
                    borderColor = new Color(41, 128, 185);
            }
            
            alert.setBackground(bgColor);
            alert.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
            ));

            JLabel messageLabel = new JLabel(message);
            messageLabel.setFont(new Font("Helvetica", Font.PLAIN, 14));
            messageLabel.setForeground(Color.WHITE);
            alert.add(messageLabel, BorderLayout.CENTER);

            if (isDismissible) {
                JButton closeButton = new JButton("×");
                closeButton.setFont(new Font("Helvetica", Font.BOLD, 16));
                closeButton.setForeground(Color.WHITE);
                closeButton.setBackground(bgColor);
                closeButton.setBorderPainted(false);
                closeButton.setFocusPainted(false);
                closeButton.addActionListener(e -> alert.setVisible(false));
                alert.add(closeButton, BorderLayout.EAST);
            }
        }

        public enum AlertType {
            SUCCESS, WARNING, ERROR, INFO
        }
    }

    public static class QuizToolbar extends ComponentNode {
        public QuizToolbar() {
            super(new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5)));
            JPanel toolbar = (JPanel) getComponent();
            toolbar.setBackground(new Color(248, 249, 250));
            toolbar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(233, 236, 239)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
        }

        public void addButton(String text, Color color, Consumer<ActionEvent> onClick) {
            JPanel toolbar = (JPanel) getComponent();
            QuizButton button = new QuizButton(
                text, color, color.darker(), Color.WHITE,
                new Font("Helvetica", Font.PLAIN, 14),
                3, 8, false, true, onClick
            );
            toolbar.add(button.getComponent());
        }
    }

    public static class QuizRoot extends ComponentNode {
        // Header Components
        public static class Header {
            public static QuizHeader createHeader(String title, String score) {
                return new QuizHeader(title, score);
            }

            public static QuizToolbar createToolbar() {
                return new QuizToolbar();
            }
        }

        // Body Components
        public static class Body {
            public static QuizPanel createPanel(
                Color backgroundColor,
                Color borderColor,
                int borderRadius,
                int padding,
                boolean hasShadow,
                boolean isOpaque,
                LayoutManager layout,
                boolean hasGradient,
                Color gradientStart,
                Color gradientEnd
            ) {
                return new QuizPanel(
                    backgroundColor, borderColor, borderRadius, padding,
                    hasShadow, isOpaque, layout, hasGradient,
                    gradientStart, gradientEnd
                );
            }

            public static QuizQuestion createQuestion(String question, String[] options, String correctAnswer) {
                return new QuizQuestion(question, options, correctAnswer);
            }

            public static QuizCircularClock createClock() {
                return new QuizCircularClock();
            }

            public static QuizProgressBar createProgressBar(
                int min,
                int max,
                Color foregroundColor,
                Color backgroundColor,
                boolean isStripped,
                boolean isAnimated,
                String stringFormat,
                Font font
            ) {
                return new QuizProgressBar(min, max, foregroundColor, backgroundColor, isStripped, isAnimated, stringFormat, font);
            }
        }

        // Footer Components
        public static class Footer {
            public static QuizToolbar createToolbar() {
                return new QuizToolbar();
            }

            public static QuizAlert createAlert(
                String message,
                QuizAlert.AlertType type,
                boolean isDismissible
            ) {
                return new QuizAlert(message, type, isDismissible);
            }
        }

        // Root Container
        private JPanel headerPanel;
        private JPanel bodyPanel;
        private JPanel footerPanel;

        public QuizRoot() {
            super(new JPanel(new BorderLayout()));
            JPanel root = (JPanel) getComponent();
            root.setBackground(Color.WHITE);

            // Header
            headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(41, 128, 185));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
            root.add(headerPanel, BorderLayout.NORTH);

            // Body
            bodyPanel = new JPanel(new BorderLayout());
            bodyPanel.setBackground(Color.WHITE);
            bodyPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            root.add(bodyPanel, BorderLayout.CENTER);

            // Footer
            footerPanel = new JPanel(new BorderLayout());
            footerPanel.setBackground(new Color(248, 249, 250));
            footerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(233, 236, 239)),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
            ));
            root.add(footerPanel, BorderLayout.SOUTH);
        }

        public void setHeader(ComponentNode component) {
            headerPanel.removeAll();
            headerPanel.add(component.getComponent(), BorderLayout.CENTER);
            headerPanel.revalidate();
            headerPanel.repaint();
        }

        public void setBody(ComponentNode component) {
            bodyPanel.removeAll();
            bodyPanel.add(component.getComponent(), BorderLayout.CENTER);
            bodyPanel.revalidate();
            bodyPanel.repaint();
        }

        public void setFooter(ComponentNode component) {
            footerPanel.removeAll();
            footerPanel.add(component.getComponent(), BorderLayout.CENTER);
            footerPanel.revalidate();
            footerPanel.repaint();
        }

        public void addToHeader(ComponentNode component, String position) {
            headerPanel.add(component.getComponent(), position);
            headerPanel.revalidate();
            headerPanel.repaint();
        }

        public void addToBody(ComponentNode component, String position) {
            bodyPanel.add(component.getComponent(), position);
            bodyPanel.revalidate();
            bodyPanel.repaint();
        }

        public void addToFooter(ComponentNode component, String position) {
            footerPanel.add(component.getComponent(), position);
            footerPanel.revalidate();
            footerPanel.repaint();
        }
    }

    public static class ThemeManager {
        private static boolean isDarkMode = false;
        
        public static final Color LIGHT_BACKGROUND = Color.WHITE;
        public static final Color LIGHT_TEXT = new Color(44, 62, 80);
        public static final Color LIGHT_BORDER = new Color(233, 236, 239);
        public static final Color LIGHT_HEADER = new Color(41, 128, 185);
        public static final Color LIGHT_FOOTER = new Color(248, 249, 250);
        
        public static final Color DARK_BACKGROUND = new Color(33, 33, 33);
        public static final Color DARK_TEXT = new Color(236, 240, 241);
        public static final Color DARK_BORDER = new Color(66, 66, 66);
        public static final Color DARK_HEADER = new Color(26, 26, 26);
        public static final Color DARK_FOOTER = new Color(38, 38, 38);
        
        public static void toggleTheme() {
            isDarkMode = !isDarkMode;
        }
        
        public static boolean isDarkMode() {
            return isDarkMode;
        }
        
        public static Color getBackground() {
            return isDarkMode ? DARK_BACKGROUND : LIGHT_BACKGROUND;
        }
        
        public static Color getText() {
            return isDarkMode ? DARK_TEXT : LIGHT_TEXT;
        }
        
        public static Color getBorder() {
            return isDarkMode ? DARK_BORDER : LIGHT_BORDER;
        }
        
        public static Color getHeader() {
            return isDarkMode ? DARK_HEADER : LIGHT_HEADER;
        }
        
        public static Color getFooter() {
            return isDarkMode ? DARK_FOOTER : LIGHT_FOOTER;
        }
        
        public static Color getButton() {
            return isDarkMode ? new Color(52, 73, 94) : new Color(52, 152, 219);
        }
        
        public static Color getButtonHover() {
            return isDarkMode ? new Color(44, 62, 80) : new Color(41, 128, 185);
        }
        
        public static Color getSuccess() {
            return isDarkMode ? new Color(39, 174, 96) : new Color(46, 204, 113);
        }
    }
}