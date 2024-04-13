package rmap.service;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class CodeGenerator {

    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL = "!@#$%";
    private static final String ALL = UPPERCASE + LOWERCASE + NUMBERS + SPECIAL;

    private CodeGenerator() {
    }

    public static String generateRandomCode() {
        String code = pickRandom(UPPERCASE, 2)
                + pickRandom(LOWERCASE, 2)
                + pickRandom(NUMBERS, 1)
                + pickRandom(SPECIAL, 1);
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
