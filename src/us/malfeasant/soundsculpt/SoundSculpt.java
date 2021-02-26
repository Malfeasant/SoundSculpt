package us.malfeasant.soundsculpt;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SoundSculpt extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Canvas canv = new Canvas(640, 400);
		HBox toneBox = new HBox();
		toneBox.setAlignment(Pos.CENTER);
		
		ToneControl[] tones = new ToneControl[9];
		for (int i = 0; i < tones.length; i++) {
			ToneControl t = new ToneControl();
			tones[i] = t;
			toneBox.getChildren().add(t.getControls());
		}
		VBox pane = new VBox(canv, toneBox);
		pane.setFillWidth(true);
		VBox.setVgrow(toneBox, Priority.ALWAYS);
		
		Draw draw = new Draw(canv, tones);
		draw.start();
		
		Play play = new Play(tones);
		primaryStage.setOnCloseRequest(evt -> {
			play.stop();
		});
		primaryStage.setScene(new Scene(pane));
		primaryStage.show();
	}
}
