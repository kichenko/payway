--web_app_user_settings

drop table if exists web_app_user_settings cascade;

create table web_app_user_settings
(
    id bigserial not null,
    app_id varchar(50) not null,
    login varchar(50) not null,
    class_name varchar(256) not null,
    key varchar(256) not null,
    value text not null,

    constraint pk_web_app_user_settings_pkey primary key (id),
    constraint unique_appid_login_key unique (app_id, login, key)
);