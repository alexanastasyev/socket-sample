Запуск:

docker compose up --build
docker exec -it chat_client1 java chat.ChatClientRunner
docker exec -it chat_client2 java chat.ChatClientRunner
docker exec -it chat_client3 java chat.ChatClientRunner