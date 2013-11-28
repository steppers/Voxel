package VOXEL.core;

import Scripts.FPSCamera;
import engine.components.Component;
import engine.core.Input;
import engine.core.Time;
import engine.ext.GameObject;
import engine.math.Vector3f;

public class Player extends Component {

    public static final float width = 0.35f;
    public static final float height = 1.8f;
    public float gravity = 25f;
    public float YVelocity = 0;
    public float TerminalVel = 22f;
    public int pickingRange = 5;
    public voxel selected;
    public int placeType = 1;
    public boolean clicked = false;
    public long clickTime = 0;
    public boolean grounded = false;
    public boolean jumped = false;
    private Vector3f torso = Vector3f.ZERO_VECTOR;
    private Vector3f knees = Vector3f.ZERO_VECTOR;

    public Player(GameObject gameObject) {
        super(gameObject, "Player");
        knees.setY(height - 0.2f);
        torso.setY(height / 4);
    }

    @Override
    public void input() {
        gameObject.getComponent(FPSCamera.class).input();

        if (Input.getKey(Input.KEY_SPACE) && grounded) {
            YVelocity = 7.5f;
            grounded = false;
            jumped = true;
        }
    }

    @Override
    public void update() {
        if (Time.getTime() - clickTime > 300000000) {
            clicked = false;
        }

        updateMovement();
        updateBlockSelection();
    }

    public void updateBlockSelection() {
        Vector3f forward = gameObject.getTransform().forward;
        VoxelChunkPair filled = getClosestFilledVoxel(forward);
        if (Input.getMouse(0) && filled != null && !clicked) {
            filled.v.setType(0);
            filled.v.setLight(16);
            filled.c.updateMesh();

            if (filled.posV.getX() == 0) {
                if (filled.c.getAdjactentChunk(-1, 0, 0) != null) {
                    filled.c.getAdjactentChunk(-1, 0, 0).updateMesh();
                }
            }
            if (filled.posV.getX() == VoxelGame.CHUNK_SIZE_XZ - 1) {
                if (filled.c.getAdjactentChunk(1, 0, 0) != null) {
                    filled.c.getAdjactentChunk(1, 0, 0).updateMesh();
                }
            }
            if (filled.posV.getZ() == 0) {
                if (filled.c.getAdjactentChunk(0, 0, -1) != null) {
                    filled.c.getAdjactentChunk(0, 0, -1).updateMesh();
                }
            }
            if (filled.posV.getZ() == VoxelGame.CHUNK_SIZE_XZ - 1) {
                if (filled.c.getAdjactentChunk(0, 0, 1) != null) {
                    filled.c.getAdjactentChunk(0, 0, 1).updateMesh();
                }
            }
            if (filled.posV.getY() == 0) {
                if (filled.c.getAdjactentChunk(0, -1, 0) != null) {
                    filled.c.getAdjactentChunk(0, -1, 0).updateMesh();
                }
            }
            if (filled.posV.getY() == VoxelGame.CHUNK_SIZE_XZ - 1) {
                if (filled.c.getAdjactentChunk(0, 1, 0) != null) {
                    filled.c.getAdjactentChunk(0, 1, 0).updateMesh();
                }
            }

            clicked = true;
            clickTime = Time.getTime();
        }
        VoxelChunkPair empty = getFarthestEmptyVoxel(forward);
        if (Input.getMouse(1) && empty != null && !clicked) {
            empty.v.setType(placeType);
            empty.v.setLight(0);
            empty.c.updateMesh();

            if (empty.posV.getX() == 0) {
                if (empty.c.getAdjactentChunk(-1, 0, 0) != null) {
                    empty.c.getAdjactentChunk(-1, 0, 0).updateMesh();
                }
            }
            if (empty.posV.getX() == VoxelGame.CHUNK_SIZE_XZ - 1) {
                if (empty.c.getAdjactentChunk(1, 0, 0) != null) {
                    empty.c.getAdjactentChunk(1, 0, 0).updateMesh();
                }
            }
            if (empty.posV.getZ() == 0) {
                if (empty.c.getAdjactentChunk(0, 0, -1) != null) {
                    empty.c.getAdjactentChunk(0, 0, -1).updateMesh();
                }
            }
            if (empty.posV.getZ() == VoxelGame.CHUNK_SIZE_XZ - 1) {
                if (empty.c.getAdjactentChunk(0, 0, 1) != null) {
                    empty.c.getAdjactentChunk(0, 0, 1).updateMesh();
                }
            }
            if (empty.posV.getY() == 0) {
                if (empty.c.getAdjactentChunk(0, -1, 0) != null) {
                    empty.c.getAdjactentChunk(0, -1, 0).updateMesh();
                }
            }
            if (empty.posV.getY() == VoxelGame.CHUNK_SIZE_Y - 1) {
                if (empty.c.getAdjactentChunk(0, 1, 0) != null) {
                    empty.c.getAdjactentChunk(0, 1, 0).updateMesh();
                }
            }

            clicked = true;
            clickTime = Time.getTime();
        }

        if (Input.getKey(Input.KEY_1)) {
            placeType = 1;
        }
        if (Input.getKey(Input.KEY_2)) {
            placeType = 2;
        }
        if (Input.getKey(Input.KEY_3)) {
            placeType = 3;
        }
        if (Input.getKey(Input.KEY_4)) {
            placeType = 4;
        }
        if (Input.getKey(Input.KEY_5)) {
            placeType = 5;
        }
        if (Input.getKey(Input.KEY_6)) {
            placeType = 6;
        }
        if (Input.getKey(Input.KEY_7)) {
            placeType = 7;
        }
        if (Input.getKey(Input.KEY_8)) {
            placeType = 8;
        }
        if (Input.getKey(Input.KEY_9)) {
            placeType = 9;
        }

    }

    public VoxelChunkPair getClosestFilledVoxel(Vector3f forward) {
        VoxelChunkPair pair = new VoxelChunkPair();
        for (int i = 15; i < pickingRange * 40; i++) {
            if (ChunkManager.getBlockFromWorldPos(gameObject.getTransform().getPos().add(forward.mul(i / 40))).getType() != 0) {
                for (int j = 0; j < 20; j++) {
                    if (ChunkManager.getBlockFromWorldPos(gameObject.getTransform().getPos().add(forward.mul(i / 40).mul(1 - (j * 0.001f)))).getType() != 0) {
                        pair.v = ChunkManager.getBlockFromWorldPos(gameObject.getTransform().getPos().add(forward.mul(i / 40).mul(1 - (j * 0.001f))));
                        pair.c = ChunkManager.getChunkFromWorldPos(gameObject.getTransform().getPos().add(forward.mul(i / 40).mul(1 - (j * 0.001f))));
                        pair.posV = ChunkManager.getBlockPositionFromWorldPos(gameObject.getTransform().getPos().add(forward.mul(i / 40).mul(1 - (j * 0.001f))));
                    }
                }
                return pair;
            }
        }
        return null;
    }

    public VoxelChunkPair getFarthestEmptyVoxel(Vector3f forward) {
        VoxelChunkPair pair = new VoxelChunkPair();
        for (int i = 15; i < pickingRange * 40; i++) {
            if (ChunkManager.getBlockFromWorldPos(gameObject.getTransform().getPos().add(forward.mul(i / 40))).getType() != 0) {
                for (int j = 0; j < 20; j++) {
                    if (ChunkManager.getBlockFromWorldPos(gameObject.getTransform().getPos().add(forward.mul(i / 40).mul(1 - (j * 0.001f)))).getType() == 0) {
                        pair.v = ChunkManager.getBlockFromWorldPos(gameObject.getTransform().getPos().add(forward.mul(i / 40).mul(1 - (j * 0.001f))));
                        pair.c = ChunkManager.getChunkFromWorldPos(gameObject.getTransform().getPos().add(forward.mul(i / 40).mul(1 - (j * 0.001f))));
                        pair.posV = ChunkManager.getBlockPositionFromWorldPos(gameObject.getTransform().getPos().add(forward.mul(i / 40).mul(1 - (j * 0.001f))));
                        break;
                    }
                }
                break;
            }
            if (ChunkManager.getBlockFromWorldPos(gameObject.getTransform().getPos().add(forward.mul(i / 40))).getType() == 0) {
                pair.v = ChunkManager.getBlockFromWorldPos(gameObject.getTransform().getPos().add(forward.mul(i / 40)));
                pair.c = ChunkManager.getChunkFromWorldPos(gameObject.getTransform().getPos().add(forward.mul(i / 40)));
                pair.posV = ChunkManager.getBlockPositionFromWorldPos(gameObject.getTransform().getPos().add(forward.mul(i / 40)));
            }
            if (i == (pickingRange * 40) - 1 && pair.v.getType() == 0) {
                return null;
            }
        }
        if (pair.v != null && pair.c != null) {
            return pair;
        } else {
            return null;
        }
    }

    public void updateMovement() {
        Vector3f move = gameObject.getComponent(FPSCamera.class).getMovementVector();

        Vector3f moveX = new Vector3f(move.getX(), 0, 0);
        Vector3f moveZ = new Vector3f(0, 0, move.getZ());
        
        //Rape

        if (move.getX() > 0) {
            moveX = moveX.add(Vector3f.X_AXIS.mul(width));
        } else {
            moveX = moveX.sub(Vector3f.X_AXIS.mul(width));
        }
        if (move.getZ() > 0) {
            moveZ = moveZ.add(Vector3f.Z_AXIS.mul(width));
        } else {
            moveZ = moveZ.sub(Vector3f.Z_AXIS.mul(width));
        }

        //X Axis Movement
        if (ChunkManager.getBlockFromWorldPos(gameObject.getTransform().getPos().sub(new Vector3f(0, 9 * (height / 10), 0)).add(moveX)).getType() != 0
                || ChunkManager.getBlockFromWorldPos(gameObject.getTransform().getPos().sub(new Vector3f(0, (height / 10), 0)).add(moveX)).getType() != 0) {
            move.setX(0);
        }

        //Z Axis Movement
        if (ChunkManager.getBlockFromWorldPos(gameObject.getTransform().getPos().sub(new Vector3f(0, 9 * (height / 10), 0)).add(moveZ)).getType() != 0
                || ChunkManager.getBlockFromWorldPos(gameObject.getTransform().getPos().sub(new Vector3f(0, (height / 10), 0)).add(moveZ)).getType() != 0) {
            move.setZ(0);
        }

        //Y Axis Movement
        if (ChunkManager.getBlockFromWorldPos(gameObject.getTransform().getPos().add(new Vector3f(0, 0.2f, 0))).getType() != 0) {
            YVelocity = 0;
        }
        if (ChunkManager.getBlockFromWorldPos(gameObject.getTransform().getPos().sub(Vector3f.Y_AXIS.mul(height + 0.1f))).getType() == 0) {
            YVelocity -= gravity * Time.getDelta();
            if (YVelocity < -TerminalVel) {
                YVelocity = -TerminalVel;
            }
            move.setY((float) (YVelocity * Time.getDelta()));
            grounded = false;
        } else if (ChunkManager.getBlockFromWorldPos(gameObject.getTransform().getPos().sub(Vector3f.Y_AXIS.mul(height + 0.1f))).getType() != 0) {
            grounded = true;
            if (!jumped) {
                YVelocity = 0;
            }

            move.setY((float) (YVelocity * Time.getDelta()));
            jumped = false;
        }

        gameObject.getTransform().translate(move);
        if (gameObject.getTransform().getPos().getY() < -16) {
            resetPlayer();
        }
    }

    public void resetPlayer() {
        gameObject.getTransform().setPos(32, (VoxelGame.MAP_DIMENSION * VoxelGame.CHUNK_SIZE_Y * VoxelGame.VOXEL_SIZE) + 5f, 32);
        gameObject.getTransform().setRotation(0, 0, 0);
    }
}

class VoxelChunkPair {

    public voxel v;
    public Chunk c;
    public Vector3f posV;
}