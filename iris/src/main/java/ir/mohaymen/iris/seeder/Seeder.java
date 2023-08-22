package ir.mohaymen.iris.seeder;

import com.github.javafaker.Faker;

import java.util.Locale;
import java.util.Random;

public interface Seeder {

    Locale locale = new Locale.Builder().setLanguage("en").setRegion("GB").build();
    Faker faker = new Faker(locale, new Random(100));

    void load();

    void clearReferences();
}
