package bidimensionalengine.graphics;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.function.Consumer;

import bidimensionalengine.core.Window;
import bidimensionalengine.datastructs.Vector2;

/**
 * @author Dylan Raiff
 * @version 1.0
 */
public final class CustomGraphics extends Component
{
	/**
	 * Methods to be called to render UI.
	 */
	private ArrayList<Consumer<Graphics2D>> imageRenderMethods;

	/**
	 * Methods to be called to render particle systems.
	 */
	private ArrayList<Consumer<Graphics2D>> particleSytemRenderMethods;

	/**
	 * Translation vector of the {@code Graphics2D}.
	 */
	public Vector2 translationVector = new Vector2(0, 0);

	/**
	 * Initializes the {@code imageRenderMethods} {@code ArrayList}.
	 */
	public CustomGraphics()
	{
		imageRenderMethods = new ArrayList<Consumer<Graphics2D>>();
		particleSytemRenderMethods = new ArrayList<Consumer<Graphics2D>>();
	}

	/**
	 * Called behind the scenes. <br>
	 * Calls the graphics function passed to the constructor of
	 * {@code bidimensionalengine.core.Window}.
	 */
	@Override
	public void paint(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;

		g2d.translate(translationVector.x, translationVector.y);

		if (Window.getTPS() > 0 && (Window.getThread() == null || !Window.getThread().isAlive()))
			return;

		Window.getGraphicsMethod().accept(g2d);

		for (Consumer<Graphics2D> method : imageRenderMethods)
			method.accept(g2d);

		for (Consumer<Graphics2D> method : particleSytemRenderMethods)
			method.accept(g2d);
	}

	/**
	 * Adds a method to list list of {@code Image} render methods to call.
	 * 
	 * @param method method to add
	 */
	public void addImageRenderMethod(Consumer<Graphics2D> method)
	{ imageRenderMethods.add(method); }

	/**
	 * Removes a method to list list of {@code Image} render methods to call.
	 * 
	 * @param method method to remove
	 */
	public void removeImageRenderMethod(Consumer<Graphics2D> method)
	{ imageRenderMethods.remove(method); }

	/**
	 * Adds a method to list list of {@code ParticleSystem} render methods to call.
	 * 
	 * @param method method to add
	 */
	public void addParticleSytemRenderMethod(Consumer<Graphics2D> method)
	{ particleSytemRenderMethods.add(method); }

	/**
	 * Removes a method to list list of {@code ParticleSystem} render methods to
	 * call.
	 * 
	 * @param method method to remove
	 */
	public void removeParticleSytemRenderMethod(Consumer<Graphics2D> method)
	{ particleSytemRenderMethods.remove(method); }
}
