create database grabber;

create table post(
id serial primary key,
link varchar(500),
name varchar(500),
description varchar(12000),
created timestamp
);