package ir.mohaymen.iris.seeder;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import org.aspectj.weaver.ISourceContext;

import java.util.Locale;

public interface Seeder {

    Locale locale = new Locale.Builder().setLanguage("en").setRegion("GB").build();
    FakeValuesService fakeValuesService = new FakeValuesService(locale, new RandomService());
    Faker faker = new Faker(locale);

    void load();
}
