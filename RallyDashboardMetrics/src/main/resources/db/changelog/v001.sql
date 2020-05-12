create table if not exists project
(
    id   bigserial not null,
    project_id  text      not null,
    project_name text      not null,
    primary key (id)
);

create table if not exists dashboard
(
    id                         bigserial not null,
    iteration                  text      not null,
    type                       text      not null,
    story_point                numeric   not null,
    story_point_of_calculation numeric   not null,
    remain_story_point         numeric,
    start_date                 DATE      not null,
    end_date                   DATE      not null,
    project_id                 bigserial not null,
    primary key (id),
    foreign key (project_id) references project (id)
);

create table if not exists release
(
    id                 bigserial not null,
    release_name       text      not null,
    release_start_date DATE      not null,
    release_date       DATE      not null,
    state              text      not null,
    day_of_diff        numeric,
    project_id         bigserial not null,
    primary key (id),
    foreign key (project_id) references project (id)
);