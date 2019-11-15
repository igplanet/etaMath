/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.etamax.assessment;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author oghomwen.aigbedion
 */
public class Application {

    /**
     * @param args the command line arguments
     */
    static List<Task> tasks = new ArrayList<Task>();
    static int currentQuestion = 0;
    private static int score = 0;
    private static long totalTime = 0;
    static Queue<Answer> answers = new ConcurrentLinkedQueue<>();
    static List<HighScore> highScores = new ArrayList<HighScore>();
    static HighScoresComparator comparator = new HighScoresComparator();

    public static void main(String[] args) {
        //start Game here
        playGame();

        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            //clear queue if it's no t empty so that only one value will be present at a time
            answers.clear();
            //wait until a value is entered and put it in the queue. Also store the time when the answer was provided so we can calculate how long it took to answer.
            answers.add(new Answer(sc.next(), System.currentTimeMillis()));
        }
    }

    static void playGame() {
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

        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        //start schedule. This schedule will loop through questions in the Tasks List.
        ses.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //if the last question has been reached record High score and reset the question counter to zero so we start over
                if (currentQuestion == 21) {
                    //convert time to seconds
                    long totalTimeInSeconds = totalTime / 1000;
                    //initially add High score without a name so we can determine it's rank
                    HighScore highScore = new HighScore(score, totalTimeInSeconds, "No name");
                    highScores.add(highScore);

                    System.out.println("Great! Within " + totalTimeInSeconds + " seconds, you solved " + score + " tasks correctly!\nYou made it to the highscore! (Place " + getPlace() + ") What is your name?");

                    //Don't do anything till player enters name
                    do {
                    } while (answers.size() < 1);
                    //get name from Queue
                    String name = answers.poll().getInput();
                    //update the high score previously entered without a name
                    highScores.get(highScores.size() - 1).setName(name);

                    //reset variables back to zero
                    currentQuestion = 0;
                    totalTime = 0;
                    score = 0;
                }

                //print the Task question
                String question = tasks.get(currentQuestion).getQuestion();
                System.out.println(question);
                //note time the question was asked
                long timeOfQuestion = System.currentTimeMillis();

                //If the Current question isn't the game starting question, give the player 10 seconds to answer the question.
                if (currentQuestion != 0) {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                Answer answer = null;

                //game start question
                if (currentQuestion == 0) {
                    //wait until player provides an input
                    do {
                    } while (answers.size() < 1);
                }
                if ((answer = answers.poll()) != null) {
                    //process the answer to the game start question
                    if (currentQuestion == 0) {
                        String startingChoice = answer.getInput();
                        if (startingChoice.equalsIgnoreCase("1")) {
                            //continue the game
                        } else if (startingChoice.equalsIgnoreCase("2")) {
                            //display the high scores. restart the game.
                            //to restart the game, we set currentQuestion to -1 because it will be incremented to 0 at the end of this block
                            System.out.println("---------------------------------------------------------");
                            System.out.printf("%-8s%-8s%-10s%s\n", "Rank", "Score", "Used Time", "Name");
                            int rank = 1;
                            for (HighScore score : highScores) {
                                System.out.printf("%-8d%-8s%-10s%s\n", rank, score.getScore(), score.getTime(), score.getName());
                                rank++;
                            }
                            System.out.println("---------------------------------------------------------");
                            currentQuestion = -1;
                        } else {
                            //unknown input. restart the game.
                            //to restart the game, we set currentQuestion to -1 because it will be incremented to 0 at the end of this block
                            System.out.println("----invalid input-----");
                            currentQuestion = -1;
                        }
                    } else {
                        //Check if answer provided is same as the Task answer
                        if (answer.getInput().equalsIgnoreCase(tasks.get(currentQuestion).getAnswer())) {
                            //increment score
                            score++;
                            //calculate the time it took to answer the question by find the difference between the time
                            //the question was asked and the time the answer was provided
                            totalTime += (answer.getTime() - timeOfQuestion);
                        }
                    }
                } else {
                    //if no answer was provided after 10 seconds
                    System.out.println("… sorry, your didn’t respond fast enough");
                }
                currentQuestion++;
            }
        }, 0, 1, TimeUnit.MILLISECONDS);

    }

    static int getPlace() {
        int position = 0;
        //sort the high scores
        highScores.sort(comparator);

        //check the position of the high score without a name
        for (HighScore highscore : highScores) {
            System.out.println(highscore.toString());
            position++;
            if (highscore.getName().equalsIgnoreCase("No name")) {
                break;
            }
        }
        return position;
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
