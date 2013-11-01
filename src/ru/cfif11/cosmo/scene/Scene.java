package ru.cfif11.cosmo.scene;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.IRenderer;
import ru.cfif11.cosmo.Ticker;
import ru.cfif11.cosmo.object.Camera;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public class Scene{

    private GameWorld       gameWorld;
    private Cursor          cur;
    private Camera          camera;
    private FrameBuffer     buffer;

    public Scene(Ticker ticker, GameWorld gameWorld) {
        buffer = new FrameBuffer(1024, 768, FrameBuffer.SAMPLINGMODE_NORMAL);
        buffer.disableRenderer(IRenderer.RENDERER_SOFTWARE);
        buffer.enableRenderer(IRenderer.RENDERER_OPENGL);
        this.gameWorld  = gameWorld;
        camera          = new Camera(gameWorld.getWorld(), ticker, buffer);
        cur             = new Cursor();
        gameWorld.tunePositionCamera(camera);
    }

    public void bufferReset(boolean state) {
        buffer.clear();
        buffer.setPaintListenerState(state);
        gameWorld.renderScene(buffer);
        gameWorld.draw(buffer);
        buffer.update();
        gameWorld.drawGraphForm(buffer, camera);
        buffer.displayGLOnly();
    }

    public void close() {
        buffer.disableRenderer(IRenderer.RENDERER_OPENGL);
        buffer.dispose();
    }

    public boolean run() {
        return gameWorld.run(camera);
    }

    protected FrameBuffer getBuffer() {
        return  buffer;
    }

}
