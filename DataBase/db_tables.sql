drop database team17_database;
create database team17_database;
use team17_database;

create table Users (
	user_id int not null auto_increment primary key,
	user_email varchar(50) not null,
    user_name varchar(50) not null,
    user_type tinyint(1) not null,
    unique (user_email)
);

create table Classes (
	class_id int not null auto_increment primary key,
    class_name varchar(50) not null,
    class_code varchar(20) not null,
    professor_id int not null,
    class_time varchar(100) not null, -- ex. "TUE 15:30-17:00, THU 15:30-17:00"
    building_number int not null,
    room_number int not null,
    foreign key (professor_id) references Users(user_id)
);

create table Takes (
	student_id int not null,
    class_id int not null,
    primary key (student_id, class_id),
    foreign key (student_id) references Users(user_id),
    foreign key (class_id) references Classes(class_id) ON DELETE CASCADE
);


create table Messages (
    message_id int not null auto_increment primary key,
    comment_id int default -1,
	class_id int not null,
    time_stamp datetime not null default now(),
    sender_id int not null,
    sender_name varchar(50) not null,
    content varchar(4000),
    foreign key (class_id) references Classes(class_id) ON DELETE CASCADE,
    foreign key (sender_id) references Users(user_id)
);

create table Attendances (
	attendance_id int not null auto_increment primary key,
    attendance_date date not null default current_date(),
    attendance_status int(1),
    attendance_duration int(10),
    is_sent tinyint(1) not null default 0,
	student_id int not null,
    class_id int not null,
    foreign key (class_id) references Takes(class_id) ON DELETE CASCADE,
	foreign key (student_id) references Takes(student_id),
	check (attendance_status >= 0 and attendance_status <= 2)
);

COMMIT;

