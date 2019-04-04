package com.make.my.day.hm5;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javafx.util.Pair;
import org.junit.Test;

public class MergeSortJoinTest {
    @Test
    public void spliteratorTest() {
        List<String> listLeft = Arrays.asList("a b c c o f g h k l".split(" "));
        Collections.shuffle(listLeft);
        Stream<String> left = listLeft.stream();
        List<String> listRight = Arrays.asList("aa bb cc ca cb cd ce dd pp ee ff gg hh kk".split(" "));
        Collections.shuffle(listRight);
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

        assertThat("Incorrect result", new HashSet<>(result), is(new HashSet<>(expected)));
        assertThat("Incorrect result order",
                result.stream()
                        .map(s -> s.substring(0,3))
                        .collect(Collectors.toList()),
                is(expected.stream()
                        .map(s -> s.substring(0,3))
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
    public void spliteratorMemoryTest() {
        Stream<Integer> left = IntStream.iterate(1, i -> i + 1).limit(Integer.MAX_VALUE >> 2).boxed();
        Stream<Integer> right = IntStream.iterate(1, i -> i + 1).limit(Integer.MAX_VALUE >> 2).boxed();

        long count = StreamSupport.stream(new MergeSortInnerJoinSpliterator<>(left,
                right, Function.identity(), Function.identity(), true), false)
                .count();
        assertThat("Incorrect result", count, is((long)Integer.MAX_VALUE >> 2));
    }

    //ToDo: Implement your own merge sort inner join spliterator. See https://en.wikipedia.org/wiki/Sort-merge_join
    public static class MergeSortInnerJoinSpliterator<C extends Comparable<C>, L, R> implements Spliterator<Pair<L, R>> {

        private final Stream<L> left;
        private final Stream<R> right;
        private final Function<L, C> keyExtractorLeft;
        private final Function<R, C> keyExtractorRight;
        private final Iterator<R> rightIterator;
        private final Iterator<L> leftIterator;
        private L currentLeftElement;
        private R currentRightElement;
        private L nextLeftElement;
        boolean greaterLeft = false;
        boolean isFirst = true;
        boolean isLast = false;


        public MergeSortInnerJoinSpliterator(Stream<L> left,
                                             Stream<R> right,
                                             Function<L, C> keyExtractorLeft,
                                             Function<R, C> keyExtractorRight,
                                              boolean isSorted) {

            if (!isSorted) {
                this.left = left.sorted();
                this.right = right.sorted();
                this.leftIterator = this.left.iterator();
                this.rightIterator = this.right.iterator();

            } else {
                this.left = left;
                this.right = right;
                this.leftIterator = left.iterator();
                this.rightIterator = right.iterator();
            }

            this.keyExtractorLeft = keyExtractorLeft;
            this.keyExtractorRight = keyExtractorRight;
                    }

        @Override
        public boolean tryAdvance(Consumer<? super Pair<L, R>> action) {
            if (leftIterator.hasNext() || rightIterator.hasNext() || !isLast) {
                if (leftIterator.hasNext() && rightIterator.hasNext() && isFirst) {
                    nextLeftElement = leftIterator.next();
                    currentLeftElement = nextLeftElement;
                    nextLeftElement = leftIterator.next();
                    currentRightElement = rightIterator.next();
                }
                if (!greaterLeft && !isFirst) {
                    if (leftIterator.hasNext()) {
                        currentLeftElement = nextLeftElement;
                        nextLeftElement = leftIterator.next();
                    }
                    else if (!isLast) {
                        currentLeftElement = nextLeftElement;
                        nextLeftElement = null;
                        isLast = true;
                    }
                    else {
                        return false;
                    }
                }
                else if (!isFirst) {
                    if (rightIterator.hasNext()) {
                        currentRightElement = rightIterator.next();
                    }
                    else {
                        return false;
                    }
                }
                int cmp = keyExtractorLeft.apply(currentLeftElement)
                    .compareTo(keyExtractorRight.apply(currentRightElement));
                if (cmp == 0) {
                    if (nextLeftElement != null && keyExtractorLeft.apply(currentLeftElement)
                        .equals(keyExtractorLeft.apply(
                            nextLeftElement))) {
                        action.accept(new Pair<>(currentLeftElement, currentRightElement));
                    }
                    action.accept(new Pair<>(currentLeftElement, currentRightElement));
                    greaterLeft = true;
                    isFirst = false;
                }
                else {
                    greaterLeft = cmp > 0;
                    isFirst = false;
                }
                return true;
            }
            return false;
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
    }

}
