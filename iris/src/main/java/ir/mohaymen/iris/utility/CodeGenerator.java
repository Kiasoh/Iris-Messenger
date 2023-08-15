package ir.mohaymen.iris.utility;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.Random;

public class CodeGenerator {
    public static String generateActivationCode() {
        Random rnd = new Random(System.currentTimeMillis());
        int number = rnd.nextInt(99999);
        return String.format("%05d", number);
    }
}
