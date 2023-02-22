/*drop table if exists task;

drop table if exists user_Info;

create table task (id varchar(255) not null, completed boolean, created_at timestamp, description varchar(255), updated_at timestamp, owner varchar(255), primary key (id));

create table user_Info (id varchar(255) not null, age integer, created_at timestamp, email varchar(255), image bytea, name varchar(255), user_password varchar(255), updated_at timestamp, primary key (id));

alter table if exists user_Info add constraint UK_ob8kqyqqgmefl0aco34akdtpe unique (email);

alter table if exists task add constraint FKkgglniwwk9fwbyagsyuico04y foreign key (owner) references user_Info (id);*/