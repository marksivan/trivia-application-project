/*
 * This class represent the login page on the landing Jframe.
 * It contains the login form and the register button.
 * It also encapsulates the logic to handle the different events,states, and actions on the GUI when the user clicks the login button.
 */
package GUI;
import javax.swing.*;
import java.awt.*;
import DOM.GenericComponents;
import java.util.function.Consumer;

public class LoginPage extends JPanel {
    private GenericComponents.QuizTextField usernameField;
    private GenericComponents.QuizTextField passwordField;
    private Consumer<Boolean> onLoginSuccess;
    private JLabel errorLabel;
    private JCheckBox rememberMeCheckbox;

    public LoginPage(Consumer<Boolean> onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
        setLayout(new BorderLayout(20, 20));
        setBackground(GenericComponents.ThemeManager.getBackground());
        
        // Create main card panel
        JPanel cardPanel = new JPanel(new BorderLayout(20, 20));
        cardPanel.setBackground(GenericComponents.ThemeManager.getBackground());
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GenericComponents.ThemeManager.getBorder(), 1),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(new GenericComponents.QuizLabel(
            "Welcome to Mark & Irenee Trivia Game",
            new Font("Times New Roman", Font.BOLD, 24),
            GenericComponents.ThemeManager.getText(),
            null, 10, false, false, true
        ).getComponent(), BorderLayout.CENTER);
        cardPanel.add(titlePanel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Username field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new GenericComponents.QuizLabel(
            "Username:",
            new Font("Times New Roman", Font.BOLD, 14),
            GenericComponents.ThemeManager.getText(),
            null, 5, false, false, false
        ).getComponent(), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        usernameField = new GenericComponents.QuizTextField(
            "",
            GenericComponents.ThemeManager.getBorder(),
            GenericComponents.ThemeManager.getBackground(),
            GenericComponents.ThemeManager.getText(),
            new Font("Times New Roman", Font.PLAIN, 14),
            8, 10, true, true, null
        );
        formPanel.add(usernameField.getComponent(), gbc);
        
        // Password field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new GenericComponents.QuizLabel(
            "Password:",
            new Font("Times New Roman", Font.BOLD, 14),
            GenericComponents.ThemeManager.getText(),
            null, 5, false, false, false
        ).getComponent(), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        passwordField = new GenericComponents.QuizTextField(
            "",
            GenericComponents.ThemeManager.getBorder(),
            GenericComponents.ThemeManager.getBackground(),
            GenericComponents.ThemeManager.getText(),
            new Font("Times New Roman", Font.PLAIN, 14),
            8, 10, true, true, null
        );
        formPanel.add(passwordField.getComponent(), gbc);

        // Remember Me checkbox
        rememberMeCheckbox = new JCheckBox("Remember Me");
        rememberMeCheckbox.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        rememberMeCheckbox.setForeground(GenericComponents.ThemeManager.getText());
        rememberMeCheckbox.setBackground(GenericComponents.ThemeManager.getBackground());
        rememberMeCheckbox.setOpaque(false);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(rememberMeCheckbox, gbc);

        // Error label
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(errorLabel, gbc);
        cardPanel.add(formPanel, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.insets = new Insets(10, 10, 10, 10);
        buttonGbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Register button
        buttonGbc.gridx = 0;
        buttonGbc.gridy = 0;
        buttonGbc.weightx = 1.0;
        GenericComponents.QuizButton registerButton = new GenericComponents.QuizButton(
            "Register",
            GenericComponents.ThemeManager.getButton(),
            GenericComponents.ThemeManager.getButtonHover(),
            Color.WHITE,
            new Font("Times New Roman", Font.BOLD, 14),
            8, 20, true, true,
            e -> onLoginSuccess.accept(false)
        );
        buttonPanel.add(registerButton.getComponent(), buttonGbc);

        // Login button
        buttonGbc.gridx = 1;
        buttonGbc.gridy = 0;
        buttonGbc.weightx = 1.0;
        GenericComponents.QuizButton loginButton = new GenericComponents.QuizButton(
            "Login",
            GenericComponents.ThemeManager.getButton(),
            GenericComponents.ThemeManager.getButtonHover(),
            Color.WHITE,
            new Font("Times New Roman", Font.BOLD, 14),
            8, 20, true, true,
            e -> handleLogin()
        );
        buttonPanel.add(loginButton.getComponent(), buttonGbc);
        
        // Add button panel to card with padding
        JPanel buttonContainer = new JPanel(new BorderLayout());
        buttonContainer.setOpaque(false);
        buttonContainer.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        buttonContainer.add(buttonPanel, BorderLayout.CENTER);
        cardPanel.add(buttonContainer, BorderLayout.SOUTH);
        
        // Center the card in the main panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(cardPanel);
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private void handleLogin() {
        String username = ((JTextField)usernameField.getComponent()).getText();
        String password = ((JTextField)passwordField.getComponent()).getText();
        boolean rememberMe = rememberMeCheckbox.isSelected();
        
        // Clear previous error
        errorLabel.setText("");
        
        if (username == null || username.trim().isEmpty() || username.equals("")) {
            errorLabel.setText("Please enter a username");
            usernameField.getComponent().requestFocusInWindow();
            return;
        }
        
        if (password == null || password.trim().isEmpty() || password.equals("")) {
            errorLabel.setText("Please enter a password");
            passwordField.getComponent().requestFocusInWindow();
            return;
        }
        
        // Show loading state
        errorLabel.setText("Logging in.........................");
        errorLabel.setForeground(GenericComponents.ThemeManager.getText());
        
        // Simulate network delay for better user experience
        Timer timer = new Timer(500, e -> {
            if (AuthManager.getInstance().login(username, password)) {
                if (rememberMe) {
                    AuthManager.getInstance().setRememberedUser(username);
                } else {
                    AuthManager.getInstance().clearRememberedUser();
                    // Clear password field if remember me is not checked
                    ((JTextField)passwordField.getComponent()).setText("");
                    ((JTextField)usernameField.getComponent()).setText("");
                }
                errorLabel.setText("");
                onLoginSuccess.accept(true);
            } else {
                errorLabel.setForeground(Color.RED);
                errorLabel.setText("Invalid credentials! Click 'Register' to create an account.");
                passwordField.getComponent().requestFocusInWindow();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
}