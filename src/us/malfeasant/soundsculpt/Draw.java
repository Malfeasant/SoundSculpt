package us.malfeasant.soundsculpt;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class Draw extends AnimationTimer {
	private final ToneControl[] controls;
	private final Canvas canvas;
	
	public Draw(Canvas canv, ToneControl[] controls) {
		this.controls = controls;
		this.canvas = canv;
	}
	@Override
	public void handle(long now) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
		PixelWriter pw = gc.getPixelWriter();
		
		double xScale = 2.0 * Math.PI / canvas.getWidth();
		double yScale = canvas.getHeight() / -3.5;
		double yShift = canvas.getHeight() / 2;
		for (int x = 0; x < canvas.widthProperty().intValue() ; x++) {
			double radians = x * xScale;
			double y = 0.0;
			for (int i = 0; i < controls.length; i++) {
				int ot = i + 1;
				ToneControl tc = controls[i];
				double scale = tc.getAmplitude().doubleValue();
				double ypart = Math.sin(radians * ot) / ot * scale;
				y += ypart;
				pw.setColor(x, (int) (ypart * yScale + yShift), Color.gray(1.0 - (1.0 / ot)));
			}
			pw.setColor(x, (int) (y * yScale + yShift), Color.BLUE);
		}
	}
}
