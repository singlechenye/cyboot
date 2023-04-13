create database usercenter;

use usercenter;

create table user
(
    id          bigint auto_increment
        primary key,
    userAccount varchar(255)                                        null,
    username    varchar(255)                                        null,
    password    varchar(255)                                        null,
    imageUrl    varchar(1024)                                       null,
    gender      tinyint                                             null,
    phone       varchar(255)                                        null,
    email       varchar(255)                                        null,
    userStatus  int unsigned              default '0'               null,
    createTime  datetime                  default CURRENT_TIMESTAMP null,
    updateTime  datetime                  default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    isDelete    tinyint unsigned zerofill default 0                 null,
    userRole    int                       default 0                 not null
);

