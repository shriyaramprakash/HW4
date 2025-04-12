package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.*;

public class ReviewerPage {

    public void show(Stage primaryStage, User reviewer) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        TextField questionField = new TextField();
        questionField.setPromptText("Enter question to review");

        TextArea reviewField = new TextArea();
        reviewField.setPromptText("Write your review here");

        Button submitButton = new Button("Submit Review");
        Button updateButton = new Button("Update Selected Review");
        Button deleteButton = new Button("Delete Selected Review");

        ListView<String> reviewList = new ListView<>();

        

        submitButton.setOnAction(e -> {
            String q = questionField.getText();
            String content = reviewField.getText();

            for (QuestionList.QuestionAnswer qa : QuestionList.getAllQuestions()) {
                if (qa.getQuestion().equals(q)) {
                    qa.addReview(content, reviewer.getUserName());
                    break;
                }
            }
            reviewField.clear();
        });

        updateButton.setOnAction(e -> {
            String q = questionField.getText();
            String selected = reviewList.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            int reviewId = Integer.parseInt(selected.split(" ")[0]); // assuming ID shown first

            for (QuestionList.QuestionAnswer qa : QuestionList.getAllQuestions()) {
                if (qa.getQuestion().equals(q)) {
                    qa.editReview(reviewId, reviewField.getText());
                    break;
                }
            }
        });

        deleteButton.setOnAction(e -> {
            String q = questionField.getText();
            String selected = reviewList.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            int reviewId = Integer.parseInt(selected.split(" ")[0]);

            for (QuestionList.QuestionAnswer qa : QuestionList.getAllQuestions()) {
                if (qa.getQuestion().equals(q)) {
                    qa.deleteReview(reviewId);
                    break;
                }
            }
        });

        layout.getChildren().addAll(new Label("Reviewer Interface"), questionField, reviewField, submitButton, updateButton, deleteButton, reviewList);
        primaryStage.setScene(new Scene(layout, 800, 500));
        primaryStage.setTitle("Reviewer Page");
    }
}
