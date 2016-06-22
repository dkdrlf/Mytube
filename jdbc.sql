create table tube(
	seq number primary key,
	category varchar(30) not null,
	title varchar(30) not null,
	url varchar(100) not null,
	name varchar(40) not null,
	constraint tube_name_fk foreign key(name) 
	references tube_user(name) on delete cascade);
 create sequence seq_num
 	increment by 10
 	start with 10;
 	
 commit
 
 delete from tube;
 delete from tube_user;
 
 alter table tube
 add(user varchar(30) not null);
 
 create table tube_user(
 name varchar(30) primary key,
 password varchar(30) not null);
 
drop table tube;

insert into tube_user
values('111','1111');

insert into tube
values(seq_num.nextval,'교육','bb','111','aaaaaaa');

constraint tube_name_fk foreign key(name) references tube_user(name) on delete cascade,