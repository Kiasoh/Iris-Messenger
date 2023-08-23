#!/bin/bash
curl -X PUT $1:9200/chat -H 'Content-Type: application/json' -d'
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
        "type": "text", 
        "search_analyzer": "standard",
        "analyzer": "my_edge_ngram_analyzer"
      }
    }
  }
}'