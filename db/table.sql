create database team17_database;
use team17_database;
create table Users (
	user_email varchar(50) not null,
	user_id int not null auto_increment,
    user_name varchar(50) not null,
    user_type tinyint(1) not null,
    primary key (user_id)
);

create table Classes (
	class_id int not null auto_increment,
    class_name varchar(50) not null,
    class_code varchar(20) not null,
    professor_id int not null,
    primary key (class_id),
    foreign key (professor_id) references Users(user_id)
);

create table Classrooms (
    building_number int not null,
    room_number int not null,
    logitude float(13, 10),
    latitude float(13, 10),
    beacon_id varchar(50),
    primary key (building_number, room_number)
);

create table Takes (
	student_id int not null,
    class_id int not null,
    foreign key (student_id) references Users(user_id),
    foreign key (class_id) references Classes(class_id)
);

create table Teaches (
	professor_id int not null,
    class_id int not null,
    foreign key (professor_id) references Users(user_id),
    foreign key (class_id) references Classes(class_id)
);

create table Class_times (
	class_id int not null,
	start_time time,
    end_time time,
    day_of_week varchar(10),
    foreign key (class_id) references Classes(class_id)
);

create table Channels (
	channel_id int not null auto_increment primary key,
    class_id int not null,
    channel_type int not null,
	foreign key (class_id) references Classes(class_id)
);

create table Messages (
    message_id int not null auto_increment primary key,
    time_stamp datetime not null default now(),
    sender_id int not null,
    content varchar(500),
    channel_id int not null,
    foreign key (channel_id) references Channels(channel_id),
    foreign key (sender_id) references Users(user_id)
);

create table Class_Classroom (
    building_number int not null,
    room_number int not null,
    class_id int not null,
    foreign key (class_id) references Classes(class_id),
    foreign key (building_number, room_number) references Classrooms(building_number, room_number)
);

create table Locations (
	time_stamp datetime not null,
    beacon_correct tinyint(1) not null,
    logitude float(13, 10),
    latitude float(13, 10),
    student_id int not null,
    foreign key (student_id) references Users(user_id)
);

create table Attendances (
	attendance_id int not null auto_increment primary key,
    attendance_date date not null,
    attendance_status int(1),
    attendance_duration int(10),
    is_sent tinyint(1),
	student_id int not null,
    foreign key (student_id) references Users(user_id),
    check (attendance_status >= 0 and attendance_status <= 2)
);




