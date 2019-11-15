/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.etamax.assessment;

import java.util.Comparator;

/**
 *
 * @author oghomwen.aigbedion
 */
public class HighScoresComparator implements Comparator<HighScore> {

    public int compare(HighScore o1, HighScore o2) {
        int value1 = o1.score.compareTo(o2.score);
        if (value1 == 0) {
            int value2 = o1.time.compareTo(o2.time);
            if (value2 == 0) {
                return o1.name.compareTo(o2.name);
            } else {
                return value2;
            }
        }
        return -1 * value1;
    }
}
