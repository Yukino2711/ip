package yqr.gui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Displays one chat message together with a compact speaker avatar.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private Label avatar;

    /**
     * Loads the reusable dialog layout and fills it with a message and avatar text.
     *
     * @param text message to display.
     * @param avatarText short label identifying the speaker.
     */
    private DialogBox(String text, String avatarText) {
        FXMLLoader loader = new FXMLLoader(DialogBox.class.getResource("/view/DialogBox.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load a dialog box", e);
        }

        dialog.setText(text);
        avatar.setText(avatarText);
        dialog.maxWidthProperty().bind(widthProperty().multiply(0.76));
    }

    /**
     * Creates a right-aligned dialog for a user command.
     *
     * @param text command entered by the user.
     * @return user dialog box.
     */
    public static DialogBox getUserDialog(String text) {
        DialogBox dialogBox = new DialogBox(text, "");
        dialogBox.getChildren().remove(dialogBox.avatar);
        dialogBox.getStyleClass().add("user-dialog");
        return dialogBox;
    }

    /**
     * Creates a left-aligned dialog for a chatbot response.
     *
     * @param text response produced by yqr.
     * @return chatbot dialog box.
     */
    public static DialogBox getYqrDialog(String text) {
        DialogBox dialogBox = new DialogBox(text, "YQR");
        dialogBox.formatAsYqrDialog();
        return dialogBox;
    }

    /**
     * Creates a highlighted left-aligned dialog for an error response.
     *
     * @param text error response produced by yqr.
     * @return highlighted error dialog box.
     */
    public static DialogBox getErrorDialog(String text) {
        DialogBox dialogBox = new DialogBox(text, "!");
        dialogBox.formatAsYqrDialog();
        dialogBox.dialog.getStyleClass().add("error-bubble");
        dialogBox.avatar.getStyleClass().add("error-avatar");
        return dialogBox;
    }

    /** Places the avatar before the response and applies chatbot-specific styles. */
    private void formatAsYqrDialog() {
        getChildren().setAll(avatar, dialog);
        setAlignment(Pos.TOP_LEFT);
        getStyleClass().add("yqr-dialog");
        dialog.getStyleClass().add("yqr-bubble");
        avatar.getStyleClass().add("yqr-avatar");
    }
}
