const mysql = require('mysql');
const express = require('express');
const bodyParser = require('body-parser');
const app = express();
const port = 3000;

app.set("port", port);
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

app.get('/', (req, res) => {
  res.send('Hello World!');
});

app.listen(port, () => {
    console.log('Server Runnning...');
});

const connection = mysql.createConnection({
    host: "swpp-2023-project-team-17-database.c8tunufhlk0u.us-east-1.rds.amazonaws.com",
    user: "admin",
    database: "team17_database",
    password: "admin1234",
    port: 3306
});

/*
    user log in using gmail
    NOTE: May need changes for social login - current: get token -> process token -> if auth, pass user_email
*/
app.post('/login', (req, res) => {
    console.log(req.body);
    const userEmail = req.body.userEmail;

    const sql = 'select * from Users where user_email = ?';
    const params = [userEmail];

    connection.query(sql, params, (err, result) => {
        let resultCode = 404;
        let message = 'Error occured';
        let userId = -1;
        let userName = -1;
        let userType = -1;

        if (err) {
            console.log(err);
        } else {
            resultCode = 200;
            message = 'login Success';
            console.log(message);
            userId = result[0].user_id;
            userName = result[0].user_name;
            userType = result[0].user_type;
        }

        res.json({
            'code': resultCode,
            'message': message,
            'user_email': userEmail,
            'user_id': userId, 
            'user_name': userName,
            'user_type': userType
        });
    });
});

/*
    user log out
*/
app.post('/logout', (req, res) => {
    console.log(req.body);

    let resultCode = 200;
    let message = 'logout Success';

    res.json({
        'code': resultCode,
        'message': message
    });
});

/*
    get user name, email, and user type with user_id of Users
*/
app.get('/users/:id', (req, res) => {
    console.log(req.body);

    const userId = req.params.id;
    const sql = 'select user_name, user_type from Users where user_id = ?';
    const params = [userId];

    connection.query(sql, params, (err, result) => {
        let resultCode = 404;
        let message = 'Error occured';
        let userEmail = -1;
        let userName = -1;
        let userType = -1;

        if (err) {
            console.log(err);
        } else {
            resultCode = 200;
            message = 'get userName and userType Success';
            console.log(message);
            userEmail = result[0].user_id;
            userName = result[0].user_name;
            userType = result[0].user_type;
        }

        res.json({
            'code': resultCode,
            'message': message,
            'user_email': userEmail,
            'user_name': userName,
            'user_type': userType
        });
    });
});

/*
    edit user name and user type with user_id of Users
*/
app.put('/users/:id', (req, res) => {
    console.log(req.body);

    const userName = req.body.userName;
    const userType = req.body.userType;
    const userId = req.params.id;

    const sql = 'update Users set user_name = ?, user_type = ? where user_id = ?';
    const params = [userName, userType, userId];

    connection.query(sql, params, (err, result) => {
        let resultCode = 404;
        let message = 'Error occured';

        if (err) {
            console.log(err);
        } else {
            resultCode = 200;
            message = 'update userName, userType Success';
            console.log(message);
        }

        res.json({
            'code': resultCode,
            'message': message
        });
    });
});

/*
    get all user's classes with user_id and user_type of Users
*/
app.get('/users/:id/classes', (req, res) => {
    console.log(req.body);

    const userId = req.params.id;
    const userType = req.body.userType;

    const sql = 'select * from Classes ';
    if(userType == 0) {
        sql = 'where class_id in (select class_id from Takes where student_id = ?)';
    } else {
        sql += 'where class_id in (select class_id from Teaches where professor_id = ?)';
    }

    const params = [userId];

    connection.query(sql, params, (err, result) => {
        let resultCode = 404;
        let message = 'Error occured';
        let classList = -1;

        if (err) {
            console.log(err);
        } else {
            resultCode = 200;
            message = 'get classes Success';
            console.log(message);
            classList = result;
        }

        res.json({
            'code': resultCode,
            'message': message,
            'classList': classList
        });
    });
});

/*
    get all attendance in specific date with user_id and user_type of Users
    User type must be professor
*/
app.get('users/attendance/:date', (req, res) => {
    console.log(req.body);

    const attendanceDate = req.params.date;
    const professorId = req.body.userId;
    const userType = req.body.userType;

    const sql = 'select * from Attendances '
                + 'where is_sent = 1 and attendance_date = ? and class_id '
                + 'in (select class_id from Teaches where professor_id = ?)';

    const params = [attendanceDate, professorId];

    connection.query(sql, params, (err, result) => {
        let resultCode = 404;
        let message = 'Error occured';
        let attendanceList = -1;
        if (!userType) message = 'You must be a professor' // only for professors

        if (err) {
            console.log(err);
        } else {
            resultCode = 200;
            message = 'get all attendances in specific date Success';
            console.log(message);
            attendanceList = result;
        }

        res.json({
            'code': resultCode,
            'message': message,
            'attendanceList': attendanceList
        });
    });
});

/*
    get attendace_date list from Attendances with user_id of Users
    User type must be professor
*/
app.get('users/attendance', (req, res) => {
    console.log(req.body);

    const professorId = req.body.userId;

    const sql = 'select attendance_date from Attendances '
                + 'where is_sent = 1 and class_id '
                + 'in (select class_id from Teaches where professor_id = ?)';

    const params = [professorId];

    connection.query(sql, params, (err, result) => {
        let resultCode = 404;
        let message = 'Error occured';
        let attendanceDateList = -1;
        if (!userType) message = 'You must be a professor' // only for professors

        if (err) {
            console.log(err);
        } else {
            resultCode = 200;
            message = 'get attendance date list Success';
            console.log(message);
            attendanceDateList = result; // TODO: change to list with only date values
        }

        res.json({
            'code': resultCode,
            'message': message,
            'attendanceDateList': attendanceDateList
        });
    });
});

/*
    create new class and insert to Classes, Class_classroom, Teaches
    User type must be professor
*/
// TODO: check user_type == 1 (TBD later)
app.post('/class/create/:user_id', (req, res) => {
    console.log(req.body);

    const className = req.body.className;
    const classCode = req.body.classCode;
    const professorId = req.params.user_id;
    const buildingNumber = req.body.buildingNumber;
    const roomNumber = req.body.roomNumber;

    var sql = 'insert into Classes(class_name, class_code, professor_id) values(?, ?, ?)';

    var params = [className, classCode, professorId];

    connection.query(sql, params, (err, result) => {
        let resultCode = 404;
        let message = 'Error occured';
        if (!userType) message = 'You must be a professor' // only for professors

        if (err) {
            console.log(err);
        } else {
            resultCode = 200;
            message = 'create class Success';
            console.log(message);
        }

        res.json({
            'code': resultCode,
            'message': message
        });
    });

    sql = 'insert into Teaches(class_id, professor_id) ' +
                '(SELECT class_id, professor_id FROM Class WHERE class_name = ? and class_code = ? and professor_id = ?)';

    params = [className, classCode, professorId];

    connection.query(sql, params, (err, result) => {
        let resultCode = 404;
        let message = 'Error occured';

        if (err) {
            console.log(err);
        } else {
            resultCode = 200;
            message = 'link class to professor Success';
            console.log(message);
        }

        res.json({
            'code': resultCode,
            'message': message
        });
    });

    sql = 'insert into Class_Classroom(building_number, room_number, class_id) ' +
                'VALUES(?, ?, (SELECT class_id FROM Class WHERE class_name = ? and class_code = ? and professor_id = ?))';

    params = [buildingNumber, roomNumber, professorId];

    connection.query(sql, params, (err, result) => {
        let resultCode = 404;
        let message = 'Error occured';

        if (err) {
            console.log(err);
        } else {
            resultCode = 200;
            message = 'link class to classroom Success';
            console.log(message);
        }

        res.json({
            'code': resultCode,
            'message': message
        });
    });
});

/*
    join class and insert to Classes, Class_classroom, Teaches
    User type must be student
*/
// TODO: check user_type == 1 (TBD later)
app.post('/class/join/:user_id', (req, res) => {
    console.log(req.body);

    const className = req.body.className;
    const classCode = req.body.classCode;
    const userId = req.params.user_id;

    const sql = 'insert into Takes(class_id, student_id) ' +
                'values((SELECT class_id FROM Class WHERE class_name = ? and class_code = ?), ?)';

    const params = [className, classCode, userId];

    connection.query(sql, params, (err, result) => {
        let resultCode = 404;
        let message = 'Error occured';

        if (err) {
            console.log(err);
        } else {
            resultCode = 200;
            message = 'class join Success';
            console.log(message);
        }

        res.json({
            'code': resultCode,
            'message': message
        });
    });
});

/*
    get specific class from Classes
*/
app.get('/class/:id', (req, res) => {
    console.log(req.body);

    const classId = req.params.id;

    const sql = 'select * from Classes where class_id = ?';

    const params = [classId];

    connection.query(sql, params, (err, result) => {
        let resultCode = 404;
        let message = 'Error occured';
        let className = -1;
        let classCode = -1;
        let professorId = -1;
        let classTime = -1;

        if (err) {
            console.log(err);
        } else {
            resultCode = 200;
            message = 'get specific class Success';
            console.log(message);
            className = result[0].class_name;
            classCode = result[0].class_code;
            professorId = result[0].professor_id;
            classTime = result[0].class_time;
        }

        res.json({
            'code': resultCode,
            'message': message,
            'className': className,
            'classCode': classCode,
            'professorId': professorId,
            'classTime': classTime
        });
    });
});

/*
    delete specific class from Classes
*/
app.delete('/class/:id', (req, res) => {
    console.log(req.body);

    const classId = req.params.id;

    const sql = 'delete from Classes where class_id = ?';

    const params = [classId];

    connection.query(sql, params, (err, result) => {
        let resultCode = 404;
        let message = 'Error occured';

        if (err) {
            console.log(err);
        } else {
            resultCode = 200;
            message = 'delete class Success';
            console.log(message);
        }

        res.json({
            'code': resultCode,
            'message': message
        });
    });
});

/*
    get chatting channel with class_id of Classes
*/
app.get('class/chat_channel/:type', (req, res) => {
    console.log(req.body);

    const channelType = req.params.type; // 0 or 1
    const classId = req.body.classId;

    const sql = 'select * from Channels where channel_type = ? and class_id = ?';
    const params = [channelType, classId];

    connection.query(sql, params, (err, result) => {
        let resultCode = 404;
        let message = 'Error occured';
        let classId = -1;
        let channelType = -1;

        if (err) {
            console.log(err);
        } else {
            resultCode = 200;
            message = 'get chatting channel Success';
            console.log(message);
            classId = result[0].class_id;
            channelType = result[0].channel_type;
        }

        res.json({
            'code': resultCode,
            'message': message,
            'classId': classId,
            'channelType': channelType
        });
    });
});

/*
    get student’s attendance information list with class_id of Classes
*/
app.get('class/attendance/:user_id', (req, res) => {
    console.log(req.body);

    const userId = req.params.user_id;
    const classId = req.body.classId;

    const sql = 'select * from Attendances where student_id = ? and class_id = ?';

    const params = [userId, classId];

    connection.query(sql, params, (err, result) => {
        let resultCode = 404;
        let message = 'Error occured';
        let attendanceList = -1;

        if (err) {
            console.log(err);
        } else {
            resultCode = 200;
            message = 'get user attendance list Success';
            console.log(message);
            attendanceList = result;
        }

        res.json({
            'code': resultCode,
            'message': message,
            'attendanceList': attendanceList
        });
    });
});

/*
    get messages list in chatting channel
*/
app.get('/chat_channel/:id', (req, res) => {
    console.log(req.body);

    const channelId = req.params.id;

    const sql = 'select * from Messages where channel_id = ?';

    const params = [channelId];

    connection.query(sql, params, (err, result) => {
        let resultCode = 404;
        let message = 'Error occured';
        let messageList = -1;

        if (err) {
            console.log(err);
        } else {
            resultCode = 200;
            message = 'get chatting list Success';
            console.log(message);
            messageList = result;
        }

        res.json({
            'code': resultCode,
            'message': message,
            'messageList': messageList
        });
    });
});

/*
    send new message with channel_id, content of Messages
*/
app.post('/chat_channel/:id', (req, res) => {
    console.log(req.body);

    const senderId = req.body.senderId;
    const content = req.body.content;
    const channelId = req.params.id;

    const sql = 'insert into Messages(sender_id, content, channel_id) values(?, ?, ?)';

    const params = [senderId, content, channelId];

    connection.query(sql, params, (err, result) => {
        let resultCode = 404;
        let message = 'Error occured';

        if (err) {
            console.log(err);
        } else {
            resultCode = 200;
            message = 'send new message Success';
            console.log(message);
        }

        res.json({
            'code': resultCode,
            'message': message
        });
    });
});

/*
    get attendance information
*/
app.get('/attendance/:id', (req, res) => {
    console.log(req.body);

    const attendanceId = req.params.id;

    const sql = 'select * from Attendances where attendance_id = ?';

    const params = [attendanceId];

    connection.query(sql, params, (err, result) => {
        let resultCode = 404;
        let message = 'Error occured';
        let attendaceDate = -1;
        let attendanceStatus = -1;
        let attendaceDuration = -1;
        let isSent = -1;
        let studentId = -1;
        let classId = -1;

        if (err) {
            console.log(err);
        } else {
            resultCode = 200;
            message = 'get attendance information Success';
            console.log(message);
            attendaceDate = result[0].attendance_date;
            attendanceStatus = result[0].attendance_status;
            attendaceDuration = result[0].attendance_duration;
            isSent = result[0].is_sent;
            studentId = result[0].student_id;
            classId = result[0].class_id;
        }

        res.json({
            'code': resultCode,
            'message': message,
            'attendaceDate': attendaceDate,
            'attendanceStatus': attendanceStatus,
            'attendaceDuration': attendaceDuration,
            'isSent': isSent,
            'studentId': studentId,
            'classId': classId
        });
    });
});

/*
    edit is_sent as sent (i.e. is_sent = 1)
*/
app.put('/attendance/:id', (req, res) => {
    console.log(req.body);

    const attendanceId = req.params.id;

    const sql = 'update Attendances set is_sent = 1 where attendance_id = ?';

    const params = [attendanceId];

    connection.query(sql, params, (err, result) => {
        let resultCode = 404;
        let message = 'Error occured';

        if (err) {
            console.log(err);
        } else {
            resultCode = 200;
            message = 'send attendance information Success';
            console.log(message);
        }

        res.json({
            'code': resultCode,
            'message': message
        });
    });
});

/*
    delete attendance information
*/
app.delete('/attendance/:id', (req, res) => {
    console.log(req.body);

    const attendanceId = req.params.id;

    const sql = 'delete from Attendances where attendance_id = ?';

    const params = [attendanceId];

    connection.query(sql, params, (err, result) => {
        let resultCode = 404;
        let message = 'Error occured';

        if (err) {
            console.log(err);
        } else {
            resultCode = 200;
            message = 'delete attendance information Success';
            console.log(message);
        }

        res.json({
            'code': resultCode,
            'message': message
        });
    });
});

/*
    add student’s attendance information with attendance_status, attendace_duration, class_id of Attendances
*/
app.post('attendance/:user_id', (req, res) => {
    console.log(req.body);

    const attendanceStatus = req.body.attendanceStatus;
    const attendanceDuration = req.body.attendanceDuration;
    const userId = req.params.user_id;
    const classId = req.body.classId;

    const sql = 'insert into Attendances(attendance_status, attendance_duration, student_id, class_id) '
              + 'values(?, ?, ?, ?)';

    const params = [attendanceStatus, attendanceDuration, userId, classId];

    connection.query(sql, params, (err, result) => {
        let resultCode = 404;
        let message = 'Error occured';

        if (err) {
            console.log(err);
        } else {
            resultCode = 200;
            message = 'add attendance information Success';
            console.log(message);
        }

        res.json({
            'code': resultCode,
            'message': message
        });
    });
});