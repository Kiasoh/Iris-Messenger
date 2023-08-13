package ir.mohaymen.iris.seeder;

import com.github.javafaker.Faker;

import java.util.Locale;

public interface Seeder {

    Locale locale = new Locale.Builder().setLanguage("en").setRegion("GB").build();
    Faker faker = new Faker(locale);

    void load();
}
