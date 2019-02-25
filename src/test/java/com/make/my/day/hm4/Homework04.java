package com.make.my.day.hm4;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.Test;

public class Homework04 {

  @Test
  public void collectToList() {
    String[] words = new String[]{"one", "two", "three"};

    List<String> result = Arrays.stream(words)
        // TODO: Add realization
        .collect(null, null, null);

    assertArrayEquals(words, result.toArray());
  }

  @Test
  public void collectToSet() {
    String[] words = new String[]{"one", "one", "two", "two", "three"};

    Set<String> result = Arrays.stream(words)
        // TODO: Add realization
        .collect(null, null, null);

    assertArrayEquals(new String[]{"one", "two", "three"}, result.toArray());
  }

  @Test
  public void collectToMap() {
    String[] words = new String[]{"one", "one", "one", "two", "two", "three"};

    Map<String, Integer> result = Arrays.stream(words)
        // TODO: Add realization to store words - count. If key the same value must increment
        .collect(null, null, null);

    Map<String, Integer> expected = new HashMap<>();
    expected.put("one", 3);
    expected.put("two", 2);
    expected.put("three", 1);

    assertEquals(expected, result);
  }

  @Test
  public void collectingAndThen() {
    String[] words = new String[]{"one", "one", "one", "two", "two", "three"};

    List<String> result = Arrays.stream(words)
        // TODO: Add realization. Should get unique words and concatenate themselves
        .collect(Collectors.collectingAndThen(
            null,
            null
        ));

    assertArrayEquals(new String[]{"oneone", "twotwo", "threethree"}, result.toArray());
  }

  @Test
  public void joining() {
    String[] words = new String[]{"Glass", "Steel", "Wood", "Stone"};

    String result = Arrays.stream(words)
        // TODO: Add realization
        .collect(null);

    assertEquals("Materials[ Glass, Steel, Wood, Stone ]", result);
  }

  @Test
  public void groupingByWordsLength() {
    String[] words = new String[]{"one", "one", "one", "two", "two", "three"};

    Map<Integer, List<String>> result = Arrays.stream(words)
        // TODO: Use here grouping by
        .collect(null);

    Map<Integer, List<String>> expected = new HashMap<>();
    expected.put(3, Arrays.asList("one", "one", "one", "two", "two"));
    expected.put(5, Arrays.asList("three"));

    assertEquals(expected, result);
  }


  private class Dog{
    private String name;
    private int age;

    public Dog(String name, int age) {
      this.name = name;
      this.age = age;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getAge() {
      return age;
    }

    public void setAge(int age) {
      this.age = age;
    }
  }


  @Test
  public void dogMappingByAge() {
    List<Dog> dogs = Arrays.asList(
        new Dog("Bim", 4), new Dog("Duke", 7), new Dog("Fenrir", 120),
        new Dog("Bim", 8), new Dog("Lucky", 6), new Dog("Duke", 13));

    Map<String, List<Integer>> result = dogs.stream()
        // TODO: Use here `groupingBy` plus `mapping`
        .collect(null);

    Map<String, List<Integer>> expected = new HashMap<>();
    expected.put("Bim", Arrays.asList(4, 8));
    expected.put("Duke", Arrays.asList(7, 13));
    expected.put("Fenrir", Arrays.asList(120));
    expected.put("Lucky", Arrays.asList(6));

    assertEquals(expected, result);
  }

  @Test
  public void partitionByOddEven() {
    int[] numbers = new int[]{2, 3, 8, 7, 8, 9, 13, 11};

  }
}