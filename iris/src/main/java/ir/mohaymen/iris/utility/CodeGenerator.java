package ir.mohaymen.iris.utility;

import org.springframework.stereotype.Service;

import java.util.Random;
@Service
public class CodeGenerator {
    public String generateActivationCode() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random(System.currentTimeMillis());
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }
}
