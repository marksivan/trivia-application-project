package GUI;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.io.*;
import java.util.Properties;

public class AuthManager {
    private static AuthManager instance;
    private Map<String, String> users;
    private String currentUser;
    private String rememberedUser;
    private static final String DATA_FILE = "users.properties";
    private static final String REMEMBERED_USER_KEY = "remembered_user";

    private AuthManager() {
        users = new HashMap<>();
        loadUsers();
        loadRememberedUser();
    }

    public static AuthManager getInstance() {
        if (instance == null) {
            instance = new AuthManager();
        }
        return instance;
    }

    private void loadUsers() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(DATA_FILE)) {
            props.load(fis);
            for (String key : props.stringPropertyNames()) {
                if (!key.equals(REMEMBERED_USER_KEY)) {
                    users.put(key, props.getProperty(key));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveUsers() {
        Properties props = new Properties();
        for (Map.Entry<String, String> entry : users.entrySet()) {
            props.setProperty(entry.getKey(), entry.getValue());
        }
        if (rememberedUser != null) {
            props.setProperty(REMEMBERED_USER_KEY, rememberedUser);
        }
        try (FileOutputStream fos = new FileOutputStream(DATA_FILE)) {
            props.store(fos, "User credentials");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadRememberedUser() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(DATA_FILE)) {
            props.load(fis);
            rememberedUser = props.getProperty(REMEMBERED_USER_KEY);
        } catch (IOException e) {
            rememberedUser = null;
        }
    }

    public boolean login(String username, String password) {
        if (users.containsKey(username) && users.get(username).equals(password)) {
            currentUser = username;
            return true;
        }
        return false;
    }

    public boolean registerUser(String username, String password) {
        if (users.containsKey(username)) {
            return false;
        }
        users.put(username, password);
        saveUsers();
        return true;
    }

    public void logout() {
        currentUser = null;
        saveUsers();
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public List<String> getAvailableUsernames() {
        return new ArrayList<>(users.keySet());
    }

    public void setRememberedUser(String username) {
        rememberedUser = username;
        saveUsers();
    }

    public String getRememberedUser() {
        return rememberedUser;
    }

    public void clearRememberedUser() {
        rememberedUser = null;
        saveUsers();
    }
} 