const port = 3000;
const bodyParser = require('body-parser');
const mysql = require('mysql');
const express = require('express');
const app = express();
const server = require('http').createServer(app);
const io = require('socket.io')(server);
const connection = mysql.createConnection({
    host: "team17-database.cqeij4pxj0w8.ap-northeast-2.rds.amazonaws.com",
    user: "admin",
    database: "team17_database",
    password: "admin1234",
    port: 3306
});

app.set("port", port);
app.use(bodyParser.json());
app.use(express.json());
app.use(bodyParser.urlencoded({ extended: true }));
server.listen(port, () => {
    console.log('Server Running...');
});
module.exports = { app, connection };


// chat with socket
io.on('connection', function (socket) {
    socket.on('joinRoom', function (data) {
        socket.join(`${data.class_id}_${data.comment_id}_${socket.userid}`);
        console.log(`joinRoom: ${data.class_id}_${data.comment_id}`);
    });

    socket.on('chat', function (data) {
        console.log('Message in room %s: %s', `${data.class_id}_${data.comment_id}_${socket.userid}`, data.msg);
        const msg = {
            from: {
                name: socket.name,
                userid: socket.userid
            },
            class_id: data.class_id,
            comment_id: data.comment_id,
            sender_name: data.sender_name,
            msg: data.msg,
            time_stamp: data.time_stamp
        };
        io.to(`${data.class_id}_${data.comment_id}_${socket.userid}`).emit('chat', msg);
    });

    socket.on('forceDisconnect', function () {
        socket.disconnect();
    });

    socket.on('disconnect', function () {
        console.log(`user disconnected: ${socket.userid}`);
    });
});


/* (1)
    user login using gmail and get user information
    NOTE: May need changes for social login - current: get token -> process token -> if auth, pass user_email
*/
app.post('/login/:email', (req, res) => {
    const userEmail = req.params.email;
    var sql = 'SELECT * FROM Users WHERE user_email = ?';
    const params = [userEmail];

    connection.query(sql, params, (err, result) => {
        if (err) {
            return res.status(500).json({
                'code': 500,
                'message': 'database error'
            });
        }

        if (result.length > 0) {
            return res.json({
                'code': 200,
                'message': 'Login success',
                'userEmail': userEmail,
                'userId': result[0].user_id,
                'userName': result[0].user_name,
                'userType': result[0].user_type
            });
        } else {
            sql = 'INSERT INTO Users (user_email, user_name, user_type) VALUES (?, "", 0)';
            connection.query(sql, params, (err, result) => {
                if (err) {
                    return res.status(500).json({
                        'code': 500,
                        'message': 'database error'
                    });
                }

                sql = 'SELECT LAST_INSERT_ID() AS user_id';
                connection.query(sql, (err, result) => {
                    if (err || result.length == 0) {
                        return res.status(500).json({
                            'code': 500,
                            'message': 'database error'
                        });
                    }

                    return res.json({
                        'code': 200,
                        'message': 'Signup success',
                        'userEmail': userEmail,
                        'userId': result[0].user_id,
                        'userName': '',
                        'userType': 0
                    });
                });
            });
        }
    });
});


/* (2)
    get all user's classes with user_type of Users
*/
app.get('/users/classes', (req, res) => {
    const userId = req.query.userId;
    const userType = req.query.userType;

    var sql = 'select * from Classes ';
    if (userType == 0 || userType == '0') {
        sql += 'where class_id in (select class_id from Takes where student_id = ?)';
    } else {
        sql += 'where professor_id = ?';
    }

    const params = [userId];

    connection.query(sql, params, (err, result) => {
        let resultCode = 404;
        let message = 'Error occured';
        let classList = [];

        if (err) {
            return res.status(500).json({
                'code': 500,
                'message': 'database error'
            });
        }

        if (result.length > 0) {
            resultCode = 200;
            message = 'get classes Success';
            classList = result.map(classItem => ({
                "classId": classItem.class_id,
                "className": classItem.class_name,
                "classCode": classItem.class_code,
                "professorId": classItem.professor_id,
                "classTime": classItem.class_time,
                "buildingNumber": classItem.building_number,
                "roomNumber": classItem.room_number,
            }));
        } else {
            resultCode = 200;
            message = 'classList is empty';
        }

        res.json({
            'code': resultCode,
            'message': message,
            'classList': classList
        });
    });
});


/* (3)
    get attendace_date list from Attendances with user_id of Users where is_sent = 1
*/
app.get('/users/attendance', (req, res) => {
    const classId = req.query.classId;
    const sql = 'select attendance_date from Attendances '
        + 'where is_sent = 1 and class_id = ? '
        + 'group by attendance_date';
    const params = [classId];

    connection.query(sql, params, (err, result) => {
        let resultCode = 404;
        let message = 'Error occured';
        let attendanceDateList = [];

        if (err) {
            return res.status(500).json({
                'code': 500,
                'message': 'database error'
            });
        }
        
        if (result.length > 0) {
            resultCode = 200;
            message = 'get attendance date list Success';
            attendanceDateList = result.map(item => ({
                "attendanceDate": item.attendance_date
            }));
        } else {
            resultCode = 200;
            message = 'attendanceDateList is empty';
        }

        res.json({
            'code': resultCode,
            'message': message,
            'attendanceDateList': attendanceDateList
        });
    });
});


/* (4)
    get all attendance in specific date with user_id and user_type of Users where is_sent = 1
*/
app.get('/users/attendance/:date', (req, res) => {
    const attendanceDate = req.params.date;
    const classId = req.query.classId;
    const sql = 'SELECT Attendances.*, Users.user_name '
                + 'FROM Attendances '
                + 'INNER JOIN Users ON Attendances.student_id = Users.user_id '
                + 'where is_sent = 1 and attendance_date = ? and class_id = ?';
    const params = [attendanceDate, classId];

    connection.query(sql, params, (err, result) => {
        let resultCode = 404;
        let message = 'Error occured';
        let attendanceList = [];

        if (err) {
            return res.status(500).json({
                'code': 500,
                'message': 'database error'
            });
        }
        
        if (result.length > 0) {
            resultCode = 200;
            message = 'get all attendances in specific date Success';
            attendanceList = result.map(item => ({
                "attendanceId": item.attendance_id,
                "attendanceStatus": item.attendance_status,
                "attendanceDate": item.attendance_date,
                "attendanceDuration": item.attendance_duration,
                "isSent": item.is_sent,
                "classId": item.class_id,
                "studentId": item.student_id,
                "userName": item.user_name,
                "attendanceDetail": item.attendance_detail
            }));
        } else {
            resultCode = 200;
            message = 'attendanceList is empty';
        }

        res.json({
            'code': resultCode,
            'message': message,
            'attendanceList': attendanceList
        });
    });
});


/* (5)
    get user name and user type
*/
app.get('/users/:id', (req, res) => {
    const userId = req.params.id;
    const sql = 'select user_name, user_type from Users where user_id = ?';
    const params = [userId];

    connection.query(sql, params, (err, result) => {
        let resultCode = 404;
        let message = 'Error occured';
        let userName = "";
        let userType = -1;

        if (err) {
            return res.status(500).json({
                'code': 500,
                'message': 'database error'
            });
        }
        
        if (result.length > 0) {
            resultCode = 200;
            message = 'get userName and userType Success';
            userName = result[0].user_name;
            userType = result[0].user_type;
        } else {
            resultCode = 200;
            message = 'There is no user corresponding to that id';
        }

        res.json({
            'code': resultCode,
            'message': message,
            'userName': userName,
            'userType': userType
        });
    });
});


/* (6)
    edit user name and user type with user_name and user_type of Users
*/
app.put('/users/:id', (req, res) => {
    const userName = req.body.userName;
    const userType = req.body.userType;
    const userId = req.params.id;
    const sql = 'update Users set user_name = ?, user_type = ? where user_id = ?';
    const params = [userName, userType, userId];

    connection.query(sql, params, (err, result) => {
        let resultCode = 200;
        let message = 'update userName, userType Success';

        if (err) {
            return res.status(500).json({
                'code': 500,
                'message': 'database error'
            });
        }

        res.json({
            'code': resultCode,
            'message': message
        });
    });
});


// (7) class create
app.post('/class/create', (req, res) => {
    const className = req.body.className;
    const classCode = req.body.classCode;
    const professorId = req.body.professorId;
    const classTime = req.body.classTime;
    const buildingNumber = req.body.buildingNumber;
    const roomNumber = req.body.roomNumber;

    if (!className || !classCode || !professorId || !classTime || !buildingNumber || !roomNumber) {
        return res.json({
            'code': 404,
            'message': 'Required fields are missing'
        });
    }

    let sql = 'INSERT INTO Classes(class_name, class_code, professor_id, class_time, building_number, room_number) VALUES (?, ?, ?, ?, ?, ?)';
    const params = [className, classCode, professorId, classTime, buildingNumber, roomNumber];

    connection.query(sql, params, (err, result) => {
        if (err) {
            return res.status(500).json({
                'code': 500,
                'message': 'database error'
            });
        }

        const insertedClassId = result.insertId;
        res.json({
            'code': 200,
            'message': "Create class success",
            'classId': insertedClassId
        });
    });
});


// (8) join class 
app.post('/class/join/:user_id', (req, res) => {
    const className = req.body.className;
    const classCode = req.body.classCode;
    const userId = req.params.user_id;
    const findClassSql = 'SELECT class_id, class_time FROM Classes WHERE class_name = ? AND class_code = ?';
    const findClassParams = [className, classCode];

    connection.query(findClassSql, findClassParams, (err, classResult) => {
        if (err) {
            return res.status(500).json({
                'code': 500,
                'message': 'database error'
            });
        }

        if (classResult.length == 0) {
            return res.json({
                'code': 404,
                'message': 'Class not found'
            });
        }

        const classId = classResult[0].class_id;
        const classTime = classResult[0].class_time;
        const joinClassSql = 'INSERT INTO Takes(class_id, student_id) VALUES(?, ?)';
        const joinClassParams = [classId, userId];

        connection.query(joinClassSql, joinClassParams, (err, joinResult) => {
            if (err) {
                return res.status(500).json({
                    'code': 500,
                    'message': 'Already joined the class'
                });
            }

            res.json({
                'code': 200,
                'message': 'Class join success',
                'classId': classId,
                'classTime': classTime
            });
        });
    });
});


/* (9)
    get specific class from Classes
*/
app.get('/class/:id', (req, res) => {
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
        let buildingNumber = -1;
        let roomNumber = -1;

        if (err) {
            return res.status(500).json({
                'code': 500,
                'message': 'database error'
            });
        }
        
        if (result.length > 0) {
            resultCode = 200;
            message = 'get specific class Success';
            className = result[0].class_name;
            classCode = result[0].class_code;
            professorId = result[0].professor_id;
            classTime = result[0].class_time;
            buildingNumber = result[0].building_number;
            roomNumber = result[0].room_number;
        } else {
            resultCode = 200;
            message = 'There is no class corresponding to that class_id';
        }

        res.json({
            'code': resultCode,
            'message': message,
            'className': className,
            'classCode': classCode,
            'professorId': professorId,
            'classTime': classTime,
            'buildingNumber': buildingNumber,
            'roomNumber': roomNumber
        });
    });
});


/* (10)
    delete specific class from Classes
*/
app.delete('/class/:id', (req, res) => {
    const classId = req.params.id;
    const sql = 'delete from Classes where class_id = ?';
    const params = [classId];

    connection.query(sql, params, (err, result) => {
        let resultCode = 200;
        let message = 'delete class Success';

        if (err) {
            return res.status(500).json({
                'code': 500,
                'message': 'database error'
            });
        }

        res.json({
            'code': resultCode,
            'message': message
        });
    });
});


/* (11)
    get student’s attendance information list with class_id of Classes
*/
app.get('/class/:id/attendance/:user_id', (req, res) => {
    const userId = req.params.user_id;
    const classId = req.params.id;
    const sql = 'SELECT Attendances.*, Users.user_name '
                + 'FROM Attendances '
                + 'INNER JOIN Users ON Attendances.student_id = Users.user_id '
                + 'where student_id = ? and class_id = ?';
    const params = [userId, classId];

    connection.query(sql, params, (err, result) => {
        let resultCode = 404;
        let message = 'Error occured';
        let attendanceList = [];

        if (err) {
            return res.status(500).json({
                'code': 500,
                'message': 'database error'
            });
        }
        
        if (result.length > 0) {
            resultCode = 200;
            message = 'get user attendance list Success';
            attendanceList = result.map(item => ({
                "attendanceId": item.attendance_id,
                "attendanceStatus": item.attendance_status,
                "attendanceDuration": item.attendance_duration,
                "attendanceDate": item.attendance_date,
                "isSent": item.is_sent,
                "classId": item.class_id,
                "studentId": item.student_id,
                "userName": item.user_name,
                "attendanceDetail": item.attendance_detail
            }));
        } else {
            resultCode = 200;
            message = 'attendanceList is empty';
        }

        res.json({
            'code': resultCode,
            'message': message,
            'attendanceList': attendanceList
        });
    });
});


// (12) get comment message list of the message
app.get('/chat_channel/:class_id/comment/:id', (req, res) => {
    const classId = req.params.class_id;
    const commentId = req.params.id;
    const sql = 'select * from Messages where class_id = ? and comment_id = ? order by time_stamp';
    const params = [classId, commentId];

    connection.query(sql, params, (err, result) => {
        let resultCode = 404;
        let message = 'Error occured';
        let messageList = [];

        if (err) {
            return res.status(500).json({
                'code': 500,
                'message': 'database error'
            });
        }
        
        if (result.length > 0) {
            resultCode = 200;
            message = 'get comment message list Success';
            messageList = result.map(item => ({
                "messageId": item.message_id,
                "commentId": item.comment_id,
                "classId": item.class_id,
                "timeStamp": item.time_stamp,
                "senderId": item.sender_id,
                "senderName": item.sender_name,
                "content": item.content
            }));
        } else {
            resultCode = 200;
            message = 'messageList is empty';
        }

        res.json({
            'code': resultCode,
            'message': message,
            'messageList': messageList
        });
    });
});


// (13) reply comment on a message
app.post('/chat_channel/:class_id/comment/:id', (req, res) => {
    const classId = req.params.class_id;
    const commentId = req.params.id;
    const senderId = req.body.senderId;
    const content = req.body.content;
    const sql = 'insert into Messages(class_id, comment_id, sender_id, content, sender_name) values(?, ?, ?, ?, (select user_name from Users where user_id = ?))';
    const params = [classId, commentId, senderId, content, senderId];

    connection.query(sql, params, (err, result) => {
        let resultCode = 200;
        let message = 'send new comment message Success';

        if (err) {
            return res.status(500).json({
                'code': 500,
                'message': 'database error'
            });
        }
    
        res.json({
            'code': resultCode,
            'message': message
        });
    });
});


// (14) update comment on a message
app.put('/chat_channel/:class_id/comment/:id', (req, res) => {
    const messageId = req.body.messageId;
    const content = req.body.content;
    const sql = 'update Messages set content = ? where message_id = ?';
    const params = [content, messageId];

    connection.query(sql, params, (err, result) => {
        let resultCode = 200;
        let message = 'update message content Success';

        if (err) {
            return res.status(500).json({
                'code': 500,
                'message': 'database error'
            });
        }
        
        res.json({
            'code': resultCode,
            'message': message
        });
    });
});


/* (15)
    get messages list in chatting channel
*/
app.get('/chat_channel/:class_id', (req, res) => {
    const classId = req.params.class_id;
    const sql = 'select M.*, (select count(*) as comment_count from Messages where comment_id = M.message_id) as comment_count '
            + 'from Messages M '
            + 'where class_id = ? and comment_id = -1 order by time_stamp';
    const params = [classId];

    connection.query(sql, params, (err, result) => {
        let resultCode = 404;
        let message = 'Error occured';
        let messageList = [];

        if (err) {
            return res.status(500).json({
                'code': 500,
                'message': 'database error'
            });
        }
        
        if (result.length > 0) {
            resultCode = 200;
            message = 'get chatting message list Success';
            messageList = result.map(item => ({
                "messageId": item.message_id,
                "commentId": item.comment_id,
                "classId": item.class_id,
                "timeStamp": item.time_stamp,
                "senderId": item.sender_id,
                "senderName": item.sender_name,
                "content": item.content,
                "commentCount": item.comment_count
            }));
        } else {
            resultCode = 200;
            message = 'messageList is empty';
        }

        res.json({
            'code': resultCode,
            'message': message,
            'messageList': messageList
        });
    });
});


/* (16)
    send new message with class_id, content of Messages
*/
app.post('/chat_channel/:class_id', (req, res) => {
    const classId = req.params.class_id;
    const senderId = req.body.senderId;
    const content = req.body.content;
    const sql = 'insert into Messages(class_id, sender_id, content, sender_name) values(?, ?, ?, (select user_name from Users where user_id = ?))';
    const params = [classId, senderId, content, senderId];

    connection.query(sql, params, (err, result) => {
        let resultCode = 200;
        let message = 'send new message Success';

        if (err) {
            return res.status(500).json({
                'code': 500,
                'message': 'database error'
            });
        }

        res.json({
            'code': resultCode,
            'message': message
        });
    });
});


// (17) modify content of message
app.put('/chat_channel/:class_id', (req, res) => {
    const messageId = req.body.messageId;
    const content = req.body.content;
    const sql = 'update Messages set content = ? where message_id = ?';
    const params = [content, messageId];

    connection.query(sql, params, (err, result) => {
        let resultCode = 200;
        let message = 'update message content Success';

        if (err) {
            return res.status(500).json({
                'code': 500,
                'message': 'database error'
            });
        }
        
        res.json({
            'code': resultCode,
            'message': message
        });
    });
});


/* (18)
    get attendance information
*/
app.get('/attendance/:id', (req, res) => {
    const attendanceId = req.params.id;
    const sql = 'SELECT Attendances.*, Users.user_name '
                + 'FROM Attendances '
                + 'INNER JOIN Users ON Attendances.student_id = Users.user_id '
                + 'where attendance_id = ?';
    const params = [attendanceId];

    connection.query(sql, params, (err, result) => {
        let resultCode = 404;
        let message = 'Error occured';
        let attendanceDate = -1;
        let attendanceStatus = -1;
        let attendanceDuration = -1;
        let isSent = -1;
        let studentId = -1;
        let classId = -1;
        let userName = "";
        let attendanceDetail = "";

        if (err) {
            return res.status(500).json({
                'code': 500,
                'message': 'database error'
            });
        }
        
        if (result.length > 0) {
            resultCode = 200;
            message = 'get attendance information Success';
            attendanceDate = result[0].attendance_date;
            attendanceStatus = result[0].attendance_status;
            attendanceDuration = result[0].attendance_duration;
            isSent = result[0].is_sent;
            studentId = result[0].student_id;
            classId = result[0].class_id;
            userName = result[0].user_name;
            attendanceDetail = result[0].attendance_detail;
        } else {
            resultCode = 200;
            message = 'There is no attendance corresponding to that id';
        }

        res.json({
            'code': resultCode,
            'message': message,
            'attendanceId': attendanceId,
            'attendanceDate': attendanceDate,
            'attendanceStatus': attendanceStatus,
            'attendanceDuration': attendanceDuration,
            'isSent': isSent,
            'studentId': studentId,
            'classId': classId,
            'userName': userName,
            'attendanceDetail': attendanceDetail
        });
    });
});


/* (19)
    edit is_sent as sent (i.e. is_sent = 1)
*/
app.put('/attendance/:id', (req, res) => {
    const attendanceId = req.params.id;
    const sql = 'update Attendances set is_sent = 1 where attendance_id = ?';
    const params = [attendanceId];

    connection.query(sql, params, (err, result) => {
        let resultCode = 200;
        let message = 'send attendance information Success';

        if (err) {
            return res.status(500).json({
                'code': 500,
                'message': 'database error'
            });
        }
        
        res.json({
            'code': resultCode,
            'message': message
        });
    });
});


/* (20)
    delete attendance information
*/
app.delete('/attendance/:id', (req, res) => {
    const attendanceId = req.params.id;
    const sql = 'delete from Attendances where attendance_id = ?';
    const params = [attendanceId];

    connection.query(sql, params, (err, result) => {
        let resultCode = 200;
        let message = 'delete attendance information Success';

        if (err) {
            return res.status(500).json({
                'code': 500,
                'message': 'database error'
            });
        }
        
        res.json({
            'code': resultCode,
            'message': message
        });
    });
});


/* (21)
    add student’s attendance information with attendance_status, attendace_duration, class_id of Attendances
*/
app.post('/attendance/:user_id', (req, res) => {
    const attendanceStatus = req.body.attendanceStatus;
    const attendanceDuration = req.body.attendanceDuration;
    const userId = req.params.user_id;
    const classId = req.body.classId;
    let attendanceDetail = null

    if(req.body.attendance_detail) {
        attendanceDetail = req.body.attendance_detail;
    }    

    const sql = 'insert into Attendances(attendance_status, attendance_duration, attendance_detail, student_id, class_id) '
        + 'values(?, ?, ?, ?, ?)';
    const params = [attendanceStatus, attendanceDuration, attendanceDetail, userId, classId];

    connection.query(sql, params, (err, result) => {
        let resultCode = 200;
        let message = 'add attendance information Success';

        if (err) {
            return res.status(500).json({
                'code': 500,
                'message': 'database error'
            });
        }
        
        res.json({
            'code': resultCode,
            'message': message
        });
    });
});