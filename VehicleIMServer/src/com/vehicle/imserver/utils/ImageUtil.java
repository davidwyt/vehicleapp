package com.vehicle.imserver.utils;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

public class ImageUtil {

	public static void scaleAndWaterPrint(File input, File output,
			File waterFile, int targetW, int targetH, String formatName)
			throws IOException {
		BufferedImage srcImg = ImageIO.read(input);
		BufferedImage waterImg = ImageIO.read(waterFile);

		BufferedImage tarImg = scale(srcImg, targetW, targetH, formatName);

		waterPrint(tarImg, waterImg, (targetW - waterImg.getWidth()) / 2,
				(targetH - waterImg.getHeight()) / 2);

		ImageIO.write(tarImg, formatName, output);
	}

	public static void scale(File input, File output, int targetW, int targetH,
			String formatName) throws IOException {

		InputStream fileInput = new FileInputStream(input);
		OutputStream fileOutput = new FileOutputStream(output);

		scale(fileInput, fileOutput, targetW, targetH, formatName);
	}

	public static void scale(InputStream input, OutputStream output,
			int targetW, int targetH, String formatName) throws IOException {
		BufferedImage sourceImg = ImageIO.read(input);

		BufferedImage targetImg = scale(sourceImg, targetW, targetH, formatName);

		ImageIO.write(targetImg, formatName, output);
	}

	public static BufferedImage scale(BufferedImage sourceImg, int targetW,
			int targetH, String formatName) {
		int type = sourceImg.getType();
		BufferedImage targetImg = null;
		double sx = (double) targetW / sourceImg.getWidth();
		double sy = (double) targetH / sourceImg.getHeight();

		if (type == BufferedImage.TYPE_CUSTOM) {
			ColorModel cm = sourceImg.getColorModel();
			WritableRaster raster = cm.createCompatibleWritableRaster(targetW,
					targetH);
			boolean alphaPremultiplied = cm.isAlphaPremultiplied();
			targetImg = new BufferedImage(cm, raster, alphaPremultiplied, null);
		} else {
			targetImg = new BufferedImage(targetW, targetH, type);
			Graphics2D g = targetImg.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY);
			g.drawRenderedImage(sourceImg,
					AffineTransform.getScaleInstance(sx, sy));
			g.dispose();
		}

		return targetImg;

	}

	public void waterPrint(File input, File output, File water, int x, int y,
			String formatName) throws IOException {

		InputStream inputStream = new FileInputStream(input);
		OutputStream outputStream = new FileOutputStream(output);
		InputStream waterStream = new FileInputStream(water);

		waterPrint(inputStream, outputStream, waterStream, x, y, formatName);
	}

	public void waterPrint(InputStream input, OutputStream output,
			InputStream water, int x, int y, String formatName)
			throws IOException {
		BufferedImage waterImg = ImageIO.read(water);
		BufferedImage sourceImg = ImageIO.read(input);

		waterPrint(sourceImg, waterImg, x, y);

		ImageIO.write(sourceImg, formatName, output);
	}

	public static void waterPrint(BufferedImage sourceImg,
			BufferedImage waterImg, int x, int y) {
		x = x > 0 ? x : 0;
		y = y > 0 ? y : 0;
		Graphics2D g = sourceImg.createGraphics();
		g.drawImage(waterImg, x, y, null);
		g.dispose();
	}
}
