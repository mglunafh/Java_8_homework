package com.make.my.day.hm2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Homework02 {

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;

  @Before
  public void setUpStreams() {
    System.setOut(new PrintStream(outContent));
  }

  @After
  public void restoreStreams() {
    System.setOut(originalOut);
  }

  @Test
  public void concatenateChars() {
    //TODO: create your realization with lambda
    Function<Character[], String> charConcatenator = null;

    String result_1 = charConcatenator.apply(new Character[]{'a', 'b', 'c'});
    String result_2 = charConcatenator.apply(new Character[]{'H', 'e', 'l', 'l', 'o'});
    String result_3 = charConcatenator.apply(new Character[]{'T', 'u', 'r', 't', 'l', 'e'});

    assertEquals("abc", result_1);
    assertEquals("Hello", result_2);
    assertEquals("Turtle", result_3);
  }

  @Test
  public void reversedWord() {
    //TODO: create your realization with lambda
    Predicate<String> isReversedStringTheSame = null;

    boolean result_1 = isReversedStringTheSame.test("abccba");
    boolean result_2 = isReversedStringTheSame.test("level");
    boolean result_3 = isReversedStringTheSame.test("cow");
    boolean result_4 = isReversedStringTheSame.test("radar");
    boolean result_5 = isReversedStringTheSame.test("mellow");
    boolean result_6 = isReversedStringTheSame.test("madam");

    assertTrue(result_1);
    assertTrue(result_2);
    assertFalse(result_3);
    assertTrue(result_4);
    assertFalse(result_5);
    assertTrue(result_6);
  }

  private class Counter {

    private Function<String, Integer> transform;
    private BinaryOperator<Integer> summarizer;

    public Counter(Function<String, Integer> transform,
        BinaryOperator<Integer> summarizer) {
      this.transform = transform;
      this.summarizer = summarizer;
    }

    public int getSum(String firstInput, String secondInput) {
      int firstTransformedNumber = transform.apply(firstInput);
      int secondTransformedNumber = transform.apply(secondInput);
      return summarizer.apply(firstTransformedNumber, secondTransformedNumber);
    }
  }

  @Test
  public void getTransformedAndIncreasedNumberOnTwo() {
    //TODO: create your realization with lambda
    Function<String, Integer> transform = null;

    //TODO: create your realization with lambda
    BinaryOperator<Integer> increment = null;

    Counter suit_1 = new Counter(transform, increment);
    Counter suit_2 = new Counter(transform, increment);
    Counter suit_3 = new Counter(transform, increment);

    assertEquals(5, suit_1.getSum("3", "2"));
    assertEquals(10, suit_2.getSum("5", "5"));
    assertEquals(57, suit_3.getSum("24", "33"));
  }

  @Test
  public void name() {
    Consumer<String> printHelloInSystemOut = System.out::print;

    printHelloInSystemOut.accept("hello");

    assertEquals("hello",outContent.toString());
  }
}
