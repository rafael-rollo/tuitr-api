-- schema definition ------------------------------------------------
create table role (
	authority varchar(255) not null,
	primary key (authority)
);

create table end_user (
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

create table end_user_authorities (
	user_id bigint not null,
    authorities_authority varchar(255) not null
);

create table end_user_followers (
    user_id bigint not null,
    followers_id bigint not null
);

create table end_user_following (
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

alter table end_user 
	drop constraint if exists end_user_email;
	
alter table end_user 
	add constraint end_user_email unique (email);

alter table end_user
    drop constraint if exists end_user_username;

alter table end_user
    add constraint end_user_username unique (username);

alter table end_user_authorities 
	add constraint end_user_authorities_authority
	foreign key (authorities_authority) 
	references role;
	
alter table end_user_authorities 
	add constraint end_user_authorities_user 
	foreign key (user_id) 
	references end_user;

alter table end_user_followers
    add constraint end_user_followers_follower
    foreign key (followers_id)
    references end_user;

alter table end_user_followers
    add constraint end_user_followers_user
    foreign key (user_id)
    references end_user;

alter table end_user_following
    add constraint end_user_following_following
    foreign key (following_id)
    references end_user;

alter table end_user_following
    add constraint end_user_following_user
    foreign key (user_id)
    references end_user;

alter table post
    add constraint post_author
    foreign key (author_id)
    references end_user;

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
    references end_user;

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

-- id 1 admin@tuitr.com
insert into end_user (email, username, full_name, profile_picture_path, password, joined_at)
	values('admin@tuitr.com', 'tuitr', 'The Tuítr', 'https://pbs.twimg.com/profile_images/1684205762451476482/kNQlcn5__400x400.jpg', '$2a$10$wYPVw2TT0v5AJO5hdQAGROnZGZFSTjx2yj33xGGaC3etHI0xks8Z.', '2012-12-12');
-- id 2 elon.musk@gmail.com
insert into end_user (email, username, full_name, profile_picture_path, password, joined_at)
	values('elon.musk@gmail.com', 'musk', 'Elon Musk', 'https://pbs.twimg.com/profile_images/1590968738358079488/IY9Gx6Ok_400x400.jpg', '$2a$10$5Q7T4hYApWtMcjKyKSLbJO3vvDTLh.v5U7vpE8wIT3YHyadzkXITO', '2012-12-12');

-- id 3 socrates@philosophers.com
insert into end_user (email, username, full_name, profile_picture_path, password, joined_at)
	values('socrates@philosophers.com', 'socrates', 'Socrates', 'https://pbs.twimg.com/profile_images/25408162/socrates_big_400x400.jpg', '$2a$10$AKMw289py3.3i4Pv87oseOy9gHTaWAoe76hMTNMz8sZbGPQcEDbyW', '2012-12-12');
-- id 4 plato@philosophers.com
insert into end_user (email, username, full_name, profile_picture_path, password, joined_at)
	values('plato@philosophers.com', 'plato', 'Plato', 'https://pbs.twimg.com/profile_images/1396912129136005124/mwV6giNY_400x400.jpg', '$2a$10$AGoQbwfIlQxxOPVSw2rYjuG9p3xhty5URz6LoR6eqcv0dCWyPm0sy', '2012-12-12');
-- id 5 aristotle@philosophers.com
insert into end_user (email, username, full_name, profile_picture_path, password, joined_at)
	values('aristotle@philosophers.com', 'aristotle', 'Aristotle', 'https://pbs.twimg.com/profile_images/1434817847490908162/Kx-0jeWf_400x400.jpg', '$2a$10$0mv840B/d8glkfjLZJaZresGSoE9GfxVOlUceY5PnB739WihbZy8y', '2012-12-12');
-- id 6 epicurus@philosophers.com
insert into end_user (email, username, full_name, profile_picture_path, password, joined_at)
	values('epicurus@philosophers.com', 'epicurus', 'Epicurus', 'https://pbs.twimg.com/profile_images/1473357026893996036/12OHJ1QE_400x400.jpg', '$2a$10$2Es6j8X7kC/prKLy8Rr13ebAzQnBCDMppowyktC3UljTBgT8GxTKS', '2012-12-12');
-- id 7 kant@philosophers.com
insert into end_user (email, username, full_name, profile_picture_path, password, joined_at)
	values('kant@philosophers.com', 'immanuelkant', 'Immanuel Kant', 'https://pbs.twimg.com/profile_images/1434480566120878081/QLI0BLvV_400x400.jpg', '$2a$10$giAbvWY8Q0HcRMQHrY/aOOGLexHwd8KlDvCHA/6yNn25.pyMKvL2G', '2012-12-12');
-- id 8 descartes@philosophers.com
insert into end_user (email, username, full_name, profile_picture_path, password, joined_at)
	values('descartes@philosophers.com', 'descartes', 'René Descartes', 'https://pbs.twimg.com/profile_images/1569809372250836992/vD7vHjuP_400x400.jpg', '$2a$10$Xqblw3em1SFF3kBmoRDGWOvZObeWwNKWWDuVEUgaId.DXdyT.YgRa', '2012-12-12');
-- id 9 spinoza@philosophers.com
insert into end_user (email, username, full_name, profile_picture_path, password, joined_at)
	values('spinoza@philosophers.com', 'spinoza', 'Baruch Spinoza', 'https://pbs.twimg.com/profile_images/1574342030775488512/Nx_I0479_400x400.jpg', '$2a$10$KklXUWLnD4h5Lkzv0SWkYeqMMembgn0EtqT1QWR.Dn2FTFTsLP92.', '2012-12-12');
-- id 10 locke@philosophers.com
insert into end_user (email, username, full_name, profile_picture_path, password, joined_at)
	values('locke@philosophers.com', 'johnlocke', 'John Locke', 'https://pbs.twimg.com/profile_images/1439524112125370369/ROPqiXFO_400x400.jpg', '$2a$10$WaJy.5Nhw8a7EfvmrV5xr.qotJjHng8J0u2Tcq8p0uFQk.3OyvXAa', '2012-12-12');
-- id 11 hume@philosophers.com
insert into end_user (email, username, full_name, profile_picture_path, password, joined_at)
	values('hume@philosophers.com', 'davidhume', 'David Hume', 'https://pbs.twimg.com/profile_images/1501944604878245897/VHVcDr83_400x400.jpg', '$2a$10$xaJafV82jX56Eg64.vTc6O7aPY9dY32TwliMLdHNjcWou6tKfWwPO', '2012-12-12');
-- id 12 hegel@philosophers.com
insert into end_user (email, username, full_name, profile_picture_path, password, joined_at)
	values('hegel@philosophers.com', 'hegel', 'Georg Wilhelm Friedrich Hegel', 'https://pbs.twimg.com/profile_images/968846865092427777/jd2jtyUj_400x400.jpg', '$2a$10$A6ixRUd14SeQCBOJHwgX9eVsRY82fl9gQq2XGRvcccAor.cD5pQM.', '2012-12-12');
-- id 13 nietzsche@philosophers.com
insert into end_user (email, username, full_name, profile_picture_path, password, joined_at)
	values('nietzsche@philosophers.com', 'nietzsche', 'Friedrich Nietzsche', 'https://pbs.twimg.com/profile_images/1393880827302498308/122KOFlD_400x400.jpg', '$2a$10$Nsf/SPxxlB0FUS.ngLVuiejuzkEFhA.pozwTH8aNrSU2kdw/iJSyC', '2012-12-12');

-- id 14 rafael.rollo@zup.com.br
insert into end_user (email, username, full_name, profile_picture_path, password, joined_at)
	values('rafaelrollo92@gmail.com', 'rollo', 'Rafael Rollo', 'https://pbs.twimg.com/profile_images/1877284963948593152/NOz_4n61_400x400.jpg', '$2a$10$XYvY6u5YPct7aqNGc3q3iuPnXvthuV4Nsebp1AimlFwytxorvUutS', '2012-12-12');

insert into end_user_authorities (user_id, authorities_authority)
	values(1,'ROLE_ADMIN');
insert into end_user_authorities (user_id, authorities_authority) 
	values(2,'ROLE_CLIENT');
insert into end_user_authorities (user_id, authorities_authority)
    values(3,'ROLE_CLIENT');
insert into end_user_authorities (user_id, authorities_authority)
    values(4,'ROLE_CLIENT');
insert into end_user_authorities (user_id, authorities_authority)
    values(5,'ROLE_CLIENT');
insert into end_user_authorities (user_id, authorities_authority)
    values(6,'ROLE_CLIENT');
insert into end_user_authorities (user_id, authorities_authority)
    values(7,'ROLE_CLIENT');
insert into end_user_authorities (user_id, authorities_authority)
    values(8,'ROLE_CLIENT');
insert into end_user_authorities (user_id, authorities_authority)
    values(9,'ROLE_CLIENT');
insert into end_user_authorities (user_id, authorities_authority)
    values(10,'ROLE_CLIENT');
insert into end_user_authorities (user_id, authorities_authority)
    values(11,'ROLE_CLIENT');
insert into end_user_authorities (user_id, authorities_authority)
    values(12,'ROLE_CLIENT');
insert into end_user_authorities (user_id, authorities_authority)
    values(13,'ROLE_CLIENT');
insert into end_user_authorities (user_id, authorities_authority)
    values(14,'ROLE_CLIENT'); -- rollo

-- all users follow admin --------------------------------------------

insert into end_user_followers (user_id, followers_id)
    values(1, 2);
insert into end_user_following (user_id, following_id)
    values(2, 1);

insert into end_user_followers (user_id, followers_id)
    values(1, 3);
insert into end_user_following (user_id, following_id)
    values(3, 1);

insert into end_user_followers (user_id, followers_id)
    values(1, 4);
insert into end_user_following (user_id, following_id)
    values(4, 1);

insert into end_user_followers (user_id, followers_id)
    values(1, 5);
insert into end_user_following (user_id, following_id)
    values(5, 1);

insert into end_user_followers (user_id, followers_id)
    values(1, 6);
insert into end_user_following (user_id, following_id)
    values(6, 1);

insert into end_user_followers (user_id, followers_id)
    values(1, 7);
insert into end_user_following (user_id, following_id)
    values(7, 1);

insert into end_user_followers (user_id, followers_id)
    values(1, 8);
insert into end_user_following (user_id, following_id)
    values(8, 1);

insert into end_user_followers (user_id, followers_id)
    values(1, 9);
insert into end_user_following (user_id, following_id)
    values(9, 1);

insert into end_user_followers (user_id, followers_id)
    values(1, 10);
insert into end_user_following (user_id, following_id)
    values(10, 1);

insert into end_user_followers (user_id, followers_id)
    values(1, 11);
insert into end_user_following (user_id, following_id)
    values(11, 1);

insert into end_user_followers (user_id, followers_id)
    values(1, 12);
insert into end_user_following (user_id, following_id)
    values(12, 1);

insert into end_user_followers (user_id, followers_id)
    values(1, 13);
insert into end_user_following (user_id, following_id)
    values(13, 1);

insert into end_user_followers (user_id, followers_id)
    values(1, 14);
insert into end_user_following (user_id, following_id)
    values(14, 1);

-- some relationships between users

insert into end_user_followers (user_id, followers_id)
    values(3, 4);
insert into end_user_followers (user_id, followers_id)
    values(3, 5);
insert into end_user_followers (user_id, followers_id)
    values(3, 6);

insert into end_user_following (user_id, following_id)
    values(4, 3);
insert into end_user_following (user_id, following_id)
    values(5, 3);
insert into end_user_following (user_id, following_id)
    values(6, 3);

insert into end_user_followers (user_id, followers_id)
    values(4, 5);
insert into end_user_followers (user_id, followers_id)
    values(4, 6);
insert into end_user_followers (user_id, followers_id)
    values(4, 7);

insert into end_user_following (user_id, following_id)
    values(5, 4);
insert into end_user_following (user_id, following_id)
    values(6, 4);
insert into end_user_following (user_id, following_id)
    values(7, 4);

insert into end_user_followers (user_id, followers_id)
    values(5, 6);
insert into end_user_followers (user_id, followers_id)
    values(5, 7);
insert into end_user_followers (user_id, followers_id)
    values(5, 3);

insert into end_user_following (user_id, following_id)
    values(6, 5);
insert into end_user_following (user_id, following_id)
    values(7, 5);
insert into end_user_following (user_id, following_id)
    values(3, 5);

insert into end_user_followers (user_id, followers_id)
    values(6, 7);
insert into end_user_followers (user_id, followers_id)
    values(6, 3);
insert into end_user_followers (user_id, followers_id)
    values(6, 4);

insert into end_user_following (user_id, following_id)
    values(7, 6);
insert into end_user_following (user_id, following_id)
    values(3, 6);
insert into end_user_following (user_id, following_id)
    values(4, 6);

insert into end_user_followers (user_id, followers_id)
    values(7, 3);
insert into end_user_followers (user_id, followers_id)
    values(7, 4);
insert into end_user_followers (user_id, followers_id)
    values(7, 5);

insert into end_user_following (user_id, following_id)
    values(3, 7);
insert into end_user_following (user_id, following_id)
    values(4, 7);
insert into end_user_following (user_id, following_id)
    values(5, 7);

insert into end_user_followers (user_id, followers_id)
    values(8, 9);
insert into end_user_followers (user_id, followers_id)
    values(8, 10);
insert into end_user_followers (user_id, followers_id)
    values(8, 11);

insert into end_user_following (user_id, following_id)
    values(9, 8);
insert into end_user_following (user_id, following_id)
    values(10, 8);
insert into end_user_following (user_id, following_id)
    values(11, 8);

insert into end_user_followers (user_id, followers_id)
    values(9, 10);
insert into end_user_followers (user_id, followers_id)
    values(9, 11);
insert into end_user_followers (user_id, followers_id)
    values(9, 12);

insert into end_user_following (user_id, following_id)
    values(10, 9);
insert into end_user_following (user_id, following_id)
    values(11, 9);
insert into end_user_following (user_id, following_id)
    values(12, 9);

insert into end_user_followers (user_id, followers_id)
    values(10, 11);
insert into end_user_followers (user_id, followers_id)
    values(10, 12);
insert into end_user_followers (user_id, followers_id)
    values(10, 13);

insert into end_user_following (user_id, following_id)
    values(11, 10);
insert into end_user_following (user_id, following_id)
    values(12, 10);
insert into end_user_following (user_id, following_id)
    values(13, 10);

insert into end_user_followers (user_id, followers_id)
    values(11, 12);
insert into end_user_followers (user_id, followers_id)
    values(11, 13);
insert into end_user_followers (user_id, followers_id)
    values(11, 8);

insert into end_user_following (user_id, following_id)
    values(12, 11);
insert into end_user_following (user_id, following_id)
    values(13, 11);
insert into end_user_following (user_id, following_id)
    values(8, 11);

insert into end_user_followers (user_id, followers_id)
    values(12, 13);
insert into end_user_followers (user_id, followers_id)
    values(12, 8);
insert into end_user_followers (user_id, followers_id)
    values(12, 9);

insert into end_user_following (user_id, following_id)
    values(13, 12);
insert into end_user_following (user_id, following_id)
    values(8, 12);
insert into end_user_following (user_id, following_id)
    values(9, 12);

insert into end_user_followers (user_id, followers_id)
    values(13, 8);
insert into end_user_followers (user_id, followers_id)
    values(13, 9);
insert into end_user_followers (user_id, followers_id)
    values(13, 10);

insert into end_user_following (user_id, following_id)
    values(8, 13);
insert into end_user_following (user_id, following_id)
    values(9, 13);
insert into end_user_following (user_id, following_id)
    values(10, 13);

-- rollo follows everyone -----------------------------------

insert into end_user_followers (user_id, followers_id)
    values(2, 14);
insert into end_user_following (user_id, following_id)
    values(14, 2);

insert into end_user_followers (user_id, followers_id)
    values(3, 14);
insert into end_user_following (user_id, following_id)
    values(14, 3);

insert into end_user_followers (user_id, followers_id)
    values(4, 14);
insert into end_user_following (user_id, following_id)
    values(14, 4);

insert into end_user_followers (user_id, followers_id)
    values(5, 14);
insert into end_user_following (user_id, following_id)
    values(14, 5);

insert into end_user_followers (user_id, followers_id)
    values(6, 14);
insert into end_user_following (user_id, following_id)
    values(14, 6);

insert into end_user_followers (user_id, followers_id)
    values(7, 14);
insert into end_user_following (user_id, following_id)
    values(14, 7);

insert into end_user_followers (user_id, followers_id)
    values(8, 14);
insert into end_user_following (user_id, following_id)
    values(14, 8);

insert into end_user_followers (user_id, followers_id)
    values(9, 14);
insert into end_user_following (user_id, following_id)
    values(14, 9);

insert into end_user_followers (user_id, followers_id)
    values(10, 14);
insert into end_user_following (user_id, following_id)
    values(14, 10);

insert into end_user_followers (user_id, followers_id)
    values(11, 14);
insert into end_user_following (user_id, following_id)
    values(14, 11);

insert into end_user_followers (user_id, followers_id)
    values(12, 14);
insert into end_user_following (user_id, following_id)
    values(14, 12);

insert into end_user_followers (user_id, followers_id)
    values(13, 14);
insert into end_user_following (user_id, following_id)
    values(14, 13);

---- posts data ---------------------------------------------------------

-- post id 1
insert into post (created_at, text_content, author_id)
    values ('2022-12-06T17:34:48.000', 'Welcome to the Tuítr', 1);

insert into post_lovers (post_id, lovers_id)
    values (1, 2);
insert into post_lovers (post_id, lovers_id)
    values (1, 3);
insert into post_lovers (post_id, lovers_id)
    values (1, 4);
insert into post_lovers (post_id, lovers_id)
    values (1, 5);
insert into post_lovers (post_id, lovers_id)
    values (1, 6);
insert into post_lovers (post_id, lovers_id)
    values (1, 7);
insert into post_lovers (post_id, lovers_id)
    values (1, 8);
insert into post_lovers (post_id, lovers_id)
    values (1, 9);
insert into post_lovers (post_id, lovers_id)
    values (1, 10);
insert into post_lovers (post_id, lovers_id)
    values (1, 11);
insert into post_lovers (post_id, lovers_id)
    values (1, 12);
insert into post_lovers (post_id, lovers_id)
    values (1, 13);
insert into post_lovers (post_id, lovers_id)
    values (1, 14);

-- post id 2
insert into post (created_at, text_content, author_id, replying_to_id)
    values ('2022-12-07T17:35:49.000', 'Vou te comprei!', 2, 1);

insert into post_lovers (post_id, lovers_id)
    values (2, 3);
insert into post_lovers (post_id, lovers_id)
    values (2, 4);

-- post id 3
insert into post (created_at, text_content, author_id)
    values ('2025-01-05T09:00:00', 'The unexamined life is not worth living.', 3);

insert into post_lovers (post_id, lovers_id)
    values (3, 3);
insert into post_lovers (post_id, lovers_id)
    values (3, 4);
insert into post_lovers (post_id, lovers_id)
    values (3, 5);
insert into post_lovers (post_id, lovers_id)
    values (3, 6);
insert into post_lovers (post_id, lovers_id)
    values (3, 8);
insert into post_lovers (post_id, lovers_id)
    values (3, 9);
insert into post_lovers (post_id, lovers_id)
    values (3, 11);
insert into post_lovers (post_id, lovers_id)
    values (3, 12);
insert into post_lovers (post_id, lovers_id)
    values (3, 13);
insert into post_lovers (post_id, lovers_id)
    values (3, 14);

-- post id 4 replying to 3
insert into post (created_at, text_content, author_id)
    values ('2025-01-05T09:01:00', 'My teacher @socrates once told me that wisdom comes from knowing that you know nothing. I''ve dedicated my life to building a society on that principle.', 4);

insert into post_lovers (post_id, lovers_id)
    values (4, 4);
insert into post_lovers (post_id, lovers_id)
    values (4, 8);
insert into post_lovers (post_id, lovers_id)
    values (4, 9);
insert into post_lovers (post_id, lovers_id)
    values (4, 13);
insert into post_lovers (post_id, lovers_id)
    values (4, 14);

-- post id 5 replying to 4
insert into post (created_at, text_content, author_id, replying_to_id)
    values ('2025-01-05T09:02:00', '@plato, while noble, your ideal society is impractical. Virtue lies in moderation and in observing the natural world to understand human behavior.', 5, 4);

insert into post_lovers (post_id, lovers_id)
    values (5, 6);
insert into post_lovers (post_id, lovers_id)
    values (5, 9);
insert into post_lovers (post_id, lovers_id)
    values (5, 11);
insert into post_lovers (post_id, lovers_id)
    values (5, 12);

-- post id 6 reposting id 4
insert into post (created_at, text_content, author_id, replying_to_id)
    values ('2025-01-05T09:03:00', 'I must disagree. The pursuit of pleasure is the path to a good life, but it''s about simple pleasures and avoiding pain, not indulgence.', 6, 4);

insert into post_lovers (post_id, lovers_id)
    values (6, 7);
insert into post_lovers (post_id, lovers_id)
    values (6, 9);
insert into post_lovers (post_id, lovers_id)
    values (6, 10);
insert into post_lovers (post_id, lovers_id)
    values (6, 13);

-- post id 7 replying to 4
insert into post (created_at, text_content, author_id, replying_to_id)
    values ('2025-01-05T09:04:00', '@epicurus, I find your views lacking a moral framework. The only true moral law is the categorical imperative: Act only according to that maxim whereby you can, at the same time, will that it should become a universal law.', 7, 4);

insert into post_lovers (post_id, lovers_id)
    values (7, 3);
insert into post_lovers (post_id, lovers_id)
    values (7, 6);
insert into post_lovers (post_id, lovers_id)
    values (7, 10);
insert into post_lovers (post_id, lovers_id)
    values (7, 12);
insert into post_lovers (post_id, lovers_id)
    values (7, 14);

-- post id 8
insert into post (created_at, text_content, author_id)
    values ('2025-01-05T09:15:00', 'All of your arguments are rooted in assumptions. I begin with doubt. "Cogito, ergo sum" — I think, therefore I am.', 8);

insert into post_lovers (post_id, lovers_id)
    values (8, 3);
insert into post_lovers (post_id, lovers_id)
    values (8, 4);
insert into post_lovers (post_id, lovers_id)
    values (8, 5);
insert into post_lovers (post_id, lovers_id)
    values (8, 6);
insert into post_lovers (post_id, lovers_id)
    values (8, 8);
insert into post_lovers (post_id, lovers_id)
    values (8, 9);
insert into post_lovers (post_id, lovers_id)
    values (8, 11);
insert into post_lovers (post_id, lovers_id)
    values (8, 12);
insert into post_lovers (post_id, lovers_id)
    values (8, 13);
insert into post_lovers (post_id, lovers_id)
    values (8, 14);

-- post id 9 replying to 8
insert into post (created_at, text_content, author_id, replying_to_id)
    values ('2025-01-05T09:16:00', '@descartes, your dualism is problematic. The mind and body are one and the same substance. God is not a separate entity but is in everything.', 9, 8);

insert into post_lovers (post_id, lovers_id)
    values (9, 10);

-- post id 10 replying to 8
insert into post (created_at, text_content, author_id, replying_to_id)
    values ('2025-01-05T09:17:00', '@spinoza, interesting take. But I would argue that knowledge is acquired through experience. We are born as a blank slate — "tabula rasa."', 10, 8);

insert into post_lovers (post_id, lovers_id)
    values (10, 4);
insert into post_lovers (post_id, lovers_id)
    values (10, 5);
insert into post_lovers (post_id, lovers_id)
    values (10, 6);
insert into post_lovers (post_id, lovers_id)
    values (10, 7);
insert into post_lovers (post_id, lovers_id)
    values (10, 11);

-- post id 11 replying to 8
insert into post (created_at, text_content, author_id, replying_to_id)
    values ('2025-01-05T09:18:00', '@locke, experience alone is not enough. We must also understand that human reason is fallible. Most of what we know is derived from habit, not logical deduction.', 11, 8);

insert into post_lovers (post_id, lovers_id)
    values (11, 4);
insert into post_lovers (post_id, lovers_id)
    values (11, 5);
insert into post_lovers (post_id, lovers_id)
    values (11, 6);
insert into post_lovers (post_id, lovers_id)
    values (11, 12);

-- post id 12
insert into post (created_at, text_content, author_id)
    values ('2025-01-05T09:20:00', 'History is a dialectical process — a constant evolution through thesis, antithesis, and synthesis.', 12);

insert into post_lovers (post_id, lovers_id)
    values (12, 3);
insert into post_lovers (post_id, lovers_id)
    values (12, 7);
insert into post_lovers (post_id, lovers_id)
    values (12, 8);
insert into post_lovers (post_id, lovers_id)
    values (12, 13);

-- post id 13 replying to 12
insert into post (created_at, text_content, author_id, replying_to_id)
    values ('2025-01-05T09:21:00', '@hegel, the dialectic leads to mediocrity. What humanity needs is a "Übermensch" — someone who creates their own values, beyond good and evil.', 13, 12);

insert into post_lovers (post_id, lovers_id)
    values (13, 4);
insert into post_lovers (post_id, lovers_id)
    values (13, 5);
insert into post_lovers (post_id, lovers_id)
    values (13, 10);
insert into post_lovers (post_id, lovers_id)
    values (13, 12);

-- post id 14 replying to 12
insert into post (created_at, text_content, author_id, replying_to_id)
    values ('2025-01-05T09:22:00', '@nietzsche, fascinating thread! But isn''t there value in combining perspectives? Perhaps through debate, we can achieve a deeper understanding of existence.', 14, 12);

insert into post_lovers (post_id, lovers_id)
    values (14, 3);
insert into post_lovers (post_id, lovers_id)
    values (14, 8);
insert into post_lovers (post_id, lovers_id)
    values (14, 13);

-- post id 15 replying to 12
insert into post (created_at, text_content, author_id, replying_to_id)
    values ('2025-01-05T09:23:00', '@rollo_rafael, indeed, young man. Dialogue is the essence of philosophical inquiry. Let us continue the pursuit of truth.', 3, 12);

insert into post_lovers (post_id, lovers_id)
    values (15, 4);
insert into post_lovers (post_id, lovers_id)
    values (15, 5);
insert into post_lovers (post_id, lovers_id)
    values (15, 6);
insert into post_lovers (post_id, lovers_id)
    values (15, 7);
insert into post_lovers (post_id, lovers_id)
    values (15, 8);
insert into post_lovers (post_id, lovers_id)
    values (15, 9);
insert into post_lovers (post_id, lovers_id)
    values (15, 10);
insert into post_lovers (post_id, lovers_id)
    values (15, 11);
insert into post_lovers (post_id, lovers_id)
    values (15, 12);
insert into post_lovers (post_id, lovers_id)
    values (15, 13);
insert into post_lovers (post_id, lovers_id)
    values (15, 14);

-- post id 16 reposting to 3
insert into post (created_at, author_id, reposting_id)
    values ('2025-01-05T09:30:00', 14, 3);

insert into post_lovers (post_id, lovers_id)
    values (16, 4);
insert into post_lovers (post_id, lovers_id)
    values (16, 5);
insert into post_lovers (post_id, lovers_id)
    values (16, 6);
insert into post_lovers (post_id, lovers_id)
    values (16, 12);

-- post id 17
insert into post (created_at, text_content, author_id)
    values ('2025-01-05T09:40:00', 'Government exists to protect our natural rights: life, liberty, and property. Without it, society would fall into chaos.', 10);

insert into post_lovers (post_id, lovers_id)
    values (17, 3);
insert into post_lovers (post_id, lovers_id)
    values (17, 6);
insert into post_lovers (post_id, lovers_id)
    values (17, 8);
insert into post_lovers (post_id, lovers_id)
    values (17, 13);

-- post id 18
insert into post (created_at, text_content, author_id, replying_to_id)
    values ('2025-01-05T09:41:00', '@locke, I question your reliance on the concept of natural rights. Human behavior is guided more by sentiment and custom than by abstract principles.', 11, 17);

-- post id 19
insert into post (created_at, text_content, author_id, reposting_id)
    values ('2025-01-05T09:42:00', 'The foundation of moral law cannot be derived from empirical observation. Instead, it must come from rational duty.', 7, 17);

insert into post_lovers (post_id, lovers_id)
    values (19, 4);
insert into post_lovers (post_id, lovers_id)
    values (19, 8);
insert into post_lovers (post_id, lovers_id)
    values (19, 10);
insert into post_lovers (post_id, lovers_id)
    values (19, 13);

-- post id 20
insert into post (created_at, text_content, author_id, replying_to_id)
    values ('2025-01-06T09:43:00', '@kant, I agree that reason is fundamental. However, we must first doubt everything to establish any certainty.', 8, 19);

-- post id 21
insert into post (created_at, text_content, author_id)
    values ('2025-01-06T09:44:00', 'Ethics must be derived from understanding the natural order of the universe. We are all part of a single, infinite substance.', 9);

-- post id 22
insert into post (created_at, text_content, author_id, replying_to_id)
    values ('2025-01-07T09:45:00', '@spinoza, that may be so, but ethics is also practical. Virtue is a habit formed by deliberate choice and balance.', 5, 21);

-- post id 23
insert into post (created_at, text_content, author_id, replying_to_id)
    values ('2025-01-07T09:46:00', '@aristotle, virtue is a relic of the past. We must transcend traditional values to create our own path.', 13, 21);

-- post id 24
insert into post (created_at, text_content, author_id)
    values ('2025-01-08T09:50:00', 'We cannot trust our senses to reveal the true nature of reality. I begin with doubt because it is the only way to find certainty.', 8);

insert into post_lovers (post_id, lovers_id)
    values (24, 9);
insert into post_lovers (post_id, lovers_id)
    values (24, 7);
insert into post_lovers (post_id, lovers_id)
    values (24, 10);
insert into post_lovers (post_id, lovers_id)
    values (24, 11);
insert into post_lovers (post_id, lovers_id)
    values (24, 12);
insert into post_lovers (post_id, lovers_id)
    values (24, 13);
insert into post_lovers (post_id, lovers_id)
    values (24, 3);
insert into post_lovers (post_id, lovers_id)
    values (24, 4);

-- post id 25
insert into post (created_at, text_content, author_id, replying_to_id)
    values ('2025-01-08T09:55:00', '@descartes Doubting everything leads to confusion. The universe operates according to natural laws, and understanding those laws leads to clarity.', 9, 24);

-- post id 26
insert into post (created_at, text_content, author_id, replying_to_id)
    values ('2025-01-08T10:00:00', 'We cannot perceive things as they are in themselves. Our mind imposes structure on the world, and we experience phenomena, not noumena.', 7, 24);

-- post id 27
insert into post (created_at, text_content, author_id, replying_to_id)
    values ('2025-01-09T10:05:00', '@kant, I must disagree. Our knowledge comes from experience. We begin with a blank slate, and through sensory input, we gain understanding.', 10, 24);

-- post id 28
insert into post (created_at, text_content, author_id, replying_to_id)
    values ('2025-01-09T10:10:00', '@locke, sensory experience alone is unreliable. Much of our understanding is based on patterns and habits we observe, not on absolute truths.', 11, 24);

-- post id 29
insert into post (created_at, text_content, author_id, replying_to_id)
    values ('2025-01-10T10:15:00', 'Reality is shaped by history and the unfolding of ideas. Truth emerges through the dialectical process over time.', 12, 24);

-- post id 30
insert into post (created_at, text_content, author_id, replying_to_id)
    values ('2025-01-10T10:20:00', '@hegel, your belief in historical progression is naive. Reality is chaotic, and meaning is something we must create ourselves.', 13, 24);
