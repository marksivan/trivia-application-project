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
import GUI.Settings;



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
                    BorderFactory.createLineBorder(new Color(0, 0, 0, 119), 1),
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
            
            
            label.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
            
        
            if (hasShadow) {
                label.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 0, 0, 30), 1),
                    label.getBorder()
                ));
            }
            

            if (isUnderlined) {
                Font originalFont = label.getFont();
                Map<TextAttribute, Object> attributes = new HashMap<>(originalFont.getAttributes());
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                label.setFont(originalFont.deriveFont(attributes));
            }
            
            if (isCentered) {
                label.setHorizontalAlignment(SwingConstants.CENTER);
            }
        }
        
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
            
            panel.setBackground(backgroundColor);
            panel.setOpaque(isOpaque);
            

            if (borderRadius > 0) {
                panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(borderColor, borderRadius),
                    BorderFactory.createEmptyBorder(padding, padding, padding, padding)
                ));
            }
            

            if (hasShadow) {
                panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 0, 0, 50), 1),
                    panel.getBorder()
                ));
            }

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


    public static class QuizComboBox extends ComponentNode {
        public QuizComboBox(String[] items) {
            super(new JComboBox<>(items));
            JComboBox<?> combo = (JComboBox<?>) getComponent();
            combo.setFont(new Font("Helvetica", Font.PLAIN, 14));
            combo.setBackground(Color.WHITE);
        }
    }



    public static class QuizCircularClock extends JPanel {
        private int secondsRemaining;
        private Timer countdownTimer;
        private Consumer<Void> onTimeUp;

        public QuizCircularClock() {
            secondsRemaining = Settings.getInstance().getQuestionTimeLimit();
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
            secondsRemaining = Settings.getInstance().getQuestionTimeLimit();
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
            if (secondsRemaining > Settings.getInstance().getQuestionTimeLimit() / 2) g2.setColor(ThemeManager.getSuccess());      // Green
            else if (secondsRemaining > Settings.getInstance().getQuestionTimeLimit() / 4) g2.setColor(ThemeManager.getButton());  // Theme button color
            else g2.setColor(ThemeManager.getButtonHover());                        // Theme hover color

            float angle = 360f * secondsRemaining / Settings.getInstance().getQuestionTimeLimit();
            g2.fillArc(x, y, diameter, diameter, 90, -(int) angle);

            // Inner circle - use theme background
            int ringThickness = 14;
            g2.setColor(ThemeManager.getBackground());
            g2.fillOval(x + ringThickness / 2, y + ringThickness / 2,
                    diameter - ringThickness, diameter - ringThickness);

            // Text - use theme text color
            g2.setColor(ThemeManager.getText());
            g2.setFont(new Font("Times New Roman", Font.BOLD, 14));
            String timeStr = secondsRemaining + "s";
            FontMetrics fm = g2.getFontMetrics();
            int strWidth = fm.stringWidth(timeStr);
            int strHeight = fm.getAscent();
            g2.drawString(timeStr, x + diameter / 2 - strWidth / 2, y + diameter / 2 + strHeight / 4);
        }

        // Root Container
        private JPanel headerPanel;
        private JPanel bodyPanel;
        private JPanel footerPanel;


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
        
        public static final Color LIGHT_BACKGROUND = new Color(255, 255, 255);
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