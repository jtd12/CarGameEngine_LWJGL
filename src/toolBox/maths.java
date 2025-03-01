package toolBox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.camera;

public class maths {
	
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}
	
public static Matrix4f createTransformationMatrix(Vector3f translation,float rx, float ry, float rz,
		float scale)
{
	Matrix4f matrix=new Matrix4f();
	matrix.setIdentity();
	Matrix4f.translate(translation, matrix, matrix);
	Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0), matrix, matrix);
	Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0), matrix, matrix);
	Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1), matrix, matrix);
	Matrix4f.scale(new Vector3f(scale,scale,scale), matrix, matrix);
	return matrix;
}
public static Matrix4f createViewMatrix(camera cam)
{
	Matrix4f viewMatrix=new Matrix4f();
	viewMatrix.setIdentity();
	Matrix4f.rotate((float)Math.toRadians(cam.getPitch()), new Vector3f(1,0,0), viewMatrix, viewMatrix);
	Matrix4f.rotate((float)Math.toRadians(cam.getYaw()), new Vector3f(0,1,0), viewMatrix, viewMatrix);
	Vector3f camPos=cam.getPosition();
	Vector3f negCamPos=new Vector3f(-camPos.x,-camPos.y,-camPos.z);
	Matrix4f.translate(negCamPos,viewMatrix,viewMatrix);
	return viewMatrix;
}
}
