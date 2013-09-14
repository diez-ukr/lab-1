public class Main
{
	public static void main(String[] args)
	{
		if (2 != args.length)
			throw new IllegalArgumentException("2 args required");

		ParallelImageGrayscale pig = new ParallelImageGrayscale(Integer.parseInt(args[0]), args[1]);
		long start = System.currentTimeMillis();
		pig.process();
		long stop = System.currentTimeMillis();
		System.out.println(args[0] + " thread(s) finished in " + (stop - start) + " ms.");
		pig.writeImage();
	}
}
