package Services;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import static Managers.SimulationLifecycleManager.skin;

public class DialogService {
    public void showDialog(Stage stage, String message) {
        com.badlogic.gdx.scenes.scene2d.ui.Dialog dialog = new com.badlogic.gdx.scenes.scene2d.ui.Dialog("", skin);

        // Create label with customized height
        Label label = new Label(message, skin);
        label.setWrap(true); // Enable wrapping if the text is too long
        label.setHeight(80f); // Change this value to set the desired height

        // Add label to the dialog's content table
        dialog.getContentTable().add(label).width(700f).pad(20f);

        dialog.button("OK");
        dialog.show(stage);
    }

    public Dialog showNoDialog(Stage stage, String message) {
        Dialog dialog = new Dialog("", skin);

        Label label = new Label(message, skin);
        label.setWrap(true);
        label.setHeight(80f); // Custom height

        dialog.getContentTable().add(label).width(700f).pad(20f);

        dialog.show(stage);

        return dialog; // Return the dialog instance
    }
}
