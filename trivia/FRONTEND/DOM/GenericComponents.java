/*
 * This class is used to create generic components used to represent visible components in the GUI.
 * It is used to create the components that are used in the GUI.
 * It is used to create the components that are used in the GUI.
 * Every component should be a subclass of ComponentNode.
 */

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

    //QuizButton is a class that is used to create a button component.
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
            //Create a new button component using the super class constructor.
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
            
            //Set the properties of the button component.
            JButton button = (JButton) getComponent();
            button.setFont(font);
            button.setBackground(backgroundColor);
            button.setForeground(textColor);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
            
            //Set the border of the button component if it has a shadow.
            if (hasShadow) {
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 0, 0, 119), 1),
                    button.getBorder()
                ));
            }
            
            //Set the mouse listener of the button component.
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

    //QuizTextField is a class that is used to create a text field component on the GUI.
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
            //Create a new text field component using the super class constructor.
            super(new JTextField(placeholder));
            JTextField field = (JTextField) getComponent();
            
            //Set the properties of the text field component.
            field.setFont(font);
            field.setBackground(backgroundColor);
            field.setForeground(textColor);
            field.setEditable(isEditable);

            //Set the document listener of the text field component.
            if (onTextChange != null) {

                //Add a document listener to the text field component.
                field.getDocument().addDocumentListener(new DocumentListener() {
                    public void changedUpdate(DocumentEvent e) { onTextChange.accept(field.getText()); }
                    public void removeUpdate(DocumentEvent e) { onTextChange.accept(field.getText()); }
                    public void insertUpdate(DocumentEvent e) { onTextChange.accept(field.getText()); }
                });
            }
        }
    }

    //QuizLabel is a class that is used to create a label component on the GUI.
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
            //Create a new label component using the super class constructor.
            super(new JLabel(text));
            JLabel label = (JLabel) getComponent();
            
            //Set the properties of the label component.
            label.setFont(font != null ? font : new Font("Times New Roman", Font.BOLD, 16));
            label.setForeground(textColor != null ? textColor : new Color(44, 62, 80));
            label.setBackground(backgroundColor);
            label.setOpaque(backgroundColor != null);
            
            //Set the border of the label component.
            label.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
            
            //Set the shadow of the label component if it has a shadow.
            if (hasShadow) {
                label.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 0, 0, 30), 0),
                    label.getBorder()
                ));
            }
            
            //Set the underline of the label component if it is underlined.
            if (isUnderlined) {
                Font originalFont = label.getFont();
                Map<TextAttribute, Object> attributes = new HashMap<>(originalFont.getAttributes());
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                label.setFont(originalFont.deriveFont(attributes));
            }
            
            //Set the horizontal alignment of the label component if it is centered.
            if (isCentered) {
                label.setHorizontalAlignment(SwingConstants.CENTER);
            }
        }
        
        //QuizLabel is a class that is used to create a label component on the GUI.
        public QuizLabel(String text) {
            this(text, null, null, null, 0, false, false, false);
        }
    }

    //QuizPanel is a class that is used to create a panel component on the GUI.
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
            //Create a new panel component using the super class constructor.
            super(new JPanel(layout));
            JPanel panel = (JPanel) getComponent();
            
            //Set the properties of the panel component.
            panel.setBackground(backgroundColor);
            panel.setOpaque(isOpaque);
            
            //Set the border of the panel component.
            if (borderRadius > 0) {
                panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(borderColor, borderRadius),
                    BorderFactory.createEmptyBorder(padding, padding, padding, padding)
                ));
            }
            
            //Set the shadow of the panel component if it has a shadow.
            if (hasShadow) {
                panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 0, 0, 50), 1),
                    panel.getBorder()
                ));
            }

            //Set the gradient of the panel component if it has a gradient.
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

    //QuizHeader is a class that is used to create a header component on the GUI.
    public static class QuizHeader extends ComponentNode {
        public QuizHeader(String title, String score) {
            //Create a new header component using the super class constructor.
            super(new JPanel(new BorderLayout()));
            JPanel header = (JPanel) getComponent();
            header.setBackground(new Color(41, 128, 185));
            header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

            //Set the properties of the title label.
            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 24));
            titleLabel.setForeground(Color.WHITE);
            header.add(titleLabel, BorderLayout.WEST);

            //Set the properties of the score label.
            JLabel scoreLabel = new JLabel(score);
            scoreLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
            scoreLabel.setForeground(Color.WHITE);
            header.add(scoreLabel, BorderLayout.EAST);
        }
    }

    //QuizComboBox is a class that is used to create a combo box component on the GUI.
    public static class QuizComboBox extends ComponentNode {
        public QuizComboBox(String[] items) {
            //Create a new combo box component using the super class constructor.
            super(new JComboBox<>(items));
            JComboBox<?> combo = (JComboBox<?>) getComponent();

            //Set the properties of the combo box component.
            combo.setFont(new Font("Times New Roman", Font.PLAIN, 14));
            combo.setBackground(Color.WHITE);
        }
    }

    //QuizCircularClock is a class that is used to create a circular clock component on the GUI.
    public static class QuizCircularClock extends JPanel {
        private int secondsRemaining;
        private Timer countdownTimer;
        private Consumer<Void> onTimeUp;

        //QuizCircularClock is the constructor of the circular clock component.
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

        //setDuration is a public method that sets the duration of the circular clock component.
        public void setDuration() {
            //Set the duration of the circular clock component based on the question time limit.
            secondsRemaining = Settings.getInstance().getQuestionTimeLimit()-1;
        }

        //start is a public method that starts the circular clock component.
        public void start() {
            countdownTimer.start();
        }

        //stop is a public method that stops the circular clock component.
        public void stop() {
            countdownTimer.stop();
        }

        //reset is a public method that resets the circular clock component.
        public void reset() {
            secondsRemaining = Settings.getInstance().getQuestionTimeLimit();
            countdownTimer.stop();
        }

        //setOnTimeUp is a public method that sets the callback function for the circular clock component.
        public void setOnTimeUp(Consumer<Void> callback) {
            this.onTimeUp = callback;
        }

        //paintComponent is a public method that paints the circular clock component.
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

            // Background use theme border color
            g2.setColor(ThemeManager.getBorder());
            g2.fillOval(x, y, diameter, diameter);

            // Time color, use theme colors
            if (secondsRemaining > Settings.getInstance().getQuestionTimeLimit() / 2) g2.setColor(ThemeManager.getSuccess());      // Green
            else if (secondsRemaining > Settings.getInstance().getQuestionTimeLimit() / 3) g2.setColor(ThemeManager.getButton());  // Theme button color
            else g2.setColor(Color.RED);  

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

        //setHeader is a public method that sets the header of the root container.
        public void setHeader(ComponentNode component) {
            headerPanel.removeAll();
            headerPanel.add(component.getComponent(), BorderLayout.CENTER);
            headerPanel.revalidate();
            headerPanel.repaint();
        }

        //setBody is a public method that sets the body of the root container.
        public void setBody(ComponentNode component) {
            bodyPanel.removeAll();
            bodyPanel.add(component.getComponent(), BorderLayout.CENTER);
            bodyPanel.revalidate();
            bodyPanel.repaint();
        }

        //setFooter is a public method that sets the footer of the root container.
        public void setFooter(ComponentNode component) {
            footerPanel.removeAll();
            footerPanel.add(component.getComponent(), BorderLayout.CENTER);
            footerPanel.revalidate();
            footerPanel.repaint();
        }

        //addToHeader is a public method that adds a component to the header of the root container.
        public void addToHeader(ComponentNode component, String position) {
            headerPanel.add(component.getComponent(), position);
            headerPanel.revalidate();
            headerPanel.repaint();
        }

        //addToBody is a public method that adds a component to the body of the root container.
        public void addToBody(ComponentNode component, String position) {
            bodyPanel.add(component.getComponent(), position);
            bodyPanel.revalidate();
            bodyPanel.repaint();
        }

        //addToFooter is a public method that adds a component to the footer of the root container.
        public void addToFooter(ComponentNode component, String position) {
            footerPanel.add(component.getComponent(), position);
            footerPanel.revalidate();
            footerPanel.repaint();
        }
    }

    //ThemeManager is a class that is used to manage the theme of the GUI.
    public static class ThemeManager {
        private static boolean isDarkMode = false;
        
        //LIGHT_BACKGROUND is a constant that is used to set the background color of the GUI in light mode.
        public static final Color LIGHT_BACKGROUND = new Color(255, 255, 255);
        public static final Color LIGHT_TEXT = new Color(44, 62, 80);
        public static final Color LIGHT_BORDER = new Color(233, 236, 239);
        public static final Color LIGHT_HEADER = new Color(41, 128, 185);
        public static final Color LIGHT_FOOTER = new Color(248, 249, 250);
        
        //DARK_BACKGROUND is a constant that is used to set the background color of the GUI in dark mode.
        public static final Color DARK_BACKGROUND = new Color(33, 33, 33);
        public static final Color DARK_TEXT = new Color(236, 240, 241);
        public static final Color DARK_BORDER = new Color(66, 66, 66);
        public static final Color DARK_HEADER = new Color(26, 26, 26);
        public static final Color DARK_FOOTER = new Color(38, 38, 38);
        
        //toggleTheme is a public method that toggles the theme of the GUI.
        public static void toggleTheme() {
            isDarkMode = !isDarkMode;
        }
        
        //isDarkMode is a public method that returns the theme of the GUI.
        public static boolean isDarkMode() {
            return isDarkMode;
        }
        
        //getBackground is a public method that returns the background color of the GUI.
        public static Color getBackground() {
            return isDarkMode ? DARK_BACKGROUND : LIGHT_BACKGROUND;
        }
        
        //getText is a public method that returns the text color of the GUI.
        public static Color getText() {
            return isDarkMode ? DARK_TEXT : LIGHT_TEXT;
        }
        
        //getBorder is a public method that returns the border color of the GUI.
        public static Color getBorder() {
            return isDarkMode ? DARK_BORDER : LIGHT_BORDER;
        }
        
        //getHeader is a public method that returns the header color of the GUI.
        public static Color getHeader() {
            return isDarkMode ? DARK_HEADER : LIGHT_HEADER;
        }
        
        //getFooter is a public method that returns the footer color of the GUI.
        public static Color getFooter() {
            return isDarkMode ? DARK_FOOTER : LIGHT_FOOTER;
        }
        
        //getButton is a public method that returns the button color of the GUI.
        public static Color getButton() {
            return isDarkMode ? new Color(52, 73, 94) : new Color(52, 152, 219);
        }
        
        //getButtonHover is a public method that returns the button hover color of the GUI.
        public static Color getButtonHover() {
            return isDarkMode ? new Color(44, 62, 80) : new Color(0xA5D6A7); // light green
        }
        
        //getSuccess is a public method that returns the success color of the GUI.
        public static Color getSuccess() {
            return isDarkMode ? new Color(39, 174, 96) : new Color(46, 204, 113);
        }
    }
}