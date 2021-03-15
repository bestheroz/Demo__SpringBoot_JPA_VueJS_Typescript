CREATE TABLE MEMBER
(
    ID            BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    USER_ID        VARCHAR(100)             NOT NULL,
    PASSWORD       CHAR(128)                NULL,
    NAME           VARCHAR(100)             NOT NULL,
    AUTHORITY      INT(3)                   NOT NULL,
    LOGIN_FAIL_CNT INT(1)  DEFAULT 0        NOT NULL,
    AVAILABLE         BOOLEAN               NOT NULL,
    THEME          VARCHAR(10)              NULL,
    TOKEN          VARCHAR(4000)            NULL,
    EXPIRED        DATETIME                 NOT NULL,
    CREATED_BY     VARCHAR(100)             NOT NULL,
    CREATED        DATETIME                 NOT NULL,
    UPDATED_BY     VARCHAR(100)             NOT NULL,
    UPDATED        DATETIME                 NOT NULL
);
CREATE TABLE MENU
(
    ID            BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    NAME          VARCHAR(1000) NOT NULL,
    TYPE          CHAR(1)       NOT NULL,
    PARENT_ID     INT(10)       NOT NULL,
    DISPLAY_ORDER INT(10)       NOT NULL,
    ICON          VARCHAR(50)   NULL,
    URL           VARCHAR(4000),
    CREATED_BY    VARCHAR(100)  NOT NULL,
    CREATED       DATETIME      NOT NULL,
    UPDATED_BY    VARCHAR(100)  NOT NULL,
    UPDATED       DATETIME      NOT NULL
);
CREATE TABLE MEMBER_MENU
(
    AUTHORITY     INT(3) NOT NULL,
    ID       BIGINT(20)       NOT NULL,
    NAME          VARCHAR(1000) NOT NULL,
    TYPE          CHAR(1)       NOT NULL,
    PARENT_ID     BIGINT(20)       NOT NULL,
    DISPLAY_ORDER INT(10)       NOT NULL,
    ICON          VARCHAR(50)   NULL,
    URL           VARCHAR(4000),
    CREATED_BY    VARCHAR(100)  NOT NULL,
    CREATED       DATETIME      NOT NULL,
    UPDATED_BY    VARCHAR(100)  NOT NULL,
    UPDATED       DATETIME      NOT NULL,
    primary key(AUTHORITY,ID)
);
CREATE TABLE CODE_GROUP
(
    NAME       VARCHAR(100) NOT NULL,
    DESCRIPTION       VARCHAR(1000)            NOT NULL,
    CREATED_BY VARCHAR(100)             NOT NULL,
    CREATED    DATETIME                 NOT NULL,
    UPDATED_BY VARCHAR(100)             NOT NULL,
    UPDATED    DATETIME                 NOT NULL
);

CREATE TABLE CODE
(
    ID            BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    GROUP_NAME    VARCHAR(100)          NOT NULL,
    VALUE          VARCHAR(100)          NOT NULL,
    NAME          VARCHAR(1000)         NOT NULL,
    AVAILABLE         BOOLEAN DEFAULT FALSE NOT NULL,
    DISPLAY_ORDER INT(10)               NOT NULL,
    AUTHORITY     INT(3)  DEFAULT 999   NOT NULL,
    CREATED_BY    VARCHAR(100)          NOT NULL,
    CREATED       DATETIME              NOT NULL,
    UPDATED_BY    VARCHAR(100)          NOT NULL,
    UPDATED       DATETIME              NOT NULL
);
