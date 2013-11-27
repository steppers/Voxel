package Scripts;

import engine.components.Component;
import engine.core.Input;
import engine.core.Time;
import engine.core.Window;
import engine.ext.GameObject;
import engine.math.Vector2f;
import engine.math.Vector3f;

public class FreeFlyCamera extends Component
{
    private static final float MOUSE_SENSITIVITY = 0.18f;
    private static final float MOVE_SPEED = 14f;
    
    private boolean mouseLocked = false;
    private Vector2f centerPosition = new Vector2f(Window.getWidth() / 2, Window.getHeight() / 2);
    private Vector3f movementVector;
    
    public FreeFlyCamera(GameObject gameObject)
    {
        super(gameObject, "FreeFlyCamera");
    }
    
    @Override
    public void input(){
        if (Input.getKey(Input.KEY_ESCAPE)) {
            Input.setCursor(true);
            mouseLocked = false;
        }
        if (Input.getMouseDown(0)) {
            Input.setMousePosition(centerPosition);
            Input.setCursor(false);
            mouseLocked = true;
        }

        movementVector = Vector3f.ZERO_VECTOR;

        if (Input.getKey(Input.KEY_W)) {
            movementVector = movementVector.add(gameObject.getTransform().forward);
        }
        if (Input.getKey(Input.KEY_S)) {
            movementVector = movementVector.sub(gameObject.getTransform().forward);
        }
        if (Input.getKey(Input.KEY_A)) {
            movementVector = movementVector.sub(gameObject.getTransform().left);
        }
        if (Input.getKey(Input.KEY_D)) {
            movementVector = movementVector.add(gameObject.getTransform().left);
        }

        if (mouseLocked) {
            Vector2f deltaPos = Input.getMousePosition().sub(centerPosition);

            boolean rotY = deltaPos.getX() != 0;
            boolean rotX = deltaPos.getY() != 0;

            if (rotY) {
                gameObject.getTransform().setRotation(gameObject.getTransform().getRotation().getX(),
                                                      gameObject.getTransform().getRotation().getY() - deltaPos.getX() * MOUSE_SENSITIVITY,
                                                      gameObject.getTransform().getRotation().getZ());
            }
            if (rotX) {
                gameObject.getTransform().setRotation(gameObject.getTransform().getRotation().getX() + deltaPos.getY() * MOUSE_SENSITIVITY,
                                                      gameObject.getTransform().getRotation().getY(),
                                                      gameObject.getTransform().getRotation().getZ());
            }
            if (rotY || rotX) {
                Input.setMousePosition(centerPosition);
            }
        }
    }
    
    @Override
    public void update(){
        float movAmt = (float) (MOVE_SPEED * Time.getDelta());
        
        if(Input.getKey(Input.KEY_LSHIFT)){
            movAmt = (float) (MOVE_SPEED * Time.getDelta()*2);
        }
        
        if (movementVector.length() > 0) {
            movementVector = movementVector.normalized();
        }
        gameObject.getTransform().setPos(gameObject.getTransform().getPos().add(movementVector.mul(movAmt)));
    }
    
}
