-- create tables if not exists


create table if not exists users
(
    username    varchar(50)          not null
        constraint users_pk
            primary key,
    password    varchar(100)         not null,
    name        varchar,
    surname     varchar,
    description varchar,
    enabled     boolean default true not null
);

comment on table users is 'logs and pass';
comment on column users.enabled is 'if true - user account is active; if false - user account is canceled';

alter table users
    owner to postgres;

create unique index if not exists users_name_uindex
    on users (username);


-----------------------------------------------
create table if not exists authorities
(
    username  varchar(50) not null
        constraint fk_authorities_users
            references users,
    authority varchar(50) not null,
    constraint authorities_pk
        primary key (username, authority)
);

alter table authorities
    owner to postgres;

----------------------------------------------

create sequence if not exists files_id_seq
    as integer;

-----------------------------------------------
create table if not exists files
(
    id       bigint default nextval('files_id_seq'::regclass) not null
        constraint files_pk
            primary key,
    filename varchar                                          not null,
    username varchar(50)                                      not null
        constraint files_users_username_fk
            references users,
    size     bigint default 0                                 not null
);

alter table files
    owner to postgres;

create unique index if not exists files_id_uindex
    on files (id);

create unique index if not exists files_filename_uindex
    on files (filename, username);

-----------------------------------------------------


