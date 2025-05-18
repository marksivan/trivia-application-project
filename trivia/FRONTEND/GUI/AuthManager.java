/*
 * This class is used to manage the authentication of the user.
 * It is used to store the users and their passwords.
 * It is used to store the remembered user.
 * It is used to store the current user and their password.
 */
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

    //AuthManager is the constructor of the AuthManager class.
    private AuthManager() {
        users = new HashMap<>();
        loadUsers();
        loadRememberedUser();
    }

    //getInstance is a public method that returns the instance of the AuthManager class.
    public static AuthManager getInstance() {
        if (instance == null) {
            instance = new AuthManager();
        }
        return instance;
    }

    //loadUsers is a private method that loads the users from the data file.
    private void loadUsers() {
        //Create a new properties object.
        Properties props = new Properties();
        //Load the users from the data file.
        try (FileInputStream fis = new FileInputStream(DATA_FILE)) {
            props.load(fis);
            //Add the users to the users map.
            for (String key : props.stringPropertyNames()) {
                //Add the users to the users map if the key is not the remembered user.
                if (!key.equals(REMEMBERED_USER_KEY)) {
                    users.put(key, props.getProperty(key));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //saveUsers is a private method that saves the users to the data file.
    private void saveUsers() {
        //Create a new properties object.
        Properties props = new Properties();

        //Add the users to the properties object.
        for (Map.Entry<String, String> entry : users.entrySet()) {
            props.setProperty(entry.getKey(), entry.getValue());
        }   
        //Add the remembered user to the properties object.
        if (rememberedUser != null) {
            props.setProperty(REMEMBERED_USER_KEY, rememberedUser);
        }
        //Save the users to the data file.
        try (FileOutputStream fos = new FileOutputStream(DATA_FILE)) {
            props.store(fos, "User credentials");
        //Catch the exception if the users cannot be saved to the data file.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //loadRememberedUser is a private method that loads the remembered user from the data file.
    private void loadRememberedUser() {
        //Create a new properties object.
        Properties props = new Properties();
        //Load the remembered user from the data file.
        try (FileInputStream fis = new FileInputStream(DATA_FILE)) {
            props.load(fis);
            rememberedUser = props.getProperty(REMEMBERED_USER_KEY);
            //Catch the exception if the remembered user cannot be loaded from the data file.
        } catch (IOException e) {
            //Set the remembered user to null if the remembered user cannot be loaded from the data file.
            rememberedUser = null;
        }
    }

    //login is a public method that logs in the user.
    public boolean login(String username, String password) {
        //Check if the user exists and the password is correct.
        if (users.containsKey(username) && users.get(username).equals(password)) {
            //Set the current user.
            currentUser = username;
            //Return true if the user is logged in.
            return true;
        }
        return false;
    }

    //registerUser is a public method that registers a new user.
    public boolean registerUser(String username, String password) {
        //Check if the user already exists.
        if (users.containsKey(username)) {
            return false;
        }
        //Add the user to the users map.
        users.put(username, password);
        //Save the users to the data file.
        saveUsers();
        return true;
    }

    //logout is a public method that logs out the user.
    public void logout() {
        //Set the current user to null.
        currentUser = null;
        //Save the users to the data file.
        saveUsers();
    }

    //isLoggedIn is a public method that checks if the user is logged in.
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    //getCurrentUser is a public method that returns the current user.
    public String getCurrentUser() {
        return currentUser;
    }

    //getAvailableUsernames is a public method that returns the available usernames.
    public List<String> getAvailableUsernames() {
        return new ArrayList<>(users.keySet());
    }

    //setRememberedUser is a public method that sets the remembered user.
    public void setRememberedUser(String username) {
        //Set the remembered user.
        rememberedUser = username;
        //Save the users to the data file.
        saveUsers();
    }

    //getRememberedUser is a public method that returns the remembered user.
    public String getRememberedUser() {
        return rememberedUser;
    }

    //clearRememberedUser is a public method that clears the remembered user.
    public void clearRememberedUser() {
        //Set the remembered user to null.
        rememberedUser = null;
        //Save the users to the data file.
        saveUsers();
    }
} 