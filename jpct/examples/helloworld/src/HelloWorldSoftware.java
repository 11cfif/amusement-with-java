import com.threed.jpct.*;
import javax.swing.*;

/**
 * A simple HelloWorld using the Software-renderer and rendering into a frame
 * using active rendering.
 * @author EgonOlsen
 * 
 */
public class HelloWorldSoftware {

	private World world;
	private FrameBuffer buffer;
	private Object3D box;
	private JFrame frame;

	public static void main(String[] args) throws Exception {
		new HelloWorldSoftware().loop();
	}

	public HelloWorldSoftware() throws Exception {
		
		frame=new JFrame("Hello world");
		frame.setSize(800, 600);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
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

		while (frame.isShowing()) {
			box.rotateY(0.01f);
			buffer.clear(java.awt.Color.BLUE);
			world.renderScene(buffer);
			world.draw(buffer);
			buffer.update();
			buffer.display(frame.getGraphics());
			Thread.sleep(10);
		}
		buffer.disableRenderer(IRenderer.RENDERER_OPENGL);
		buffer.dispose();
		frame.dispose();
		System.exit(0);
	}
}

	