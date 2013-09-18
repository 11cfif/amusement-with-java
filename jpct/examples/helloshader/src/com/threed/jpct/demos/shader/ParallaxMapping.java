package com.threed.jpct.demos.shader;

import org.lwjgl.input.Mouse;

import com.threed.jpct.*;
import com.threed.jpct.util.*;

public class ParallaxMapping {

	private World world;
	private FrameBuffer buffer;
	private Object3D plane;
	private Light light;
	private GLSLShader shader;
	private float scale = 0.05f;

	public static void main(String[] args) throws Exception {
		new ParallaxMapping().loop();
	}

	public ParallaxMapping() throws Exception {
		Config.maxPolysVisible = 1000;
		Config.lightMul = 1;
		Config.glTrilinear = true;
		Config.glUseVBO = true;

		world = new World();

		TextureManager tm = TextureManager.getInstance();

		Texture face = new Texture("data/face.png");
		Texture normals = new Texture("data/face_norm.png", true);
		Texture height = new Texture("data/face_height2.png");
		plane = Primitives.getPlane(1, 100);

		TexelGrabber grabber = new TexelGrabber();
		height.setEffect(grabber);
		height.applyEffect();
		int[] heighties = grabber.getAlpha();

		AlphaMerger setter = new AlphaMerger(heighties);
		normals.setEffect(setter);
		normals.applyEffect();

		tm.addTexture("face", face);
		tm.addTexture("normals", normals);

		TextureInfo ti = new TextureInfo(TextureManager.getInstance().getTextureID("face"));
		ti.add(TextureManager.getInstance().getTextureID("normals"), TextureInfo.MODE_BLEND);

		plane.setTexture(ti);

		shader = new GLSLShader(Loader.loadTextFile("data/vertexshader_offset.glsl"), Loader.loadTextFile("data/fragmentshader_offset.glsl"));
		plane.setRenderHook(shader);
		plane.setSpecularLighting(true);
		shader.setStaticUniform("invRadius", 0.0003f);
		shader.setStaticUniform("textureUnit0", 0);
		shader.setStaticUniform("textureUnit1", 1);

		plane.build();
		plane.compile();
		plane.strip();

		world.addObject(plane);

		light = new Light(world);
		light.enable();

		light.setIntensity(60, 50, 50);
		light.setPosition(SimpleVector.create(-10, -50, -100));

		world.setAmbientLight(10, 10, 10);

		Camera cam = world.getCamera();
		cam.moveCamera(Camera.CAMERA_MOVEOUT, 120);
		cam.lookAt(plane.getTransformedCenter());
	}

	private void loop() throws Exception {
		buffer = new FrameBuffer(800, 600, FrameBuffer.SAMPLINGMODE_HARDWARE_ONLY);
		buffer.disableRenderer(IRenderer.RENDERER_SOFTWARE);
		buffer.enableRenderer(IRenderer.RENDERER_OPENGL);

		long time = System.nanoTime() / 1000000L;
		int fps = 0;

		while (!org.lwjgl.opengl.Display.isCloseRequested()) {

			shader.setUniform("heightScale", scale);

			buffer.clear(java.awt.Color.BLACK);
			world.renderScene(buffer);
			world.draw(buffer);
			buffer.update();
			buffer.displayGLOnly();

			move();

			fps++;
			long now = System.nanoTime() / 1000000L;
			if (now - time >= 1000) {
				time = now;
				System.out.println(fps + " fps");
				fps = 0;
			}
		}
		buffer.disableRenderer(IRenderer.RENDERER_OPENGL);
		buffer.dispose();
		System.exit(0);
	}

	private void move() {
		int x = Mouse.getDX();
		int y = Mouse.getDY();
		int w = Mouse.getDWheel();

		if (Mouse.isButtonDown(0)) {
			SimpleVector line = new SimpleVector(x, 0, y);
			Matrix m = line.normalize().getRotationMatrix();
			plane.rotateAxis(m.getXAxis(), line.length() / 200f);

		}
		if (w != 0) {
			float d = w / 20000f;
			scale += d;

			if (scale > 0.063f) {
				scale = 0.063f;
			}
			if (scale < 0) {
				scale = 0;
			}
		}
	}

	/**
	 * Merges the height map into the alpha channel of the normal map.
	 * 
	 * @author EgonOlsen
	 * 
	 */
	private static class AlphaMerger implements ITextureEffect {

		private int[] alpha = null;

		public AlphaMerger(int[] alpha) {
			this.alpha = alpha;
		}

		public void apply(int[] arg0, int[] arg1) {
			int end = arg1.length;
			for (int i = 0; i < end; i++) {
				arg0[i] = arg1[i] & 0x00ffffff | alpha[i];
			}
		}

		public boolean containsAlpha() {
			return true;
		}

		public void init(Texture arg0) {
			// TODO Auto-generated method stub
		}
	}

	/**
	 * Extracts the alpha channel from a texture.
	 * 
	 * @author EgonOlsen
	 * 
	 */
	private static class TexelGrabber implements ITextureEffect {

		private int[] alpha = null;

		public void apply(int[] arg0, int[] arg1) {
			alpha = new int[arg1.length];
			int end = arg1.length;
			for (int i = 0; i < end; i++) {
				alpha[i] = (arg1[i] << 24);
			}
		}

		public int[] getAlpha() {
			return alpha;
		}

		public boolean containsAlpha() {
			return true;
		}

		public void init(Texture arg0) {
			// TODO Auto-generated method stub
		}
	}
}
