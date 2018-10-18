package Lab1.kasiski;

import java.util.*;
import java.util.concurrent.*;

public class MultithreadedRepetitionCounter {

    /**
     * Count all the substring repetitions in the text.
     *
     * @param fullText The text to look for substring
     * @return A List containing all repetitions
     * @throws ExecutionException Multithread error
     * @throws InterruptedException Multithread error
     */
    Collection<Repetition> count(final String fullText) throws ExecutionException, InterruptedException {
        // This is for multithreading.
        final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        final Collection<Future<Map<String, Repetition>>> futures = new ArrayList<>();

        // We are going to iterate over all character's index in the string, for each index, we start a Thread. Each Thread
        //  is going to return all repetition of substring that begins with the character at current index. (see MatcherFromCharIndex)
        for (int startIndex = 0; startIndex < fullText.length() - 3; ++startIndex) {
            // Put thread into the thread executor service, and get the Future
            futures.add(
                    executor.submit(
                            new MatcherFromCharIndex(fullText, startIndex)
                    )
            );
        }

        final Map<String, Repetition> repetitions = new ConcurrentHashMap<>();
        // As soon as the thread is over, we get the result and merge the maps into the main one.
        for (final Future<Map<String, Repetition>> future : futures) {
            // When a thread completes we merge his results to the main Map
            future.get().forEach((key, value) -> {
                    repetitions.compute(
                            key,
                            (s, oldValue) -> {
                                if (oldValue != null && oldValue.getDistances().size() > value.getDistances().size()){
                                    return oldValue;
                                }
                                return value;
                            }
                    );
            });
        }

        return repetitions.values();
    }

    /**
     * Extends Callable to be a Thread, we will be able to launch multiple callable at the same time to take advantage of multithreading.
     */
    private static final class MatcherFromCharIndex implements Callable<Map<String, Repetition>> {

        private final String fullText;
        private final int startIndex;
        private final Map<String, Repetition> map;

        /**
         * Create a new MatcherFromCharIndex.
         * Example:
         * Lets say the text is "mmabcdefghijklabc" and startIndex is 2.
         * We are going to search for every substring that could start at index 2.
         * First we search for "abc" repetition, then "abcd", then "abcde" then "abcdefg" and so on.
         *
         * @param fullText The text to look for substring
         * @param startIndex The first letter we should use as a substring
         */
        private MatcherFromCharIndex(final String fullText, final int startIndex) {
            this.fullText = fullText;
            this.startIndex = startIndex;
            map = new HashMap<>();
        }

        @Override
        public Map<String, Repetition> call() throws Exception {
            // Try to find all repetition of 3 or more character into the string (start to 3 and end at 10 inclusive)
            for (Integer letterCount = 3; startIndex + letterCount < fullText.length() && letterCount < 5 ; ++letterCount) {
                final String lookfor = fullText.substring(startIndex, startIndex + letterCount);
                Integer lastIndex = startIndex;
                final List<Integer> positions = new ArrayList<>();

                while(lastIndex != -1) {
                    lastIndex = fullText.indexOf(lookfor, lastIndex);

                    // If a repetition is found, we add a match to the list, and we search for another one.
                    if(lastIndex != -1) {
                        positions.add(lastIndex);
                        lastIndex += lookfor.length();
                    }
                }
                // If we found more than one occurence of the string, we store the repetition un a map.
                if (positions.size() > 1) {
                    map.put(lookfor, new Repetition(lookfor, positions));
                }
            }

            return map;
        }


    }
}
