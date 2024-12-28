package rmap.service;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class RandomNicknameGenerator {

    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String ALL = UPPERCASE + LOWERCASE + NUMBERS;

    private RandomNicknameGenerator() {
    }

    public static String generate() {
        String code = pickRandom(UPPERCASE, 5)
                + pickRandom(LOWERCASE, 4)
                + pickRandom(NUMBERS, 1);
        List<String> fragments = Arrays.asList(code.split(""));
        Collections.shuffle(fragments);
        return String.join("", fragments);
    }

    private static String pickRandom(String target, int n) {
        StringBuilder randomCode = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < n; i++) {
            randomCode.append(target.charAt(random.nextInt(target.length())));
        }
        return randomCode.toString();
    }

}
