import com.threed.jpct.*;

/**
 * A simple HelloWorld using the OpenGL-renderer.
 * @author EgonOlsen
 *
 */
public class HelloWorldOGL {

	private World world;
	private FrameBuffer buffer;
	private Object3D box;

	public static void main(String[] args) throws Exception {
		new HelloWorldOGL().loop();
	}

	public HelloWorldOGL() throws Exception {
		world = new World();
		world.setAmbientLight(0, 255, 0);

		TextureManager.getInstance().addTexture("box", new Texture("box.jpg"));

		box = Primitives.getBox(13f, 2f);
		box.setTexture("box");
		box.setEnvmapped(Object3D.ENVMAP_ENABLED);
		box.build();
		world.addObject(box);

		world.getCamera().setPosition(50, -50, -5);
		world.getCamera().lookAt(box.getTransformedCenter());
	}

	private void loop() throws Exception {
		buffer = new FrameBuffer(800, 600, FrameBuffer.SAMPLINGMODE_NORMAL);
		buffer.disableRenderer(IRenderer.RENDERER_SOFTWARE);
		buffer.enableRenderer(IRenderer.RENDERER_OPENGL);

		while (!org.lwjgl.opengl.Display.isCloseRequested()) {
			box.rotateY(0.01f);
			buffer.clear(java.awt.Color.BLUE);
			world.renderScene(buffer);
			world.draw(buffer);
			buffer.update();
			buffer.displayGLOnly();
			Thread.sleep(10);
		}
		buffer.disableRenderer(IRenderer.RENDERER_OPENGL);
		buffer.dispose();
		System.exit(0);
	}
}

	