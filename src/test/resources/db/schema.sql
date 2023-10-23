CREATE TABLE `user`
(
    `id`         bigint(20)  NOT NULL AUTO_INCREMENT,
    `name`       varchar(20) NOT NULL COMMENT '名字',
    `password`   varchar(48) NOT NULL DEFAULT 'e10adc3949ba59abbe56e057f20f883e' COMMENT '加密后的密码',
    `phone`      varchar(20) NOT NULL COMMENT '手机号码',
    `age`        smallint(3) NOT NULL COMMENT '年龄',
    `permission` smallint(1)          DEFAULT '0' COMMENT '是否授权，0表示未授权，1表示已授权',
    `admin`      smallint(1)          DEFAULT '0' COMMENT '是否管理员，0表示未授权，1表示已授权',
    PRIMARY KEY (`id`),
    UNIQUE KEY `name_phone` (`name`, `phone`) COMMENT '一个人可能有多个手机号码'
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8;