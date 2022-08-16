drop table if exists planet CASCADE;
drop table if exists space_probe CASCADE;
drop sequence if exists planet_seq;
drop sequence if exists space_probe_seq;

create sequence planet_seq start with 1 increment by 1;
create sequence space_probe_seq start with 1 increment by 1;

create table planet
(
    id         bigint  not null primary key,
    height     integer not null,
    width      integer not null,
    created_at timestamp,
    updated_at timestamp
);

create table space_probe
(
    id         bigint     not null primary key,
    direction  varchar(5) not null,
    x          integer    not null,
    y          integer    not null,
    planet_id  bigint     not null
        constraint FK_PROBE_PLANET
            references planet
            on delete cascade,
    created_at timestamp,
    updated_at timestamp
);

alter table space_probe
    add constraint UK_SPACE_PROBE unique (x, y, planet_id);