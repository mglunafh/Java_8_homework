package com.make.my.day.hm2;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.function.*;

import static org.junit.Assert.*;

public class Homework02 {

  @Test
  public void concatenateChars() {
    //TODO: create your realization with lambda
    Function<Character[], String> charConcatenator = chars -> Arrays.toString(chars).replaceAll("[\\[\\], ]", "");

    /* Function<Character[], String> charConcatenator = chars -> {
      StringBuilder sb = new StringBuilder();
      for (char c : chars) {
        sb.append(c);
      }
      return sb.toString();
    };*/


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
    Predicate<String> isReversedStringTheSame = str -> new StringBuilder(str).reverse().toString().equals(str);

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
  public void transformAndProvideSumWithCounter() {
    //TODO: create your realization with lambda
    Function<String, Integer> transform = Integer::parseInt;

    //TODO: create your realization with lambda
    BinaryOperator<Integer> increment = (int1,int2) -> int1+int2;

    Counter sut_1 = new Counter(transform, increment);
    Counter sut_2 = new Counter(transform, increment);
    Counter sut_3 = new Counter(transform, increment);

    assertEquals(5, sut_1.getSum("3", "2"));
    assertEquals(10, sut_2.getSum("5", "5"));
    assertEquals(57, sut_3.getSum("24", "33"));
  }

  @Test
  public void consumerPrintInPrintStream() {
    final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    final PrintStream original = System.out;
    System.setOut(new PrintStream(outContent));

    //TODO: method must print parameters in System.out
    Consumer<Object> printHelloInSystemOut = System.out::print;

    printHelloInSystemOut.accept("hello");
    assertEquals("hello", outContent.toString());

    outContent.reset();

    printHelloInSystemOut.accept("epam");
    assertEquals("epam", outContent.toString());

    outContent.reset();

    printHelloInSystemOut.accept(12345);
    assertEquals("12345", outContent.toString());

    System.setOut(original);
  }

  @Test
  public void testPredicateScenario() {

    //TODO: create your realization with lambda
    Predicate<String> isStartsWithXX = str -> str.substring(0,2).equals("XX");
    //Predicate<String> isStartsWithXX = str -> str.startsWith("XX");

    //TODO: create your realization with lambda
    Predicate<String> isEndWithZZZ = isStartsWithXX.and(str -> str.substring(str.length()-3).equals("ZZZ"));
    //Predicate<String> isEndWithZZZ = isStartsWithXX.and(str -> str.endsWith("ZZZ"));

    //TODO: create your realization with lambda
    Predicate<String> haveFiveStarsInRow = isEndWithZZZ.or(str -> str.matches("^[\\w*]*\\*{5}[\\w*]*$"));
    //Predicate<String> haveFiveStarsInRow = isEndWithZZZ.or(str -> str.contains("*****"));

    assertFalse(haveFiveStarsInRow.test("Xnot_rightZZZ"));
    assertTrue(haveFiveStarsInRow.test("XXsuperduperZZZ"));
    assertFalse(haveFiveStarsInRow.test("XXnotrighttooZZ"));
    assertTrue(haveFiveStarsInRow.test("one*two**three***four****five*****"));
    assertFalse(haveFiveStarsInRow.test("this**will**fail*"));
  }

  @Test
  public void chainOfFunctionInvocation() {
    List<Integer> numbers = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10));

    //TODO: create realization with lambda
    Predicate<Integer> isEven = integer -> integer % 2 == 0;

    List<Integer> evenNumbersList = new ArrayList<>();

    for (Integer num : numbers) {
      if (isEven.test(num)){
        evenNumbersList.add(num);
      }
    }

    assertTrue(evenNumbersList.containsAll(Arrays.asList(2,4,6,8,10)));

    //TODO: create realization with lambda
    Function<List<Integer>, List<Integer>> sumWithNextElement = list -> {
      List<Integer> result = new ArrayList<>();
      for (int i = 0; i < list.size(); i++) {
          result.add(i+1 != list.size() ? list.get(i)+ list.get(i+1) : list.get(i) +list.get(0));
      }
      return result;
    };

    List<Integer> newNumbers = sumWithNextElement.apply(evenNumbersList);

    assertTrue(newNumbers.containsAll(Arrays.asList(6,10,14,18,12)));

    //TODO: create realization with lambda
    Predicate<Integer> moreThanThirteen = integer -> integer > 13;

    List<Integer> numbersMoreThanFifteen = new ArrayList<>();

    for (Integer num : newNumbers) {
      if (moreThanThirteen.test(num)){
        numbersMoreThanFifteen.add(num);
      }
    }

    assertTrue(numbersMoreThanFifteen.containsAll(Arrays.asList(14,18)));
  }

  //TODO: implement predicate with lambda
  private static Predicate<String> lengthMoreThanSeven() {
      return str -> str.length() > 7;
  }

  //TODO: implement, if predicate correct "str" => "strstr" if not "str..."
  private static Function<Predicate<String>, Function<String, String>> doubleStringOrAddThreeDots() {
      return  predicate -> str -> predicate.test(str) ? str.concat(str) : str.concat("...");
  }

  //TODO: implement, return word length from function
  private static Function<String, Integer> lengthOfWord(
      Function<Predicate<String>, Function<String, String>> doubledOrWithThreeDots) {
    return str -> doubledOrWithThreeDots.apply(lengthMoreThanSeven()).apply(str).length();
  }

  @Test
  public void checkDeepCallBack() {
    assertFalse(lengthMoreThanSeven().test("no"));
    assertTrue(lengthMoreThanSeven().test("wonderful"));
    assertFalse(lengthMoreThanSeven().test("welcome"));


    assertEquals("no...", doubleStringOrAddThreeDots()
        .apply(lengthMoreThanSeven()).apply("no"));

    assertEquals("wonderfulwonderful", doubleStringOrAddThreeDots()
        .apply(lengthMoreThanSeven()).apply("wonderful"));

    assertEquals("welcome...", doubleStringOrAddThreeDots()
        .apply(lengthMoreThanSeven()).apply("welcome"));


    assertEquals(5, lengthOfWord(doubleStringOrAddThreeDots()).apply("no").intValue());
    assertEquals(18, lengthOfWord(doubleStringOrAddThreeDots()).apply("wonderful").intValue());
    assertEquals(10, lengthOfWord(doubleStringOrAddThreeDots()).apply("welcome").intValue());
  }

  private class LazyProperty {
    private Supplier<Integer> supplier;
    private Integer lazy;

    public LazyProperty(Supplier<Integer> supplier) {
      this.supplier = supplier;
    }

    //TODO: initialize "lazy" using "supplier" only one time
    public int getLazy() {
      return lazy = supplier.get();
    }
  }

  @Test
  public void lazyLoading() {

    //TODO: provide supplier in constructor with lambda
    LazyProperty sut = new LazyProperty(() -> 1);
    LazyProperty sut_2 = new LazyProperty(() -> 2);
    LazyProperty sut_3 = new LazyProperty(() -> 3);

    assertNull(sut.lazy);
    assertNull(sut_2.lazy);
    assertNull(sut_3.lazy);

    assertEquals(1,sut.getLazy());
    assertEquals(2,sut_2.getLazy());
    assertEquals(3,sut_3.getLazy());

    assertEquals(1,sut.lazy.intValue());
    assertEquals(2,sut_2.lazy.intValue());
    assertEquals(3,sut_3.lazy.intValue());
  }

  @Test
  public void andThenTest() {
    //TODO: realize with lambda
    Function<Integer, Integer> sumIntegerOnSix = int1 -> int1 + 6;
    Function<Integer, Integer> thenMinusThree = int1 -> int1 - 3;
    Function<Integer, Integer> afterMultipleOnFive = int1 -> int1*5;

    Integer result = sumIntegerOnSix
        .andThen(thenMinusThree)
        .andThen(afterMultipleOnFive)
        .apply(10);

    assertEquals(65, result.intValue());
  }
}
