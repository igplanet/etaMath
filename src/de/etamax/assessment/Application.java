/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.etamax.assessment;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author oghomwen.aigbedion
 */
public class Application {

    /**
     * @param args the command line arguments
     */
    static List<Task> tasks = new ArrayList<Task>();

    public static void main(String[] args) {
        // TODO code application logic here
    }

    static void playGame() {
//        while (true) {

        tasks.add(new Task("Welcome to etaMath!\nType 1 to start a game,\nType 2 to show highscores", "0"));
        tasks.add(new Task("4 * 12 = ?", "48"));
        tasks.add(new Task("18 + 42 = ?", "60"));
        tasks.add(new Task("7 * 5 = ?", "35"));
        tasks.add(new Task("3 * 9 = ?", "27"));
        tasks.add(new Task("60 - 42 = ?", "18"));
        tasks.add(new Task("9 + 17 = ?", "26"));
        tasks.add(new Task("55 + 16 = ?", "72"));
        tasks.add(new Task("26 + 34 = ?", "60"));
        tasks.add(new Task("27 / 9 = ?", "3"));
        tasks.add(new Task("35 / 5 = ?", "7"));
        tasks.add(new Task("26 - 9 = ?", "17"));
        tasks.add(new Task("77 / 7 = ?", "11"));
        tasks.add(new Task("60 - 34 = ?", "26"));
        tasks.add(new Task("48 / 4 = ?", "12"));
        tasks.add(new Task("14 + 7 = ?", "21"));
        tasks.add(new Task("11 * 7 = ?", "77"));
        tasks.add(new Task("72 - 55 = ?", "16"));
        tasks.add(new Task("6 * 3 = ?", "18"));
        tasks.add(new Task("18 / 6 = ?", "3"));
        tasks.add(new Task("21 - 7 = ?", "14"));

    }

    static class Task {

        String question;
        String answer;

        public Task(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }
    }

    static class Answer {

        String input;
        long time;

        public Answer(String input, long time) {
            this.input = input;
            this.time = time;
        }

        public String getInput() {
            return input;
        }

        public void setInput(String input) {
            this.input = input;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }
}
