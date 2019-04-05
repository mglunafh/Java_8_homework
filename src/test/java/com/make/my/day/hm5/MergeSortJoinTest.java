package com.make.my.day.hm5;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import javafx.util.Pair;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.Spliterator;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MergeSortJoinTest {
  
    @Test
    public void spliteratorTest() {

      List<String> listLeft = Arrays.asList("a b c c o f g h k l".split(" "));
      List<String> listRight = Arrays.asList("aa bb cc ca cb cd ce dd pp ee ff gg hh kk".split(" "));

      Collections.shuffle(listLeft);
      Collections.shuffle(listRight);
      Stream<String> left = listLeft.stream();
      Stream<String> right = listRight.stream();

      List<String> result = StreamSupport.stream(new MergeSortInnerJoinSpliterator<>(left,
          right, Function.identity(), s -> s.substring(0, 1), false), false)
          .map(pair -> pair.getKey() + " " + pair.getValue())
          .collect(Collectors.toList());
      List<String> expected = Stream.of(
          "a aa",
          "b bb",
          "c cc",
          "c ca",
          "c cb",
          "c cd",
          "c ce",
          "c cc",
          "c ca",
          "c cb",
          "c cd",
          "c ce",
          "f ff",
          "g gg",
          "h hh",
          "k kk"
      ).collect(Collectors.toList());

      left = listLeft.stream();
      right = listRight.stream();
      long count = StreamSupport.stream(new MergeSortInnerJoinSpliterator<>(left,
          right, Function.identity(), s -> s.substring(0, 1), false), false).count();

      assertThat("Incorrect count", expected.size(), is((int) count));

      assertThat("Incorrect result", new HashSet<>(result), is(new HashSet<>(expected)));
      assertThat("Incorrect result order",
          result.stream()
              .map(s -> s.substring(0, 3))
              .collect(Collectors.toList()),
          is(expected.stream()
              .map(s -> s.substring(0, 3))
              .collect(Collectors.toList()))
      );
    }

    @Test
    public void spliteratorIntTest() {
        Stream<Integer> left = IntStream.iterate(1, i -> i + 1).limit(10).boxed();
        Stream<String> right = Arrays.stream("0x 1a 2b 3c 4e 5g 9l".split(" "));

        List<String> result = StreamSupport.stream(new MergeSortInnerJoinSpliterator<>(left,
                right, String::valueOf, s -> s.substring(0, 1), false), false)
                .map(pair -> pair.getKey() + " " + pair.getValue())
                .collect(Collectors.toList());
        List<String> expected = Arrays.asList(
                "1 1a",
                "2 2b",
                "3 3c",
                "4 4e",
                "5 5g",
                "9 9l"
        );

        assertThat("Incorrect result", result, is(expected));
    }

    @Test
    public void spliteratorStudentsTest() {
      String[] names = { "Hans", "Ivanhoe", "Jae-Dong", "John" };
      String[] surnames = { "Adams", "Kim", "Louis", "Schwartz", "Smith" };
      
      int four = names.length;
      int five = surnames.length;
      int N = four * five;
      
      int generator = 3;
      int acc = 0;
      List<String> people = new ArrayList<>(N);
      for (int i = 0; i < N; i++) {
        int surnameIndex = acc / four;
        int nameIndex = acc - four * surnameIndex;
        String stud = String.format("%02d %s %s", i % generator, names[nameIndex], surnames[surnameIndex]);
        people.add(stud);
        System.out.println(stud);
        
        acc = (acc + generator) % N;
      }
      
      int coeff = 111;
      List<String> courses = new ArrayList<>();
      for (int i = 0; i < 3 * generator; i++) {
        String str = String.format("%02d %d", i / generator, coeff * i);
        courses.add(str);
        System.out.println(str);
      }
      
      int split = N / generator + 1;
      List<String> peopleCopy = new ArrayList<>(people);
      Collections.sort(peopleCopy);
      peopleCopy.forEach(System.out::println);
      
      List<Pair<String, String>> joinResult = new ArrayList<>();
      for (int i = 0; i < generator; i++) {
        for (int j = i * split; j < peopleCopy.size() && j < (i + 1) * split; j++) {
          for (int k = i * generator; k < (i + 1) * generator; k++) {
            joinResult.add(new Pair<>(peopleCopy.get(j), courses.get(k)));
          }        
        }
      }
      joinResult.forEach(System.out::println);
      
      Stream<String> left = people.stream();
      Stream<String> right = courses.stream();
      
      Stream<Pair<String, String>> resultStream = 
        StreamSupport.stream(new MergeSortInnerJoinSpliterator<>(left,
            right, 
            s -> s.substring(0, 2), 
            s -> s.substring(0, 2), 
            false), false);
      
      List<String> result = resultStream
          .map(pair -> pair.toString()).collect(Collectors.toList());
      
      List<String> expectedList = 
          joinResult.stream().map(pair -> pair.toString()).collect(Collectors.toList());
      
      
      SortedSet<String> peopleSet = new TreeSet<>(people);
      assertThat("There are duplicates", people.size(), is(peopleSet.size()));    
      
      assertThat("Incorrect result", expectedList, is(result));
    }
        

    @Test
    public void spliteratorMemoryTest() {
        Stream<Integer> left = IntStream.iterate(1, i -> i + 1).limit(Integer.MAX_VALUE >> 2).boxed();
        Stream<Integer> right = IntStream.iterate(1, i -> i + 1).limit(Integer.MAX_VALUE >> 2).boxed();

        long count = StreamSupport.stream(new MergeSortInnerJoinSpliterator<>(left,
                right, Function.identity(), Function.identity(), true), false)
                .count();
        assertThat("Incorrect result", count, is((long)(Integer.MAX_VALUE >> 2)));
    }

    //ToDo: Implement your own merge sort inner join spliterator. See https://en.wikipedia.org/wiki/Sort-merge_join
    public static class MergeSortInnerJoinSpliterator<C extends Comparable<C>, L, R> implements Spliterator<Pair<L, R>> {

        private final Spliterator<L> left;
        private final Spliterator<R> right;
        private final Function<L, C> keyExtractorLeft;
        private final Function<R, C> keyExtractorRight;

        private CartesianProduct<L, R> product;

        // elements popped from the left stream will be stored here
        private L leftObj;
        private final Consumer<L> setterLeft = t -> leftObj = t;
        
        // elements popped from the right stream will be stored here
        private R rightObj;
        private final Consumer<R> setterRight = t -> rightObj = t;

        public MergeSortInnerJoinSpliterator(Stream<L> left,
                                             Stream<R> right,
                                             Function<L, C> keyExtractorLeft,
                                             Function<R, C> keyExtractorRight,
                                             boolean isSorted)  {
            if (!isSorted) {
                this.left = left.sorted(Comparator.comparing(keyExtractorLeft::apply)).spliterator();
                this.right = right.sorted(Comparator.comparing(keyExtractorRight::apply)).spliterator();
            }
            else {
                this.left = left.spliterator();
                this.right = right.spliterator();
            }

            this.keyExtractorLeft = keyExtractorLeft;
            this.keyExtractorRight = keyExtractorRight;

            this.left.tryAdvance(setterLeft);
            this.right.tryAdvance(setterRight);
        }

        @Override
        public boolean tryAdvance(Consumer<? super Pair<L, R>> action) {

            if (product == null || !product.tryAdvance(action)) {
                List<L> listLeft = advanceKeyLeft();
                if (listLeft == null) {
                  return false;
                }
                List<R> listRight = advanceKeyRight();
                if (listRight == null) {
                  return false;
                }

                int cmp = keyExtractorLeft.apply(listLeft.get(0))
                    .compareTo(keyExtractorRight.apply(listRight.get(0)));
                while (cmp != 0) {
                    if (cmp < 0) {
                        listLeft = advanceKeyLeft();
                        if (listLeft == null) {
                            return  false;
                        }
                    } else {
                        listRight = advanceKeyRight();
                        if (listRight == null) {
                            return false;
                        }
                    }
                    cmp = keyExtractorLeft.apply(listLeft.get(0))
                        .compareTo(keyExtractorRight.apply(listRight.get(0)));
                }

                product = new CartesianProduct<>(listLeft, listRight);
                product.tryAdvance(action);
            }
            return true;
        }

        @Override
        public Spliterator<Pair<L, R>> trySplit() {
            return null;
        }

        @Override
        public long estimateSize() {
            return Long.MAX_VALUE;
        }

        @Override
        public int characteristics() {
            return ORDERED;
        }

        /**
         * This method returns a sequence of elements 
         * from the left stream that have the same key.
         * @return list of elements with the same key or 
         * {@code null} if no nore elements left.
         */
        private List<L> advanceKeyLeft() {

          // In spliterator constructor we tried to set 'leftObj' value.
          // It is null, if the left stream is empty or has ended.
            if (leftObj == null) {
                return null;
            }
            L initial = leftObj;
            List<L> list = new ArrayList<>();
            int cmp = 0;
            while (cmp == 0) {
                list.add(leftObj);

                if (left.tryAdvance(setterLeft)) {
                  cmp = keyExtractorLeft.apply(initial).compareTo(keyExtractorLeft.apply(leftObj));
                } else {
                  leftObj = null;
                  break;
                }
            }
            return list;
        }

        /**
         * This method returns a sequence of elements from 
         * the right stream that have the same key.
         * @return list of elements with the same key or 
         * {@code null} if no nore elements left.
         */
        private List<R> advanceKeyRight() {

            // In spliterator constructor we tried to set 'rightObj' value.
            // It is null, if the right stream is empty or has ended.
            if (rightObj == null) {
                return null;
            }
            R initial = rightObj;
            List<R> list = new ArrayList<>();
            int cmp = 0;
            while (cmp == 0) {
                list.add(rightObj);
                if (right.tryAdvance(setterRight)) {
                    cmp = keyExtractorRight.apply(initial).compareTo(keyExtractorRight.apply(rightObj));
                } else {
                    rightObj = null;
                    break;
                }
            }
            return list;
        }

        /**
         * This class represents cartesian product of two lists that 
         * satisfy certain join condition.
         * You can fetch 
         * @param <L>
         * @param <R> 
         */
        static class CartesianProduct<L, R> {

            private final List<L> left;
            private final List<R> right;
            private int counterLeft;
            private int counterRight;

            CartesianProduct(List<L> left, List<R> right) {
                this.left = left;
                this.right = right;
            }

            boolean tryAdvance(Consumer<? super Pair<L, R>> action) {
                if (counterLeft < left.size()) {
                    action.accept(new Pair<>(left.get(counterLeft), right.get(counterRight)));
                    if (++counterRight == right.size()) {
                        counterLeft++;
                        counterRight = 0;
                    }
                    return true;
                }
                else
                    return false;
            }
        }
    }
}
