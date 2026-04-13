# K8s Deployment Guide

## Файлы:
- namespace.yaml - создание namespace
- deployment.yaml - поды (8 сервисов)
- service.yaml - сервисы
- ingress.yaml - внешний доступ

## Команды:

```bash
# Подключиться к кластеру (выполни в PowerShell)
yc managed-kubernetes cluster get-credentials --id ТВОЙ_ID --external

# Применить манифесты
kubectl apply -f k8s/

# Проверить
kubectl get pods
kubectl get svc
kubectl get ingress

# Логи
kubectl logs -f deployment/gateway
```

## URL после деплоя:
- Frontend: http://car-service.ru (или IP)
- API: http://car-service.ru/api
- Keycloak: http://car-service.ru/auth

## Важно:
1. Замени makvay/gateway:v1 на твой Docker Hub логин
2. Настрой DNS или hosts файл
3. Для HTTPS нужно настроить TLS сертификат
