
1. cd labs-service

prevajanje:
2. mvn clean package -DskipTests

zagon:
3. cd ..
4. docker compose up --build

docker status: docker ps -a