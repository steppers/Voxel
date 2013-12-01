package engine.core;

import engine.math.Vector3f;
import engine.math.VectorMath;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Animation 
{
    public ArrayList<AnimationFrame> frames = new ArrayList<>();
    private boolean looping;
    private float length;
    private String name;
    private float totalTime = 0;
    
    private float speed = 1;
    public boolean paused = false;
    
    public Animation(String name, String fileName)
    {
        this.name = name;
        try{
            BufferedReader reader = new BufferedReader(new FileReader("./res/animations/" + fileName));
            String line;
            
            while ((line = reader.readLine()) != null) {
                if(!line.startsWith("#"))
                    throw new Exception();
                else{
                    String[] tokens = line.split(" ");
                    frames.add(new AnimationFrame(Float.parseFloat(tokens[1]),
                            new Vector3f(Float.parseFloat(tokens[2].split("/")[0]), Float.parseFloat(tokens[2].split("/")[1]), Float.parseFloat(tokens[2].split("/")[2])),
                            new Vector3f(Float.parseFloat(tokens[3].split("/")[0]), Float.parseFloat(tokens[3].split("/")[1]), Float.parseFloat(tokens[3].split("/")[2])),
                            new Vector3f(Float.parseFloat(tokens[4].split("/")[0]), Float.parseFloat(tokens[4].split("/")[1]), Float.parseFloat(tokens[4].split("/")[2]))));
                    length = Float.parseFloat(tokens[1]);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            System.err.println("Error: Cannot load animation file, " + fileName);
            System.exit(1);
        }
    }
    
    public AnimationFrame getFrame(){
        totalTime += Time.getDelta()*speed;
        if(isLooping()){
            if(totalTime > length){
                totalTime -= length;
            }
        }else if(totalTime > length){
            return null;
        }
        
        AnimationFrame low = new AnimationFrame();
        AnimationFrame high = new AnimationFrame();
        AnimationFrame res = new AnimationFrame();
        
        for(AnimationFrame f : frames){
            if(f.getTime() < totalTime)
                low = f;
            if(f.getTime() > totalTime){
                high = f;
                break;
            }
        }
        
        float timeDif = high.getTime() - low.getTime();
        float lerpVar = (totalTime - low.getTime())/timeDif;
        
        res.setPosition(VectorMath.lerp(lerpVar, low.getPosition(), high.getPosition()));
        res.setRotation(VectorMath.lerp(lerpVar, low.getRotation(), high.getRotation()));
        res.setScale(VectorMath.lerp(lerpVar, low.getScale(), high.getScale()));
        return res;
    }

    public boolean isLooping() {
        return looping;
    }

    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    public float getLength() {
        return length;
    }

    public String getName() {
        return name;
    }

    public float getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(float totalTime) {
        this.totalTime = totalTime;
    }
    
    public void setSpeed(float speed){
        this.speed = speed;
    }
}
