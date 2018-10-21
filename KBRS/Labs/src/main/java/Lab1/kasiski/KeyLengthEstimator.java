package Lab1.kasiski;

import java.util.List;

public interface KeyLengthEstimator {

    List<KeyLength> estimate(final String encoded);

}
