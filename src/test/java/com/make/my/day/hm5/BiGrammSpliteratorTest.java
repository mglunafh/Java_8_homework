package com.make.my.day.hm5;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.junit.Test;

public class BiGrammSpliteratorTest {

    @Test
    public void biGramSplitTest() throws Exception {
        List<String> tokens = Arrays.asList("I should never try to implement my own spliterator".split(" "));

        Set<String> result = StreamSupport.stream(new BigrammSpliterator(tokens, " "), true)
                .collect(Collectors.toSet());

        Set<String> expected = Arrays.stream(new String[]{
                "I should",
                "should never",
                "never try",
                "try to",
                "to implement",
                "implement my",
                "my own",
                "own spliterator"
        }).collect(Collectors.toSet());

        assertThat("Incorrect result", result, is(expected));

    }

    @Test
    public void biGramSplitTestSplit() throws Exception {
        List<String> tokens = Arrays.asList("I should never try to implement my own spliterator".split(" "));

        BigrammSpliterator biGrammSpliterator = new BigrammSpliterator(tokens, " ");
        BigrammSpliterator biGramSpliterator1 = biGrammSpliterator.trySplit();

        assertThat("Spliterator 1 is null", biGramSpliterator1, notNullValue());

        BigrammSpliterator biGramSpliterator2 = biGramSpliterator1.trySplit();

        assertThat("Spliterator 2 is null", biGramSpliterator2, notNullValue());
        Consumer<String> consumer = (String s) -> {
        };
        int count = 0;
        while (biGrammSpliterator.tryAdvance(consumer)) {
            count++;
        }

        assertThat("Incorrect Spliterator 0 size", count, is(4));

        count = 0;
        while (biGramSpliterator1.tryAdvance(consumer)) {
            count++;
        }

        assertThat("Incorrect Spliterator 1 size", count, is(2));

        count = 0;
        while (biGramSpliterator2.tryAdvance(consumer)) {
            count++;
        }

        assertThat("Incorrect Spliterator 2 size", count, is(2));

    }

    class BigrammSpliterator implements Spliterator<String> {
        //ToDo: Write your own bi-gram spliterator
        //Todo: Should works in parallel

        /**
         * Read about bi and n-grams https://en.wikipedia.org/wiki/N-gram.
         *
         * @param source
         */
        private final  List<String> source;
        private String delimeter;

        //thread-safe saving current position/index
        private AtomicInteger currentPosition = new AtomicInteger(0);

        public BigrammSpliterator(List<String> source, String delimeter) {
        this.source = source;
        this.delimeter = delimeter;
        }

        @Override
        public boolean tryAdvance(Consumer<? super String> action) {

          //check if it's not the one before last element so we can iterate to get new pair
          if (estimateSize() > 1) {
            String leftPart = source.get(currentPosition.getAndIncrement());
            String rightPart = source.get(currentPosition.get());

            //just send through this consumer new pair that we need to collect later
            action.accept(leftPart + delimeter + rightPart);
            return true;
          }

          //return false if "current position" and next element create the last pair for this spliterator
          return false;
        }

        @Override
        public BigrammSpliterator trySplit() {

          //won't split if we have less than 3 element (only one pair left)
          if (estimateSize() < 3) {
            return null;
          }

          //getting an offset to reach the center of the remaining part from current position
          int offset = (source.size() - currentPosition.get())/2;

          //getting index of the center of the remaining part
          int centerOfRemainingPart = currentPosition.get() + offset;

          //creating new BigrammSpliterator as part of the current source from the current position to
          //the center of remaining part (add 1 to avoid cutting of result and make the full last pair )
          BigrammSpliterator newBigrammSpliterator = new BigrammSpliterator(this.source.subList(currentPosition.get(), centerOfRemainingPart + 1), delimeter);

          //after every new split new currentPosition will be at the first elem of prev split
          currentPosition.set(centerOfRemainingPart);
          return newBigrammSpliterator;
        }

        @Override
        public long estimateSize() {
            return source.size() - currentPosition.get();
        }

        @Override
        public int characteristics() {
            return SIZED | SUBSIZED | ORDERED | IMMUTABLE | CONCURRENT ;
        }
    }


}
