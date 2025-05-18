import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.IllegalArgumentException;
import java.io.File;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Color;

// for some reason I cannot directly access Rectangle2D.Double with import
import java.awt.geom.*;

public class SlideExtractor {
	public static void usage() {
		System.out.println("Usage: slideextractor <filename> <output_image_format> <start_slide> <end_slide>");
	}

	public static void extractSlidesInRange(String filePath, String outputFormat, int startSlide, int endSlide) throws IOException, IllegalArgumentException {
		try (FileInputStream is = new FileInputStream(filePath)) {
			XMLSlideShow pptx = new XMLSlideShow(is);

			Dimension pgsize = pptx.getPageSize();

			if (startSlide < 1 || startSlide > endSlide || endSlide > pptx.getSlides().size()) {
				throw new IllegalArgumentException("Invalid slide range");
			}

			for (int i = startSlide - 1; i < endSlide; i++) {
				XSLFSlide slide = pptx.getSlides().get(i);

				BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);
				Graphics2D graphics = img.createGraphics();

				// clear the drawing area
				graphics.setPaint(Color.white);
				graphics.fill(new Rectangle2D.Double(0, 0, pgsize.width, pgsize.height));

				// render
				slide.draw(graphics);

				// save the output
				System.out.println("Saving slide " + (i + 1) + "...");
				FileOutputStream out = new FileOutputStream("slide-" + (i + 1) + "." + outputFormat);
				javax.imageio.ImageIO.write(img, outputFormat, out);
				out.close();
			}
		} 
		catch (FileNotFoundException e) {
			System.err.println("Could not locate: " + e.getMessage());
		}
	}

	public static void main (String[] args) throws IOException, IllegalArgumentException {
 		if (args.length == 0) {
			usage();
			return;
		}

		System.out.println(args[0]);
		System.out.println("User Working Dir: " + System.getProperty("user.dir"));
		String classpath = System.getProperty("java.class.path");
		String[] classpathEntries = classpath.split(File.pathSeparator);

		// Using the process outlined on Apache POI tutorial pages, but for pptx files
		try(FileInputStream is = new FileInputStream(args[0])) {
			XMLSlideShow pptx = new XMLSlideShow(is);

			Dimension pgsize = pptx.getPageSize();

			int startSlide = 1;
			int endSlide = Integer.MAX_VALUE;
			int idx = 1;
			String output_image_format = "png";
			if (args.length == 2) {
				output_image_format = args[1].toLowerCase();
			}

			if (!output_image_format.equals("png") && !output_image_format.equals("jpeg") && !output_image_format.equals("gif")) {
				throw new IllegalArgumentException("Invalid output format" + output_image_format);
			}
			if (args.length == 4) {
				startSlide = Integer.parseInt(args[2]);
				endSlide = Integer.parseInt(args[3]);
				extractSlidesInRange(args[0], output_image_format, startSlide, endSlide);
			}
			else {
				for (XSLFSlide slide : pptx.getSlides()) {
					BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);
					Graphics2D graphics = img.createGraphics();

					// clear the drawing area
					graphics.setPaint(Color.white);
					graphics.fill(new Rectangle2D.Double(0, 0, pgsize.width, pgsize.height));

					//render
					slide.draw(graphics);

					//save the output
					System.out.println("Saving slide" + idx + "...");
					FileOutputStream out = new FileOutputStream("slide-" + idx + "." + output_image_format);
					javax.imageio.ImageIO.write(img, output_image_format, out);
					out.close();

					idx = idx+1;
				}
			}
		}
		catch(FileNotFoundException e) {
			System.err.println("Could not locate: " + e.getMessage());
		}
    }
}
