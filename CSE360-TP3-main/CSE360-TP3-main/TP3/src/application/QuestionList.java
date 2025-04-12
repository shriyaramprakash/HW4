package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.*;
import java.util.stream.Collectors;


public class QuestionList {
    private static final List<QuestionAnswer> questionAnswers = new ArrayList<>();
	
    private final StudentReviewTracker studentTracker = new StudentReviewTracker(); // dynamic per logged-in student
    private final ReviewReplyTracker replyTracker = new ReviewReplyTracker();


    public void show(Stage primaryStage) {
        VBox layout = new VBox(5);
        
        // Create scrollable lists
        TextField searchField = new TextField();
        ListView<String> questionListView = new ListView<>();
        ListView<String> answerListView = new ListView<>();
        TextField answerField = new TextField();
        ListView<String> unansweredListView = new ListView<>();
        TextField replyField = new TextField();
        ListView<String> replyListView = new ListView<>();
        
        // Search button for questions
        Button searchButton = new Button("Search");
        searchButton.setOnAction(a -> searchQuestions(searchField.getText(), questionListView));
        
        Button editButton = new Button("Edit Question");
        editButton.setOnAction(a -> editQuestion(questionListView));

        Button editAnswerButton = new Button("Edit Answer");
        editAnswerButton.setOnAction(a -> editAnswer(questionListView, answerListView));
        
        // Submit answer button
        Button answerButton = new Button("Submit Answer");
        answerButton.setOnAction(a -> addAnswer(questionListView, answerField, answerListView, unansweredListView));
        
        // Like buttons
        Button likeButton = new Button("Like Selected Answer");
        likeButton.setOnAction(a -> likeAnswer(questionListView, answerListView));
        
        // Unanswered questions button
        Button showUnansweredButton = new Button("Show Unanswered Questions");
        showUnansweredButton.setOnAction(a -> showUnansweredQuestions(unansweredListView));
	// Review as viewed
	Button viewReviewButton = new Button("Mark Review as Viewed");
viewReviewButton.setOnAction(a -> {
    String selectedQuestion = questionListView.getSelectionModel().getSelectedItem();
    if (selectedQuestion != null) {
        for (QuestionAnswer qa : questionAnswers) {
            if (qa.getQuestion().equals(selectedQuestion)) {
                List<Review> reviews = qa.getReviews();
                if (!reviews.isEmpty()) {
                    int reviewId = reviews.get(0).getId(); // Simulating viewing first review
                    studentTracker.markReviewAsViewed(reviewId);
                    System.out.println("Marked review " + reviewId + " as viewed by " + studentTracker.getStudentName());
                }
                break;
            }
        }
    }
});

Button checkReplyButton = new Button("Check for Reviewer Reply");
checkReplyButton.setOnAction(a -> {
    String selectedQuestion = questionListView.getSelectionModel().getSelectedItem();
    if (selectedQuestion != null) {
        for (QuestionAnswer qa : questionAnswers) {
            if (qa.getQuestion().equals(selectedQuestion)) {
                List<Review> reviews = qa.getReviews();
                if (!reviews.isEmpty()) {
                    int reviewId = reviews.get(0).getId();
                    boolean replied = replyTracker.hasReply(reviewId, studentTracker);
                    if (replied) {
                        System.out.println("Reviewer has replied.");
                    } else {
                        System.out.println("No reply yet.");
                    }
                }
            }
        }
    }
});

        
        Button replyTo = new Button("Reply"); 
        Label correct = new Label();
        
        replyTo.setOnAction(a -> {
        reply(replyListView, replyField.getText().trim());
        String studentR = replyField.getText(); // Store question typed
        correct.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        if (!studentR.trim().isEmpty()) { // If not empty store question and print success
            replyField.clear(); // Clear text field
            correct.setText("Reply Sent!"); // Output message
            }
        else {
        	// If submitted question is empty
        	correct.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        	correct.setText("No reply asked");
    	}
        }); 
        
        Button goHome = new Button ("Return Home");
        	goHome.setOnAction(a ->  {
        		
        		new UserHomePage(null).show(primaryStage);
        		
        	});
        
        

       layout.getChildren().addAll(
		    new Label("Search"), searchField, searchButton, questionListView,
		    new Label("Answers"), answerListView, answerField, answerButton, likeButton,
		    new Label("Unanswered Questions"), showUnansweredButton, unansweredListView,
		    new Label("Send message to user"), replyField, replyTo, correct,
    viewReviewButton, checkReplyButton, goHome
);

        primaryStage.setScene(new Scene(layout, 800, 500));
        primaryStage.setTitle("Question List");
    }
    
    // Adds question to list from Question class
    public static void addQuestion(String question, User user) {
        questionAnswers.add(new QuestionAnswer(question));
    }
    
    // Searches for similar questions 
    private void searchQuestions(String query, ListView<String> questionListView) {
        List<String> results = new ArrayList<>();
        for (QuestionAnswer qa : questionAnswers) { // iterate through questions list
            if (qa.getQuestion().toLowerCase().contains(query.toLowerCase())) { // If questions list contains words similar to question
                results.add(qa.getQuestion()); // Add similar questions to list to return
            }
        }
        questionListView.getItems().setAll(results);
    }
    
    //ask clarification
    private void reply(ListView<String> questionListView, String replyFieldText) {
    String questionText = questionListView.getSelectionModel().getSelectedItem();
    if (questionText != null && !replyFieldText.isEmpty()) {
        for (QuestionAnswer qa : questionAnswers) {
            if (qa.getQuestion().equals(questionText)) {
                List<Review> reviews = qa.getReviews();
                if (!reviews.isEmpty()) {
                    int reviewId = reviews.get(0).getId(); // simulate messaging first review
                    replyTracker.sendMessage(reviewId, studentTracker, replyFieldText);
                    System.out.println("Message sent for review " + reviewId);
                }
            }
        }
    }
}

    
    // Add answer
    private void addAnswer(ListView<String> questionListView, TextField answerField, ListView<String> answerListView, ListView<String> unansweredListView) {
        String question = questionListView.getSelectionModel().getSelectedItem(); // Get selected question from user
        String answer = answerField.getText().trim(); // Get rid of extra spaces
        // If not empty, add answer to answer box
        if (question != null && !answer.isEmpty()) {
            for (QuestionAnswer qa : questionAnswers) {
                if (qa.getQuestion().equals(question)) {
                    qa.addAnswer(answer);
                    break;
                }
            }
            answerField.clear(); // clear textfield
            updateAnswers(question, answerListView);
            showUnansweredQuestions(unansweredListView);
        }
    }
    
 // Edit question function
    private void editQuestion(ListView<String> questionListView) {
        String selectedQuestion = questionListView.getSelectionModel().getSelectedItem(); // Get selected question
        if (selectedQuestion != null) {
            TextInputDialog dialog = new TextInputDialog(selectedQuestion);
            dialog.setTitle("Edit Question");
            dialog.setHeaderText("Modify your question:");
            dialog.setContentText("New Question:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(newQuestion -> {
                for (QuestionAnswer qa : questionAnswers) {
                    if (qa.getQuestion().equals(selectedQuestion)) {
                        qa.setQuestion(newQuestion); // Update question text
                        break;
                    }
                }
                updateQuestions(questionListView);
            });
        }
    }

    // Edit answer function
    private void editAnswer(ListView<String> questionListView, ListView<String> answerListView) {
        String selectedQuestion = questionListView.getSelectionModel().getSelectedItem();
        int selectedIndex = answerListView.getSelectionModel().getSelectedIndex();

        if (selectedQuestion != null && selectedIndex >= 0) {
            for (QuestionAnswer qa : questionAnswers) {
                if (qa.getQuestion().equals(selectedQuestion)) {
                    String currentAnswer = qa.getAnswers().get(selectedIndex).getText();
                    TextInputDialog dialog = new TextInputDialog(currentAnswer);
                    dialog.setTitle("Edit Answer");
                    dialog.setHeaderText("Modify the answer:");
                    dialog.setContentText("New Answer:");

                    Optional<String> result = dialog.showAndWait();
                    result.ifPresent(newAnswer -> {
                        qa.editAnswer(selectedIndex, newAnswer);
                        updateAnswers(selectedQuestion, answerListView);
                    });
                    break;
                }
            }
        }
    }

    // Update question list after editing
    private void updateQuestions(ListView<String> questionListView) {
        List<String> questions = new ArrayList<>();
        for (QuestionAnswer qa : questionAnswers) {
            questions.add(qa.getQuestion());
        }
        questionListView.getItems().setAll(questions);
    }
    
    // Like answers
    private void likeAnswer(ListView<String> questionListView, ListView<String> answerListView) {
        String question = questionListView.getSelectionModel().getSelectedItem(); // Get selected answer
        int selectedIndex = answerListView.getSelectionModel().getSelectedIndex(); // Create likes for answer
        // If not empty, increase likes
        if (question != null && selectedIndex >= 0) {
            for (QuestionAnswer qa : questionAnswers) {
                if (qa.getQuestion().equals(question)) {
                    qa.increaseLikes(selectedIndex);
                    break;
                }
            }
            updateAnswers(question, answerListView);
        }
    }
    
    // Update answers
    private void updateAnswers(String question, ListView<String> answerListView) {
        for (QuestionAnswer qa : questionAnswers) { // iterate through list
            if (qa.getQuestion().equals(question)) { // If searched question equals question in list
                answerListView.getItems().clear();
                answerListView.getItems().addAll(qa.getFormattedAnswers());
                break;
            }
        }
    }
    
    // Unanswered questions function
    private void showUnansweredQuestions(ListView<String> unansweredListView) {
        List<String> unanswered = new ArrayList<>();
        for (QuestionAnswer qa : questionAnswers) { // If question is unanswered, add to list
            if (qa.isUnanswered()) {
                unanswered.add(qa.getQuestion());
            }
        }
        unansweredListView.getItems().setAll(unanswered); // Get list of unanswered questions
    }
    
    // Class to get questions and answers
    static class QuestionAnswer {
        private String question;
	private final String id;
        private final List<Answer> answers = new ArrayList<>();

	// Set question and generate random unique ID

        public QuestionAnswer(String question) {

            this.id = UUID.randomUUID().toString(); // Generate a random unique ID

            this.question = question;

        }

	     // returns question

        public String getQuestion() {

            return question;

        }



        // returns the unique ID

        public String getId() {

            return id;

        }



        // Answer gets added to list

        public void addAnswer(String answer) {

            answers.add(new Answer(answer));

        }

        

        // Increases likes

        public void increaseLikes(int index) {

            if (index >= 0 && index < answers.size()) {

                answers.get(index).increaseLikes();

            }

        }

        

        // Formats likes

        public List<String> getFormattedAnswers() {

            List<String> formattedAnswers = new ArrayList<>();

            for (Answer a : answers) {

                formattedAnswers.add(a.getText() + " (Likes: " + a.getLikes() + ")");

            }

            return formattedAnswers;

        }

        

        // Returns unanswered questions

        public boolean isUnanswered() {

            return answers.isEmpty();

        }
        
        // Set question
        public QuestionAnswer(String question) {
            this.question = question;
        }

        public void increaseLikes(int selectedIndex) {
			// TODO Auto-generated method stub
			
		}

		public String getQuestion() {
            return question;
        }

        public void setQuestion(String newQuestion) {
            this.question = newQuestion; // Allow editing the question
        }

        public List<Answer> getAnswers() {
            return answers;
        }

        public void addAnswer(String answer) {
            answers.add(new Answer(answer));
        }

        public void editAnswer(int index, String newAnswer) {
            if (index >= 0 && index < answers.size()) {
                answers.get(index).setText(newAnswer);
            }
        }

        public List<String> getFormattedAnswers() {
            List<String> formattedAnswers = new ArrayList<>();
            for (Answer a : answers) {
                formattedAnswers.add(a.getText() + " (Likes: " + a.getLikes() + ")");
            }
            return formattedAnswers;
        }
        
        // Returns unanswered questions
        public boolean isUnanswered() {
            return answers.isEmpty();
        }

        private final List<Review> reviews = new ArrayList<>();

        public void addReview(String content, String reviewerName) {
            reviews.add(new Review(content, reviewerName));
        }

        public void editReview(int reviewId, String newContent) {
            for (Review r : reviews) {
                if (r.getId() == reviewId) {
                    r.setContent(newContent);
                    break;
                }
            }
        }

        public void deleteReview(int reviewId) {
            reviews.removeIf(r -> r.getId() == reviewId);
        }

        public List<Review> getReviews() {
            return reviews;
        }

        
        public List<String> getSortedReviewStrings(User student) {
            return reviews.stream()
                .sorted((r1, r2) -> {
                    int w1 = student.getReviewerWeight(r1.getReviewerName());
                    int w2 = student.getReviewerWeight(r2.getReviewerName());
                    return Integer.compare(w2, w1);
                })
                .map(r -> r.getId() + " | " + r.getReviewerName() + ": " + r.getContent())
                .collect(Collectors.toList());
        }
       
    }

    // Class for getting answers
    private static class Answer {
        private String text;
        private int likes;

        public Answer(String text) {
            this.text = text;
            this.likes = 0;
        }

        public String getText() {
            return text;
        }

        public void setText(String newText) {
            this.text = newText; // Allow editing answer text
        }

        public int getLikes() {
            return likes;
        }
/*
        public void increaseLikes() {
            likes++;
        }
    }
  */  
        /*
	public Question getQuestionById(int questionId) {
		// TODO Auto-generated method stub
		return null;
	}
 */ 

    }

    public static List<QuestionAnswer> getAllQuestions() {
        return questionAnswers;
    }

 public QuestionAnswer getQuestionById(String questionId) {

        for (QuestionAnswer qa : questionAnswers) {

            if (qa.getId().equals(questionId)) {

                return qa;

            }

        }

        return null;

    }

}
