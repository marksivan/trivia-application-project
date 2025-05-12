package trivia.FRONTEND.DOM;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.util.ArrayList;
import java.util.HashMap;

public class ComponentNode {
    private Component component;
    private String id;
    private String className;
    private ArrayList<ComponentNode> children;
    private HashMap<String, String> properties;
    private HashMap<String, Object> styles;
    private ArrayList<ActionListener> behaviors;

    public ComponentNode(Component component) {
        this.component = component;
        this.id = generateId();
        this.className = component.getClass().getSimpleName();
        this.children = new ArrayList<>();
        this.properties = new HashMap<>();
        this.styles = new HashMap<>();
        this.behaviors = new ArrayList<>();
    }

    private String generateId() {
        return component.getClass().getSimpleName().toLowerCase() + "_" + System.identityHashCode(component);
    }

    public void setSize(int width, int height) {
        component.setPreferredSize(new Dimension(width, height));
        styles.put("width", width);
        styles.put("height", height);
        component.revalidate();
    }

    public void setColor(Color color) {
        if (component instanceof JComponent) {
            ((JComponent) component).setBackground(color);
            styles.put("background-color", color);
        }
    }

    public void setForegroundColor(Color color) {
        component.setForeground(color);
        styles.put("color", color);
    }

    public void setFont(Font font) {
        component.setFont(font);
        styles.put("font", font);
    }

    public void setBorder(Border border) {
        if (component instanceof JComponent) {
            ((JComponent) component).setBorder(border);
            styles.put("border", border);
        }
    }

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

    public void addBehavior(ActionListener listener) {
        if (component instanceof AbstractButton) {
            ((AbstractButton) component).addActionListener(listener);
            behaviors.add(listener);
        }
    }

    public void addMouseBehavior(MouseListener listener) {
        component.addMouseListener(listener);
        behaviors.add(e -> {});
    }

    public void addChild(ComponentNode child) {
        children.add(child);
    }

    public void addProperty(String key, String value) {
        properties.put(key, value);
    }

    public ArrayList<ComponentNode> getChildren() {
        return children;
    }

    public HashMap<String, String> getProperties() {
        return properties;
    }

    public Component getComponent() {
        return component;
    }

    public String getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setClassName(String className) {
        this.className = className;
    }

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

    public void removeChildComponent(String id) {
        children.removeIf(child -> child.getId().equals(id));
    }

    public HashMap<String, Object> getStyles() {
        return styles;
    }

    public ArrayList<ActionListener> getBehaviors() {
        return behaviors;
    }
} 