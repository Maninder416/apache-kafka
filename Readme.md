Run the docker-compose file:

In this code, we are creating 3 topics:
   1. home-service
   2. takeaway-service
   3. user

we are sending the order object to the user topic. Afterwards, using filter we are checking either the delivery type is takeaway or homedeliver and according to it we are sending it to the different topic.

Body for post:

{
"item": "chicken",
"quantity": 9,
"deliveryType": "homeDelivery"
}

