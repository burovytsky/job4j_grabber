create database grabber;

create table posts(
id serial primary key,
link text unique,
name text,
description text,
created timestamp
);