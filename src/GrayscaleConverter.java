import java.awt.*;
import java.awt.image.BufferedImage;

public class GrayscaleConverter implements Runnable
{
	private BufferedImage aSrc;
	private BufferedImage aDst;
	private Integer aStartLine;
	private Integer aStopLine;

	public GrayscaleConverter(BufferedImage aSrc, BufferedImage aDst, Integer aStartLine, Integer aStopLine)
	{
		this.aSrc = aSrc;
		this.aDst = aDst;
		this.aStartLine = aStartLine;
		this.aStopLine = aStopLine;
	}

	public void run()
	{
		assert (aStartLine > 0);
		assert (aStopLine <= aSrc.getHeight());
		assert (aStopLine >= aStartLine);

		int data[] = aSrc.getRGB(0, aStartLine, this.aSrc.getWidth(), aStopLine - aStartLine + 1, null, 0, this.aSrc.getWidth());

		for (int i = 0; i < data.length; i++)
		{
			Color c = new Color(data[i]);
			int intensivity = (int) (0.2126 * c.getRed() + 0.7152 * c.getGreen() + 0.0722 * c.getBlue());
			data[i] = (new Color(
					intensivity,
					intensivity,
					intensivity
			)).getRGB();
		}

		aDst.setRGB(0, aStartLine, this.aSrc.getWidth(), aStopLine - aStartLine + 1, data, 0, this.aSrc.getWidth());
	}
}
