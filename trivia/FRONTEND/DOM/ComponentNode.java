/*
 * This class is used to create a component node. The component node is a node in the DOM tree of the GUI.
 * It contains the component, the id, the class name, the children, the properties, the styles, and the behaviors.
 * The component is the actual component that is being created in the GUI.
 * The id is the id of the component.
 * The className is the class name of the component.
 * The children are the children component nodes that the component holds.
 * The properties are the properties of the component.
 * The styles are the styles of the component.
 * The behaviors are the behaviors of the component.
 */

package DOM;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.util.ArrayList;
import java.util.HashMap;

//ComponentNode is the class that is used to create a component node.
public class ComponentNode {
    private Component component;
    private String id;
    private String className;
    private ArrayList<ComponentNode> children;
    private HashMap<String, String> properties;
    private HashMap<String, Object> styles;
    private ArrayList<ActionListener> behaviors;

    //ComponentNode is the constructor that is used to create a component node.
    public ComponentNode(Component component) {
        this.component = component;
        this.id = generateId();
        this.className = component.getClass().getSimpleName();
        this.children = new ArrayList<>();
        this.properties = new HashMap<>();
        this.styles = new HashMap<>();
        this.behaviors = new ArrayList<>();
    }

    //generateId is a private method that generates a string representation of a unique id for the component.
    private String generateId() {
        return component.getClass().getSimpleName().toLowerCase() + "_" + System.identityHashCode(component);
    }

    //setSize is a public method that sets the size of the component in the GUI.
    public void setSize(int width, int height) {
        component.setPreferredSize(new Dimension(width, height));
        styles.put("width", width);
        styles.put("height", height);
        component.revalidate();
    }

    //setColor is a public method that sets the color of the component in the GUI.
    public void setColor(Color color) {
        if (component instanceof JComponent) {
            ((JComponent) component).setBackground(color);
            styles.put("background-color", color);
        }
    }

    //setForegroundColor is a public method that sets the foreground color of the component in the GUI.
    public void setForegroundColor(Color color) {
        component.setForeground(color);
        styles.put("color", color);
    }

    public void setFont(Font font) {
        component.setFont(font);
        styles.put("font", font);
    }

    //setBorder is a public method that sets the border of the component in the GUI.
    public void setBorder(Border border) {
        if (component instanceof JComponent) {
            ((JComponent) component).setBorder(border);
            styles.put("border", border);
        }
    }

    //addHoverEffect is a public method that adds a hover effect to the component in the GUI.
    public void addHoverEffect(Color hoverColor) {
        if (component instanceof JComponent) {
            JComponent jc = (JComponent) component;
            Color originalColor = jc.getBackground();
            
            jc.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    jc.setBackground(hoverColor);
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    jc.setBackground(originalColor);
                }
            });
            styles.put("hover-color", hoverColor);
        }
    }

    //addClickEffect is a public method that adds a click effect to the component in the GUI.
    public void addClickEffect() {
        if (component instanceof JComponent) {
            JComponent jc = (JComponent) component;
            jc.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    jc.setLocation(jc.getX() + 2, jc.getY() + 2);
                }
                
                @Override
                public void mouseReleased(MouseEvent e) {
                    jc.setLocation(jc.getX() - 2, jc.getY() - 2);
                }
            });
            styles.put("click-effect", true);
        }
    }

    //addBehavior is a public method that adds a behavior to the component.
    public void addBehavior(ActionListener listener) {
        if (component instanceof AbstractButton) {
            ((AbstractButton) component).addActionListener(listener);
            behaviors.add(listener);
        }
    }

    //addMouseBehavior is a public method that adds a mouse behavior to the component.
    public void addMouseBehavior(MouseListener listener) {
        component.addMouseListener(listener);
        behaviors.add(e -> {});
    }

    //addChild is a public method that adds a child component node to the component.
    public void addChild(ComponentNode child) {
        children.add(child);
    }

    //addProperty is a public method that adds a property to the component.
    public void addProperty(String key, String value) {
        properties.put(key, value);
    }

    //getChildren is a public method that returns the children of the component.
    public ArrayList<ComponentNode> getChildren() {
        return children;
    }

    //getProperties is a public method that returns the properties of the component.
    public HashMap<String, String> getProperties() {
        return properties;
    }

    //getComponent is a public method that returns the component.
    public Component getComponent() {
        return component;
    }

    //getId is a public method that returns the id of the component.
    public String getId() {
        return id;
    }

    //getClassName is a public method that returns the class name of the component.
    public String getClassName() {
        return className;
    }

    //setId is a public method that sets the id of the component.
    public void setId(String id) {
        this.id = id;
    }

    //setClassName is a public method that sets the class name of the component.
    public void setClassName(String className) {
        this.className = className;
    }

    //findById is a public method that finds a child component node by id.
    public ComponentNode findById(String id) {
        if (this.id.equals(id)) {
            return this;
        }
        for (ComponentNode child : children) {
            ComponentNode found = child.findById(id);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    //findByClassName is a public method that finds a child component node by class name.
    public ArrayList<ComponentNode> findByClassName(String className) {
        ArrayList<ComponentNode> results = new ArrayList<>();
        if (this.className.equals(className)) {
            results.add(this);
        }
        for (ComponentNode child : children) {
            results.addAll(child.findByClassName(className));
        }
        return results;
    }

    //removeChildComponent is a public method that removes a child component node by id.
        public void removeChildComponent(String id) {
        children.removeIf(child -> child.getId().equals(id));
    }

    //getStyles is a public method that returns the styles of the component.
    public HashMap<String, Object> getStyles() {
        return styles;
    }

    //getBehaviors is a public method that returns the behaviors of the component.
    public ArrayList<ActionListener> getBehaviors() {
        return behaviors;
    }

    //addKeyListener is a public method that adds a key listener to the component.
    public void addKeyListener(KeyListener listener) {
        component.addKeyListener(listener);
        behaviors.add(e -> {});
    }
} 