package entities;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import java.util.Random;

import models.texturedModel;
import renderEngine.DisplayManager;
import terrains.terrain;

public class player extends entity {
	
	private  static final float turn_speed=60.5f;
	private static final float gravity=-10f;
	//private static final float terrain_height=0;
	private  float currentSpeed=0;
	private  float currentTurnSpeed=0;
	private float upwardspeed=0;
	private float maxSpeed=100.0f;
	private float acc=0.48f;
	private float dec=1.15f;
	private float dec2=2.55f;
	private int up,down,right,left,space=0;
	private int currentWaypoint = 0;
	private float maxSpeedAI = 12.5f;
	private Random random = new Random();
	public float speedAI=0.0f;
	private boolean terrain1;
	private boolean terrain2;
	private boolean terrain3;
	private boolean terrain4;
	private float[][] waypoints = {{180, 380}, {230, 380}, {250, 600}, {550, 600},{550, 2350},{-2850, 2350},{-2850, 400},{120, 400}};
	
	public player(texturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale,float scaleX,float scaleY,float scaleZ) {
		super(model, position, rotX, rotY, rotZ, scale,scaleX,scaleY,scaleZ);
		speedAI=random.nextFloat()+0.85f * maxSpeedAI;
		// TODO Auto-generated constructor stub
	}
	public float getCurrentSpeed()
	{
		return currentSpeed;
	}
	public void setSpeed(float s)
	{
		currentSpeed=s;
	}
	
	public boolean checkCollision(AABB box1, AABB box2) {
	    return (box1.getMinX() <= box2.getMaxX() && box1.getMaxX() >= box2.getMinX()) &&
	           (box1.getMinY() <= box2.getMaxY() && box1.getMaxY() >= box2.getMinY()) &&
	           (box1.getMinZ() <= box2.getMaxZ() && box1.getMaxZ() >= box2.getMinZ());
	}
	
	  public AABB getBoundingBox() {
	        // Cr�er et retourner une bo�te englobante AABB bas�e sur la position et la taille de la voiture
	        return new AABB(getPosition().x, getPosition().y, getPosition().z, getScaleX(), getScaleY(), getScaleZ());
	    }
	  
	public void handleCollision(player car1, player car2) {
	    // D�placer les voitures dans des directions oppos�es pour �viter le chevauchement
		Vector3f pos1 = car1.getPosition();  // Position de la premi�re voiture
		Vector3f pos2 = car2.getPosition();  // Position de la deuxi�me voiture

		// Calculer la direction en soustrayant les positions composante par composante
		Vector3f direction = new Vector3f(pos2.x - pos1.x, pos2.y - pos1.y, pos2.z - pos1.z);
		if (direction.length() > 0)
		{
		direction.normalise();
		
		Vector3f directionScaled = new Vector3f(direction);  // Copie explicite du vecteur direction

		// Multiplier la copie du vecteur par 0.1f
		directionScaled.x*=(4.1f);  // Cette op�ration modifie directionScaled et non direction
		directionScaled.y*=(4.1f); 
		directionScaled.z*=(4.1f); 
		// Cr�er une copie de la position de car1 pour ne pas la modifier directement
		Vector3f newPositionCar1 = new Vector3f(pos1);

		// Ajuster la position en reculant l�g�rement dans la direction oppos�e
		newPositionCar1.x-=(directionScaled).x;
		newPositionCar1.y-=(directionScaled).y; 
		newPositionCar1.z-=(directionScaled).z; 
		// Appliquer la modification � car1
		car1.setPosition(newPositionCar1);  // Mettre � jour la position de car1

		// Faire de m�me pour car2 (en avan�ant l�g�rement)
		Vector3f newPositionCar2 = new Vector3f(pos2);
		newPositionCar2.x+=(directionScaled).x; 
		newPositionCar2.y+=(directionScaled).y;
		newPositionCar2.z+=(directionScaled).z;// Appliquer la modification � car2
		car2.setPosition(newPositionCar2);  // Mettre � jour la position de car2
		}
		else
		{
			 System.out.println("Les voitures sont � la m�me position, pas de mouvement n�cessaire.");
		}
	}
	
	
	
	public void updateAI(terrain t)
	{
		 	float[] target = waypoints[currentWaypoint];
	        float dx = target[0] - position.x;
	        float dy = target[1] - position.z;
	        float targetDirection = (float) Math.toDegrees(Math.atan2(dy, dx));

	        float angleDiff = targetDirection - super.getRotY();
	        
	        
	        if (angleDiff > 180) angleDiff -= 360;
	        if (angleDiff < -180) angleDiff += 360;

	  
	        super.setIncrementeRotY(angleDiff * 0.1f);
	        //super.rotY += angleDiff * 0.1f;  // Turn towards the target
	        
	        
	        currentSpeed = speedAI/2;  // Fixed speed for simplicity
	        
	        position.x += Math.cos(Math.toRadians(super.getRotY())) * currentSpeed;
	        position.z += Math.sin(Math.toRadians(super.getRotY())) * currentSpeed;

	        // Check if we're close to the current waypoint
	        float distance = (float) Math.sqrt(dx * dx + dy * dy);
	        if (distance < 30.5f) {
	            currentWaypoint = (currentWaypoint + 1) % waypoints.length;
	        }
	        
	        upwardspeed+=gravity*DisplayManager.getTimeFrameSeconds();
			super.increasePosition(0, upwardspeed*DisplayManager.getTimeFrameSeconds(), 0);
			float terrainHeights=t.getHeightTerrain(super.getPosition().x, super.getPosition().z);
			if(super.getPosition().y<terrainHeights+2)
			{
				upwardspeed=0;
				super.getPosition().y=terrainHeights+2;
			}
			
	}
	
	public void setTerrain1(boolean t)
	{
		terrain1=t;
	}
	public void setTerrain2(boolean t)
	{
		terrain2=t;
	}
	public void setTerrain3(boolean t)
	{
		terrain3=t;
	}
	public void setTerrain4(boolean t)
	{
		terrain4=t;
	}
	public boolean getTerrain1()
	{
		return terrain1;
	}
	public boolean getTerrain2()
	{
		return terrain2;
	}
	public boolean getTerrain3()
	{
		return terrain3;
	}
	public boolean getTerrain4()
	{
		return terrain4;
	}
	
	public void move(terrain t,List<roues> ww,List<roues> rr)
	{
		System.out.print(currentSpeed);
		checkInputs(ww,rr);
		super.increaseRotation(0, currentTurnSpeed*DisplayManager.getTimeFrameSeconds(), 0);
		float dist=currentSpeed*DisplayManager.getTimeFrameSeconds();
		float dx= (float) (dist*Math.sin(Math.toRadians(super.getRotY())));
		float dz= (float) (dist*-Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dz, 0, dx);
		upwardspeed+=gravity*DisplayManager.getTimeFrameSeconds();
		super.increasePosition(0, upwardspeed*DisplayManager.getTimeFrameSeconds(), 0);
		float terrainHeights=t.getHeightTerrain(super.getPosition().x, super.getPosition().z);
		if(super.getPosition().y<terrainHeights+2)
		{
			upwardspeed=0;
			super.getPosition().y=terrainHeights+2;
		}
	}
	
	private void control(List<roues> ww,List<roues> rr)
	{
		ww.get(0).turnRotZ();
		ww.get(1).turnRotZ();
		rr.get(0).turnRotZ();
		rr.get(1).turnRotZ();
		
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN))
		{
			up=1;
			
		
			ww.get(0).turnRotY();
			rr.get(0).turnRotY();
			
		}
		
		else if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)==false)
		{
			up=0;
			
		
			
		}
		
		 if(Keyboard.isKeyDown(Keyboard.KEY_UP))
		{
			down=1;
			
			ww.get(0).turnRotY();
			rr.get(0).turnRotY();
			
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_UP)==false)
		{
			down=0;
		
			
		}
		
		 if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
		{
		
			right=1;
			
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)==false)
		{
		
			right=0;
			
		}
		 if(Keyboard.isKeyDown(Keyboard.KEY_LEFT))
		{
			left=1;
		
			
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)==false)
		{
			left=0;
			
			
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
		{
			//jump();
			space=1;
			
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)==false)
		{
			//jump();
			space=0;
			
		}
		
	}
	
	private void checkInputs(List<roues> ww,List<roues> rr)
	{
		control(ww, rr);
		
		if(up==1 && currentSpeed<maxSpeed)
		{
			if(currentSpeed<0)
			{
				currentSpeed+=dec;
		 }
			else
			{
				currentSpeed+=acc;
			}
		}
		if(down==1 && currentSpeed>-maxSpeed)
		{
			if(currentSpeed>0)
			{
				currentSpeed-=dec;
			}
			else
			{
				currentSpeed-=acc;
			}
			
		}
		
		if(up==0 && down==0)
		{
			if(currentSpeed-dec>0)
			{
				currentSpeed-=dec;
			}
			else if(currentSpeed+dec<0)
			{
				currentSpeed+=dec;
			}
			else
			{
				currentSpeed=0;
			}
		}
		
			if(space==1)
		{
			if(currentSpeed-dec2>0)
			{
				currentSpeed-=dec2;
			}
			else if(currentSpeed+dec2<0)
			{
				currentSpeed+=dec2;
			}
			else
			{
				currentSpeed=0;
			}
		}
			if(right==1)
			{
				currentTurnSpeed=turn_speed*(currentSpeed/maxSpeed);
				//angle=turnSpeed;
			//	+=turnSpeed*(speed/maxSpeed);
			}
		
			else if(left==1)
			{
				currentTurnSpeed=-turn_speed*(currentSpeed/maxSpeed);
				//angle=turnSpeed;
			//	+=turnSpeed*(speed/maxSpeed);
			}
			else if(left==0 || right==0)
			{
				currentTurnSpeed=0;
				//angle=turnSpeed;
			//	+=turnSpeed*(speed/maxSpeed);
			}

	}
	
	public int getUP()
	{
		return up;
	}
	public void setUP(int u)
	{
		up=u;
	}
	public void setDOWN(int u)
	{
		down=u;
	}
	public int getDOWN()
	{
		return down;
	}
}
