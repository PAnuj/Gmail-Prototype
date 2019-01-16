# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table folder (
  id                            integer auto_increment not null,
  email                         varchar(255),
  folder_type                   varchar(255),
  mail_id                       integer,
  is_read                       tinyint(1) default 0 not null,
  thread_id                     integer,
  time_stamp                    integer,
  from_user                     varchar(255),
  constraint pk_folder primary key (id)
);

create table mails (
  id                            integer auto_increment not null,
  body                          varchar(255),
  thread_id                     integer,
  time_stamp                    integer,
  attachment_id                 integer,
  constraint pk_mails primary key (id)
);

create table user (
  id                            integer auto_increment not null,
  name                          varchar(255),
  email                         varchar(255),
  geolocation                   varchar(255),
  domain                        varchar(255),
  constraint pk_user primary key (id)
);


# --- !Downs

drop table if exists folder;

drop table if exists mails;

drop table if exists user;

