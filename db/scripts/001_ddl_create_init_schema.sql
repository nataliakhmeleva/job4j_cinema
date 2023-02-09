create table if not exists files
(
    id   serial primary key,
    name varchar not null,
    path varchar not null unique
);

create table if not exists genres
(
    id   serial primary key,
    name varchar unique not null
);

create table if not exists films
(
    id                  serial primary key,
    name                varchar                    not null,
    description         varchar                    not null,
    year_of_issue       int                        not null,
    genre_id            int references genres (id),
    minimal_age         int                        not null,
    duration_in_minutes int                        not null,
    file_id             int references files (id)
);

create table if not exists halls
(
    id          serial primary key,
    name        varchar not null,
    row_count   int     not null,
    place_count int     not null,
    description varchar not null
);

create table if not exists film_sessions
(
    id         serial primary key,
    film_id    int references films (id),
    halls_id   int references halls (id),
    start_time timestamp,
    price      varchar not null
);

create table if not exists users
(
    id        serial primary key,
    full_name varchar        not null,
    email     varchar unique not null,
    password  varchar        not null
);

create table if not exists tickets
(
    id           serial primary key,
    session_id   int references film_sessions (id),
    row_number   int                               not null,
    place_number int                               not null,
    user_id      int                               not null,
    unique (session_id, user_id, row_number, place_number)
);