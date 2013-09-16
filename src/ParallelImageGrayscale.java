import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/*
 * Author: R.Bietin
 * Date: 14.09.13
 * Time: 0:37
 */
public class ParallelImageGrayscale
{
	private Integer threadNum;
	private String filePath;
	private BufferedImage inImage;
	private BufferedImage outImage;

	public ParallelImageGrayscale(Integer threadNum, String filepath)
	{
		assert (0 < threadNum);
		this.threadNum = threadNum;
		this.filePath = filepath;
		this.inImage = null;
		readImage();
		this.outImage = new BufferedImage(this.inImage.getWidth(), this.inImage.getHeight(), BufferedImage.TYPE_INT_RGB);
	}

	private void readImage()
	{
		File f = new File(this.filePath);
		if (!f.exists())
			throw new IllegalArgumentException("no such file \"" + this.filePath + "\"");
		try
		{
			inImage = ImageIO.read(f);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void writeImage()
	{
		String outFilePath = this.filePath;
		int dotIndex = outFilePath.length();
		for (int i = outFilePath.length() - 1; i > 0; i--)
		{
			if ('.' == outFilePath.charAt(i))
			{
				dotIndex = i;
				continue;
			}
			if (('\\' == this.filePath.charAt(i)) || ('/' == this.filePath.charAt(i)))
			{
				outFilePath = outFilePath.substring(0, dotIndex) + "_gray" + outFilePath.substring(dotIndex, outFilePath.length());
				try
				{
					ImageIO.write(this.outImage, this.filePath.substring(dotIndex + 1, this.filePath.length()), new File(outFilePath));
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				return;
			}
		}
	}

	public void process()
	{
		Thread threads[] = new Thread[this.threadNum];
		Integer stepSize = this.inImage.getHeight() / this.threadNum;
		Integer startLine = 0;
		for (int i = 0; i < threadNum - 1; i++)
		{
			threads[i] = new Thread(new GrayscaleConverter(this.inImage, this.outImage, startLine, startLine + stepSize));
			startLine += stepSize;
		}
		threads[threadNum - 1] = new Thread(new GrayscaleConverter(this.inImage, this.outImage, startLine, this.inImage.getHeight() - 1));

		for (Thread t : threads)
			t.start();

		for (Thread t : threads)
			try
			{
				t.join();
			} catch (InterruptedException ignored)
			{
			}
	}
}
