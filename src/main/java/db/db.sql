create user soft_manager with password 'soft_manager' ;

ALTER USER soft_manager WITH PASSWORD 'soft_manager';

create database soft_manager_dev with encoding='utf8' ;
create database soft_manager_prod with encoding='utf8' ;
create database soft_manager_test with encoding='utf8' ;

grant all privileges on database soft_manager_dev to soft_manager ;
grant all privileges on database soft_manager_prod to soft_manager;
grant all privileges on database soft_manager_test to soft_manager;

\connect soft_manager_dev;
create schema extensions;
create extension hstore schema extensions;
ALTER DATABASE soft_manager_dev SET search_path to "$user",public,extensions;
alter database soft_manager_dev owner to soft_manager;
alter schema public owner to soft_manager;
alter schema extensions owner to soft_manager;
GRANT USAGE ON SCHEMA public to soft_manager;

\connect soft_manager_prod;
create schema extensions;
create extension hstore schema extensions;
ALTER DATABASE soft_manager_prod SET search_path to "$user",public,extensions;
alter database soft_manager_prod owner to soft_manager;
alter schema public owner to soft_manager;
alter schema extensions owner to soft_manager;
GRANT USAGE ON SCHEMA public to soft_manager;


\connect soft_manager_test;
create schema extensions;
create extension hstore schema extensions;
ALTER DATABASE soft_manager_test SET search_path to "$user",public,extensions;
alter database soft_manager_test owner to soft_manager;
alter schema public owner to soft_manager;
alter schema extensions owner to soft_manager;
GRANT USAGE ON SCHEMA public to soft_manager;
