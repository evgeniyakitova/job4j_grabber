create database grabber;
\c grabber;

create table if not exists posts (
    id serial primary key,
    name text,
    text text,
    link varchar(255) unique,
    created timestamp
)