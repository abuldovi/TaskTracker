create sequence "User_id_seq";

alter sequence "User_id_seq" owner to postgres;

create sequence "Projects_id_seq";

alter sequence "Projects_id_seq" owner to postgres;

create sequence "Tickets_id_seq";

alter sequence "Tickets_id_seq" owner to postgres;

create table userentity
(
    id        integer generated always as identity
        constraint "User_pkey"
            primary key,
    username  varchar(20)  not null,
    password  varchar(256) not null,
    firstname varchar,
    surname   varchar,
    position  varchar(20),
    createdat timestamp    not null,
    changedat timestamp,
    role      varchar(20) default 'ROLE_USER'::character varying,
    email     varchar      not null
);

alter table userentity
    owner to postgres;

alter sequence "User_id_seq" owned by userentity.id;

create unique index user_username_uindex
    on userentity (username);

create table projects
(
    id           integer generated always as identity
        constraint "Projects_pkey"
            primary key,
    name         varchar(20) not null,
    status       integer default 0,
    description  varchar,
    createdat    timestamp   not null,
    changedat    timestamp,
    createdby_id integer     not null
        constraint projects_userentity__fk
            references userentity
);

alter table projects
    owner to postgres;

alter sequence "Projects_id_seq" owned by projects.id;

create unique index projects_name_uindex
    on projects (name);

create table ticketDtos
(
    id           integer generated always as identity
        constraint "Tickets_pkey"
            primary key,
    name         varchar(20) not null,
    status       varchar(20) default 'Not Started'::character varying,
    description  varchar,
    createdat    timestamp   not null,
    changedat    timestamp,
    createdby_id integer
        constraint tickets___fk
            references userentity,
    project_id   integer
        references projects,
    constraint name_project_id
        unique (name, project_id)
);

alter table ticketDtos
    owner to postgres;

alter sequence "Tickets_id_seq" owned by ticketDtos.id;

create table user_tickets
(
    user_id    integer not null
        constraint "User_Tickets_User_id_fkey"
            references userentity,
    tickets_id integer not null
        constraint "User_Tickets_Tickets_id_fkey"
            references ticketDtos,
    constraint "User_Tickets_pkey"
        primary key (user_id, tickets_id)
);

alter table user_tickets
    owner to postgres;

create table user_projects
(
    user_id     integer not null
        constraint "User_Projects_User_id_fkey"
            references userentity,
    projects_id integer not null
        constraint "User_Projects_Projects_id_fkey"
            references projects,
    constraint "User_Projects_pkey"
        primary key (user_id, projects_id)
);

alter table user_projects
    owner to postgres;

create table owner_projects
(
    user_id     integer not null
        constraint "Owner_Projects_Owner_id_fkey"
            references userentity,
    projects_id integer not null
        constraint "Owner_Projects_Projects_id_fkey"
            references projects,
    constraint "Owner_Projects_pkey"
        primary key (user_id, projects_id)
);

alter table owner_projects
    owner to postgres;

create table ticket_changelog
(
    id          integer generated always as identity
        primary key,
    description varchar   not null,
    createdat   timestamp not null,
    madeby_id   integer
        references userentity,
    ticket_id   integer
        references ticketDtos
);

alter table ticket_changelog
    owner to postgres;

create table project_changelog
(
    id          integer generated always as identity
        primary key,
    description varchar   not null,
    createdat   timestamp not null,
    madeby_id   integer
        references userentity,
    project_id  integer
        references projects
);

alter table project_changelog
    owner to postgres;

create table owner_tickets
(
    user_id    integer not null
        constraint "Owner_Tickets_User_id_fkey"
            references userentity,
    tickets_id integer not null
        constraint "Owner_Tickets_Tickets_id_fkey"
            references ticketDtos,
    constraint "Owner_Tickets_pkey"
        primary key (user_id, tickets_id)
);

alter table owner_tickets
    owner to postgres;

