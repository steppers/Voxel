package engine.ext;

import engine.components.Component;
import engine.components.Transform;
import engine.math.Vector3f;
import java.util.ArrayList;

public class GameObject {

    private boolean active = true;
    private boolean Static;
    private String tag = "";
    private ArrayList<Component> components = new ArrayList<>();
    private ArrayList<GameObject> children = new ArrayList<>();
    private GameObject parent;

    public GameObject() {
        this(Vector3f.ZERO_VECTOR, Vector3f.ZERO_VECTOR, Vector3f.ONE_VECTOR, "");
    }

    public GameObject(Vector3f pos, Vector3f rot, Vector3f scale, String tag) {
        components.add(new Transform(this, pos, rot, scale));
        this.tag = tag;
    }

    public void input() {
        for (Component c : components) {
            if (c.isActive() && this.isActive()) {
                c.input();
            }
        }
        for (GameObject g : children) {
            if (g.isActive()) {
                g.input();
            }
        }
    }

    public void update() {
        for (Component c : components) {
            if (c.isActive() && this.isActive()) {
                c.update();
            }
        }
        for (GameObject g : children) {
            if (g.isActive()) {
                g.update();
            }
        }
    }

    public void render() {
        for (Component c : components) {
            if ("MeshRenderer".equals(c.getType()) && c.isActive() && this.isActive()) {
                c.render();
                return;
            }
        }
        for (GameObject g : children) {
            if (g.isActive()) {
                g.render();
            }
        }
    }

    public void AddComponent(Component component) {
        for (Component c : components) {
            if (c.getType().equals(component.getType())) {
                return;
            }
        }
        components.add(component);
    }

    public Transform getTransform() {
        for (Component c : components) {
            if (c.getType().equals("Transform")) {
                return (Transform) c;
            }
        }
        return null;
    }

    public GameObject getChildWithTag(String tag) {
        for (GameObject g : children) {
            if (g.getTag().equals(tag)) {
                return g;
            }
        }
        return null;
    }
    
    public ArrayList<GameObject> getChildrenWithTag(String tag) {
        ArrayList<GameObject> gos = new ArrayList<>();
        for (GameObject g : children) {
            
        }
        return gos;
    }

    public GameObject getParent() {
        if(parent != null)
            return parent;
        else
            return null;
    }

    public void addChild(GameObject gameObject) {
        gameObject.setParent(this);
        children.add(gameObject);
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setActiveRecursively(boolean val) {
        for (GameObject g : children) {
            g.setActiveRecursively(val);
        }
    }

    public boolean isStatic() {
        return Static;
    }

    public void setStatic(boolean Static) {
        this.Static = Static;
    }
    
    public <T extends Component> T getComponent(Class<T> type){
        for (Component c : components) {
            if (c.getClass() == type) {
                return type.cast(c);
            }
        }
        return null;
    }

    public void setParent(GameObject parent) {
        this.parent = parent;
    }
    
    public void destroy(){
        for (Component c : components) {
            c.destroy();
        }
    }
}
