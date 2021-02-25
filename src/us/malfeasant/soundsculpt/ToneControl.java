package us.malfeasant.soundsculpt;

import javafx.beans.property.DoubleProperty;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Slider;

public class ToneControl {
	private final DoubleProperty amplitude;	// range of -1.0 (invert) to +1.0
	
	private final Node pane;
	
	public ToneControl() {
		Slider amplitudeControl = new Slider(-1.0, 1.0, 0.0);
		amplitude = amplitudeControl.valueProperty();
		amplitudeControl.setOrientation(Orientation.VERTICAL);
		amplitudeControl.setShowTickMarks(true);
		amplitudeControl.setMajorTickUnit(0.25);
		amplitudeControl.setMinorTickCount(3);
		
		pane = amplitudeControl;	// if this gets more complex, a pane will fit in a node...
	}
	
	public Node getControls() {
		return pane;
	}
	
	public DoubleProperty getAmplitude() {
		return amplitude;
	}
}
