# 🚗 Car Service Auto — Автосервис B2B Платформа

![Java](https://img.shields.io/badge/Java-17-blue)
![React](https://img.shields.io/badge/React-18-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3-green)
![Kubernetes](https://img.shields.io/badge/K8s-Ready-purple)

Полноценная микросервисная платформа для автосервиса с веб-интерфейсом.

---

## 📋 Содержание

1. [Описание системы](#описание-системы)
2. [Архитектура](#архитектура)
3. [Быстрый старт](#быстрый-старт)
4. [Настройка Keycloak](#настройка-keycloak)
5. [Использование системы](#использование-системы)
6. [API Документация](#api-документация)
7. [Деплой в Kubernetes](#деплой-в-kubernetes)
8. [Устранение проблем](#устранение-проблем)

---

## 📝 Описание системы

**Car Service Auto** — это микросервисная платформа для управления автосервисом, включающая:

- **7 микросервисов** на Spring Boot
- **React frontend** с авторизацией через Keycloak
- **PostgreSQL** база данных
- **Apache Kafka** для межсервисного взаимодействия
- **Docker** и **Kubernetes** готовность

### Функционал

| Модуль | Возможности |
|--------|-------------|
| **Клиенты** | Добавление клиентов и их автомобилей |
| **Мастера** | Управление мастерами и специализациями |
| **Заявки** | Создание и отслеживание заявок на ремонт |
| **Склад** | Учёт запчастей, резервирование |
| **Уведомления** | Автоматические уведомления о статусе |
| **НСИ** | Справочники: марки авто, услуги, категории запчастей |

---

## 🏗️ Архитектура

```
┌─────────────────────────────────────────────────────────────────┐
│                        Frontend (React)                          │
│                         http://localhost:3000                   │
└─────────────────────────────────────────────────────────────────┘
                                  │
                                  ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Spring Cloud Gateway                         │
│                         http://localhost:8081                   │
│                     (Авторизация, маршрутизация)               │
└─────────────────────────────────────────────────────────────────┘
                                  │
         ┌──────────────┬─────────┴─────────┬──────────────┐
         │              │                   │              │
    ┌────▼────┐   ┌────▼────┐        ┌────▼────┐   ┌────▼────┐
    │ Claims  │   │ Clients │        │ Masters │   │Warehouse│
    │  :8082  │   │  :8083  │        │  :8084  │   │  :8085  │
    └────┬────┘   └────┬────┘        └────┬────┘   └────┬────┘
         │              │                   │              │
         └──────────────┴─────────┬─────────┴──────────────┘
                                 │
              ┌──────────────────┼──────────────────┐
              │                  │                  │
        ┌─────▼─────┐    ┌──────▼──────┐   ┌──────▼──────┐
        │   NSI    │    │Notification │   │   PostgreSQL │
        │  :8091   │    │   :8086     │   │    :5432     │
        └──────────┘    └─────────────┘   └──────────────┘
```

### Порты сервисов

| Порт | Сервис | Описание |
|------|--------|----------|
| 3000 | Frontend | Веб-интерфейс |
| 8081 | Gateway | API Gateway |
| 8082 | Claims | Заявки |
| 8083 | Clients | Клиенты |
| 8084 | Masters | Мастера |
| 8085 | Warehouse | Склад |
| 8086 | Notifications | Уведомления |
| 8091 | NSI | Справочники |
| 8180 | Keycloak | Авторизация |
| 5432 | PostgreSQL | База данных |
| 6379 | Redis | Кэш |

---

## 🚀 Быстрый старт

### Вариант 1: Docker Compose (рекомендуется)

```bash
# 1. Клонировать проект
git clone https://github.com/Makvay/car-service-auto.git
cd car-service-auto

# 2. Запустить все сервисы
docker-compose up -d

# 3. Проверить статус
docker-compose ps
```

**После запуска:**
- 🌐 Frontend: http://localhost:3000
- 🔐 Keycloak: http://localhost:8180 (admin/admin)
- 📊 Gateway: http://localhost:8081

### Вариант 2: Локальная разработка

```bash
# Сборка всех сервисов
./gradlew clean build -x test

# Запуск каждого сервиса в отдельном терминале
./gradlew :common-service-parent:gateway-service:bootRun
./gradlew :common-service-parent:claim-microservice:bootRun
./gradlew :common-service-parent:client-microservice:bootRun
./gradlew :common-service-parent:master-microservice:bootRun
./gradlew :common-service-parent:warehouse-microservice:bootRun
./gradlew :common-service-parent:notification-microservice:bootRun
./gradlew :common-service-parent:nsi-microservice:bootRun
```

### Сборка Docker образов

```bash
# Собрать и запустить
cd car-service-frontend
npm run build

# Собрать образы
docker build -t makvay/gateway:v1 ./docker/gateway
docker build -t makvay/claims:v1 ./docker/claims
# ... и так далее для всех сервисов

# Запустить
docker-compose up -d
```

---

## 🔐 Настройка Keycloak

### 1. Доступ к Keycloak

Откройте http://localhost:8180 и войдите:
- **Логин:** `admin`
- **Пароль:** `admin`

### 2. Создание Realm

1. Нажмите **Create realm**
2. Имя: `car-service`
3. Нажмите **Create**

### 3. Настройка Client

1. Перейдите в **Clients** → **Create**
2. **Client ID:** `car-service-frontend`
3. **Client Protocol:** `openid-connect`
4. **Root URL:** `http://localhost:3000`
5. **Valid Redirect URIs:** `http://localhost:3000/*`
6. **Web Origins:** `http://localhost:3000`
7. **Access Type:** `public`
8. **Direct Access Grants:** Enabled

### 4. Создание пользователей

1. Перейдите в **Users** → **Add user**
2. Заполните данные (username, email, first name, last name)
3. Нажмите **Create**
4. Перейдите в **Credentials** → **Set password**

---

## 📖 Использование системы

### Экран входа

```
┌─────────────────────────────────────────────────────────────┐
│                    Car Service Auto                          │
│                                                              │
│                    [Логотип автосервиса]                     │
│                                                              │
│   ┌─────────────────────────────────────────────────────┐   │
│   │  🔑 Вход в систему                                  │   │
│   ├─────────────────────────────────────────────────────┤   │
│   │  Логин:     [________________]                      │   │
│   │  Пароль:    [________________]                      │   │
│   │                        [Показать]                   │   │
│   │                                                     │   │
│   │              [  ВОЙТИ  ]                           │   │
│   │                                                     │   │
│   └─────────────────────────────────────────────────────┘   │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

**Порядок работы:**
1. Войдите в систему (логин/пароль из Keycloak)
2. Перейдите в раздел "Документация" в боковом меню
3. Следуйте инструкциям

### Порядок работы

```
┌────────────────────────────────────────────────────────────────┐
│  📋 ПОРЯДОК РАБОТЫ                                            │
│                                                                │
│  1️⃣  Создайте Клиентов (Clients)                               │
│       → Добавьте клиента и его автомобиль                     │
│                                                                │
│  2️⃣  Добавьте Мастеров (Masters)                              │
│       → Укажите ФИО и специализацию                           │
│                                                                │
│  3️⃣  Создайте Заявку (Claims)                                 │
│       → Выберите клиента и услугу                             │
│       → Укажите дату                                          │
│                                                                │
│  4️⃣  Зарезервируйте Запчасти (Warehouse)                      │
│       → Укажите ID заявки                                     │
│       → Выберите запчасти                                     │
│       → Укажите количество                                    │
│                                                                │
│  5️⃣  Следите за Уведомлениями (Notifications)                 │
│       → Автоматические уведомления о статусе                   │
│                                                                │
└────────────────────────────────────────────────────────────────┘
```

### Главная панель (Dashboard)

```
┌─────────────────────────────────────────────────────────────────┐
│ [Меню]   Dashboard  —  Car Service B2B           [🔔] [👤] [RU] │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌────────────┐ ┌────────────┐ ┌────────────┐ ┌────────────┐   │
│  │ Активные   │ │ В работе   │ │ Готовые    │ │ Всего      │   │
│  │   заявки   │ │            │ │            │ │ клиентов   │   │
│  │     12     │ │     5     │ │     8     │ │     45    │   │
│  └────────────┘ └────────────┘ └────────────┘ └────────────┘   │
│                                                                 │
│  ┌────────────────────────────────────────────────────────┐   │
│  │ 📊 График заявок по месяцам                             │   │
│  │                                                        │   │
│  │   янв  фев  мар  апр  май  июн                        │   │
│  │   ▓▓   ▓▓▓   ▓▓▓▓  ▓▓▓   ▓▓▓▓  ▓▓▓▓▓                  │   │
│  └────────────────────────────────────────────────────────┘   │
│                                                                 │
│  ┌────────────────────────────────────────────────────────┐   │
│  │ 📝 Последние заявки                                     │   │
│  │ ─────────────────────────────────────────────────────  │   │
│  │ #001 | Toyota Camry | Замена масла | В работе         │   │
│  │ #002 | Honda Civic   | Диагностика | Новая            │   │
│  │ #003 | BMW X5        | ТО        | Готова             │   │
│  └────────────────────────────────────────────────────────┘   │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### Боковое меню

```
┌─────────────────────────────────────────────────────────────────┐
│ [Лого] Car Service                                              │
│                                                                 │
│ ─────────────────────────────────────────────────────────────  │
│ 🏠  Дашборд                                                    │
│ 📋  Заявки                                                      │
│ 👥  Клиенты                                                     │
│ 🔧  Мастера                                                     │
│ 📦  Склад                                                       │
│ 🔔  Уведомления                                                 │
│ 📚  НСИ                                                         │
│                                                                 │
│ ─────────────────────────────────────────────────────────────  │
│ 📖  Документация     ← НОВОЕ                                    │
│                                                                 │
│ ─────────────────────────────────────────────────────────────  │
│ Gateway: localhost:8081                                         │
└─────────────────────────────────────────────────────────────────┘
```

### Статусы заявок

| Статус | Описание | Цвет |
|--------|----------|------|
| NEW | Новая заявка | Синий |
| IN_PROGRESS | В работе | Жёлтый |
| WAITING_PARTS | Ожидает запчасти | Оранжевый |
| DONE | Готова | Зелёный |
| CANCELLED | Отменена | Красный |

---

## 📚 API Документация

### Swagger UI

После запуска откройте:
- Gateway: http://localhost:8081/swagger-ui.html

### Основные endpoints

#### Claims (Заявки)
```
POST   /api/v1/claims              - Создать заявку
GET    /api/v1/claims              - Список заявок
GET    /api/v1/claims/{id}         - Детали заявки
PUT    /api/v1/claims/{id}/status - Изменить статус
```

#### Clients (Клиенты)
```
POST   /api/client                 - Создать клиента
GET    /api/client                - Список клиентов
GET    /api/client/{id}           - Детали клиента
POST   /api/client/vehicle        - Добавить автомобиль
```

#### Masters (Мастера)
```
POST   /api/v1/masters            - Создать мастера
GET    /api/v1/masters            - Список мастеров
GET    /api/v1/masters/active     - Активные мастера
PUT    /api/v1/masters/{id}       - Обновить мастера
```

#### Warehouse (Склад)
```
POST   /api/v1/parts              - Создать запчасть
GET    /api/v1/parts              - Список запчастей
POST   /api/v1/reservations       - Создать резерв
GET    /api/v1/reservations/claim/{claimId} - Резервы заявки
```

#### NSI (Справочники)
```
GET    /api/nsi/vehicle-brands    - Марки автомобилей
GET    /api/nsi/services          - Виды услуг
GET    /api/nsi/part-categories  - Категории запчастей
```

---

## ☸️ Деплой в Kubernetes

### Требования
- Kubernetes кластер (Yandex Cloud, DigitalOcean, etc.)
- kubectl настроенный на кластер
- Docker образы в registry

### Файлы манифестов

```bash
cd k8s

# Применить все манифесты
kubectl apply -f namespace.yaml
kubectl apply -f deployment.yaml
kubectl apply -f service.yaml
kubectl apply -f ingress.yaml

# Проверить статус
kubectl get pods -n car-service
kubectl get services -n car-service
```

### Структура deployment

```yaml
# deployment.yaml включает:
- Gateway (реплика: 1)
- Claims (реплика: 1)
- Clients (реплика: 1)
- Masters (реплика: 1)
- Warehouse (реплика: 1)
- Notifications (реплика: 1)
- NSI (реплика: 1)
- Frontend (реплика: 1)
```

---

## 🔧 Устранение проблем

### Frontend не подключается к API

Проверьте переменные окружения:
```env
REACT_APP_API_GATEWAY=http://localhost:8081
REACT_APP_KEYCLOAK_URL=http://localhost:8180
REACT_APP_REALM=car-service
REACT_APP_CLIENT_ID=car-service-frontend
```

### Keycloak ошибка авторизации

1. Проверьте URL Keycloak в конфигурации
2. Проверьте Client settings в Keycloak
3. Проверьте Credentials пользователя

### Ошибка подключения к БД

```bash
# Проверить подключение к PostgreSQL
docker exec -it car-postgres psql -U car_user -d car_service

# Проверить схемы
\dn
```

### Логи сервисов

```bash
# Посмотреть логи конкретного сервиса
docker-compose logs gateway
docker-compose logs claims
docker-compose logs frontend

# Все логи
docker-compose logs -f
```

---

## 📁 Структура проекта

```
car-service-auto/
├── docker/                    # Docker файлы
│   ├── gateway/
│   ├── claims/
│   ├── clients/
│   ├── masters/
│   ├── warehouse/
│   ├── notifications/
│   ├── nsi/
│   └── frontend/
├── k8s/                      # Kubernetes манифесты
├── car-service-frontend/     # React frontend
├── common-dto-parent/        # DTO
├── common-entity-parent/     # JPA entities
├── common-service-parent/    # Microservices
└── docker-compose.yml       # Локальный запуск
```

---

## 🔗 Полезные ссылки

| Сервис | URL |
|--------|-----|
| Frontend | http://localhost:3000 |
| Keycloak | http://localhost:8180 |
| Gateway API | http://localhost:8081 |
| Swagger | http://localhost:8081/swagger-ui.html |

---

## 📄 Лицензия

MIT License

---

**Автор:** Makvay  
**Версия:** 1.0.0