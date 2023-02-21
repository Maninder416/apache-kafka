curl --request POST \
  --url http://localhost:8082/topics/transaction-details \
  --header 'accept: application/vnd.kafka.v2+json' \
  --header 'content-type: application/vnd.kafka.avro.v2+json' \
  --data '{
    "key_schema": "{\"name\":\"key\",\"type\": \"string\"}",
    "value_schema_id": "input the correct id here",
    "records": [
        {
            "key" : "001",
            "value": {
                "balanceId": "001",
                "accountId" : "12345",
                "balance" : 12122.34
            }
        }
    ]
}'