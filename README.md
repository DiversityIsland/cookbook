# 📖 Книга Рецептов

Веб-приложение для управления кулинарными рецептами с минимальным функционалом.

## Технологии

- **Backend**: Kotlin + Spring Boot 3
- **Database**: PostgreSQL 15
- **Frontend**: Vanilla JavaScript + HTML + CSS
- **Build Tool**: Gradle

## Архитектура проекта

```
cookbook/
├── cookbook-api/       # API модуль с DTO
├── cookbook-app/       # Основное Spring Boot приложение
├── cookbook-domain/    # Доменные модели (Enums, Value Objects)
├── cookbook-ui/        # Frontend (HTML/CSS/JS)
└── docker-compose.yml  # PostgreSQL контейнер
```

## Функционал

- ✅ Управление блюдами (создание, просмотр, удаление)
- ✅ Управление рецептами для блюд
- ✅ Управление категориями
- ✅ Просмотр списка блюд
- ✅ Детальная информация о блюде
- ✅ Создание рецептов с указанием времени приготовления и количества порций

## Быстрый старт

### 1. Запустить PostgreSQL

```bash
docker-compose up -d
```

Это запустит PostgreSQL на порту 5432 с базой данных `cookbook`.

### 2. Собрать проект

```bash
./gradlew build
```

### 3. Запустить приложение

```bash
./gradlew :cookbook-app:bootRun
```

Приложение будет доступно по адресу: http://localhost:8080

## API Endpoints

### Dishes (Блюда)
- `GET /api/v1/dishes` - получить все блюда
- `GET /api/v1/dishes/{id}` - получить блюдо по ID
- `POST /api/v1/dishes` - создать блюдо
- `PUT /api/v1/dishes/{id}` - обновить блюдо
- `DELETE /api/v1/dishes/{id}` - удалить блюдо

### Recipes (Рецепты)
- `GET /api/v1/recipes` - получить все рецепты
- `GET /api/v1/recipes/{id}` - получить рецепт по ID
- `GET /api/v1/recipes/dish/{dishId}` - получить рецепты блюда
- `POST /api/v1/recipes/dish/{dishId}` - создать рецепт для блюда
- `PUT /api/v1/recipes/{id}` - обновить рецепт
- `DELETE /api/v1/recipes/{id}` - удалить рецепт

### Categories (Категории)
- `GET /api/v1/categories` - получить все категории
- `GET /api/v1/categories/{id}` - получить категорию по ID
- `POST /api/v1/categories` - создать категорию
- `PUT /api/v1/categories/{id}` - обновить категорию
- `DELETE /api/v1/categories/{id}` - удалить категорию

## Модель данных

```
Category (Категория)
  ├── id: UUID
  ├── name: String
  ├── description: String?
  └── dishes: [Dish] (через CategoryDish)

Dish (Блюдо)
  ├── id: UUID
  ├── name: String
  ├── description: String
  ├── image: Image?
  ├── recipes: [Recipe]
  └── categories: [Category] (через CategoryDish)

Recipe (Рецепт)
  ├── id: UUID
  ├── name: String
  ├── description: String
  ├── cookingTime: Duration
  ├── servings: Int
  ├── dish: Dish
  ├── ingredients: [Ingredient]
  └── steps: [CookingStep]

Ingredient (Ингредиент)
  ├── id: UUID
  ├── name: String
  ├── amount: BigDecimal
  ├── unit: MeasurementUnit
  ├── isOptional: Boolean
  └── recipe: Recipe

CookingStep (Шаг приготовления)
  ├── id: UUID
  ├── stepNumber: Int
  ├── description: String
  ├── estimatedTime: Duration?
  ├── image: Image?
  └── recipe: Recipe
```

## Остановка приложения

1. Остановить Spring Boot приложение: `Ctrl+C`
2. Остановить PostgreSQL: `docker-compose down`

## Конфигурация

Основные настройки находятся в `cookbook-app/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/cookbook
    username: postgres
    password: postgres
```

## Разработка

### Запуск тестов

```bash
./gradlew test
```

### Сборка без тестов

```bash
./gradlew build -x test
```

## Примечания

- База данных создается автоматически при первом запуске через Flyway миграции
- Frontend находится в `cookbook-ui/src/main/resources/static/`
- Все API endpoint'ы работают с JSON
- Duration для рецептов указывается в формате ISO 8601 (например, `PT45M` для 45 минут)
