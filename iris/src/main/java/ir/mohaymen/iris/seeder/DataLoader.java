package ir.mohaymen.iris.seeder;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        new UserSeeder().load();
    }
}
