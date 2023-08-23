package ir.mohaymen.iris.search;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

@Component
@Order(1)
@RequiredArgsConstructor
public class ConfigureAnalyzer implements CommandLineRunner {
    Logger logger = LoggerFactory.getLogger(ConfigureAnalyzer.class);
    @Value("${application.resource.path}")
    private String resourcePath;
    @Value("${application.elasticsearch.host}")
    private String host = "localhost";

    public void run(String... args) throws Exception {

        try {
            logger.info("start curl configure analyzer");
            configureMessage();
            configureChat();
            configureContact();
            logger.info("end curl configure analyzer");
        } catch (Exception e) {
            logger.error("error on configuring analyzer");
        }
    }
    private void executeCommandWithArgs(String path,String args) {
        String command=path+" "+args;
        try {
            logger.info(command);
            Process process = Runtime.getRuntime().exec(command);
            logOutput(process.getInputStream(), "");
            logOutput(process.getErrorStream(), "Error: ");
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void logOutput(InputStream inputStream, String prefix) {
        new Thread(() -> {
            Scanner scanner = new Scanner(inputStream, "UTF-8");
            while (scanner.hasNextLine()) {
                synchronized (this) {
                    logger.info(prefix + scanner.nextLine());
                }
            }
            scanner.close();
        }).start();
    }
    private void configureMessage() {
        executeCommandWithArgs(resourcePath + "/scripts/elastic_configure_analyzer_message.sh", host);
    }

    private void configureChat() {
        executeCommandWithArgs(resourcePath + "/scripts/elastic_configure_analyzer_chat.sh", host);
    }

    private void configureContact() {
        executeCommandWithArgs(resourcePath + "/scripts/elastic_configure_analyzer_contact.sh", host);
    }
}
