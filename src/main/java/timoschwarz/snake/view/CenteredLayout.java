package timoschwarz.snake.view;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;

import javax.swing.JPanel;

public class CenteredLayout implements LayoutManager2
{
	private JPanel panel;

	public CenteredLayout(JPanel panel)
	{
		this.panel = panel;
	}

	@Override
	public void addLayoutComponent(String name, Component comp)
	{

	}

	@Override
	public void layoutContainer(Container parent)
	{
		Component[] components = panel.getComponents();
		if (components.length == 0)
		{
			return;
		}

		Dimension parentSize = parent.getSize();

		Component component = components[0];
		Dimension compSize = component.getSize();

		int freeSpaceWidth = 0;
		int freeSpaceHeight = 0;

		freeSpaceHeight = (int) (parentSize.getHeight() - compSize.getHeight());
		freeSpaceWidth = (int) (parentSize.getWidth() - compSize.getWidth());

		freeSpaceHeight /= 2;
		freeSpaceWidth /= 2;

		component.setBounds(freeSpaceWidth, freeSpaceHeight, component.getWidth(), component.getHeight());
	}

	@Override
	public Dimension minimumLayoutSize(Container parent)
	{
		Dimension size = panel.getPreferredSize();
		size.setSize(size.getWidth() + 15, size.getHeight() + 15);
		return size;
	}

	@Override
	public Dimension preferredLayoutSize(Container parent)
	{
		Dimension size = panel.getPreferredSize();
		size.setSize(size.getWidth(), size.getHeight());
		return size;
	}

	@Override
	public void removeLayoutComponent(Component comp)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void addLayoutComponent(Component comp, Object constraints)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public float getLayoutAlignmentX(Container target)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getLayoutAlignmentY(Container target)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void invalidateLayout(Container target)
	{
		layoutContainer(target);
	}

	@Override
	public Dimension maximumLayoutSize(Container target)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
