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
    profile_picture_path varchar(255),
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

insert into user (email, username, full_name, profile_picture_path, password, joined_at)
	values('admin@tuitr.com', 'tuitr', 'The Tuítr', 'https://avatars.githubusercontent.com/u/50278?s=200&v=4', '$2a$10$3Qrx0rv8qSmZ8s3RlD5qE.upleP7.Qzbg5EoIAm62evEkY4c023TK', '2012-12-12');
insert into user (email, username, full_name, profile_picture_path, password, joined_at)
	values('elon.musk@gmail.com', 'elonmusk', 'Elon Musk', 'https://pbs.twimg.com/profile_images/1590968738358079488/IY9Gx6Ok_400x400.jpg', '$2a$10$3Qrx0rv8qSmZ8s3RlD5qE.upleP7.Qzbg5EoIAm62evEkY4c023TK', '2012-12-12');

insert into user (email, username, full_name, profile_picture_path, password, joined_at)
	values('jeovane.barbosa@zup.com.br', 'jeovanebarbosazup', 'Jeovane Barbosa', 'https://lh3.googleusercontent.com/a-/AD5-WCmzTNM1O5Nvo7NKZK4DmRDYyaNVgnWdZ3U5qVuD=s64-p-k-rw-no', '$2a$10$h4HMatU5Bfr1zyYLBfV7ceZyF.C3r6M6sHYmB5Jf8SJUbCUH8BqYi', '2012-12-12');
insert into user (email, username, full_name, profile_picture_path, password, joined_at)
	values('noelle.figueiredo@zup.com.br', 'noellefigueiredozup', 'Noelle Figueiredo', 'https://lh3.googleusercontent.com/a-/AD5-WClufCHLdM1-IrLEMW-ks3GZXP6f9BWaNvReSxey=s64-p-k-rw-no', '$2a$10$wYpgxRTbLqe9wq2sGvIWBujDzVRlrEgb5PBZxfRBVqsC0RkFe74kW', '2012-12-12');
insert into user (email, username, full_name, profile_picture_path, password, joined_at)
	values('italo.cavalcanti@zup.com.br', 'italocavalcantizup', 'Italo Cavalcanti', 'https://lh3.googleusercontent.com/a-/AD5-WCkOTcAq7_FAWGlkiP3OgQOaPni6gYERS8vIpFyN=s64-p-k-rw-no', '$2a$10$Zh5fTqCmYps/lj2gsZFfkubC.g.s5Eu3kp51luo/b.YPAvujSy0bm', '2012-12-12');
insert into user (email, username, full_name, profile_picture_path, password, joined_at)
	values('luis.felipe@zup.com.br', 'luisfelipesantoszup', 'Luis Felipe Santos', 'https://lh3.googleusercontent.com/a-/AD5-WCmBxsY_5hTp0OIGZffeAZJCzRdvOhc0n98w28_0=s64-p-k-rw-no', '$2a$10$uhjKCIsqzHJmCGq3zUbyWuHN32JpkFvC2ELfkl2vZZ7Z6LePi/ICe', '2012-12-12');
insert into user (email, username, full_name, profile_picture_path, password, joined_at)
	values('laura.marson@zup.com.br', 'lauramarsonzup', 'Laura Marson', 'https://lh3.googleusercontent.com/a-/AD5-WCkHnDX5LUScnMEb2esY9ZqK4QiJTU0iceCwgSkO=s64-p-k-rw-no', '$2a$10$W1zofnfIcz3RyBdS7CEZJ.huXVlH1L9bMMkah2d/6pIjCysTvwaaC', '2012-12-12');

insert into user (email, username, full_name, profile_picture_path, password, joined_at)
	values('debora.souza@zup.com.br', 'deborasouzazup', 'Debora Souza', 'https://lh3.googleusercontent.com/a-/AD5-WCmiFrhQHdCodxNFOxrUitv1xpe_qAFqm4cMaaYG=s64-p-k-rw-no', '$2a$10$gtbEWz3tpp2PKnP1yf0KjOjIHubMU8iPUD3BCkF4MO5pKIfJ5mwUi', '2012-12-12');
insert into user (email, username, full_name, profile_picture_path, password, joined_at)
	values('luan.gomes@zup.com.br', 'luanlimazup', 'Luan Gomes Lima', 'https://lh3.googleusercontent.com/a-/AD5-WCkOqo-M9b3OuRfQwDXaFwLFz8yOQ8EYiSDGniES=s64-p-k-rw-no', '$2a$10$ROoB6ULPPPFrHUaT1KhVpeLCb5JqkxYHHJ2DRR68MvqIrFjV4vzKu', '2012-12-12');
insert into user (email, username, full_name, profile_picture_path, password, joined_at)
	values('nilo.martins@zup.com.br', 'nilomartinszup', 'Nilo Martins', 'https://lh3.googleusercontent.com/a-/AD5-WClX0qgJhw_-njDoIrpXQ8WuBIsPtCB-AVsdaNaJuQ=s64-p-k-rw-no', '$2a$10$puqJBJ7OcaCprit7MXzdC.cydnnbb60sXsngO05QzALwB.I2u2CRS', '2012-12-12');
insert into user (email, username, full_name, profile_picture_path, password, joined_at)
	values('raline.silva@zup.com.br', 'ralinesilvazup', 'Raline Silva', 'https://lh3.googleusercontent.com/a-/AD5-WCmgmcAF_lemYF1ItIU2ipjFj3RqxzEcFJP1v65m=s88-w88-h88-c-k', '$2a$10$D0KVMYEx1LFgojyN.bzNJumQ9PjTdn1ZpJnXw/cbh7fxuftDXO.mu', '2012-12-12');
insert into user (email, username, full_name, profile_picture_path, password, joined_at)
	values('stella.medeiros@zup.com.br', 'stellamedeiroszup', 'Stella Medeiros', 'https://lh3.googleusercontent.com/a/AEdFTp49yInXI7WJGabnD2B4llwZrnFlbdnYs_pWJIIC=s88-w88-h88-c-k', '$2a$10$pwJ7EQYtWhCWWbSfTblTfeohpjyiF/FqId5CK4hT/pjwdzxcVMxAK', '2012-12-12');
insert into user (email, username, full_name, profile_picture_path, password, joined_at)
	values('thiago.pereira@zup.com.br', 'thiagopereirazup', 'Thiago Pereira', 'https://lh3.googleusercontent.com/a-/AD5-WCmkl_Cb3XCrIp4Kjv4Ckl3pIp1qO9vG5eF3yKkM=s64-p-k-rw-no', '$2a$10$k6wk0ESGdJepV5EhMfApcOL6T7o6Elg9h1XLhW9ojsUN/qKKx8llO', '2012-12-12');

insert into user (email, username, full_name, profile_picture_path, password, joined_at)
	values('rafael.rollo@zup.com.br', 'rafaelrollozup', 'Rafael Rollo', 'https://lh3.googleusercontent.com/a/AEdFTp6icOhoLwUSiZW8xA3UBSd7K7a5uOk5xSECTuvbcQ=s64-p-k-rw-no', '$2a$10$ZoctEqHEkxPYhNj/wFhWo.Q9LSTLkBwOhw5nht/dLpYide5ciY9Eu', '2012-12-12');
insert into user (email, username, full_name, profile_picture_path, password, joined_at)
	values('matheus.almeida@zup.com.br', 'matheushenriquezup', 'Matheus Henrique Santos', 'https://lh3.googleusercontent.com/a-/AD5-WCkrsss7ozq32EAjiugkUJd5x1CuAy1Ox3eC8Ug9=s64-p-k-rw-no', '$2a$10$xDLu0IvJwtaw//ZXyyQiReHr/fBo2UfSIPcYqnBg/W8vFsabB2Chu', '2012-12-12');

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

insert into user_authorities (user_id, authorities_authority)
    values(8,'ROLE_CLIENT'); -- debora
insert into user_authorities (user_id, authorities_authority)
    values(9,'ROLE_CLIENT'); -- luan
insert into user_authorities (user_id, authorities_authority)
    values(10,'ROLE_CLIENT'); -- nilo
insert into user_authorities (user_id, authorities_authority)
    values(11,'ROLE_CLIENT'); -- raline
insert into user_authorities (user_id, authorities_authority)
    values(12,'ROLE_CLIENT'); -- stella
insert into user_authorities (user_id, authorities_authority)
    values(13,'ROLE_CLIENT'); -- thiago

insert into user_authorities (user_id, authorities_authority)
    values(14,'ROLE_CLIENT'); -- rollo
insert into user_authorities (user_id, authorities_authority)
    values(15,'ROLE_CLIENT'); -- matheus

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

insert into user_followers (user_id, followers_id)
    values(1, 8);
insert into user_following (user_id, following_id)
    values(8, 1);

insert into user_followers (user_id, followers_id)
    values(1, 9);
insert into user_following (user_id, following_id)
    values(9, 1);

insert into user_followers (user_id, followers_id)
    values(1, 10);
insert into user_following (user_id, following_id)
    values(10, 1);

insert into user_followers (user_id, followers_id)
    values(1, 11);
insert into user_following (user_id, following_id)
    values(11, 1);

insert into user_followers (user_id, followers_id)
    values(1, 12);
insert into user_following (user_id, following_id)
    values(12, 1);

insert into user_followers (user_id, followers_id)
    values(1, 13);
insert into user_following (user_id, following_id)
    values(13, 1);

insert into user_followers (user_id, followers_id)
    values(1, 14);
insert into user_following (user_id, following_id)
    values(14, 1);

insert into user_followers (user_id, followers_id)
    values(1, 15);
insert into user_following (user_id, following_id)
    values(15, 1);

-- all students follow rollo --------------------------------------------

insert into user_followers (user_id, followers_id)
    values(14, 3);
insert into user_following (user_id, following_id)
    values(3, 14);

insert into user_followers (user_id, followers_id)
    values(14, 4);
insert into user_following (user_id, following_id)
    values(4, 14);

insert into user_followers (user_id, followers_id)
    values(14, 5);
insert into user_following (user_id, following_id)
    values(5, 14);

insert into user_followers (user_id, followers_id)
    values(14, 6);
insert into user_following (user_id, following_id)
    values(6, 14);

insert into user_followers (user_id, followers_id)
    values(14, 7);
insert into user_following (user_id, following_id)
    values(7, 14);

insert into user_followers (user_id, followers_id)
    values(14, 8);
insert into user_following (user_id, following_id)
    values(8, 14);

insert into user_followers (user_id, followers_id)
    values(14, 9);
insert into user_following (user_id, following_id)
    values(9, 14);

insert into user_followers (user_id, followers_id)
    values(14, 10);
insert into user_following (user_id, following_id)
    values(10, 14);

insert into user_followers (user_id, followers_id)
    values(14, 11);
insert into user_following (user_id, following_id)
    values(11, 14);

insert into user_followers (user_id, followers_id)
    values(14, 12);
insert into user_following (user_id, following_id)
    values(12, 14);

insert into user_followers (user_id, followers_id)
    values(14, 13);
insert into user_following (user_id, following_id)
    values(13, 14);

-- all students follow matheus --------------------------------------------

insert into user_followers (user_id, followers_id)
    values(15, 3);
insert into user_following (user_id, following_id)
    values(3, 15);

insert into user_followers (user_id, followers_id)
    values(15, 4);
insert into user_following (user_id, following_id)
    values(4, 15);

insert into user_followers (user_id, followers_id)
    values(15, 5);
insert into user_following (user_id, following_id)
    values(5, 15);

insert into user_followers (user_id, followers_id)
    values(15, 6);
insert into user_following (user_id, following_id)
    values(6, 15);

insert into user_followers (user_id, followers_id)
    values(15, 7);
insert into user_following (user_id, following_id)
    values(7, 15);

insert into user_followers (user_id, followers_id)
    values(15, 8);
insert into user_following (user_id, following_id)
    values(8, 15);

insert into user_followers (user_id, followers_id)
    values(15, 9);
insert into user_following (user_id, following_id)
    values(9, 15);

insert into user_followers (user_id, followers_id)
    values(15, 10);
insert into user_following (user_id, following_id)
    values(10, 15);

insert into user_followers (user_id, followers_id)
    values(15, 11);
insert into user_following (user_id, following_id)
    values(11, 15);

insert into user_followers (user_id, followers_id)
    values(15, 12);
insert into user_following (user_id, following_id)
    values(12, 15);

insert into user_followers (user_id, followers_id)
    values(15, 13);
insert into user_following (user_id, following_id)
    values(13, 15);

-- rollo follows john doe ----------------------------------------------

insert into user_followers (user_id, followers_id)
    values(2, 14);
insert into user_following (user_id, following_id)
    values(14, 2);

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

-- luan, nilo and raline follow debora -------------------------------

insert into user_followers (user_id, followers_id)
    values(8, 9);
insert into user_followers (user_id, followers_id)
    values(8, 10);
insert into user_followers (user_id, followers_id)
    values(8, 11);

insert into user_following (user_id, following_id)
    values(9, 8);
insert into user_following (user_id, following_id)
    values(10, 8);
insert into user_following (user_id, following_id)
    values(11, 8);

-- nilo, raline and stella follow luan -------------------------------

insert into user_followers (user_id, followers_id)
    values(9, 10);
insert into user_followers (user_id, followers_id)
    values(9, 11);
insert into user_followers (user_id, followers_id)
    values(9, 12);

insert into user_following (user_id, following_id)
    values(10, 9);
insert into user_following (user_id, following_id)
    values(11, 9);
insert into user_following (user_id, following_id)
    values(12, 9);

-- raline, stella and thiago follow nilo -------------------------------

insert into user_followers (user_id, followers_id)
    values(10, 11);
insert into user_followers (user_id, followers_id)
    values(10, 12);
insert into user_followers (user_id, followers_id)
    values(10, 13);

insert into user_following (user_id, following_id)
    values(11, 10);
insert into user_following (user_id, following_id)
    values(12, 10);
insert into user_following (user_id, following_id)
    values(13, 10);

-- stella, thiago and debora follow raline -------------------------------

insert into user_followers (user_id, followers_id)
    values(11, 12);
insert into user_followers (user_id, followers_id)
    values(11, 13);
insert into user_followers (user_id, followers_id)
    values(11, 8);

insert into user_following (user_id, following_id)
    values(12, 11);
insert into user_following (user_id, following_id)
    values(13, 11);
insert into user_following (user_id, following_id)
    values(8, 11);

-- thiago, debora and luan follow stella -------------------------------

insert into user_followers (user_id, followers_id)
    values(12, 13);
insert into user_followers (user_id, followers_id)
    values(12, 8);
insert into user_followers (user_id, followers_id)
    values(12, 9);

insert into user_following (user_id, following_id)
    values(13, 12);
insert into user_following (user_id, following_id)
    values(8, 12);
insert into user_following (user_id, following_id)
    values(9, 12);

-- debora, luan and nilo follow thiago -------------------------------

insert into user_followers (user_id, followers_id)
    values(13, 8);
insert into user_followers (user_id, followers_id)
    values(13, 9);
insert into user_followers (user_id, followers_id)
    values(13, 10);

insert into user_following (user_id, following_id)
    values(8, 13);
insert into user_following (user_id, following_id)
    values(9, 13);
insert into user_following (user_id, following_id)
    values(10, 13);

-- rollo and matheus follow each other -------------------------------

insert into user_followers (user_id, followers_id)
    values(14, 15);
insert into user_following (user_id, following_id)
    values(15, 14);

insert into user_followers (user_id, followers_id)
    values(15, 14);
insert into user_following (user_id, following_id)
    values(14, 15);

---- posts data ---------------------------------------------------------

-- post id 1
insert into post (created_at, text_content, author_id)
    values ('2022-12-06T17:34:48.000', 'Eu que mando aqui!', 2);

insert into post_lovers (post_id, lovers_id)
    values (1, 3);
insert into post_lovers (post_id, lovers_id)
    values (1, 4);

-- post id 2
insert into post (created_at, text_content, author_id, replying_to_id)
    values ('2022-12-07T17:35:49.000', 'Welcome to the Tuítr', 1, 1);

insert into post_lovers (post_id, lovers_id)
    values (2, 2);
insert into post_lovers (post_id, lovers_id)
    values (2, 3);
insert into post_lovers (post_id, lovers_id)
    values (2, 4);
insert into post_lovers (post_id, lovers_id)
    values (2, 5);
insert into post_lovers (post_id, lovers_id)
    values (2, 6);
insert into post_lovers (post_id, lovers_id)
    values (2, 7);

-- post id 3
insert into post (created_at, text_content, author_id)
    values ('2022-12-07T17:36:50.000', 'O hexa ainda vem!', 3);

insert into post_lovers (post_id, lovers_id)
    values (3, 4);
insert into post_lovers (post_id, lovers_id)
    values (3, 5);
insert into post_lovers (post_id, lovers_id)
    values (3, 6);
insert into post_lovers (post_id, lovers_id)
    values (3, 7);

-- post id 4
insert into post (created_at, author_id, reposting_id)
    values ('2022-12-07T17:38:52.000', 4, 3);

insert into post_lovers (post_id, lovers_id)
    values (4, 3);
insert into post_lovers (post_id, lovers_id)
    values (4, 5);
insert into post_lovers (post_id, lovers_id)
    values (4, 6);
insert into post_lovers (post_id, lovers_id)
    values (4, 7);

-- post id 5
insert into post (created_at, text_content, author_id)
    values ('2022-12-08T17:34:48.000', 'Hi everyone!', 3);

insert into post_lovers (post_id, lovers_id)
    values (5, 4);
insert into post_lovers (post_id, lovers_id)
    values (5, 5);

-- post id 6
insert into post (created_at, text_content, author_id)
    values ('2022-12-08T18:30:00.000', 'Vel facilisis volutpat est velit egestas dui id ornare arcu odio ut sem nulla pharetra.', 3);

insert into post_lovers (post_id, lovers_id)
    values (6, 4);
insert into post_lovers (post_id, lovers_id)
    values (6, 5);

-- post id 7
insert into post (created_at, text_content, author_id)
    values ('2022-12-08T18:50:00.000', 'Nulla at volutpat diam ut venenatis tellus in metus vulputate eu scelerisque felis imperdiet proin fermentum leo vel orci porta non pulvinar neque laoreet suspendisse interdum consectetur libero id faucibus nisl tincidunt eget nullam non nisi.', 3);

insert into post_lovers (post_id, lovers_id)
    values (7, 4);
insert into post_lovers (post_id, lovers_id)
    values (7, 5);

-- post id 8
insert into post (created_at, text_content, author_id)
    values ('2022-12-09T18:45:00.000', 'Vel facilisis volutpat est velit egestas dui id ornare arcu odio ut sem nulla pharetra.', 4);

insert into post_lovers (post_id, lovers_id)
    values (8, 5);
insert into post_lovers (post_id, lovers_id)
    values (8, 6);

-- post id 9
insert into post (created_at, text_content, author_id)
    values ('2022-12-09T19:05:00.000', 'Nulla at volutpat diam ut venenatis tellus in metus vulputate eu scelerisque felis imperdiet proin fermentum leo vel orci porta non pulvinar neque laoreet suspendisse interdum consectetur libero id faucibus nisl tincidunt eget nullam non nisi.', 4);

insert into post_lovers (post_id, lovers_id)
    values (9, 5);
insert into post_lovers (post_id, lovers_id)
    values (9, 6);

-- post id 10
insert into post (created_at, text_content, author_id)
    values ('2022-12-09T19:00:00.000', 'Vel facilisis volutpat est velit egestas dui id ornare arcu odio ut sem nulla pharetra.', 5);

insert into post_lovers (post_id, lovers_id)
    values (10, 6);
insert into post_lovers (post_id, lovers_id)
    values (10, 7);

-- post id 11
insert into post (created_at, text_content, author_id)
    values ('2022-12-09T19:20:00.000', 'Nulla at volutpat diam ut venenatis tellus in metus vulputate eu scelerisque felis imperdiet proin fermentum leo vel orci porta non pulvinar neque laoreet suspendisse interdum consectetur libero id faucibus nisl tincidunt eget nullam non nisi.', 5);

insert into post_lovers (post_id, lovers_id)
    values (11, 6);
insert into post_lovers (post_id, lovers_id)
    values (11, 7);

-- post id 12
insert into post (created_at, text_content, author_id)
    values ('2022-12-09T19:15:00.000', 'Vel facilisis volutpat est velit egestas dui id ornare arcu odio ut sem nulla pharetra.', 6);

insert into post_lovers (post_id, lovers_id)
    values (12, 7);
insert into post_lovers (post_id, lovers_id)
    values (12, 3);

-- post id 13
insert into post (created_at, text_content, author_id)
    values ('2022-12-10T19:35:00.000', 'Nulla at volutpat diam ut venenatis tellus in metus vulputate eu scelerisque felis imperdiet proin fermentum leo vel orci porta non pulvinar neque laoreet suspendisse interdum consectetur libero id faucibus nisl tincidunt eget nullam non nisi.', 6);

insert into post_lovers (post_id, lovers_id)
    values (13, 7);
insert into post_lovers (post_id, lovers_id)
    values (13, 3);

-- post id 14
insert into post (created_at, text_content, author_id)
    values ('2022-12-10T19:30:00.000', 'Vel facilisis volutpat est velit egestas dui id ornare arcu odio ut sem nulla pharetra.', 7);

insert into post_lovers (post_id, lovers_id)
    values (14, 3);
insert into post_lovers (post_id, lovers_id)
    values (14, 4);

-- post id 15
insert into post (created_at, text_content, author_id)
    values ('2022-12-10T19:50:00.000', 'Nulla at volutpat diam ut venenatis tellus in metus vulputate eu scelerisque felis imperdiet proin fermentum leo vel orci porta non pulvinar neque laoreet suspendisse interdum consectetur libero id faucibus nisl tincidunt eget nullam non nisi.', 7);

insert into post_lovers (post_id, lovers_id)
    values (15, 3);
insert into post_lovers (post_id, lovers_id)
    values (15, 4);

-- post id 16
insert into post (created_at, text_content, author_id)
    values ('2022-12-10T19:45:00.000', 'Vel facilisis volutpat est velit egestas dui id ornare arcu odio ut sem nulla pharetra.', 8);

insert into post_lovers (post_id, lovers_id)
    values (16, 9);
insert into post_lovers (post_id, lovers_id)
    values (16, 10);

-- post id 17
insert into post (created_at, text_content, author_id)
    values ('2022-12-11T20:05:00.000', 'Nulla at volutpat diam ut venenatis tellus in metus vulputate eu scelerisque felis imperdiet proin fermentum leo vel orci porta non pulvinar neque laoreet suspendisse interdum consectetur libero id faucibus nisl tincidunt eget nullam non nisi.', 8);

insert into post_lovers (post_id, lovers_id)
    values (17, 9);
insert into post_lovers (post_id, lovers_id)
    values (17, 10);

-- post id 18
insert into post (created_at, text_content, author_id)
    values ('2022-12-11T20:00:00.000', 'Vel facilisis volutpat est velit egestas dui id ornare arcu odio ut sem nulla pharetra.', 9);

insert into post_lovers (post_id, lovers_id)
    values (18, 10);
insert into post_lovers (post_id, lovers_id)
    values (18, 11);

-- post id 19
insert into post (created_at, text_content, author_id)
    values ('2022-12-11T20:20:00.000', 'Nulla at volutpat diam ut venenatis tellus in metus vulputate eu scelerisque felis imperdiet proin fermentum leo vel orci porta non pulvinar neque laoreet suspendisse interdum consectetur libero id faucibus nisl tincidunt eget nullam non nisi.', 9);

insert into post_lovers (post_id, lovers_id)
    values (19, 10);
insert into post_lovers (post_id, lovers_id)
    values (19, 11);

-- post id 20
insert into post (created_at, text_content, author_id)
    values ('2022-12-11T20:15:00.000', 'Vel facilisis volutpat est velit egestas dui id ornare arcu odio ut sem nulla pharetra.', 10);

insert into post_lovers (post_id, lovers_id)
    values (20, 11);
insert into post_lovers (post_id, lovers_id)
    values (20, 12);

-- post id 21
insert into post (created_at, text_content, author_id)
    values ('2022-12-12T20:35:00.000', 'Nulla at volutpat diam ut venenatis tellus in metus vulputate eu scelerisque felis imperdiet proin fermentum leo vel orci porta non pulvinar neque laoreet suspendisse interdum consectetur libero id faucibus nisl tincidunt eget nullam non nisi.', 10);

insert into post_lovers (post_id, lovers_id)
    values (21, 11);
insert into post_lovers (post_id, lovers_id)
    values (21, 12);

-- post id 22
insert into post (created_at, text_content, author_id)
    values ('2022-12-12T20:30:00.000', 'Vel facilisis volutpat est velit egestas dui id ornare arcu odio ut sem nulla pharetra.', 11);

insert into post_lovers (post_id, lovers_id)
    values (22, 12);
insert into post_lovers (post_id, lovers_id)
    values (22, 13);

-- post id 23
insert into post (created_at, text_content, author_id)
    values ('2022-12-12T20:50:00.000', 'Nulla at volutpat diam ut venenatis tellus in metus vulputate eu scelerisque felis imperdiet proin fermentum leo vel orci porta non pulvinar neque laoreet suspendisse interdum consectetur libero id faucibus nisl tincidunt eget nullam non nisi.', 11);

insert into post_lovers (post_id, lovers_id)
    values (23, 12);
insert into post_lovers (post_id, lovers_id)
    values (23, 13);

-- post id 24
insert into post (created_at, text_content, author_id)
    values ('2022-12-13T20:45:00.000', 'Vel facilisis volutpat est velit egestas dui id ornare arcu odio ut sem nulla pharetra.', 12);

insert into post_lovers (post_id, lovers_id)
    values (24, 13);
insert into post_lovers (post_id, lovers_id)
    values (24, 8);

-- post id 25
insert into post (created_at, text_content, author_id)
    values ('2022-12-13T21:05:00.000', 'Nulla at volutpat diam ut venenatis tellus in metus vulputate eu scelerisque felis imperdiet proin fermentum leo vel orci porta non pulvinar neque laoreet suspendisse interdum consectetur libero id faucibus nisl tincidunt eget nullam non nisi.', 12);

insert into post_lovers (post_id, lovers_id)
    values (25, 13);
insert into post_lovers (post_id, lovers_id)
    values (25, 8);

-- post id 26
insert into post (created_at, text_content, author_id)
    values ('2022-12-13T21:00:00.000', 'Vel facilisis volutpat est velit egestas dui id ornare arcu odio ut sem nulla pharetra.', 13);

insert into post_lovers (post_id, lovers_id)
    values (26, 8);
insert into post_lovers (post_id, lovers_id)
    values (26, 9);

-- post id 27
insert into post (created_at, text_content, author_id)
    values ('2022-12-13T21:20:00.000', 'Nulla at volutpat diam ut venenatis tellus in metus vulputate eu scelerisque felis imperdiet proin fermentum leo vel orci porta non pulvinar neque laoreet suspendisse interdum consectetur libero id faucibus nisl tincidunt eget nullam non nisi.', 13);

insert into post_lovers (post_id, lovers_id)
    values (27, 8);
insert into post_lovers (post_id, lovers_id)
    values (27, 9);

-- post id 28
insert into post (created_at, text_content, author_id)
    values ('2022-12-14T21:15:00.000', 'Vel facilisis volutpat est velit egestas dui id ornare arcu odio ut sem nulla pharetra.', 2);

insert into post_lovers (post_id, lovers_id)
    values (28, 1);
insert into post_lovers (post_id, lovers_id)
    values (28, 15);

-- post id 29
insert into post (created_at, text_content, author_id)
    values ('2022-12-14T21:35:00.000', 'Nulla at volutpat diam ut venenatis tellus in metus vulputate eu scelerisque felis imperdiet proin fermentum leo vel orci porta non pulvinar neque laoreet suspendisse interdum consectetur libero id faucibus nisl tincidunt eget nullam non nisi.', 2);

insert into post_lovers (post_id, lovers_id)
    values (29, 1);
insert into post_lovers (post_id, lovers_id)
    values (29, 15);

-- post id 30
insert into post (created_at, text_content, author_id)
    values ('2022-12-14T21:30:00.000', 'Vel facilisis volutpat est velit egestas dui id ornare arcu odio ut sem nulla pharetra.', 15);

insert into post_lovers (post_id, lovers_id)
    values (30, 3);
insert into post_lovers (post_id, lovers_id)
    values (30, 4);
insert into post_lovers (post_id, lovers_id)
    values (30, 5);
insert into post_lovers (post_id, lovers_id)
    values (30, 6);

-- post id 31
insert into post (created_at, text_content, author_id)
    values ('2022-12-09T14:00:00.000', 'O hexa não vem mais..', 3);

insert into post_lovers (post_id, lovers_id)
    values (31, 4);
insert into post_lovers (post_id, lovers_id)
    values (31, 5);
insert into post_lovers (post_id, lovers_id)
    values (31, 6);
insert into post_lovers (post_id, lovers_id)
    values (31, 7);
insert into post_lovers (post_id, lovers_id)
    values (31, 15);

-- post id 32
insert into post (created_at, author_id, reposting_id)
    values ('2022-12-15T18:50:00.000', 15, 31);

insert into post_lovers (post_id, lovers_id)
    values (32, 3);
insert into post_lovers (post_id, lovers_id)
    values (32, 4);
insert into post_lovers (post_id, lovers_id)
    values (32, 5);
insert into post_lovers (post_id, lovers_id)
    values (32, 6);
insert into post_lovers (post_id, lovers_id)
    values (32, 7);