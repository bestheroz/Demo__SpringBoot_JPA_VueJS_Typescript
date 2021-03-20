CREATE TABLE member_menu
(
    AUTHORITY     INT(3)        NOT NULL,
    ID            BIGINT(20)    NOT NULL,
    NAME          VARCHAR(1000) NOT NULL,
    TYPE          CHAR(1)       NOT NULL,
    PARENT_ID     BIGINT(20)    NOT NULL,
    DISPLAY_ORDER INT(10)       NOT NULL,
    ICON          VARCHAR(50)   NULL,
    URL           VARCHAR(4000),
    CREATED_BY    VARCHAR(100)  NOT NULL,
    CREATED       DATETIME      NOT NULL,
    UPDATED_BY    VARCHAR(100)  NOT NULL,
    UPDATED       DATETIME      NOT NULL,
    primary key (AUTHORITY, ID)
);
