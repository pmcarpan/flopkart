package lab.it.arpan.flopkart.utils;

import java.util.Optional;

public class NumberParseUtilities {
    public static Optional<Long> parseLong(String input) {
        try {
            return Optional.of(Long.parseLong(input));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
