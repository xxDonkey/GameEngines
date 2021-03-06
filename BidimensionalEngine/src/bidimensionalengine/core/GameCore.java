package bidimensionalengine.core;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;

import bidimensionalengine.graphics.CustomGraphics;

/**
 * <p>
 * Your {@code Main} or {@code Run} class should implement this interface. In
 * it's {@code main()} method, it should instantiate an instance of itself. <br>
 * This interface is not required, but you will have to manually call the Window
 * constructor, <b>it would be much more difficult.</b>
 * <p>
 * In order to use input, you must the overridden methods in the {@code super}.
 * At the end of your main class's constructor, invoke
 * {@code super.createWindow}, and pass in all required arguments.
 * <p>
 * <u>Example:</u><br>
 * <BLOCKQUOTE>public Main(String title, String assetDirectory, int width, int
 * height, int ticksPerSecond) <br>
 * { <BLOCKQUOTE>super.onKeyPressed(null);<br>
 * super.createWindow(title, assetDirectory, width, height,
 * ticksPerSecond);</BLOCKQUOTE> }</BLOCKQUOTE>
 * 
 * @author Dylan Raiff
 * @version 1.0
 */
public abstract class GameCore
{
	/* Static Data */

	/**
	 * Gets the asset directory.
	 * 
	 * @return asset directory
	 */
	public static String getAssetDirectory()
	{
		File file = new File(CustomGraphics.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		String packagePath = file.toString().substring(0, file.toString().length() - 4);
		return packagePath + "/assets/";
	}

	/* Instance Data */

	/**
	 * Game's start method. See {@code Window} for more detail.
	 */
	public abstract void start();

	/**
	 * Game's upate method. See {@code Window} for more detail. <br>
	 * Leave empty if unused.
	 */
	public abstract void update();

	/**
	 * Game's graphics method. See {@code Window} for more detail.
	 */
	public abstract void graphics(Graphics2D g);

	/**
	 * Called when a key has been pressed if keyboard input has been turned on.
	 */
	public void onKeyPressed(KeyEvent e)
	{}

	/**
	 * Called when a key has been typed if keyboard input has been turned on.
	 */
	public void onKeyTyped(KeyEvent e)
	{}

	/**
	 * Called when a key has been released if keyboard input has been turned on.
	 * 
	 */
	public void onKeyReleased(KeyEvent e)
	{}

	/**
	 * Called when the user clicks the listened to component if mouse input has been
	 * turned on.
	 */
	public void onMouseClicked(MouseEvent e)
	{}

	/**
	 * Called when the cursor enters the bounds of the listened to component if
	 * mouse input has been turned on.
	 */
	public void onMouseEntered(MouseEvent e)
	{}

	/**
	 * Called when the cursor exits the bounds of the listened to component if mouse
	 * input has been turned on.
	 */
	public void onMouseExited(MouseEvent e)
	{}

	/**
	 * Called when the user presses a mouse button while the cursor is over the
	 * listened to component if mouse input has been turned on.
	 */
	public void onMousePressed(MouseEvent e)
	{}

	/**
	 * Called when the user releases a mouse button after a mouse press over the
	 * listened to component if mouse input has been turned on.
	 */
	public void onMouseReleased(MouseEvent e)
	{}

	/**
	 * <p>
	 * Creates a new {@code Window}. If no input methods are used, and an
	 * {@code ticksPerSecond} is greater than 0, a window with an update method will
	 * be created. Otherwise, a once-updated window will be instantiated.
	 * <p>
	 * If input is used, a window with either keyboard input, mouse input, or both
	 * will be created.
	 * 
	 * @param title          title of the window
	 * @param assetDirectory directory where assets are located
	 * @param useInput       data for input
	 * @param width          width of the window
	 * @param height         height of the window
	 * @param update         whether or not to run the game loop and graphics method
	 *                       multiple times
	 * @param runVisualizer  whether or not to run the structure visualizer
	 */
	public void createWindow(String title, String assetDirectory, UseInput useInput, int width, int height,
			boolean update, boolean runVisualizer)
	{
		int ticksPerSecond = 0;

		/* Sets ticks per second to predifined 60 tps if update is true. */
		if (update)
			ticksPerSecond = 60;

		Window.KeyboardInputMethodData keyboardInput = new Window.KeyboardInputMethodData(
				useInput.useOnKeyPressed ? this::onKeyPressed : null, useInput.useOnKeyTyped ? this::onKeyTyped : null,
				useInput.useOnKeyReleased ? this::onKeyReleased : null);

		Window.MouseInputMethodData mouseInput = new Window.MouseInputMethodData(
				useInput.useOnMouseClicked ? this::onMouseClicked : null,
				useInput.useOnMouseEntered ? this::onMouseEntered : null,
				useInput.useOnMouseExited ? this::onMouseExited : null,
				useInput.useOnMousePressed ? this::onMousePressed : null,
				useInput.useOnMouseReleased ? this::onMouseReleased : null);

		// Accounts for top bar of a Mac window.
		if (System.getProperty("os.name").startsWith("Mac"))
			height += 22;

		if (keyboardInput.allVoid() && mouseInput.allVoid())
		{
			if (!update)
				new Window(title, width, height, assetDirectory, runVisualizer, this::start, this::graphics, this);
			else
				new Window(title, width, height, ticksPerSecond, assetDirectory, runVisualizer, this::start,
						this::update, this::graphics, this);
		}
		else if (!keyboardInput.allVoid() && mouseInput.allVoid())
		{
			if (!update)
				System.err.println("Cannot have less than or equal to 0 tps and input.");
			else
				new Window(title, width, height, ticksPerSecond, assetDirectory, runVisualizer, this::start,
						this::update, this::graphics, keyboardInput, this);
		}
		else if (keyboardInput.allVoid() && !mouseInput.allVoid())
		{
			if (!update)
				System.err.println("Cannot have less than or equal to 0 tps and input.");
			else
				new Window(title, width, height, ticksPerSecond, assetDirectory, runVisualizer, this::start,
						this::update, this::graphics, mouseInput, this);
		}
		else
		{
			if (ticksPerSecond <= 0)
				System.err.println("Cannot have less than or equal to 0 tps and input.");
			else
				new Window(title, width, height, ticksPerSecond, assetDirectory, runVisualizer, this::start,
						this::update, this::graphics, keyboardInput, mouseInput, this);
		}
	}

	/**
	 * Structure for holding data to be used in construction of the window to
	 * determine what input listeners to set up.
	 * 
	 * @author Dylan Raiff
	 * @version 1.0
	 */
	public static class UseInput
	{
		/**
		 * Whether or not to use the {@code onKeyPressed} method.
		 */
		private boolean useOnKeyPressed;

		/**
		 * Whether or not to use the {@code onKeyTyped} method.
		 */
		private boolean useOnKeyTyped;

		/**
		 * Whether or not to use the {@code onKeyReleased} method.
		 */
		private boolean useOnKeyReleased;

		/**
		 * Whether or not to use the {@code onMouseClicked} method.
		 */
		private boolean useOnMouseClicked;

		/**
		 * Whether or not to use the {@code onMouseEntered} method.
		 */
		private boolean useOnMouseEntered;

		/**
		 * Whether or not to use the {@code onMouseExited} method.
		 */
		private boolean useOnMouseExited;

		/**
		 * Whether or not to use the {@code onMousePressed} method.
		 */
		private boolean useOnMousePressed;

		/**
		 * Whether or not to use the {@code onMouseReleased} method.
		 */
		private boolean useOnMouseReleased;

		/**
		 * Creates a new object to hold boolean values of the {@code Window's} input.
		 * 
		 * @param kp  Whether or not to use the {@code onKeyTyped} method
		 * @param kt  Whether or not to use the {@code onKeyReleased} method.
		 * @param kr  Whether or not to use the {@code onMouseClicked} method.
		 * @param mc  Whether or not to use the {@code onMouseClicked} method.
		 * @param mEn Whether or not to use the {@code onMouseEntered} method.
		 * @param mEx Whether or not to use the {@code onMouseExited} method.
		 * @param mp  Whether or not to use the {@code onMousePressed} method.
		 * @param mr  Whether or not to use the {@code onMouseReleased} method.
		 */
		public UseInput(boolean kp, boolean kt, boolean kr, boolean mc, boolean mEn, boolean mEx, boolean mp,
				boolean mr)
		{
			useOnKeyPressed = kp;
			useOnKeyTyped = kt;
			useOnKeyReleased = kr;

			useOnMouseClicked = mc;
			useOnMouseEntered = mEn;
			useOnMouseExited = mEx;
			useOnMousePressed = mp;
			useOnMouseReleased = mr;
		}
	}
}
