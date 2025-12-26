
1. cd labs-service

prevajanje:
2. mvn clean package -DskipTests

zagon:
3. cd ..
4. docker compose up --build

fe:
5. npm run dev

docker status: docker ps -a