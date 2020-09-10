var mysql = require('mysql');
var express = require('express');
var bodyParser = require('body-parser');
var app = express();

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

app.listen(8080, function () {
    console.log('서버 실행 중...');
});

var connection = mysql.createConnection({
    host: "dbinstance3.cjytw5i33eqd.us-west-2.rds.amazonaws.com",
    user: "luck0707",
    database: "example",
    password: "disorder2848",
    port: 3306
});

app.post('/user/join', function (req, res) {
    console.log(req.body);
    var userEmail = req.body.userEmail;
    var userPwd = req.body.userPwd;
    var userName = req.body.userName;

    // 삽입을 수행하는 sql문.
    var sql = 'INSERT INTO Users (UserEmail, UserPwd, UserName) VALUES (?, ?, ?)';
    var params = [userEmail, userPwd, userName];

    // sql 문의 ?는 두번째 매개변수로 넘겨진 params의 값으로 치환된다.
    connection.query(sql, params, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';

        if (err) {
            console.log(err);
        } else {
            resultCode = 200;
            message = '회원가입에 성공했습니다.';
        }

        res.json({
            'code': resultCode,
            'message': message
        });
    });
});

app.post('/user/login', function (req, res) {
    var userEmail = req.body.userEmail;
    var userPwd = req.body.userPwd;
    var sql = 'select * from Users where UserEmail = ?';

    connection.query(sql, userEmail, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';

        if (err) {
            console.log(err);
        } else {
            if (result.length === 0) {
                resultCode = 204;
                message = '아이디가 틀렸습니다.';
            } else if (userPwd !== result[0].UserPwd) {
                resultCode = 204;
                message = '비밀번호가 틀렸습니다!';
            } else {
                resultCode = 200;
                message = '로그인 성공! ' + result[0].UserName + '님 환영합니다!';
            }
        }

        res.json({
            'code': resultCode,
            'message': message
        });
    })
});

////////////////////////////////////////////////////////
/////////////IS THE FUCKING CUSTOM//////////////////////
////////////////////////////////////////////////////////
app.post('/sensor/recent', function (req, res) {

    //저장된 온습도 센서, 토양수분 센서 값
    var Soil = req.body.SoilMoisture;
    var Humi = req.body.Humi;
    var Temp = req.body.Temp;

    //쿼리문을 통한 불러오기
    var sql = 'SELECT SoilMoisture, Humi, Temp FROM RaspiData, Users WHERE Users.UserID = RaspiData.RaspiID;';

    connection.query(sql, Humi_Temp, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';

        if (err) {
            console.log(err);
        } else {
            if (result.length === 0) {
                resultCode = 204;
                message = '정보를 불러오지 못했습니다!';
            } else if (Soil !== result[0].Soil) {
                resultCode = 204;
                message = '토양 수분을 불러오지 못했습니다!';
            } else if (Humi !== result[0].Humi) {
                resultCode = 204;
                message = '습도를 불러오지 못했습니다!';
            } else if (Temp !== result[0].Temp) {
                resultCode = 204;
                message = '온도를 불러오지 못했습니다!';
            } else {
                resultCode = 200;
                message = '불러오기 완료.';
            }
        }

        res.json({
            'code': resultCode,
            'message': message
        });
    })
});