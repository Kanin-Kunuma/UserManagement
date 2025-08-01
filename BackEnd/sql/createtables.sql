-- auto-generated definition
create table user
(
    id           bigint auto_increment comment 'id'
        primary key,
    username     varchar(256)                        null comment '用户名称',
    userAccount  varchar(256)                        not null comment '账号',
    avatarUrl    varchar(256)                        null comment '用户头像',
    gender       tinyint                             null comment '性别',
    userPassword varchar(512)                        null comment '密码',
    phone        varchar(128)                        not null comment '电话',
    email        varchar(512)                        null comment '邮箱',
    userStatus   int       default 0                 null comment '用户状态 0 - 正常',
    createTime   datetime  default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime  default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    isDelete     tinyint   default 0                 null comment '是否删除',
    userRole     int       default 0                 null comment '0-普通用户 1 - 管理员'
)
    comment '用户';