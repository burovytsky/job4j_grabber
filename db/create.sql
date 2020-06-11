create database grabber;

create table posts(
id serial primary key,
link varchar(128) unique,
name varchar(128),
description text,
created timestamp
);