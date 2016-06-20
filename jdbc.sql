create table tube(
	seq number primary key,
	category varchar(30) not null,
	title varchar(30) not null,
	url varchar(30) not null);
	
 create sequence seq_num
 	increment by 10
 	start with 10;
 	
 commit
 
 delete from tube;
 
 alter table tube
 add(user varchar(30) not null);
 
 create table tube_user(
 name varchar(30) primary key,
 password varchar(30) not null);
 

 