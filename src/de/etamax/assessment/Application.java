/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.etamax.assessment;

import java.security.SecureRandom;
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
    static long timeOfQuestion = 0;
    static Task currentTask = new Task();
    static List<Task> additionQuestions = new ArrayList<>();
    static List<Task> subtractionQuestions = new ArrayList<>();
    static List<Task> multiplicationQuestions = new ArrayList<>();
    static List<Task> divisionQuestions = new ArrayList<>();
    static List<String> allQuestions = new ArrayList<>();
    static int additionCounter = 0;
    static int subtractionCounter = 0;
    static int multiplicationCounter = 0;
    static int divisionCounter = 0;
    static int currentQuestion = 0;
    static int score = 0;
    static long totalTime = 0;
    static Queue<Answer> answers = new ConcurrentLinkedQueue<>();
    static List<HighScore> highScores = new ArrayList<HighScore>();
    static HighScoresComparator comparator = new HighScoresComparator();

    public static void main(String[] args) {

        //generate all possible questions with the given condition
        loadQuestions();

        //start Game here
        playGame();

        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            //clear queue if it's not empty so that only one value will be present at a time
            answers.clear();
            //wait until a value is entered and put it in the queue. Also store the time when the answer was provided so we can calculate how long it took to answer.
            answers.add(new Answer(sc.next(), System.currentTimeMillis()));
        }
    }

    static void playGame() {
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

                //If the Current question isn't the game starting question, give the player 10 seconds to answer the question.
                if (currentQuestion != 0) {
                    //print the Task question
                    currentTask = getRandomTask();
                    System.out.println(currentTask.getQuestion());
                    //note time the question was asked
                    timeOfQuestion = System.currentTimeMillis();
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    //Current question is the game starting question
                    System.out.println("Welcome to etaMath!\nType 1 to start a game,\nType 2 to show highscores ");
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
                            displayHighScores();
                            currentQuestion = -1;
                        } else {
                            //unknown input. restart the game.
                            //to restart the game, we set currentQuestion to -1 because it will be incremented to 0 at the end of this block
                            System.out.println("----invalid input-----");
                            currentQuestion = -1;
                        }
                    } else {
                        //Check if answer provided is same as the Task answer
                        if (answer.getInput().equalsIgnoreCase(currentTask.getAnswer().toString())) {
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
            position++;
            if (highscore.getName().equalsIgnoreCase("No name")) {
                break;
            }
        }
        return position;
    }

    static void displayHighScores() {
        System.out.println("---------------------------------------------------------");
        System.out.printf("%-8s%-8s%-10s%s\n", "Rank", "Score", "Used Time", "Name");
        int rank = 1;
        for (HighScore highscore : highScores) {
            System.out.printf("%-8d%-8s%-10s%s\n", rank, highscore.getScore(), highscore.getTime(), highscore.getName());
            rank++;
            if (rank > 20) {
                break;
            }
        }
        System.out.println("---------------------------------------------------------");
    }

    static int getRandomQuestionType() {
        //generate random number to determine the question type to show
        //1 for Addition
        //2 for Multiplication
        //3 for Subtraction
        //4 for division
        int determinant = 0;
        SecureRandom s = new SecureRandom();
        do {
            determinant = Integer.valueOf(String.valueOf(s.nextDouble()).substring(2, 3));
        } while (determinant > 4 || determinant < 1);
        return determinant;
    }

    static int getDigitForAddition() {
        //Randomly generate a 1 or 2 digits number for the creation of Addition Tasks
        SecureRandom s = new SecureRandom();
        int determinant = Integer.valueOf(String.valueOf(Math.abs(s.nextInt())).substring(0, 1));
        //if random number is even, generate a 1 digit number, else generate a 2 digits number
        if ((determinant % 2) == 0) {
            return Integer.valueOf(String.valueOf(Math.abs(s.nextInt())).substring(0, 1));
        } else {
            return Integer.valueOf(String.valueOf(Math.abs(s.nextInt())).substring(0, 2));
        }
    }

    static int getDigitForMultiplication() {
        //randomly select a Multiplication table type to generate multiplication problems from
        SecureRandom s = new SecureRandom();
        int determinant = Integer.valueOf(String.valueOf(Math.abs(s.nextInt())).substring(0, 1));
        int multiplicationTables[] = {3, 4, 5, 6, 7, 8, 9, 11, 12};
        return multiplicationTables[determinant - 1];
    }

    static int getDigitbetween1And12() {
        //randomly select a number between 1 and 12 to generate multiplication problems from
        int digit = 0;

        //generate a number between 1 and 12 using the getDigitForAddition method that generates a 1 or 2 digits number at random
        while (true) {
            digit = getDigitForAddition();
            if (digit <= 12) {
                break;
            }
        }
        return digit;
    }

    static void getAdditionQuestion() {
        String question = "";
        while (true) {
            //get a random digit
            int a = getDigitForAddition();
            //get a random digit
            int b = getDigitForAddition();
            int c = a + b;

            //check if the result is in the range of 21 to 99
            //and if the Addition has decimal Overflow
            if ((c >= 21 && c <= 99) && ((a % 10) > (c % 10)) && ((b % 10) > (c % 10))) {
                question = a + " + " + b + " = ?";
                //create task variant to check repitition
                String reverseQuestion = b + " + " + a + " = ?";

                //check if both tasks already exist
                if (allQuestions.contains(question) || allQuestions.contains(reverseQuestion)) {
                } else {
                    //store the addition task
                    additionQuestions.add(new Task(question, c));
                    //Substraction as reversion task
                    String SubtractionQuestion = c + " - " + a + " = ?";
                    subtractionQuestions.add(new Task(SubtractionQuestion, b));

                    //store both addition and subtraction task in the general task list
                    allQuestions.add(question);
                    allQuestions.add(SubtractionQuestion);
                    break;
                }
            } else {
            }
        }
    }

    static String getMultiplicationQuestion() {
        String question = "";
        while (true) {
            //get multiplication type in 3, 4, 5, 6, 7, 8, 9, 11, 12 at random
            int a = getDigitForMultiplication();
            //get a random number between 1 and 12
            int b = getDigitbetween1And12();
            int c = a * b;
            question = a + " * " + b + " = ?";
            //create task variant to check repitition
            String reverseQuestion = b + " * " + a + " = ?";

            //check for duplicates
            if (allQuestions.contains(question) || allQuestions.contains(reverseQuestion)) {
                //do nothing
            } else {

                multiplicationQuestions.add(new Task(question, c));
                //Division as reversion task
                String divisionQuestion = c + " / " + a + " = ?";
                divisionQuestions.add(new Task(divisionQuestion, b));

                //store both multiplication and division task in the general task list
                allQuestions.add(question);
                allQuestions.add(divisionQuestion);
                break;
            }
        }
        return question;
    }

    static void loadQuestions() {
        //generate all possible tasks
        while (multiplicationQuestions.size() < 72) {
            getMultiplicationQuestion();
        }
        while (additionQuestions.size() < 991) {
            getAdditionQuestion();
        }
    }

    static Task getRandomTask() {
        Task task = new Task();

        //Get one of the following tasks at random
        //1 for Addtion
        //2 for Multiplication
        //3 for Subtraction
        //4 for division
        switch (getRandomQuestionType()) {
            case 1:
                if (additionCounter < 991) {
                    task = additionQuestions.get(additionCounter);
                    additionCounter++;
                } else {
                    //if addition Questions are exhausetd, get a multiplication Question
                    if (multiplicationCounter < 72) {
                        task = multiplicationQuestions.get(multiplicationCounter);
                        multiplicationCounter++;
                    } else {
                        //if multiplication Questions are exhausetd, get a Subtraction Question
                        if (subtractionCounter < 991) {
                            task = subtractionQuestions.get(subtractionCounter);
                            subtractionCounter++;
                        } else {
                            //if Subtraction Questions are exhausetd, get a division Question
                            if (divisionCounter < 72) {
                                task = divisionQuestions.get(divisionCounter);
                                divisionCounter++;
                            } else {
                                //if all Questions are exhausetd, end the play
                                System.out.println("no more questions");
                                currentQuestion = 21;
                            }
                        }
                    }
                }
                break;
            case 2:
                if (multiplicationCounter < 72) {
                    task = multiplicationQuestions.get(multiplicationCounter);
                    multiplicationCounter++;
                } else {
                    if (additionCounter < 991) {
                        task = additionQuestions.get(additionCounter);
                        additionCounter++;
                    } else {
                        if (subtractionCounter < 991) {
                            task = subtractionQuestions.get(subtractionCounter);
                            subtractionCounter++;
                        } else {
                            if (divisionCounter < 72) {
                                task = divisionQuestions.get(divisionCounter);
                                divisionCounter++;
                            } else {
                                //if all Questions are exhausetd, end the play
                                System.out.println("no more questions");
                                currentQuestion = 21;
                            }
                        }
                    }
                }
                break;
            case 3:
                if (subtractionCounter < 991) {
                    task = subtractionQuestions.get(subtractionCounter);
                    subtractionCounter++;
                } else {
                    if (additionCounter < 991) {
                        task = additionQuestions.get(additionCounter);
                        additionCounter++;
                    } else {
                        if (multiplicationCounter < 72) {
                            task = multiplicationQuestions.get(multiplicationCounter);
                            multiplicationCounter++;
                        } else {
                            if (divisionCounter < 72) {
                                task = divisionQuestions.get(divisionCounter);
                                divisionCounter++;
                            } else {
                                //if all Questions are exhausetd, end the play
                                System.out.println("no more questions");
                                currentQuestion = 21;
                            }
                        }
                    }
                }
                break;
            case 4:
                if (divisionCounter < 72) {
                    task = divisionQuestions.get(divisionCounter);
                    divisionCounter++;
                } else {
                    if (additionCounter < 991) {
                        task = additionQuestions.get(additionCounter);
                        additionCounter++;
                    } else {
                        if (multiplicationCounter < 72) {
                            task = multiplicationQuestions.get(multiplicationCounter);
                            multiplicationCounter++;
                        } else {
                            if (subtractionCounter < 991) {
                                task = subtractionQuestions.get(subtractionCounter);
                                subtractionCounter++;
                            } else {
                                //if all Questions are exhausetd, end the play
                                System.out.println("no more questions");
                                currentQuestion = 21;
                            }
                        }
                    }
                }
                break;
        }
        return task;
    }

    static class Task {

        String question;
        Integer answer;

        public Task() {
        }

        public Task(String question, Integer answer) {
            this.question = question;
            this.answer = answer;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public Integer getAnswer() {
            return answer;
        }

        public void setAnswer(Integer answer) {
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
