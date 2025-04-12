package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class InstructorReviewPage extends Application {

    @Override
    public void start(Stage primaryStage) {
        Label title = new Label("Instructor - Review Student's Answers");

        TextArea studentAnswers = new TextArea("Q: What is Java?\nA: Java is a programming language.\n\n...");
        studentAnswers.setEditable(false);

        Button approve = new Button("Approve Reviewer Access");
        Button deny = new Button("Deny Access");

        approve.setOnAction(e -> {
            System.out.println("Reviewer access approved.");
            // connect to database logic to update reviewer status
        });

        deny.setOnAction(e -> {
            System.out.println("Reviewer access denied.");
        });

        VBox layout = new VBox(10, title, studentAnswers, approve, deny);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(layout, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Instructor Approval");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
