drop table if exists planet CASCADE;
drop table if exists space_probe CASCADE;
drop sequence if exists planet_seq;
drop sequence if exists space_probe_seq;

create sequence planet_seq start with 1 increment by 1;
create sequence space_probe_seq start with 1 increment by 1;

create table planet
(
    id         bigint  not null primary key,
    created_at timestamp,
    height     integer not null,
    updated_at timestamp,
    width      integer not null
);

create table space_probe
(
    id         bigint       not null primary key,
    created_at timestamp,
    direction  varchar(5) not null,
    x          integer      not null,
    y          integer      not null,
    updated_at timestamp,
    planet_id  bigint       not null
        constraint FK_PROBE_PLANET
            references planet
            on delete cascade
);

alter table space_probe
    add constraint UK_SPACE_PROBE unique (x, y, planet_id);