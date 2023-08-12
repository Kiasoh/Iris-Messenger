package ir.mohaymen.iris.utility;

import org.springframework.stereotype.Service;

import java.util.Random;
@Service
public class CodeGenerator {
    public String generateActivationCode() {
        Random rnd = new Random(System.currentTimeMillis());
        int number = rnd.nextInt(99999);
        return String.format("%05d", number);
    }
}
