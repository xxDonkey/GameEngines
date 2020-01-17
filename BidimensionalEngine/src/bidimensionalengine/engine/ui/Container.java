package bidimensionalengine.engine.ui;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Container extends UIElement
{
	private ArrayList<UIElement> children;

	public Container(String name, Container parent)
	{
		super(name, parent);
		children = new ArrayList<UIElement>();
	}

	public boolean addElement(Class<?> type, String name)
	{
		Object obj = null;
		try
		{
			obj = type.getConstructors()[0].newInstance(name, this);
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| SecurityException e)
		{
			e.printStackTrace();
		}

		if (obj != null && obj instanceof UIElement)
		{
			children.add((UIElement) obj);
			return true;
		}
		return false;
	}

	public void removeElement(UIElement element)
	{ children.remove(element); }
}