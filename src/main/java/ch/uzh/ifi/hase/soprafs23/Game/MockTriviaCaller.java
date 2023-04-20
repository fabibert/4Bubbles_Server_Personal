package ch.uzh.ifi.hase.soprafs23.Game;

import ch.uzh.ifi.hase.soprafs23.Game.stateStorage.Question;

import java.util.List;

public class MockTriviaCaller {
    //create 1 Mock Trivia Question

    public List<Question> getTriviaQuestions() {
        Question triviaQuestion1 = new Question();
        triviaQuestion1.setQuestion("What is the largest Country in Africa by inhabitants?");
        triviaQuestion1.setAnswers(List.of("South Africa", "Congo", "Kenia", "Nigeria"));
        triviaQuestion1.setCorrectAnswer("Nigeria");
        Question triviaQuestion2 = new Question();
        triviaQuestion2.setQuestion("What is the largest Country in Europe by size?");
        triviaQuestion2.setAnswers(List.of("Germany", "Ukraine", "Spain", "Finland"));
        triviaQuestion2.setCorrectAnswer("Ukraine");
        List<Question> questions = List.of(triviaQuestion1, triviaQuestion2);
        return questions;
    }
}
