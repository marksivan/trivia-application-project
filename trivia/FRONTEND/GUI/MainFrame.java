package GUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MainFrame extends JFrame {
    private LoginPage loginPage;
    private RegisterPage registerPage;
    private HomePage homePage;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public MainFrame() {
        setTitle("Irenee & Mark's Trivia Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 768);
        setMinimumSize(new Dimension(200, 200));
        setLocationRelativeTo(null);
        setResizable(true);
        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);
        mainPanel.setPreferredSize(new Dimension(1024, 768));
        add(mainPanel);

        // Initialize login page
        loginPage = new LoginPage(success -> {
            if (success) {
                showHomePage();
            } else {
                showRegisterPage();
            }
        });

        // Initialize register page
        registerPage = new RegisterPage(success -> {
            if (success) {
                showHomePage();
            } else {
                showLoginPage();
            }
        });
        mainPanel.add(loginPage, "LOGIN");
        mainPanel.add(registerPage, "REGISTER");

        // Show login page initially
        showLoginPage();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                mainPanel.setPreferredSize(getContentPane().getSize());
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });
    }

    public void showLoginPage() {
        cardLayout.show(mainPanel, "LOGIN");
        loginPage.requestFocusInWindow();
        loginPage.revalidate();
        loginPage.repaint();
    }

    private void showRegisterPage() {
        cardLayout.show(mainPanel, "REGISTER");
        registerPage.requestFocusInWindow();
        registerPage.revalidate();
        registerPage.repaint();
    }

    private void showHomePage() {
        if (homePage == null) {
            homePage = new HomePage();
            mainPanel.add(homePage, "HOME");
        } else {
            homePage = new HomePage();
            mainPanel.remove(homePage);
            mainPanel.add(homePage, "HOME");
        }
        cardLayout.show(mainPanel, "HOME");
        homePage.requestFocusInWindow();
        homePage.revalidate();
        homePage.repaint();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }

    public static Object getInstance() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getInstance'");
    }
} 