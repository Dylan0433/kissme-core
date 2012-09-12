package com.kissme.core.helper;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;

import com.kissme.lang.Files;
import com.kissme.lang.IOs;
import com.kissme.lang.Lang;
import com.kissme.lang.Preconditions;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 
 * @author loudyn
 * 
 */
public class PhotoHelper {

	/**
	 * 
	 * @param in
	 * @param compress
	 * @param compressQuality
	 */
	public static void compress(InputStream in, String compress, float compressQuality) {
		BufferedImage image = null;
		try {

			image = ImageIO.read(in);
			compress(image, compress, compressQuality);

		} catch (Exception e) {
			throw Lang.uncheck(e);
		}
	}

	private static void compress(BufferedImage image, String compress, float compressQuality) {
		Preconditions.notNull(image, "image must not null");
		Preconditions.hasText(compress, "compress must not blank");

		OutputStream out = null;
		try {
			out = new FileOutputStream(compress);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			JPEGEncodeParam encodeParam = encoder.getDefaultJPEGEncodeParam(image);
			encodeParam.setQuality(compressQuality, true);
			encoder.encode(image, encodeParam);
		} catch (Exception e) {
			throw Lang.uncheck(e);
		} finally {
			IOs.freeQuietly(out);
		}
	}

	/**
	 * 
	 * @param in
	 * @param thumbnail
	 * @param width
	 * @param height
	 */
	public static void createThumbnail(InputStream in, String thumbnail, int width, int height) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(in);

			int imageWidth = image.getWidth();
			int imageHeight = image.getHeight();
			double ratioV = (double) width / (double) imageWidth;
			double ratioH = (double) height / (double) imageHeight;

			if (ratioV >= 1.0 && ratioH >= 1.0) {
				createThumbnailInternal(image, thumbnail, imageWidth, imageHeight);
				return;
			}

			if (ratioV <= 0 || ratioH <= 0) {
				createThumbnailInternal(image, thumbnail, imageWidth, imageHeight);
				return;
			}

			double ratio = Math.min(ratioH, ratioV);
			width = (int) (ratio * imageWidth);
			height = (int) (ratio * imageHeight);

			createThumbnailInternal(image, thumbnail, width, height);
		} catch (Exception e) {
			throw Lang.uncheck(e);
		}
	}

	private static void createThumbnailInternal(BufferedImage image, String thumbnail, int imageWidth, int imageHeight) throws IOException {
		Preconditions.hasText(thumbnail, "thumbnail must has text");

		BufferedImage thumb = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = thumb.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(image, 0, 0, imageWidth, imageHeight, null);
		g.dispose();

		String format = Files.suffix(thumbnail);
		ImageIO.write(thumb, StringUtils.isBlank(format) ? "jpg" : format, new File(thumbnail));
	}

	/**
	 * 
	 * @param dest
	 * @param watermark
	 */
	public static void watermark(File dest, File watermark) {
		BufferedImage destImage = null;

		try {

			destImage = ImageIO.read(dest);
			BufferedImage watermarkImage = ImageIO.read(watermark);

			int destImageWidth = destImage.getWidth();
			int destImageHeight = destImage.getHeight();

			int pointX = Math.max(destImageWidth - watermarkImage.getWidth(), 0);
			int pointY = Math.max(destImageHeight - watermarkImage.getHeight(), 0);
			int width = Math.min(destImageWidth, watermarkImage.getWidth());
			int height = Math.min(destImageHeight, watermarkImage.getHeight());

			Graphics g = destImage.getGraphics();
			g.drawImage(watermarkImage, pointX, pointY, width, height, null);
			// must dispose
			g.dispose();

			String format = Files.suffix(dest.getName());
			ImageIO.write(destImage, StringUtils.isBlank(format) ? "jpg" : format, dest);
		} catch (IOException e) {
			throw Lang.uncheck(e);
		}
	}

	/**
	 * 
	 * @param dest
	 * @param watermark
	 */
	public static void watermark(String dest, String watermark) {
		watermark(new File(dest), new File(watermark));
	}
}
