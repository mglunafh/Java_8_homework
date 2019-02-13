package com.make.my.day.hm3;

import static org.junit.Assert.assertArrayEquals;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.Test;

public class Homework03 {

  @Test
  public void createWithBuilder() {
    // TODO: uncomment and add entities
    Stream<String> sut = null;// = Stream.<String>builder()

    List<String> resultList = sut.collect(Collectors.toList());

    assertArrayEquals(new String[]{"Hello", "Wonderful", "Word"},
        resultList.toArray());
  }

  @Test
  public void concatStreams() {
    Stream<Integer> intStream = Stream.of(1, 2);
    Stream<Integer> intStream_2 = Stream.of(3, 4);
    Stream<Integer> intStream_3 = Stream.of(5, 6);

    // TODO: Concat streams correctly
    Stream<Integer> prepared = Stream.concat(null, null);
    Stream<Integer> result = Stream.concat(null, null);

    assertArrayEquals(new Integer[]{1,2,3,4,5,6},result.toArray());
  }

  @Test
  public void iterateForNineHundredsElements() {
    // TODO: Add correctly realization of iterate
    Stream<Integer> stream = Stream.iterate(0, null);

    Integer[] expected = new Integer[900];
    for (int i = 100, j = 0; j < 900; i++, j++) {
      expected[j] = i;
    }

    assertArrayEquals(expected, stream.toArray());
  }

  @Test
  public void createWithArraysMethod() {
    // TODO: Create realization with Arrays.stream
    IntStream sut = null;

    assertArrayEquals(new int[]{'t', 'u', 'r', 't', 'l', 'e'}, sut.toArray());
  }

  private class Agent{
    private final String name = "Smith";

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Agent agent = (Agent) o;
      return Objects.equals(name, agent.name);
    }
  }

  @Test
  public void provideStreamWithGenerate() {
    // TODO: Generate 3000 agents
    Stream<Agent> agents = Stream.generate(null);

    Agent[] expected = new Agent[3000];
    for (int i = 0; i < 3000; i++) {
      expected[i] = new Agent();
    }

    assertArrayEquals(expected, agents.toArray());
  }

}
