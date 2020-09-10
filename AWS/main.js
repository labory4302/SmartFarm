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

app.post('/user/register', function (req, res) {
    console.log(req.body);

    var userName = req.body.userName;
    var userNickName = req.body.userNickName;
    var userEmail = req.body.userEmail;
    var userID = req.body.userID;
    var userPwd = req.body.userPwd;
    var userLocation = req.body.userLocation;

    // 삽입을 수행하는 sql문.
    var sql = 'INSERT INTO Users(userName, userNickName, userEmail, userID, userPwd, userLocation) VALUES (?, ?, ?, ?, ?, ?);';
    var params = [userName, userNickName, userEmail, userID, userPwd, userLocation];

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
    var userID = req.body.userID;
    var userPwd = req.body.userPwd;

    //////////////////////////////////////
    // var Soil = req.body.SoilMoisture;
    // var Humi = req.body.Humi;
    // var Temp = req.body.Temp;

    var sql = 'SELECT * FROM Users WHERE userID = ?;';

    connection.query(sql, userID, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';

        if (err) {
            console.log(err);
        } else {
            if (result.length === 0) {
                resultCode = 204;
                message = '아이디가 틀렸습니다.';
            } else if (userPwd !== result[0].userPwd) {
                resultCode = 204;
                message = '비밀번호가 틀렸습니다!';
            } else {
                resultCode = 200;
                message = '로그인 성공! ' + result[0].userName + '님 환영합니다!' + '\n'
                + '당신의 이름은' + result[0].userName + '이메일은' + result[0].userEmail + '주소는' + result[0].userLocation + '입니다.';
            }
        }

        res.json({
            'code': resultCode,
            'message': message
        });
    })
});

// ////////////////////////////////////////////////////////
// /////////////IS THE FUCKING CUSTOM//////////////////////
// ////////////////////////////////////////////////////////
// app.post('/sensor/recent', function (req, res) {

//     //저장된 온습도 센서, 토양수분 센서 값
//     var ID = req.body.raspiID
//     var Soil = req.body.SoilMoisture;
//     var Humi = req.body.Humi;
//     var Temp = req.body.Temp;

//     //쿼리문을 통한 불러오기
//     var sql = 'SELECT SoilMoisture, Humi, Temp FROM RaspiData, Users WHERE Users.UserID = RaspiData.RaspiID;';

//     connection.query(sql, ID, function (err, result) {
//         var resultCode = 404;
//         var message = '에러가 발생했습니다';

//         if (err) {
//             console.log(err);
//         } else {
//             if (result.length === 0) {
//                 resultCode = 204;
//                 message = '정보를 불러오지 못했습니다!';
//             } else if (Soil !== result[0].Soil) {
//                 resultCode = 204;
//                 message = '토양 수분을 불러오지 못했습니다!';
//             } else if (Humi !== result[0].Humi) {
//                 resultCode = 204;
//                 message = '습도를 불러오지 못했습니다!';
//             } else if (Temp !== result[0].Temp) {
//                 resultCode = 204;
//                 message = '온도를 불러오지 못했습니다!';
//             } else {
//                 resultCode = 200;
//                 message = '불러오기 완료.';
//             }
//         }

//         res.json({
//             'code': resultCode,
//             'message': message
//         });
//     })
// });