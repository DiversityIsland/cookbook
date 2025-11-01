-- Вставка категорий
INSERT INTO category (id, name, description, created_at, updated_at) VALUES
    (gen_random_uuid(), 'Супы', 'Первые блюда, горячие и холодные супы', NOW(), NOW()),
    (gen_random_uuid(), 'Вторые блюда', 'Горячие блюда из мяса, рыбы и овощей', NOW(), NOW()),
    (gen_random_uuid(), 'Десерты', 'Сладкие блюда и выпечка', NOW(), NOW()),
    (gen_random_uuid(), 'Салаты', 'Холодные закуски и салаты', NOW(), NOW());

-- Вставка блюд
INSERT INTO dish (id, name, description, image_id, created_at, updated_at) VALUES
    (gen_random_uuid(), 'Борщ', 'Традиционный украинский суп с свеклой, капустой и мясом. Подается со сметаной и чесночными пампушками.', NULL, NOW(), NOW()),
    (gen_random_uuid(), 'Пельмени', 'Русское национальное блюдо - мясо в тонком тесте. Готовятся вручную и подаются с маслом или сметаной.', NULL, NOW(), NOW()),
    (gen_random_uuid(), 'Оливье', 'Классический праздничный салат с вареными овощами, колбасой и майонезом.', NULL, NOW(), NOW()),
    (gen_random_uuid(), 'Блины', 'Тонкие русские блинчики. Можно подавать с медом, сметаной, икрой или начинками.', NULL, NOW(), NOW()),
    (gen_random_uuid(), 'Шашлык', 'Маринованное мясо, жареное на углях. Традиционное блюдо кавказской кухни.', NULL, NOW(), NOW());

-- Связь блюд и категорий (пока пропустим, т.к. нужны ID из предыдущих вставок)

-- Вставка рецептов
INSERT INTO recipe (id, dish_id, name, description, cooking_time, servings, created_at, updated_at)
SELECT 
    gen_random_uuid(),
    d.id,
    r.name,
    r.description,
    r.cooking_time,
    r.servings,
    NOW(),
    NOW()
FROM dish d
CROSS JOIN (VALUES
    ('Борщ', 'Классический борщ', 'Традиционный рецепт борща на мясном бульоне с добавлением свеклы, капусты, картофеля и зажарки.', 5400000000000, 6),
    ('Пельмени', 'Сибирские пельмени', 'Пельмени с начинкой из трех видов мяса: говядина, свинина и баранина.', 7200000000000, 4),
    ('Оливье', 'Оливье по-домашнему', 'Классический рецепт салата Оливье с докторской колбасой и солеными огурцами.', 1800000000000, 6),
    ('Блины', 'Тонкие блины на молоке', 'Простой рецепт тонких блинов на молоке с добавлением яиц и сахара.', 2400000000000, 4),
    ('Шашлык', 'Шашлык из свинины', 'Сочный шашлык из свиной шейки, маринованный в луке с уксусом и специями.', 3600000000000, 5)
) AS r(dish_name, name, description, cooking_time, servings)
WHERE d.name = r.dish_name;

-- Вставка ингредиентов для борща
INSERT INTO ingredient (id, recipe_id, name, amount, unit, is_optional)
SELECT 
    gen_random_uuid(),
    r.id,
    i.name,
    i.amount,
    i.unit,
    i.is_optional
FROM recipe r
CROSS JOIN (VALUES
    ('Говядина на кости', 500, 'GRAM', false),
    ('Свекла', 300, 'GRAM', false),
    ('Капуста белокочанная', 300, 'GRAM', false),
    ('Картофель', 400, 'GRAM', false),
    ('Морковь', 150, 'GRAM', false),
    ('Лук репчатый', 150, 'GRAM', false),
    ('Томатная паста', 2, 'TABLESPOON', false),
    ('Чеснок', 3, 'PIECE', false),
    ('Соль', 1, 'TO_TASTE', false),
    ('Лавровый лист', 2, 'PIECE', true)
) AS i(name, amount, unit, is_optional)
WHERE r.name = 'Классический борщ';

-- Вставка ингредиентов для пельменей
INSERT INTO ingredient (id, recipe_id, name, amount, unit, is_optional)
SELECT 
    gen_random_uuid(),
    r.id,
    i.name,
    i.amount,
    i.unit,
    i.is_optional
FROM recipe r
CROSS JOIN (VALUES
    ('Мука', 500, 'GRAM', false),
    ('Яйца', 2, 'PIECE', false),
    ('Вода', 200, 'MILLILITER', false),
    ('Говядина', 200, 'GRAM', false),
    ('Свинина', 200, 'GRAM', false),
    ('Баранина', 100, 'GRAM', false),
    ('Лук репчатый', 200, 'GRAM', false),
    ('Соль', 1, 'TO_TASTE', false),
    ('Черный перец', 1, 'TO_TASTE', false)
) AS i(name, amount, unit, is_optional)
WHERE r.name = 'Сибирские пельмени';

-- Вставка ингредиентов для Оливье
INSERT INTO ingredient (id, recipe_id, name, amount, unit, is_optional)
SELECT 
    gen_random_uuid(),
    r.id,
    i.name,
    i.amount,
    i.unit,
    i.is_optional
FROM recipe r
CROSS JOIN (VALUES
    ('Картофель', 300, 'GRAM', false),
    ('Морковь', 200, 'GRAM', false),
    ('Яйца', 4, 'PIECE', false),
    ('Колбаса докторская', 300, 'GRAM', false),
    ('Огурцы соленые', 200, 'GRAM', false),
    ('Зеленый горошек', 200, 'GRAM', false),
    ('Майонез', 200, 'GRAM', false),
    ('Соль', 1, 'TO_TASTE', false)
) AS i(name, amount, unit, is_optional)
WHERE r.name = 'Оливье по-домашнему';

-- Вставка шагов приготовления для борща
INSERT INTO cooking_step (id, recipe_id, step_number, description, estimated_time, image_id)
SELECT 
    gen_random_uuid(),
    r.id,
    s.step_number,
    s.description,
    s.estimated_time,
    NULL
FROM recipe r
CROSS JOIN (VALUES
    (1, 'Сварить мясной бульон из говядины. Мясо залить холодной водой, довести до кипения, снять пену. Варить 1,5-2 часа на медленном огне.', 7200000000000),
    (2, 'Пока варится бульон, натереть свеклу на крупной терке. Обжарить на растительном масле с добавлением томатной пасты и небольшого количества бульона. Тушить 20 минут.', 1200000000000),
    (3, 'Нашинковать капусту, нарезать картофель кубиками. Морковь натереть на терке, лук мелко нарезать. Обжарить лук с морковью до золотистого цвета.', 900000000000),
    (4, 'Достать мясо из готового бульона. В кипящий бульон добавить картофель, варить 10 минут. Добавить капусту, варить еще 10 минут.', 1200000000000),
    (5, 'Добавить тушеную свеклу и зажарку из лука с морковью. Варить 10 минут. Добавить соль, перец, лавровый лист. Выключить огонь и дать настояться 15-20 минут.', 1800000000000)
) AS s(step_number, description, estimated_time)
WHERE r.name = 'Классический борщ';

-- Вставка шагов приготовления для блинов
INSERT INTO cooking_step (id, recipe_id, step_number, description, estimated_time, image_id)
SELECT 
    gen_random_uuid(),
    r.id,
    s.step_number,
    s.description,
    s.estimated_time,
    NULL
FROM recipe r
CROSS JOIN (VALUES
    (1, 'Взбить яйца с сахаром и солью. Добавить половину молока и перемешать.', 300000000000),
    (2, 'Постепенно добавить просеянную муку, размешивая венчиком до однородности. Влить оставшееся молоко и растительное масло.', 600000000000),
    (3, 'Разогреть сковороду, смазать маслом. Налить тонким слоем тесто, распределить по всей поверхности.', 120000000000),
    (4, 'Жарить блин с каждой стороны по 1-2 минуты до золотистого цвета. Повторить со всем тестом.', 1200000000000)
) AS s(step_number, description, estimated_time)
WHERE r.name = 'Тонкие блины на молоке';
