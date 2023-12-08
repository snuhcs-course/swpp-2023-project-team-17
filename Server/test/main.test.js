const sinon = require('sinon');
const chai = require('chai');
const chaiHttp = require('chai-http');
const { app, connection } = require('../main');

chai.should();
chai.use(chaiHttp);


// (1) Login Endpoint
describe('Login Endpoint', () => {
  const mockUserEmail = 'test@example.com';
  const mockUserResult = [{ user_id: 1, user_name: 'TestUser', user_type: 1 }];

  beforeEach(() => {
    sinon.stub(connection, 'query');
  });

  afterEach(() => {
    connection.query.restore();
  });

  it('should return success on login with existing user', (done) => {
    connection.query.onCall(0).callsArgWith(2, null, mockUserResult);

    chai.request(app)
      .post(`/login/${mockUserEmail}`)
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.have.property('code').eql(200);
        res.body.should.have.property('message').eql('Login success');
        res.body.should.have.property('userEmail').eql(mockUserEmail);
        res.body.should.have.property('userId').eql(mockUserResult[0].user_id);
        res.body.should.have.property('userName').eql(mockUserResult[0].user_name);
        res.body.should.have.property('userType').eql(mockUserResult[0].user_type);
        done();
      });
  });

  it('should return success on signup with new user', (done) => {
    connection.query.onCall(0).callsArgWith(2, null, []);
    connection.query.onCall(1).callsArgWith(2, null, []);
    connection.query.onCall(2).callsArgWith(1, null, [{ user_id: 2 }]);

    chai.request(app)
      .post(`/login/${mockUserEmail}`)
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.have.property('code').eql(200);
        res.body.should.have.property('message').eql('Signup success');
        res.body.should.have.property('userEmail').eql(mockUserEmail);
        res.body.should.have.property('userId').eql(2);
        res.body.should.have.property('userName').eql('');
        res.body.should.have.property('userType').eql(0);
        done();
      });
  });

  it('should handle database error during user retrieval', (done) => {
    connection.query.onCall(0).callsArgWith(2, new Error('database error'), null);

    chai.request(app)
      .post(`/login/${mockUserEmail}`)
      .end((err, res) => {
        res.should.have.status(500);
        res.body.should.have.property('code').eql(500);
        res.body.should.have.property('message').eql('database error');
        done();
      });
  });

  it('should handle database error during user creation', (done) => {
    connection.query.onCall(0).callsArgWith(2, null, []);
    connection.query.onCall(1).callsArgWith(2, new Error('database error'), null);

    chai.request(app)
      .post(`/login/${mockUserEmail}`)
      .end((err, res) => {
        res.should.have.status(500);
        res.body.should.have.property('code').eql(500);
        res.body.should.have.property('message').eql('database error');
        done();
      });
  });

  it('should handle database error during last inserted ID retrieval', (done) => {
    connection.query.onCall(0).callsArgWith(2, null, []);
    connection.query.onCall(1).callsArgWith(2, null, []);
    connection.query.onCall(2).callsArgWith(1, new Error('database error'), null);

    chai.request(app)
      .post(`/login/${mockUserEmail}`)
      .end((err, res) => {
        res.should.have.status(500);
        res.body.should.have.property('code').eql(500);
        res.body.should.have.property('message').eql('database error');
        done();
      });
  });
});


// (2) Get Classes Endpoint
describe('Get Classes Endpoint', () => {
  const mockUserId = 1;
  const mockUserType = 0;
  const mockClassesResult = [
    {
      class_id: 1,
      class_name: 'Math',
      class_code: 'MATH101',
      professor_id: 101,
      class_time: 'Mon 10:00 AM',
      building_number: 'Bldg A',
      room_number: '101',
    },
  ];

  beforeEach(() => {
    sinon.stub(connection, 'query');
  });

  afterEach(() => {
    connection.query.restore();
  });

  it('should return classes for student', (done) => {
    connection.query.onCall(0).callsArgWith(2, null, mockClassesResult);

    chai.request(app)
      .get('/users/classes')
      .query({ userId: mockUserId, userType: mockUserType })
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.have.property('code').eql(200);
        res.body.should.have.property('message').eql('get classes Success');
        res.body.should.have.property('classList').to.be.an('array').that.is.not.empty;
        done();
      });
  });

  it('should return classes for professor', (done) => {
    connection.query.onCall(0).callsArgWith(2, null, mockClassesResult);

    chai.request(app)
      .get('/users/classes')
      .query({ userId: mockUserId, userType: 1 })
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.have.property('code').eql(200);
        res.body.should.have.property('message').eql('get classes Success');
        res.body.should.have.property('classList').to.be.an('array').that.is.not.empty;
        done();
      });
  });

  it('should handle empty class list', (done) => {
    connection.query.onCall(0).callsArgWith(2, null, []);

    chai.request(app)
      .get('/users/classes')
      .query({ userId: mockUserId, userType: mockUserType })
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.have.property('code').eql(200);
        res.body.should.have.property('message').eql('classList is empty');
        res.body.should.have.property('classList').to.be.an('array').that.is.empty;
        done();
      });
  });

  it('should handle database error', (done) => {
    connection.query.onCall(0).callsArgWith(2, new Error('database error'), null);

    chai.request(app)
      .get('/users/classes')
      .query({ userId: mockUserId, userType: mockUserType })
      .end((err, res) => {
        res.should.have.status(500);
        res.body.should.have.property('code').eql(500);
        res.body.should.have.property('message').eql('database error');
        done();
      });
  });
});


// (3) Get Attendance Dates Endpoint
describe('Get Attendance Dates Endpoint', () => {
  const mockClassId = 1;
  const mockAttendanceResult = [
    { attendance_date: '2023-11-26' },
    { attendance_date: '2023-11-27' },
  ];

  beforeEach(() => {
    sinon.stub(connection, 'query');
  });

  afterEach(() => {
    connection.query.restore();
  });

  it('should return attendance dates for a class', (done) => {
    connection.query.onCall(0).callsArgWith(2, null, mockAttendanceResult);

    chai.request(app)
      .get('/users/attendance')
      .query({ classId: mockClassId })
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.have.property('code').eql(200);
        res.body.should.have.property('message').eql('get attendance date list Success');
        res.body.should.have.property('attendanceDateList').to.be.an('array').that.is.not.empty;
        res.body.attendanceDateList.should.have.lengthOf(mockAttendanceResult.length);
        done();
      });
  });

  it('should handle empty attendance date list', (done) => {
    connection.query.onCall(0).callsArgWith(2, null, []);

    chai.request(app)
      .get('/users/attendance')
      .query({ classId: mockClassId })
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.have.property('code').eql(200);
        res.body.should.have.property('message').eql('attendanceDateList is empty');
        res.body.should.have.property('attendanceDateList').to.be.an('array').that.is.empty;
        done();
      });
  });

  it('should handle database error', (done) => {
    connection.query.onCall(0).callsArgWith(2, new Error('database error'), null);

    chai.request(app)
      .get('/users/attendance')
      .query({ classId: mockClassId })
      .end((err, res) => {
        res.should.have.status(500);
        res.body.should.have.property('code').eql(500);
        res.body.should.have.property('message').eql('database error');
        done();
      });
  });
});


// (4) Get Attendance for a Specific Date Endpoint
describe('Get Attendance for a Specific Date Endpoint', () => {
  const mockDate = '2023-11-26';
  const mockClassId = 1;
  const mockAttendanceResult = [
    {
      attendance_id: 1,
      attendance_status: 'Present',
      attendance_date: '2023-11-26',
      attendance_duration: 120,
      is_sent: 1,
      class_id: 1,
      student_id: 101,
      user_name: 'kms',
      attendance_detail: '0111110001100011'
    },
  ];

  beforeEach(() => {
    sinon.stub(connection, 'query');
  });

  afterEach(() => {
    connection.query.restore();
  });

  it('should return attendances for a specific date', (done) => {
    connection.query.onCall(0).callsArgWith(2, null, mockAttendanceResult);

    chai.request(app)
      .get(`/users/attendance/${mockDate}`)
      .query({ classId: mockClassId })
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.have.property('code').eql(200);
        res.body.should.have.property('message').eql('get all attendances in specific date Success');
        res.body.should.have.property('attendanceList').to.be.an('array').that.is.not.empty;
        res.body.attendanceList.should.have.lengthOf(mockAttendanceResult.length);
        done();
      });
  });

  it('should handle empty attendance list for a specific date', (done) => {
    connection.query.onCall(0).callsArgWith(2, null, []);

    chai.request(app)
      .get(`/users/attendance/${mockDate}`)
      .query({ classId: mockClassId })
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.have.property('code').eql(200);
        res.body.should.have.property('message').eql('attendanceList is empty');
        res.body.should.have.property('attendanceList').to.be.an('array').that.is.empty;
        done();
      });
  });

  it('should handle database error', (done) => {
    connection.query.onCall(0).callsArgWith(2, new Error('database error'), null);

    chai.request(app)
      .get(`/users/attendance/${mockDate}`)
      .query({ classId: mockClassId })
      .end((err, res) => {
        res.should.have.status(500);
        res.body.should.have.property('code').eql(500);
        res.body.should.have.property('message').eql('database error');
        done();
      });
  });
});


// (5) Get User Information Endpoint
describe('Get User Information Endpoint', () => {
  const mockUserId = 1;
  const mockUserInfoResult = [
    {
      user_name: 'TestUser',
      user_type: 1,
    },
  ];

  beforeEach(() => {
    sinon.stub(connection, 'query');
  });

  afterEach(() => {
    connection.query.restore();
  });

  it('should return user information for a valid user id', (done) => {
    connection.query.onCall(0).callsArgWith(2, null, mockUserInfoResult);

    chai.request(app)
      .get(`/users/${mockUserId}`)
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.have.property('code').eql(200);
        res.body.should.have.property('message').eql('get userName and userType Success');
        res.body.should.have.property('userName').eql(mockUserInfoResult[0].user_name);
        res.body.should.have.property('userType').eql(mockUserInfoResult[0].user_type);
        done();
      });
  });

  it('should handle no user corresponding to the given id', (done) => {
    connection.query.onCall(0).callsArgWith(2, null, []);

    chai.request(app)
      .get(`/users/${mockUserId}`)
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.have.property('code').eql(200);
        res.body.should.have.property('message').eql('There is no user corresponding to that id');
        res.body.should.have.property('userName').eql('');
        res.body.should.have.property('userType').eql(-1);
        done();
      });
  });

  it('should handle database error', (done) => {
    connection.query.onCall(0).callsArgWith(2, new Error('database error'), null);

    chai.request(app)
      .get(`/users/${mockUserId}`)
      .end((err, res) => {
        res.should.have.status(500);
        res.body.should.have.property('code').eql(500);
        res.body.should.have.property('message').eql('database error');
        done();
      });
  });
});


// (6) Update User Information Endpoint
describe('Update User Information Endpoint', () => {
  const mockUserId = 1;
  const mockRequestBody = {
    userName: 'UpdatedUserName',
    userType: 2,
  };

  beforeEach(() => {
    sinon.stub(connection, 'query');
  });

  afterEach(() => {
    connection.query.restore();
  });

  it('should update user information for a valid user id', (done) => {
    connection.query.onCall(0).callsArgWith(2, null, []);

    chai.request(app)
      .put(`/users/${mockUserId}`)
      .send(mockRequestBody)
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.have.property('code').eql(200);
        res.body.should.have.property('message').eql('update userName, userType Success');
        done();
      });
  });

  it('should handle database error', (done) => {
    connection.query.onCall(0).callsArgWith(2, new Error('database error'), null);

    chai.request(app)
      .put(`/users/${mockUserId}`)
      .send(mockRequestBody)
      .end((err, res) => {
        res.should.have.status(500);
        res.body.should.have.property('code').eql(500);
        res.body.should.have.property('message').eql('database error');
        done();
      });
  });
});


// (7) Create Class Endpoint
describe('Create Class Endpoint', () => {
  const mockRequestBody = {
    className: 'Math',
    classCode: 'MATH101',
    professorId: 101,
    classTime: 'Mon 10:00 AM',
    buildingNumber: 'Bldg A',
    roomNumber: '101',
  };

  beforeEach(() => {
    sinon.stub(connection, 'query');
  });

  afterEach(() => {
    connection.query.restore();
  });

  it('should create a new class with valid input', (done) => {
    connection.query.onCall(0).callsArgWith(2, null, { insertId: 1 });

    chai.request(app)
      .post('/class/create')
      .send(mockRequestBody)
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.have.property('code').eql(200);
        res.body.should.have.property('message').eql('Create class success');
        res.body.should.have.property('classId').eql(1);
        done();
      });
  });

  it('should handle missing required fields', (done) => {
    chai.request(app)
      .post('/class/create')
      .send({})
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.have.property('code').eql(404);
        res.body.should.have.property('message').eql('Required fields are missing');
        done();
      });
  });

  it('should handle database error', (done) => {
    connection.query.onCall(0).callsArgWith(2, new Error('database error'), null);

    chai.request(app)
      .post('/class/create')
      .send(mockRequestBody)
      .end((err, res) => {
        res.should.have.status(500);
        res.body.should.have.property('code').eql(500);
        res.body.should.have.property('message').eql('database error');
        done();
      });
  });
});


// (8) Class Join Endpoint
describe('Class Join Endpoint', () => {
  const mockUserId = 1;
  const mockRequestBody = {
    className: 'Math',
    classCode: 'MATH101',
  };

  beforeEach(() => {
    sinon.stub(connection, 'query');
  });

  afterEach(() => {
    connection.query.restore();
  });

  it('should successfully join a class with valid input', (done) => {
    connection.query.onCall(0).callsArgWith(2, null, [{ class_id: 1, class_time: 'Mon 10:00 AM' }]);
    connection.query.onCall(1).callsArgWith(2, null, {});

    chai.request(app)
      .post(`/class/join/${mockUserId}`)
      .send(mockRequestBody)
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.have.property('code').eql(200);
        res.body.should.have.property('message').eql('Class join success');
        res.body.should.have.property('classId').eql(1);
        res.body.should.have.property('classTime').eql('Mon 10:00 AM');
        done();
      });
  });

  it('should handle joining a non-existent class', (done) => {
    connection.query.onCall(0).callsArgWith(2, null, []);

    chai.request(app)
      .post(`/class/join/${mockUserId}`)
      .send(mockRequestBody)
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.have.property('code').eql(404);
        res.body.should.have.property('message').eql('Class not found');
        done();
      });
  });

  it('should handle attempting to join an already joined class', (done) => {
    connection.query.onCall(0).callsArgWith(2, null, [{ class_id: 1, class_time: 'Mon 10:00 AM' }]);
    connection.query.onCall(1).callsArgWith(2, new Error('Already joined the class'), null);

    chai.request(app)
      .post(`/class/join/${mockUserId}`)
      .send(mockRequestBody)
      .end((err, res) => {
        res.should.have.status(500);
        res.body.should.have.property('code').eql(500);
        res.body.should.have.property('message').eql('Already joined the class');
        done();
      });
  });

  it('should handle database error', (done) => {
    connection.query.onCall(0).callsArgWith(2, new Error('database error'), null);

    chai.request(app)
      .post(`/class/join/${mockUserId}`)
      .send(mockRequestBody)
      .end((err, res) => {
        res.should.have.status(500);
        res.body.should.have.property('code').eql(500);
        res.body.should.have.property('message').eql('database error');
        done();
      });
  });
});


// (9) Get Specific Class Endpoint
describe('Get Specific Class Endpoint', () => {
  const mockClassId = 1;

  beforeEach(() => {
    sinon.stub(connection, 'query');
  });

  afterEach(() => {
    connection.query.restore();
  });

  it('should successfully get details of a specific class', (done) => {
    const mockClassDetails = {
      class_id: mockClassId,
      class_name: 'Math',
      class_code: 'MATH101',
      professor_id: 101,
      class_time: 'Mon 10:00 AM',
      building_number: 'Bldg A',
      room_number: '101',
    };

    connection.query.onCall(0).callsArgWith(2, null, [mockClassDetails]);

    chai.request(app)
      .get(`/class/${mockClassId}`)
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.have.property('code').eql(200);
        res.body.should.have.property('message').eql('get specific class Success');
        res.body.should.have.property('className').eql('Math');
        res.body.should.have.property('classCode').eql('MATH101');
        res.body.should.have.property('professorId').eql(101);
        res.body.should.have.property('classTime').eql('Mon 10:00 AM');
        res.body.should.have.property('buildingNumber').eql('Bldg A');
        res.body.should.have.property('roomNumber').eql('101');
        done();
      });
  });

  it('should handle non-existent class', (done) => {
    connection.query.onCall(0).callsArgWith(2, null, []);

    chai.request(app)
      .get(`/class/${mockClassId}`)
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.have.property('code').eql(200);
        res.body.should.have.property('message').eql('There is no class corresponding to that class_id');
        done();
      });
  });

  it('should handle database error', (done) => {
    connection.query.onCall(0).callsArgWith(2, new Error('database error'), null);

    chai.request(app)
      .get(`/class/${mockClassId}`)
      .end((err, res) => {
        res.should.have.status(500);
        res.body.should.have.property('code').eql(500);
        res.body.should.have.property('message').eql('database error');
        done();
      });
  });
});


// (10) Delete Class Endpoint
describe('Delete Class Endpoint', () => {
  const mockClassId = 1;

  beforeEach(() => {
    sinon.stub(connection, 'query');
  });

  afterEach(() => {
    connection.query.restore();
  });

  it('should successfully delete a class', (done) => {
    connection.query.onCall(0).callsArgWith(2, null, { affectedRows: 1 });

    chai.request(app)
      .delete(`/class/${mockClassId}`)
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.have.property('code').eql(200);
        res.body.should.have.property('message').eql('delete class Success');
        done();
      });
  });

  it('should handle database error', (done) => {
    connection.query.onCall(0).callsArgWith(2, new Error('database error'), null);

    chai.request(app)
      .delete(`/class/${mockClassId}`)
      .end((err, res) => {
        res.should.have.status(500);
        res.body.should.have.property('code').eql(500);
        res.body.should.have.property('message').eql('database error');
        done();
      });
  });
});


// (11) Get User Attendance List Endpoint
describe('Get User Attendance List Endpoint', () => {
  const mockUserId = 1;
  const mockClassId = 2;
  const mockAttendanceResult = [
    {
      attendance_id: 1,
      attendance_status: 'Present',
      attendance_duration: 60,
      attendance_date: '2023-11-26',
      is_sent: 1,
      class_id: 2,
      student_id: 1,
      user_name: 'kms',
      attendance_detail: '0111110001100011'
    },
  ];

  beforeEach(() => {
    sinon.stub(connection, 'query');
  });

  afterEach(() => {
    connection.query.restore();
  });

  it('should get user attendance list successfully', (done) => {
    connection.query.onCall(0).callsArgWith(2, null, mockAttendanceResult);

    chai.request(app)
      .get(`/class/${mockClassId}/attendance/${mockUserId}`)
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.have.property('code').eql(200);
        res.body.should.have.property('message').eql('get user attendance list Success');
        res.body.should.have.property('attendanceList').to.be.an('array').that.is.not.empty;
        done();
      });
  });

  it('should handle empty attendance list', (done) => {
    connection.query.onCall(0).callsArgWith(2, null, []);

    chai.request(app)
      .get(`/class/${mockClassId}/attendance/${mockUserId}`)
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.have.property('code').eql(200);
        res.body.should.have.property('message').eql('attendanceList is empty');
        res.body.should.have.property('attendanceList').to.be.an('array').that.is.empty;
        done();
      });
  });

  it('should handle database error', (done) => {
    connection.query.onCall(0).callsArgWith(2, new Error('database error'), null);

    chai.request(app)
      .get(`/class/${mockClassId}/attendance/${mockUserId}`)
      .end((err, res) => {
        res.should.have.status(500);
        res.body.should.have.property('code').eql(500);
        res.body.should.have.property('message').eql('database error');
        done();
      });
  });
});


// (12) Get Comment Message List Endpoint
describe('Get Comment Message List Endpoint', () => {
  const mockClassId = 1;
  const mockCommentId = 2;
  const mockMessageResult = [
    {
      message_id: 1,
      comment_id: 2,
      class_id: 1,
      time_stamp: '2023-11-26 10:00:00',
      sender_id: 1,
      sender_name: 'TestUser',
      content: 'Hello, this is a test message.',
    },
  ];

  beforeEach(() => {
    sinon.stub(connection, 'query');
  });

  afterEach(() => {
    connection.query.restore();
  });

  it('should get comment message list successfully', (done) => {
    connection.query.onCall(0).callsArgWith(2, null, mockMessageResult);

    chai.request(app)
      .get(`/chat_channel/${mockClassId}/comment/${mockCommentId}`)
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.have.property('code').eql(200);
        res.body.should.have.property('message').eql('get comment message list Success');
        res.body.should.have.property('messageList').to.be.an('array').that.is.not.empty;
        done();
      });
  });

  it('should handle empty message list', (done) => {
    connection.query.onCall(0).callsArgWith(2, null, []);

    chai.request(app)
      .get(`/chat_channel/${mockClassId}/comment/${mockCommentId}`)
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.have.property('code').eql(200);
        res.body.should.have.property('message').eql('messageList is empty');
        res.body.should.have.property('messageList').to.be.an('array').that.is.empty;
        done();
      });
  });

  it('should handle database error', (done) => {
    connection.query.onCall(0).callsArgWith(2, new Error('database error'), null);

    chai.request(app)
      .get(`/chat_channel/${mockClassId}/comment/${mockCommentId}`)
      .end((err, res) => {
        res.should.have.status(500);
        res.body.should.have.property('code').eql(500);
        res.body.should.have.property('message').eql('database error');
        done();
      });
  });
});


// (13) Send New Comment Message Endpoint
describe('Send New Comment Message Endpoint', () => {
  const mockClassId = 1;
  const mockCommentId = 2;
  const mockSenderId = 1;
  const mockContent = 'Hello, this is a new test message.';

  beforeEach(() => {
    sinon.stub(connection, 'query');
  });

  afterEach(() => {
    connection.query.restore();
  });

  it('should send new comment message successfully', (done) => {
    connection.query.onCall(0).callsArgWith(2, null, {});

    chai.request(app)
      .post(`/chat_channel/${mockClassId}/comment/${mockCommentId}`)
      .send({ senderId: mockSenderId, content: mockContent })
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.have.property('code').eql(200);
        res.body.should.have.property('message').eql('send new comment message Success');
        done();
      });
  });

  it('should handle database error', (done) => {
    connection.query.onCall(0).callsArgWith(2, new Error('database error'), null);

    chai.request(app)
      .post(`/chat_channel/${mockClassId}/comment/${mockCommentId}`)
      .send({ senderId: mockSenderId, content: mockContent })
      .end((err, res) => {
        res.should.have.status(500);
        res.body.should.have.property('code').eql(500);
        res.body.should.have.property('message').eql('database error');
        done();
      });
  });
});


// (14) Update Message Content Endpoint
describe('Update Message Content Endpoint', () => {
  const mockClassId = 1;
  const mockCommentId = 2;
  const mockMessageId = 3;
  const mockContent = 'Updated message content.';

  beforeEach(() => {
    sinon.stub(connection, 'query');
  });

  afterEach(() => {
    connection.query.restore();
  });

  it('should update message content successfully', (done) => {
    connection.query.onCall(0).callsArgWith(2, null, {});

    chai.request(app)
      .put(`/chat_channel/${mockClassId}/comment/${mockCommentId}`)
      .send({ messageId: mockMessageId, content: mockContent })
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.have.property('code').eql(200);
        res.body.should.have.property('message').eql('update message content Success');
        done();
      });
  });

  it('should handle database error', (done) => {
    connection.query.onCall(0).callsArgWith(2, new Error('database error'), null);

    chai.request(app)
      .put(`/chat_channel/${mockClassId}/comment/${mockCommentId}`)
      .send({ messageId: mockMessageId, content: mockContent })
      .end((err, res) => {
        res.should.have.status(500);
        res.body.should.have.property('code').eql(500);
        res.body.should.have.property('message').eql('database error');
        done();
      });
  });
});


// (15) Get Chatting Messages Endpoint
describe('Get Chatting Messages Endpoint', () => {
  const mockClassId = 1;
  const mockMessageList = [
    {
      message_id: 1,
      comment_id: -1,
      class_id: mockClassId,
      time_stamp: '2023-11-26T12:34:56',
      sender_id: 2,
      sender_name: 'TestUser',
      content: 'Hello, this is a test message.',
      comment_count: '6'
    },
    // Additional mock messages can be added for testing variations
  ];

  beforeEach(() => {
    sinon.stub(connection, 'query');
  });

  afterEach(() => {
    connection.query.restore();
  });

  it('should get chatting message list successfully', (done) => {
    connection.query.onCall(0).callsArgWith(2, null, mockMessageList);

    chai.request(app)
      .get(`/chat_channel/${mockClassId}`)
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.have.property('code').eql(200);
        res.body.should.have.property('message').eql('get chatting message list Success');
        res.body.should.have.property('messageList').to.be.an('array').that.is.not.empty;
        done();
      });
  });

  it('should handle empty message list', (done) => {
    connection.query.onCall(0).callsArgWith(2, null, []);

    chai.request(app)
      .get(`/chat_channel/${mockClassId}`)
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.have.property('code').eql(200);
        res.body.should.have.property('message').eql('messageList is empty');
        res.body.should.have.property('messageList').to.be.an('array').that.is.empty;
        done();
      });
  });

  it('should handle database error', (done) => {
    connection.query.onCall(0).callsArgWith(2, new Error('database error'), null);

    chai.request(app)
      .get(`/chat_channel/${mockClassId}`)
      .end((err, res) => {
        res.should.have.status(500);
        res.body.should.have.property('code').eql(500);
        res.body.should.have.property('message').eql('database error');
        done();
      });
  });
});


// (16) Post New Message Endpoint
describe('Post New Message Endpoint', () => {
  const mockClassId = 1;
  const mockSenderId = 2;
  const mockContent = 'Hello, this is a test message.';
  const mockInsertId = 1;

  beforeEach(() => {
    sinon.stub(connection, 'query');
  });

  afterEach(() => {
    connection.query.restore();
  });

  it('should send a new message successfully', (done) => {
    connection.query.onCall(0).callsArgWith(2, null, { insertId: mockInsertId });

    chai.request(app)
      .post(`/chat_channel/${mockClassId}`)
      .send({ senderId: mockSenderId, content: mockContent })
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.have.property('code').eql(200);
        res.body.should.have.property('message').eql('send new message Success');
        done();
      });
  });

  it('should handle database error', (done) => {
    connection.query.onCall(0).callsArgWith(2, new Error('database error'), null);

    chai.request(app)
      .post(`/chat_channel/${mockClassId}`)
      .send({ senderId: mockSenderId, content: mockContent })
      .end((err, res) => {
        res.should.have.status(500);
        res.body.should.have.property('code').eql(500);
        res.body.should.have.property('message').eql('database error');
        done();
      });
  });
});


// (17) Update Message Content Endpoint
describe('Update Message Content Endpoint', () => {
  const mockMessageId = 1;
  const mockContent = 'Updated test message content';

  beforeEach(() => {
    sinon.stub(connection, 'query');
  });

  afterEach(() => {
    connection.query.restore();
  });

  it('should update message content successfully', (done) => {
    connection.query.onCall(0).callsArgWith(2, null, {});

    chai.request(app)
      .put(`/chat_channel/${mockMessageId}`)
      .send({ messageId: mockMessageId, content: mockContent })
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.have.property('code').eql(200);
        res.body.should.have.property('message').eql('update message content Success');
        done();
      });
  });

  it('should handle database error', (done) => {
    connection.query.onCall(0).callsArgWith(2, new Error('database error'), null);

    chai.request(app)
      .put(`/chat_channel/${mockMessageId}`)
      .send({ messageId: mockMessageId, content: mockContent })
      .end((err, res) => {
        res.should.have.status(500);
        res.body.should.have.property('code').eql(500);
        res.body.should.have.property('message').eql('database error');
        done();
      });
  });
});


// (18) Get Attendance Information Endpoint
describe('Get Attendance Information Endpoint', () => {
  const mockAttendanceId = '1';

  beforeEach(() => {
    sinon.stub(connection, 'query');
  });

  afterEach(() => {
    connection.query.restore();
  });

  it('should get attendance information successfully', (done) => {
    const mockResult = [{
      attendance_id: mockAttendanceId,
      attendance_date: '2023-11-26',
      attendance_status: 0,
      attendance_duration: 60,
      is_sent: 0,
      student_id: 123,
      class_id: 456,
      user_name: 'kms',
      attendance_detail: '0111110001100011'
    }];

    connection.query.onCall(0).callsArgWith(2, null, mockResult);

    chai.request(app)
      .get(`/attendance/${mockAttendanceId}`)
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.have.property('code').eql(200);
        res.body.should.have.property('message').eql('get attendance information Success');
        res.body.should.have.property('attendanceId').eql(mockAttendanceId);
        res.body.should.have.property('attendanceDate').eql(mockResult[0].attendance_date);
        res.body.should.have.property('attendanceStatus').eql(mockResult[0].attendance_status);
        res.body.should.have.property('attendanceDuration').eql(mockResult[0].attendance_duration);
        res.body.should.have.property('isSent').eql(mockResult[0].is_sent);
        res.body.should.have.property('studentId').eql(mockResult[0].student_id);
        res.body.should.have.property('classId').eql(mockResult[0].class_id);
        res.body.should.have.property('userName').eql(mockResult[0].user_name);
        res.body.should.have.property('attendanceDetail').eql(mockResult[0].attendance_detail);
        done();
      });
  });

  it('should handle database error', (done) => {
    connection.query.onCall(0).callsArgWith(2, new Error('database error'), null);

    chai.request(app)
      .get(`/attendance/${mockAttendanceId}`)
      .end((err, res) => {
        res.should.have.status(500);
        res.body.should.have.property('code').eql(500);
        res.body.should.have.property('message').eql('database error');
        done();
      });
  });

  it('should handle no attendance corresponding to the given id', (done) => {
    connection.query.onCall(0).callsArgWith(2, null, []);

    chai.request(app)
      .get(`/attendance/${mockAttendanceId}`)
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.have.property('code').eql(200);
        res.body.should.have.property('message').eql('There is no attendance corresponding to that id');
        done();
      });
  });
});


// (19) Update Attendance Information Endpoint
describe('Update Attendance Information Endpoint', () => {
  const mockAttendanceId = 1;

  beforeEach(() => {
    sinon.stub(connection, 'query');
  });

  afterEach(() => {
    connection.query.restore();
  });

  it('should update attendance information successfully', (done) => {
    const mockResult = { affectedRows: 1 };

    connection.query.onCall(0).callsArgWith(2, null, mockResult);

    chai.request(app)
      .put(`/attendance/${mockAttendanceId}`)
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.have.property('code').eql(200);
        res.body.should.have.property('message').eql('send attendance information Success');
        done();
      });
  });

  it('should handle database error', (done) => {
    connection.query.onCall(0).callsArgWith(2, new Error('database error'), null);

    chai.request(app)
      .put(`/attendance/${mockAttendanceId}`)
      .end((err, res) => {
        res.should.have.status(500);
        res.body.should.have.property('code').eql(500);
        res.body.should.have.property('message').eql('database error');
        done();
      });
  });
});


// (20) Delete Attendance Information Endpoint
describe('Delete Attendance Information Endpoint', () => {
  const mockAttendanceId = 1;

  beforeEach(() => {
    sinon.stub(connection, 'query');
  });

  afterEach(() => {
    connection.query.restore();
  });

  it('should delete attendance information successfully', (done) => {
    const mockResult = { affectedRows: 1 };

    connection.query.onCall(0).callsArgWith(2, null, mockResult);

    chai.request(app)
      .delete(`/attendance/${mockAttendanceId}`)
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.have.property('code').eql(200);
        res.body.should.have.property('message').eql('delete attendance information Success');
        done();
      });
  });

  it('should handle database error', (done) => {
    connection.query.onCall(0).callsArgWith(2, new Error('database error'), null);

    chai.request(app)
      .delete(`/attendance/${mockAttendanceId}`)
      .end((err, res) => {
        res.should.have.status(500);
        res.body.should.have.property('code').eql(500);
        res.body.should.have.property('message').eql('database error');
        done();
      });
  });
});


// (21) Add Attendance Information Endpoint
describe('Add Attendance Information Endpoint', () => {
  const mockUserId = 1;
  const mockAttendanceStatus = 'Present';
  const mockAttendanceDuration = 30;
  const mockClassId = 123;
  const mockAttendanceDetail = '0111110001100011';

  beforeEach(() => {
    sinon.stub(connection, 'query');
  });

  afterEach(() => {
    connection.query.restore();
  });

  it('should add attendance information successfully without attendanceDetail', (done) => {
    const mockResult = { insertId: 1 };

    connection.query.onCall(0).callsArgWith(2, null, mockResult);

    chai.request(app)
      .post(`/attendance/${mockUserId}`)
      .send({
        attendanceStatus: mockAttendanceStatus,
        attendanceDuration: mockAttendanceDuration,
        classId: mockClassId
      })
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.have.property('code').eql(200);
        res.body.should.have.property('message').eql('add attendance information Success');
        done();
      });
  });

  it('should add attendance information successfully with attendanceDetail', (done) => {
    const mockResult = { insertId: 1 };

    connection.query.onCall(0).callsArgWith(2, null, mockResult);

    chai.request(app)
      .post(`/attendance/${mockUserId}`)
      .send({
        attendanceStatus: mockAttendanceStatus,
        attendanceDuration: mockAttendanceDuration,
        classId: mockClassId,
        attendanceDetail: mockAttendanceDetail
      })
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.have.property('code').eql(200);
        res.body.should.have.property('message').eql('add attendance information Success');
        done();
      });
  });

  it('should handle database error', (done) => {
    connection.query.onCall(0).callsArgWith(2, new Error('database error'), null);

    chai.request(app)
      .post(`/attendance/${mockUserId}`)
      .send({
        attendanceStatus: mockAttendanceStatus,
        attendanceDuration: mockAttendanceDuration,
        classId: mockClassId,
        attendanceDetail: mockAttendanceDetail
      })
      .end((err, res) => {
        res.should.have.status(500);
        res.body.should.have.property('code').eql(500);
        res.body.should.have.property('message').eql('database error');
        done();
      });
  });
});