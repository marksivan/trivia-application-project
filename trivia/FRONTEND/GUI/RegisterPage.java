/*
 * This class represent the register page on the landing Jframe.
 * It contains the register form and the back to login button.
 * It also encapsulates the logic to handle the different events,states, and actions on the GUI when the user clicks the register button.
 */
package GUI;
import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;
import DOM.GenericComponents;

public class RegisterPage extends JPanel {
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField usernameField;
    private JLabel errorLabel;
    private Consumer<Boolean> onRegisterComplete;

    public RegisterPage(Consumer<Boolean> onRegisterComplete) {
        this.onRegisterComplete = onRegisterComplete;
        setLayout(new BorderLayout());
        setBackground(GenericComponents.ThemeManager.getBackground());
        initializeComponents();
    }

    // This method initializes the components of the register page.
    private void initializeComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(GenericComponents.ThemeManager.getBackground());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        GenericComponents.QuizLabel titleLabel = new GenericComponents.QuizLabel(
            "Create New Account",
            new Font("Times New Roman", Font.BOLD, 24),
            GenericComponents.ThemeManager.getText(),
            null, 20, true, false, true
        );
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel.getComponent(), gbc);

        // Username field
        GenericComponents.QuizLabel usernameLabel = new GenericComponents.QuizLabel(
            "Username:",
            new Font("Times New Roman", Font.PLAIN, 14),
            GenericComponents.ThemeManager.getText(),
            null, 5, false, false, false
        );
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(usernameLabel.getComponent(), gbc);

        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Helvetica", Font.PLAIN, 14));
        gbc.gridx = 1;
        mainPanel.add(usernameField, gbc);

        // Password field
        GenericComponents.QuizLabel passwordLabel = new GenericComponents.QuizLabel(
            "Password:",
            new Font("Times New Roman", Font.PLAIN, 14),
            GenericComponents.ThemeManager.getText(),
            null, 5, false, false, false
        );
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(passwordLabel.getComponent(), gbc);

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        gbc.gridx = 1;
        mainPanel.add(passwordField, gbc);

        // Confirm password field
        GenericComponents.QuizLabel confirmPasswordLabel = new GenericComponents.QuizLabel(
            "Confirm Password:",
            new Font("Times New Roman", Font.PLAIN, 14),
            GenericComponents.ThemeManager.getText(),
            null, 5, false, false, false
        );
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(confirmPasswordLabel.getComponent(), gbc);

        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        gbc.gridx = 1;
        mainPanel.add(confirmPasswordField, gbc);

        // Password requirements label
        GenericComponents.QuizLabel requirementsLabel = new GenericComponents.QuizLabel(
            "<html>Password must contain:<br>" +
            "• At least 8 characters<br>" +
            "• At least one uppercase letter<br>" +
            "• At least one lowercase letter<br>" +
            "• At least one number<br>" +
            "• At least one special character</html>",
            new Font("Times New Roman", Font.PLAIN, 12),
            GenericComponents.ThemeManager.getText(),
            null, 5, false, false, false
        );
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        mainPanel.add(requirementsLabel.getComponent(), gbc);

        // Error label
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        gbc.gridy = 5;
        mainPanel.add(errorLabel, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        GenericComponents.QuizButton registerButton = new GenericComponents.QuizButton(
            "Register",
            GenericComponents.ThemeManager.getButton(),
            GenericComponents.ThemeManager.getButtonHover(),
            Color.WHITE,
            new Font("Times New Roman", Font.BOLD, 14),
            8, 20, true, true,
            e -> handleRegister()
        );

        GenericComponents.QuizButton backButton = new GenericComponents.QuizButton(
            "Back to Login",
            GenericComponents.ThemeManager.getButton(),
            GenericComponents.ThemeManager.getButtonHover(),
            Color.WHITE,
            new Font("Times New Roman", Font.BOLD, 14),
            8, 20, true, true,
            e -> onRegisterComplete.accept(false)
        );

        buttonPanel.add(registerButton.getComponent());
        buttonPanel.add(backButton.getComponent());

        gbc.gridy = 6;
        mainPanel.add(buttonPanel, gbc);
        add(mainPanel, BorderLayout.CENTER);
    }

    // This method handles the register button of the register page.
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        errorLabel.setText("");

        // Validate username
        if (username.isEmpty()) {
            errorLabel.setText("Username cannot be empty");
            usernameField.requestFocusInWindow();
            return;
        }

        // Check if username already exists
        if (AuthManager.getInstance().getAvailableUsernames().contains(username)) {
            errorLabel.setText("Username already exists");
            usernameField.requestFocusInWindow();
            return;
        }

        // Validate password
        if (!isValidPassword(password)) {
            errorLabel.setText("Password does not meet requirements");
            passwordField.requestFocusInWindow();
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Passwords do not match");
            confirmPasswordField.requestFocusInWindow();
            return;
        }

        // Show loading state
        errorLabel.setText("Creating account.........................");
        errorLabel.setForeground(GenericComponents.ThemeManager.getText());

        Timer timer = new Timer(500, e -> {
            // Try to register the user
            if (AuthManager.getInstance().registerUser(username, password)) {
                // Autologin after successful registration
                if (AuthManager.getInstance().login(username, password)) {
                    errorLabel.setText("");
                    onRegisterComplete.accept(true);
                } else {
                    errorLabel.setForeground(Color.RED);
                    errorLabel.setText("Registration successful but login failed");
                }
            } else {
                errorLabel.setForeground(Color.RED);
                errorLabel.setText("Registration failed");
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    // This method checks if the password is valid.
    private boolean isValidPassword(String password) {
        if (password.length() < 8) return false;
        
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasNumber = true;
            else if (!Character.isWhitespace(c)) hasSpecial = true;
        }
        
        return hasUpper && hasLower && hasNumber && hasSpecial;
    }
} 