package Lab1.kasiski;

import Lab1.kasiski.utils.CommonDivisors;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class KasiskiKeyLengthEstimator {

    private final CommonDivisors.CommonDivisorLimit maxDivisor = new CommonDivisors.CommonDivisorLimit(20);
    private final CommonDivisors commonDivisorsUtils = new CommonDivisors(maxDivisor);
    private final ProbableKeySelector selector = new ProbableKeySelector();

   //Нахожу длину ключа в зависимости от расстояния между словами повторяющимися
    public List<KeyLength> estimate(final String encoded) {
        final List<KeyLength> probableKeys;
        try {
            // Получаю все текстовые повторения
            probableKeys = new MultithreadedRepetitionCounter().count(encoded).parallelStream()
                    // Based on text repetition, we find distance between repetition and common divisors between these distances
                    .map(Repetition::getDistances)
                    .flatMap(commonDivisorsUtils::findFor)
                    // with this commons divisors, we create a list of KeyLength, which correspond to probables key's length.
                    // Remarks : we get the divisor 2 out of the field, i don't think someone would ever pick a 2 characters key.
                    .collect(Collectors.groupingBy(divisor -> divisor, Collectors.counting()))
                    .entrySet().stream()
                    .map(entry -> new KeyLength(entry.getKey(), entry.getValue().intValue()))
                    .collect(Collectors.toList());
        } catch (ExecutionException | InterruptedException e) {
            throw new IllegalArgumentException(e);
        }

        // Finally, we exclude non irrelevant keys from the list.
        return selector.removeUnlikelyKeys(probableKeys);
    }

}
