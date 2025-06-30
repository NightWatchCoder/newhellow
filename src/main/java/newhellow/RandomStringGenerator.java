package newhellow;

import java.security.SecureRandom;

public class RandomStringGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();

    public String generateRandomString(int length) {
        if (length <= 0) throw new IllegalArgumentException("Length must be positive");

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randIndex));
        }
        return sb.toString();
    }
}
