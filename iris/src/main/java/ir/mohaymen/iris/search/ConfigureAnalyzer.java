package ir.mohaymen.iris.search;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.MessageFormat;

@Component
@RequiredArgsConstructor
public class ConfigureAnalyzer implements CommandLineRunner {
    Logger logger= LoggerFactory.getLogger(ConfigureAnalyzer.class);

    @Value("${application.elasticsearch.host}")
    private String host="localhost";
    @Override
    public void run(String... args) throws Exception {
        try {
            logger.info(configureMessage());
            logger.info(configureChat());
            logger.info(configureContact());
        } catch (Exception e) {
            logger.error("error on configuring analyzer");
        }
    }

    private String executeCommand(String command) {

        StringBuffer output = new StringBuffer();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();

    }

    private String configureMessage() {
        return executeCommand(MessageFormat.format("""
                curl -X PUT {0}:9200/message -H 'Content-Type: application/json' -d'
                {
                  "settings": {
                    "analysis": {
                      "filter": {
                        "my_edge_ngram_filter":{
                          "type": "edge_ngram",
                          "min_gram": 1,
                          "max_gram": 10
                        }
                      },
                      "analyzer": {
                        "my_edge_ngram_analyzer":{
                          "type": "custom",
                          "tokenizer": "standard",
                          "filter": [
                            "lowercase",
                            "my_edge_ngram_filter"
                            ]
                        }
                      }
                    }
                  },
                  "mappings": {
                    "properties": {
                      "text":{
                        "type": "text",\s
                        "search_analyzer": "standard",
                        "analyzer": "my_edge_ngram_analyzer"
                      }
                    }
                  }
                }'
                """,host));
    }

    private String configureChat() {
        return executeCommand(MessageFormat.format("""
                curl -X PUT {0}:9200/chat -H 'Content-Type: application/json' -d'
                {
                  "settings": {
                    "analysis": {
                      "filter": {
                        "my_edge_ngram_filter":{
                          "type": "edge_ngram",
                          "min_gram": 1,
                          "max_gram": 10
                        }
                      },
                      "analyzer": {
                        "my_edge_ngram_analyzer":{
                          "type": "custom",
                          "tokenizer": "standard",
                          "filter": [
                            "lowercase",
                            "my_edge_ngram_filter"
                            ]
                        }
                      }
                    }
                  },
                  "mappings": {
                    "properties": {
                      "title":{
                        "type": "text",\s
                        "search_analyzer": "standard",
                        "analyzer": "my_edge_ngram_analyzer"
                      }
                    }
                  }
                }'
                """,host));
    }

    private String configureContact() {
        return executeCommand(MessageFormat.format("""
                curl -X PUT {0}:9200/contact -H 'Content-Type: application/json' -d'
                {
                  "settings": {
                    "analysis": {
                      "filter": {
                        "my_edge_ngram_filter":{
                          "type": "edge_ngram",
                          "min_gram": 1,
                          "max_gram": 10
                        }
                      },
                      "analyzer": {
                        "my_edge_ngram_analyzer":{
                          "type": "custom",
                          "tokenizer": "standard",
                          "filter": [
                            "lowercase",
                            "my_edge_ngram_filter"
                            ]
                        }
                      }
                    }
                  },
                  "mappings": {
                    "properties": {
                      "firstName":{
                        "type": "text",\s
                        "search_analyzer": "standard",
                        "analyzer": "my_edge_ngram_analyzer"
                      },
                      "secondName":{
                        "type": "text",\s
                        "search_analyzer": "standard",
                        "analyzer": "my_edge_ngram_analyzer"
                      }
                    }
                  }
                }'
                """,host));
    }
}
