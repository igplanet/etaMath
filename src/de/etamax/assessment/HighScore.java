/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.etamax.assessment;

/**
 *
 * @author oghomwen.aigbedion
 */
public class HighScore {

    Integer score;
    Long time;
    String name;

    public HighScore(int score, Long time, String name) {
        this.score = score;
        this.time = time;
        this.name = name;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        //return "HighScore{" + "score=" + score + ", time=" + time + ", name=" + name + '}';
        return name + " " + score + " " + time;
    }

}
