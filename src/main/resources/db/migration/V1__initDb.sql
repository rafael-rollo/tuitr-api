-- schema definition ------------------------------------------------
create table role (
	authority varchar(255) not null,
	primary key (authority)
);

create table user (
	id bigint generated by default as identity,
	username varchar(255) not null,
    email varchar(255) not null,
    password varchar(255),
    full_name varchar(255),
    birth_date date,
    joined_at date not null,
    location varchar(255),
    primary key (id)
);

create table user_authorities (
	user_id bigint not null,
    authorities_authority varchar(255) not null
);

create table user_followers (
    user_id bigint not null,
    followers_id bigint not null
);

create table user_following (
    user_id bigint not null,
    following_id bigint not null
);

-- ///////////////////////////////////////////////////////////////////////

create table post (
    id bigint generated by default as identity,
    created_at timestamp not null,
    image_path varchar(255),
    text_content varchar(280),
    author_id bigint not null,
    replying_to_id bigint,
    reposting_id bigint,
    primary key (id)
);

create table post_lovers (
    post_id bigint not null,
    lovers_id bigint not null,
    primary key (post_id, lovers_id)
);

alter table user 
	drop constraint if exists user_email;
	
alter table user 
	add constraint user_email unique (email);

alter table user
    drop constraint if exists user_username;

alter table user
    add constraint user_username unique (username);

alter table user_authorities 
	add constraint user_authorities_authority
	foreign key (authorities_authority) 
	references role;
	
alter table user_authorities 
	add constraint user_authorities_user 
	foreign key (user_id) 
	references user;

alter table user_followers
    add constraint user_followers_follower
    foreign key (followers_id)
    references user;

alter table user_followers
    add constraint user_followers_user
    foreign key (user_id)
    references user;

alter table user_following
    add constraint user_following_following
    foreign key (following_id)
    references user;

alter table user_following
    add constraint user_following_user
    foreign key (user_id)
    references user;

alter table post
    add constraint post_author
    foreign key (author_id)
    references user;

alter table post
    add constraint post_replying_to_origin_post
    foreign key (replying_to_id)
    references post;

alter table post
    add constraint post_reposting_origin_post
    foreign key (reposting_id)
    references post;

alter table post_lovers
    add constraint post_lovers_lover
    foreign key (lovers_id)
    references user;

alter table post_lovers
    add constraint post_lovers_post
    foreign key (post_id)
    references post;

-- spring security acl schema ---------------------------------------
create table acl_sid (
    id bigint generated by default as identity(start with 100) not null primary key,
    principal boolean not null,
    sid varchar_ignorecase(100) not null,
    constraint uk_acl_sid unique(sid,principal)
);

create table acl_class (
    id bigint generated by default as identity(start with 100) not null primary key,
    class varchar_ignorecase(500) not null,
    constraint uk_acl_class unique(class)
);

create table acl_object_identity (
    id bigint generated by default as identity(start with 100) not null primary key,
    object_id_class bigint not null,
    object_id_identity bigint not null,
    parent_object bigint,
    owner_sid bigint not null,
    entries_inheriting boolean not null,
    constraint uk_acl_objid
        unique(object_id_class,object_id_identity),
    constraint fk_acl_obj_parent
        foreign key(parent_object) references acl_object_identity(id),
    constraint fk_acl_obj_class
        foreign key(object_id_class) references acl_class(id),
    constraint fk_acl_obj_owner
        foreign key(owner_sid) references acl_sid(id)
);

create table acl_entry (
    id bigint generated by default as identity(start with 100) not null primary key,
    acl_object_identity bigint not null,
    ace_order int not null,
    sid bigint not null,
    mask integer not null,
    granting boolean not null,
    audit_success boolean not null,
    audit_failure boolean not null,
    constraint uk_acl_entry
        unique(acl_object_identity,ace_order),
    constraint fk_acl_entry_obj_id
        foreign key(acl_object_identity) references acl_object_identity(id),
    constraint fk_acl_entry_sid
        foreign key(sid) references acl_sid(id)
);

-- initial data -----------------------------------------------------

insert into role values ('ROLE_CLIENT');
insert into role values ('ROLE_ADMIN');

insert into user (email, username, full_name, password, joined_at)
	values('admin@tuitr.com', 'tuitr', 'The Tuítr', '$2a$10$3Qrx0rv8qSmZ8s3RlD5qE.upleP7.Qzbg5EoIAm62evEkY4c023TK', '2012-12-12');
insert into user (email, username, full_name, password, joined_at)
	values('john.doe@gmail.com', 'john-doe', 'John Doe', '$2a$10$3Qrx0rv8qSmZ8s3RlD5qE.upleP7.Qzbg5EoIAm62evEkY4c023TK', '2012-12-12');
insert into user (email, username, full_name, password, joined_at)
	values('jeovane.barbosa@zup.com.br', 'jeovanebarbosazup', 'Jeovane Barbosa', '$2a$10$h4HMatU5Bfr1zyYLBfV7ceZyF.C3r6M6sHYmB5Jf8SJUbCUH8BqYi', '2012-12-12');
insert into user (email, username, full_name, password, joined_at)
	values('noelle.figueiredo@zup.com.br', 'noellefigueiredozup', 'Noelle Figueiredo', '$2a$10$wYpgxRTbLqe9wq2sGvIWBujDzVRlrEgb5PBZxfRBVqsC0RkFe74kW', '2012-12-12');
insert into user (email, username, full_name, password, joined_at)
	values('italo.cavalcanti@zup.com.br', 'italocavalcantizup', 'Italo Cavalcanti', '$2a$10$Zh5fTqCmYps/lj2gsZFfkubC.g.s5Eu3kp51luo/b.YPAvujSy0bm', '2012-12-12');
insert into user (email, username, full_name, password, joined_at)
	values('luis.felipe@zup.com.br', 'luisfelipesantoszup', 'Luis Felipe Santos', '$2a$10$uhjKCIsqzHJmCGq3zUbyWuHN32JpkFvC2ELfkl2vZZ7Z6LePi/ICe', '2012-12-12');
insert into user (email, username, full_name, password, joined_at)
	values('laura.marson@zup.com.br', 'lauramarsonzup', 'Laura Marson', '$2a$10$W1zofnfIcz3RyBdS7CEZJ.huXVlH1L9bMMkah2d/6pIjCysTvwaaC', '2012-12-12');

insert into user_authorities (user_id, authorities_authority)
	values(1,'ROLE_ADMIN');
insert into user_authorities (user_id, authorities_authority) 
	values(2,'ROLE_CLIENT');
insert into user_authorities (user_id, authorities_authority)
    values(3,'ROLE_CLIENT');
insert into user_authorities (user_id, authorities_authority)
    values(4,'ROLE_CLIENT');
insert into user_authorities (user_id, authorities_authority)
    values(5,'ROLE_CLIENT');
insert into user_authorities (user_id, authorities_authority)
    values(6,'ROLE_CLIENT');
insert into user_authorities (user_id, authorities_authority)
    values(7,'ROLE_CLIENT');

-- all follow admin --------------------------------------------

insert into user_followers (user_id, followers_id)
    values(1, 2);
insert into user_following (user_id, following_id)
    values(2, 1);

insert into user_followers (user_id, followers_id)
    values(1, 3);
insert into user_following (user_id, following_id)
    values(3, 1);

insert into user_followers (user_id, followers_id)
    values(1, 4);
insert into user_following (user_id, following_id)
    values(4, 1);

insert into user_followers (user_id, followers_id)
    values(1, 5);
insert into user_following (user_id, following_id)
    values(5, 1);

insert into user_followers (user_id, followers_id)
    values(1, 6);
insert into user_following (user_id, following_id)
    values(6, 1);

insert into user_followers (user_id, followers_id)
    values(1, 7);
insert into user_following (user_id, following_id)
    values(7, 1);

-- noelle, italo and pipe follow jeovane -------------------------------

insert into user_followers (user_id, followers_id)
    values(3, 4);
insert into user_followers (user_id, followers_id)
    values(3, 5);
insert into user_followers (user_id, followers_id)
    values(3, 6);

insert into user_following (user_id, following_id)
    values(4, 3);
insert into user_following (user_id, following_id)
    values(5, 3);
insert into user_following (user_id, following_id)
    values(6, 3);

-- italo, pipe and laura follow noelle -------------------------------

insert into user_followers (user_id, followers_id)
    values(4, 5);
insert into user_followers (user_id, followers_id)
    values(4, 6);
insert into user_followers (user_id, followers_id)
    values(4, 7);

insert into user_following (user_id, following_id)
    values(5, 4);
insert into user_following (user_id, following_id)
    values(6, 4);
insert into user_following (user_id, following_id)
    values(7, 4);

-- pipe, laura and jeovane follow italo -------------------------------

insert into user_followers (user_id, followers_id)
    values(5, 6);
insert into user_followers (user_id, followers_id)
    values(5, 7);
insert into user_followers (user_id, followers_id)
    values(5, 3);

insert into user_following (user_id, following_id)
    values(6, 5);
insert into user_following (user_id, following_id)
    values(7, 5);
insert into user_following (user_id, following_id)
    values(3, 5);

-- laura, jeovane and noelle follow pipe -------------------------------

insert into user_followers (user_id, followers_id)
    values(6, 7);
insert into user_followers (user_id, followers_id)
    values(6, 3);
insert into user_followers (user_id, followers_id)
    values(6, 4);

insert into user_following (user_id, following_id)
    values(7, 6);
insert into user_following (user_id, following_id)
    values(3, 6);
insert into user_following (user_id, following_id)
    values(4, 6);

-- jeovane, noelle and italo follow laura -------------------------------

insert into user_followers (user_id, followers_id)
    values(7, 3);
insert into user_followers (user_id, followers_id)
    values(7, 4);
insert into user_followers (user_id, followers_id)
    values(7, 5);

insert into user_following (user_id, following_id)
    values(3, 7);
insert into user_following (user_id, following_id)
    values(4, 7);
insert into user_following (user_id, following_id)
    values(5, 7);
