public class SlideExtractor {
	public static void usage() {
		System.out.println("Usage: slideextractor <filename>");
	}

	public static void main (String[] args) {
 		if (args.length == 0) {
			usage();
			return;
		}
		
		System.out.println(args[0]);
		return;
        }
}
