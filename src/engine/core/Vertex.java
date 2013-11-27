package engine.core;

import engine.math.Vector2f;
import engine.math.Vector3f;

public class Vertex {

    public static final int SIZE = 11;
    private Vector3f pos;
    private Vector2f texCoord;
    private Vector3f normal;
    private Vector3f color;

    public Vertex(Vector3f pos) {
        this(pos, new Vector2f(0, 0));
    }

    public Vertex(Vector3f pos, Vector2f texCoord) {
        this(pos, texCoord, new Vector3f(0, 0, 0));
    }
    
    public Vertex(Vector3f pos, Vector2f texCoord, Vector3f color) {
        this(pos, texCoord, Color.grey, new Vector3f(0, 0, 0));
    }

    public Vertex(Vector3f pos, Vector2f texCoord, Vector3f color, Vector3f normal) {
        this.pos = pos;
        this.texCoord = texCoord;
        this.normal = normal;
        this.color = color;
    }

    public Vector3f getPos() {
        return pos;
    }

    public void setPos(Vector3f pos) {
        this.pos = pos;
    }

    public Vector2f getTexCoord() {
        return texCoord;
    }

    public Vertex setTexCoord(Vector2f texCoord) {
        this.texCoord = texCoord;
        return this;
    }

    public Vector3f getNormal() {
        return normal;
    }

    public void setNormal(Vector3f normal) {
        this.normal = normal;
    }

    public Vector3f getColor() {
        return color;
    }

    public Vertex setColor(Vector3f color) {
        this.color = color;
        return this;
    }
}
