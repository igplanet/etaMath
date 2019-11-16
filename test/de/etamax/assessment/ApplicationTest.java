/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.etamax.assessment;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author oghomwen.aigbedion
 */
public class ApplicationTest {

    Application application;

    public ApplicationTest() {
    }

    /**
     * Test that total number of questions generated is 2126 Total number of
     * possible questions according to the conditions given are 2126
     */
    @Test
    public void testTotalNumberOfQuestion() {
        Application.loadQuestions();
        assertEquals(Application.allQuestions.size(), 2126);
    }

    /**
     * Test that generated questions contains an Addition “with Overflow” of
     * calculations resulting in a range of 21 to 99
     */
    @Test
    public void testAdditionWithOverflow() {
        Application.loadQuestions();
        assertTrue(Application.allQuestions.contains("14 + 7 = ?") || Application.allQuestions.contains("7 + 14 = ?"));
    }

    /**
     * Test that generated questions contains a Subtraction as reversion task
     */
    @Test
    public void testSubtractionAsReversion() {
        Application.loadQuestions();
        assertTrue(Application.allQuestions.contains("21 - 7 = ?") || Application.allQuestions.contains("21 - 14 = ?"));
    }

    /**
     * Test that generated questions contains a Multiplication of the
     * multiplication tables 3, 4, 5, 6, 7, 8, 9, 11, 12
     */
    @Test
    public void testMultiplication() {
        Application.loadQuestions();
        assertTrue(Application.allQuestions.contains("7 * 8 = ?") || Application.allQuestions.contains("8 * 7 = ?"));
    }

    /**
     * Test that generated questions contains a division as reversion task
     */
    @Test
    public void testDivision() {
        Application.loadQuestions();
        assertTrue(Application.allQuestions.contains("56 / 8 = ?") || Application.allQuestions.contains("56 / 7 = ?"));
    }
}
