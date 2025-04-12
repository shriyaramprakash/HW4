package application;

public class AcceptedAnswerManager {

    /**
     * Allows the author of the question to mark an answer as accepted.
     *
     * @param questionId The ID of the question.
     * @param answerId The ID of the answer to be accepted.
     * @param user The student making the request.
     * @return A success or failure message.
     */
    public String markAnswerAsAccepted(String questionId, String answerId, String user) {
        // Retrieve the question using the questionId
        QuestionList.QuestionAnswer question = new QuestionList().getQuestionById(questionId);

        // Check if the question exists
        if (question == null) {
            return "Error: Question not found.";
        }

        // Search for the answer using the answerId
        for (QuestionList.Answer answer : question.answers) {
            if (answer.getText().equals(answerId)) {
                answer.setAccepted(true);
                return "Success: Answer marked as accepted.";
            }
        }

        return "Error: Answer not found.";
    }
}
