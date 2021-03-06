package bidimensionalengine.core;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.function.Consumer;

import javax.swing.JFrame;

import bidimensionalengine.audio.AudioLoader;
import bidimensionalengine.core.structurevisualizer.StructureVisualizer;
import bidimensionalengine.datastructs.GameObject;
import bidimensionalengine.graphics.CustomGraphics;
import bidimensionalengine.graphics.SpriteLoader;
import bidimensionalengine.input.KeyboardInput;
import bidimensionalengine.input.MouseInput;

/**
 * Main class that holds all global references in the Bidimensional Engine. Do
 * not instantiate, done so by {@code GameCore}.
 * 
 * @author Dylan Raiff
 * @version 1.0
 */
public final class Window extends JFrame
{
	/**
	 * Singleton variable to assure only one instance of this class is ever
	 * instantiated.
	 */
	private static Window instance;

	/**
	 * The main program running the game.
	 */
	private static GameCore gameCore;

	/**
	 * Parent of all {@code GameObjects} who are constructed with a null parent.
	 */
	private static GameObject worldParent;

	/**
	 * The structure visualizer running along side the engine.
	 */
	private static StructureVisualizer visualizer;

	/**
	 * How many times per second {@code updateMethod} and {@code graphicsMethod} are
	 * called.
	 */
	private static int tps;

	/**
	 * Reference to the window's {@code CustomGraphics}.
	 */
	private static CustomGraphics gfx;

	/**
	 * Reference to the {@code GameLoop} running on {@code thread}.
	 */
	private static GameLoop gameLoop;

	/**
	 * Method to be called once upon the start of {@code thread}.
	 */
	private static ComplexInterface startMethod;

	/**
	 * Method to be called every tick on {@code thread}.
	 */
	private static ComplexInterface updateMethod;

	/**
	 * Method to be called every tick to handle graphics.
	 */
	private static Consumer<Graphics2D> graphicsMethod;

	/**
	 * Stores method references to be called by the {@code KeyboardListener}.
	 */
	private static KeyboardInputMethodData onKeyboardInputMethodData;

	/**
	 * Stores method references to be called by the {@code MouseListener}.
	 */
	private static MouseInputMethodData onMouseInputMethodData;

	/**
	 * Directory where all assets are stored.
	 */
	private static String assetDirectory;

	/**
	 * Reference to the main thread.
	 */
	private static Thread thread;

	/**
	 * Window constructer with <b>all possible arguements</b>. Creates a window and
	 * sets up runtime calls.
	 * 
	 * @param name                      name of the window
	 * @param width                     width of the window
	 * @param height                    height of the window
	 * @param ticksPerSecond            number of times per second to update game
	 *                                  logic and graphics
	 * @param assetDirectory            directory of assets: <u>images to use in
	 * @param runVisualizer             whether or not to run a structure visualizer
	 *                                  graphics, audio files, etc...</u>
	 * @param startMethod               reference to the method that will be called
	 *                                  once on start <br>
	 *                                  <BLOCKQUOTE><u>Returns:</u> void <br>
	 *                                  <u>Arguments</u>: None
	 * @param updateMethod              reference to the method that will be called
	 *                                  every tick <br>
	 *                                  <BLOCKQUOTE><u>Returns:</u> void <br>
	 *                                  <u>Arguments</u>: None
	 * @param graphicsMethod            reference to the method that will draw
	 *                                  graphics <br>
	 *                                  <BLOCKQUOTE><u>Returns:</u> void <br>
	 *                                  <u>Arguments (1)</u>: Graphics2D
	 * @param onKeyboardInputMethodData object with references to the methods that
	 *                                  will be called by the key listener
	 * @param onMouseInputMethodData    object with references to the methods that
	 *                                  will be called by the mouse listener
	 * @param gameCore                  user extended core of the game
	 */
	public Window(String name, int width, int height, int ticksPerSecond, String assetDirectory, boolean runVisualizer,
			ComplexInterface startMethod, ComplexInterface updateMethod, Consumer<Graphics2D> graphicsMethod,
			KeyboardInputMethodData onKeyboardInputMethodData, MouseInputMethodData onMouseInputMethodData,
			GameCore gameCore)
	{
		super(name);

		if (instance != null)
		{
			System.err.println("Two instances of basic2Dgraphics.Window are not allowed.");
			return;
		}

		Window.instance = this;
		if (runVisualizer)
			Window.visualizer = new StructureVisualizer();

		Window.startMethod = startMethod;
		Window.graphicsMethod = graphicsMethod;
		Window.updateMethod = updateMethod;

		Window.onKeyboardInputMethodData = onKeyboardInputMethodData;
		Window.onMouseInputMethodData = onMouseInputMethodData;

		Window.assetDirectory = assetDirectory;

		Window.tps = ticksPerSecond;

		Window.gfx = new CustomGraphics();
		this.add(gfx);

		this.addKeyListener(new KeyboardInput());
		this.addMouseListener(new MouseInput());

		onConstructed(this, width, height, gameCore);
	}

	/**
	 * Window constructer with <b>no input</b>. Creates a window and sets up runtime
	 * calls.
	 * 
	 * @param name           name of the window
	 * @param width          width of the window
	 * @param height         height of the window
	 * @param ticksPerSecond number of times per second to update game logic and
	 *                       graphics
	 * @param assetDirectory directory of assets: <u>images to use in graphics,
	 *                       audio files, etc...</u>
	 * @param runVisualizer  whether or not to run a structure visualizer
	 * @param startMethod    reference to the method that will be called once on
	 *                       start <br>
	 *                       <BLOCKQUOTE><u>Returns:</u> void <br>
	 *                       <u>Arguments</u>: None
	 * @param updateMethod   reference to the method that will be called every tick
	 *                       <br>
	 *                       <BLOCKQUOTE><u>Returns:</u> void <br>
	 *                       <u>Arguments</u>: None
	 * @param graphicsMethod reference to the method that will draw graphics <br>
	 *                       <BLOCKQUOTE><u>Returns:</u> void <br>
	 *                       <u>Arguments (1)</u>: Graphics2D
	 * @param gameCore       user extended core of the game
	 */
	public Window(String name, int width, int height, int ticksPerSecond, String assetDirectory, boolean runVisualizer,
			ComplexInterface startMethod, ComplexInterface updateMethod, Consumer<Graphics2D> graphicsMethod,
			GameCore gameCore)
	{
		super(name);

		if (instance != null)
		{
			System.err.println("Two instances of basic2Dgraphics.Window are not allowed.");
			return;
		}

		Window.instance = this;
		if (runVisualizer)
			Window.visualizer = new StructureVisualizer();

		Window.startMethod = startMethod;
		Window.graphicsMethod = graphicsMethod;
		Window.updateMethod = updateMethod;

		Window.assetDirectory = assetDirectory;

		Window.tps = ticksPerSecond;

		Window.gfx = new CustomGraphics();
		this.add(gfx);

		onConstructed(this, width, height, gameCore);
	}

	/**
	 * Window constructer with <b>no mouse listener</b>. Creates a window and runs
	 * sets up runtime calls.
	 * 
	 * @param name
	 * @param name                      name of the window
	 * @param width                     width of the window
	 * @param height                    height of the window
	 * @param ticksPerSecond            number of times per second to update game
	 *                                  logic and graphics
	 * @param assetDirectory            directory of assets: <u>images to use in
	 *                                  graphics, audio files, etc...</u>
	 * @param runVisualizer             whether or not to run a structure visualizer
	 * @param startMethod               reference to the method that will be called
	 *                                  once on start <br>
	 *                                  <BLOCKQUOTE><u>Returns:</u> void <br>
	 *                                  <u>Arguments</u>: None
	 * @param updateMethod              reference to the method that will be called
	 *                                  every tick <br>
	 *                                  <BLOCKQUOTE><u>Returns:</u> void <br>
	 *                                  <u>Arguments</u>: None
	 * @param graphicsMethod            reference to the method that will draw
	 *                                  graphics <br>
	 *                                  <BLOCKQUOTE><u>Returns:</u> void <br>
	 *                                  <u>Arguments (1)</u>: Graphics2D
	 * @param onKeyboardInputMethodData object with references to the methods that
	 *                                  will be called by the key listener
	 * @param gameCore                  user extended core of the game
	 */
	public Window(String name, int width, int height, int ticksPerSecond, String assetDirectory, boolean runVisualizer,
			ComplexInterface startMethod, ComplexInterface updateMethod, Consumer<Graphics2D> graphicsMethod,
			KeyboardInputMethodData onKeyboardInputMethodData, GameCore gameCore)
	{
		super(name);

		if (instance != null)
		{
			System.err.println("Two instances of basic2Dgraphics.Window are not allowed.");
			return;
		}

		Window.instance = this;
		if (runVisualizer)
			Window.visualizer = new StructureVisualizer();

		Window.startMethod = startMethod;
		Window.graphicsMethod = graphicsMethod;
		Window.updateMethod = updateMethod;

		Window.onKeyboardInputMethodData = onKeyboardInputMethodData;

		Window.assetDirectory = assetDirectory;

		Window.tps = ticksPerSecond;

		Window.gfx = new CustomGraphics();
		this.add(gfx);

		this.addKeyListener(new KeyboardInput());

		onConstructed(this, width, height, gameCore);
	}

	/**
	 * Window constructer with <b>no keyboard listener</b>. Creates a window and
	 * runs sets up runtime calls.
	 * 
	 * @param name                   name of the window
	 * @param width                  width of the window
	 * @param height                 height of the window
	 * @param ticksPerSecond         number of times per second to update game logic
	 *                               and graphics
	 * @param assetDirectory         directory of assets: <u>images to use in
	 *                               graphics, audio files, etc...</u>
	 * @param runVisualizer          whether or not to run a structure visualizer
	 * @param startMethod            reference to the method that will be called
	 *                               once on start <br>
	 *                               <BLOCKQUOTE><u>Returns:</u> void <br>
	 *                               <u>Arguments</u>: None
	 * @param updateMethod           reference to the method that will be called
	 *                               every tick <br>
	 *                               <BLOCKQUOTE><u>Returns:</u> void <br>
	 *                               <u>Arguments</u>: None
	 * @param graphicsMethod         reference to the method that will draw graphics
	 *                               <br>
	 *                               <BLOCKQUOTE><u>Returns:</u> void <br>
	 *                               <u>Arguments (1)</u>: Graphics2D
	 * @param onMouseInputMethodData object with references to the methods that will
	 *                               be called by the mouse listener
	 * @param gameCore               user extended core of the game
	 */
	public Window(String name, int width, int height, int ticksPerSecond, String assetDirectory, boolean runVisualizer,
			ComplexInterface startMethod, ComplexInterface updateMethod, Consumer<Graphics2D> graphicsMethod,
			MouseInputMethodData onMouseInputMethodData, GameCore gameCore)
	{
		super(name);

		if (instance != null)
		{
			System.err.println("Two instances of basic2Dgraphics.Window are not allowed.");
			return;
		}

		Window.instance = this;
		if (runVisualizer)
			Window.visualizer = new StructureVisualizer();

		Window.startMethod = startMethod;
		Window.graphicsMethod = graphicsMethod;
		Window.updateMethod = updateMethod;

		Window.onMouseInputMethodData = onMouseInputMethodData;

		Window.assetDirectory = assetDirectory;

		Window.tps = ticksPerSecond;

		Window.gfx = new CustomGraphics();
		this.add(gfx);

		this.addMouseListener(new MouseInput());

		onConstructed(this, width, height, gameCore);
	}

	/**
	 * Window constructer with <b>no update method, and therefore no input</b>. <br>
	 * Calls the start method and graphics method <u>one time each</u>.
	 * 
	 * @param name           name of the window
	 * @param width          width of the window
	 * @param height         height of the window
	 * @param assetDirectory directory of assets: <u>images to use in graphics,
	 *                       audio files, etc...</u>
	 * @param runVisualizer  whether or not to run a structure visualizer
	 * @param startMethod    reference to the method that will be called once on
	 *                       start <br>
	 *                       <BLOCKQUOTE><u>Returns:</u> void <br>
	 *                       <u>Arguments</u>: None
	 * @param graphicsMethod reference to the method that will draw graphics <br>
	 *                       <BLOCKQUOTE><u>Returns:</u> void <br>
	 *                       <u>Arguments (1)</u>: Graphics2D
	 * @param gameCore       user extended core of the game
	 */
	public Window(String name, int width, int height, String assetDirectory, boolean runVisualizer,
			ComplexInterface startMethod, Consumer<Graphics2D> graphicsMethod, GameCore gameCore)
	{
		super(name);

		if (instance != null)
		{
			System.err.println("Two instances of basic2Dgraphics.Window are not allowed.");
			return;
		}

		Window.instance = this;
		if (runVisualizer)
			Window.visualizer = new StructureVisualizer();

		Window.startMethod = startMethod;
		Window.graphicsMethod = graphicsMethod;

		Window.assetDirectory = assetDirectory;

		Window.tps = -1;

		Window.gfx = new CustomGraphics();
		this.add(gfx);

		onConstructed(this, width, height, gameCore);
	}

	/**
	 * Consolidates all code that is used by all of the constructors.
	 * 
	 * @param frame  reference to the {@code Window}.
	 * @param width  width of the window
	 * @param height height of the window
	 */
	private static void onConstructed(JFrame frame, int width, int height, GameCore gameCore)
	{
		frame.setPreferredSize(new Dimension(width, height));
		frame.setMaximumSize(new Dimension(width, height));
		frame.setMinimumSize(new Dimension(width, height));

		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);

		try
		{
			String[] files = new File(Window.assetDirectory + "sprites/").list();
			for (String file : files)
			{
				if (!SpriteLoader.loadImage(file))
					break;
			}
		}
		catch (Exception e)
		{
			System.err.println("Images could not be loaded.");
		}

		try
		{
			String[] files = new File(Window.assetDirectory + "audio/").list();
			for (String file : files)
			{
				if (!AudioLoader.loadAudio(file))
					break;
			}
		}
		catch (Exception e)
		{
			System.err.println("Audio files could not be loaded.");
		}

		Window.worldParent = new GameObject("WorldParent", null);

		Window.gameLoop = new GameLoop();
		Window.thread = new Thread(gameLoop);
		Window.thread.start();
	}

	/**
	 * Class for storing all method references used by the {@code KeyboardListener}.
	 * All methods return void and take a {@code KeyEvent}.
	 * 
	 * @author Dylan Raiff
	 */
	public final static class KeyboardInputMethodData
	{
		/**
		 * Called when a key has been pressed if not {@code null}.
		 */
		public ComplexKeyEventInterface onKeyPressed;

		/**
		 * Called when a key has been typed if not {@code null}.
		 */
		public ComplexKeyEventInterface onKeyTyped;

		/**
		 * Called when a key has been released if not {@code null}.
		 */
		public ComplexKeyEventInterface onKeyReleased;

		/**
		 * Assigns method refences.
		 * 
		 * @param onKeyPressed  method called on press
		 * @param onKeyTyped    method called on type
		 * @param onKeyReleased method called on release
		 */
		public KeyboardInputMethodData(ComplexKeyEventInterface onKeyPressed, ComplexKeyEventInterface onKeyTyped,
				ComplexKeyEventInterface onKeyReleased)
		{
			this.onKeyPressed = onKeyPressed;
			this.onKeyTyped = onKeyTyped;
			this.onKeyReleased = onKeyReleased;
		}

		/**
		 * @return true if all methods are null, false otherwise
		 */
		public boolean allVoid()
		{ return (onKeyPressed == null && onKeyTyped == null && onKeyReleased == null); }
	}

	/**
	 * Class for storing all method references used by the {@code MouseListener}.
	 * All methods return void and take a {@code MouseEvent}.
	 * 
	 * @author Dylan Raiff
	 */
	public final static class MouseInputMethodData
	{
		/**
		 * Called when the user clicks the listened to component if not {@code null}.
		 */
		public ComplexMouseEventInterface onMouseClicked;

		/**
		 * Called when the cursor enters the bounds of the listened to component if not
		 * {@code null}.
		 */
		public ComplexMouseEventInterface onMouseEntered;

		/**
		 * Called when the cursor exits the bounds of the listened to component if not
		 * {@code null}.
		 */
		public ComplexMouseEventInterface onMouseExited;

		/**
		 * Called when the user presses a mouse button while the cursor is over the
		 * listened to component if not {@code null}.
		 */
		public ComplexMouseEventInterface onMousePressed;

		/**
		 * Called when the user releases a mouse button after a mouse press over the
		 * listened to component if not {@code null}.
		 */
		public ComplexMouseEventInterface onMouseReleased;

		/**
		 * Assigns method refences.
		 * 
		 * @param onMouseClicked  method called on click
		 * @param onMouseEntered  method called on enter
		 * @param onMouseExited   method called on exit
		 * @param onMousePressed  method called on press
		 * @param onMouseReleased method called on release
		 */
		public MouseInputMethodData(ComplexMouseEventInterface onMouseClicked,
				ComplexMouseEventInterface onMouseEntered, ComplexMouseEventInterface onMouseExited,
				ComplexMouseEventInterface onMousePressed, ComplexMouseEventInterface onMouseReleased)
		{
			this.onMouseClicked = onMouseClicked;
			this.onMouseEntered = onMouseEntered;
			this.onMouseExited = onMouseExited;
			this.onMousePressed = onMousePressed;
			this.onMouseReleased = onMouseReleased;
		}

		/**
		 * @return true if all methods are null, false otherwise
		 */
		public boolean allVoid()
		{
			return (onMouseClicked == null && onMouseEntered == null && onMouseExited == null && onMousePressed == null
					&& onMouseReleased == null);
		}
	}

	/**
	 * Stores a method.<br>
	 * <BLOCKQUOTE> <u>Returns:</u> void <br>
	 * <u>Arguments:</u> None </BLOCKQUOTE>
	 * 
	 * @author Dylan Raiff
	 * @version 1.0
	 */
	@FunctionalInterface
	public static interface ComplexInterface
	{
		/**
		 * Stored method of the {@code ComplexInterface}.
		 */
		public void method();
	}

	/**
	 * Stores a method.<br>
	 * <BLOCKQUOTE> <u>Returns:</u> void <br>
	 * <u>Arguments (1):</u> KeyEvent </BLOCKQUOTE>
	 * 
	 * @author Dylan Raiff
	 * @version 1.0
	 */
	@FunctionalInterface
	public static interface ComplexKeyEventInterface
	{
		/**
		 * Stored method of the {@code ComplexKeyEventInterface}.
		 */
		public void method(KeyEvent e);
	}

	/**
	 * Stores a method.<br>
	 * <BLOCKQUOTE> <u>Returns:</u> void <br>
	 * <u>Arguments (1):</u> MouseEvent </BLOCKQUOTE>
	 * 
	 * @author Dylan Raiff
	 * @version 1.0
	 */
	@FunctionalInterface
	public static interface ComplexMouseEventInterface
	{
		/**
		 * Stored method of the {@code ComplexMouseEventInterface}.
		 */
		public void method(MouseEvent e);
	}

	/* Access methods */

	/**
	 * @return singleton {@code Window} variable
	 */
	public static Window getInstance()
	{ return Window.instance; }

	/**
	 * @return main {@code GameCore} variable
	 */
	public static GameCore getGameCore()
	{ return Window.gameCore; }

	/**
	 * @return {@code GameObject} that all others are desendant from
	 */
	public static GameObject getWorldParent()
	{ return Window.worldParent; }

	/**
	 * @return {@code StructureVisualizer} of the instance of the engine that is
	 *         running
	 */
	public static StructureVisualizer getStructureVisualizer()
	{ return Window.visualizer; }

	/**
	 * @return ticks per second of the engine
	 */
	public static int getTPS()
	{ return Window.tps; }

	/**
	 * @return {@code CustomGraphics} object of the engine
	 */
	public static CustomGraphics getGFX()
	{ return Window.gfx; }

	/**
	 * @return main game loop
	 */
	public static GameLoop getGameLoop()
	{ return Window.gameLoop; }

	/**
	 * @return method called once upon start
	 */
	public static ComplexInterface getStartMethod()
	{ return Window.startMethod; }

	/**
	 * @return method called every tick
	 */
	public static ComplexInterface getUpdateMethod()
	{ return Window.updateMethod; }

	/**
	 * @return method that handles graphics
	 */
	public static Consumer<Graphics2D> getGraphicsMethod()
	{ return Window.graphicsMethod; }

	/**
	 * @return variable to store method referenes to be used on keyboard input
	 */
	public static KeyboardInputMethodData getOnKeyboardInputMethodData()
	{ return Window.onKeyboardInputMethodData; }

	/**
	 * @return variable to store method referenes to be used on mouse input
	 */
	public static MouseInputMethodData getOnMouseInputMethodData()
	{ return Window.onMouseInputMethodData; }

	/**
	 * @return directory of the engine's assets
	 */
	public static String getAssetDirectory()
	{ return Window.assetDirectory; }

	/**
	 * @return main thread of the engine
	 */
	public static Thread getThread()
	{ return Window.thread; }

}
