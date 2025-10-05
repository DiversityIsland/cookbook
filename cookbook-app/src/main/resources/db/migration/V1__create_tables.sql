create table image (
    id uuid primary key,
    data bytea not null,
    content_type varchar(100) not null,
    size bigint not null,
    created_at timestamptz not null
);

create table dish (
    id uuid primary key,
    name varchar(255) not null,
    description text not null,
    image_id uuid references image(id),
    created_at timestamptz not null,
    updated_at timestamptz not null
);

create table category (
    id uuid primary key,
    name varchar(255) not null,
    description text,
    created_at timestamptz not null,
    updated_at timestamptz not null
);

create table category_dish (
    id uuid primary key,
    dish_id uuid not null references dish(id),
    category_id uuid not null references category(id)
);

create table recipe (
    id uuid primary key,
    dish_id uuid not null references dish(id),
    name varchar(255) not null,
    description text not null,
    cooking_time numeric not null,
    servings int not null,
    created_at timestamptz not null,
    updated_at timestamptz not null
);

create table ingredient (
    id uuid primary key,
    recipe_id uuid not null references recipe(id),
    name varchar(255) not null,
    amount decimal not null,
    unit varchar(50) not null,
    is_optional boolean not null default false
);

create table cooking_step (
    id uuid primary key,
    recipe_id uuid not null references recipe(id),
    step_number int not null,
    description text not null,
    estimated_time numeric,
    image_id uuid references image(id),
    constraint cooking_step_recipe_step_number_key unique (recipe_id, step_number)
);

-- индексы для поиска по имени
create index idx_dish_name on dish(name);
create index idx_category_name on category(name);
create index idx_recipe_name on recipe(name);
