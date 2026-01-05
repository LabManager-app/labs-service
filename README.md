
1. cd labs-service

prevajanje:
2. mvn clean package -DskipTests

zagon:
3. cd ..
4. docker compose up --build

fe:
nvm install 20
5. npm run dev

docker status: docker ps -a


kubernetes:
kubectl apply -f labs-service/k8s/labs-service-configmap.yaml
kubectl apply -f labs-service/k8s/labs-service-deployment.yaml
kubectl apply -f labs-service/k8s/labs-service-service.yaml

kubectl apply -f users-service/k8s/
kubectl apply -f labs-service/k8s/
kubectl apply -f projects-service/k8s/


1. najprej narediš docker
docker build -t leakr109/users-service:latest ./users-service

2. narediš push na DockerHub
docker push leakr109/users-service:latest

docker push leakr109/frontend:latest

URL NA KATEREM TEČE:
minikube service frontend --url