# Kafka Avro producer and consumer

```bash
In this project, we are sending and consuming the avro message to the kafka topic
and saving into the MySQL DB:

1. docker-compose up
2. run the spring boot app
3. Post the avro object on :  "localhost:9000/sendStockHistory"

payload for stock history:

{
    "tradeQuantity":100,
    "tradeMarket": "NSE",
    "stockName": "Tata power limited",
    "tradeType": "Sell",
    "price": "250.25",
    "amount": 1000,
    "tradeId": 100
} 
 
```
