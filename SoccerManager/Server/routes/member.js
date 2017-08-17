var express = require('express');
var router = express.Router();

// 디비 연결
var mongojs = require('mongojs');
var db = mongojs('smdb', ['member']);

/* GET home page. */
 router.get('/', function(req, res, next) {
   res.render('member', { title: 'Member' });
 });

router.post('/register', register);     // 회원가입
router.post('/login', login);           // 로그인
router.get('/findAll', findAll);        // 전체 회원 조회
router.post('/modify/:id', modify);     // 내 정보 변경
router.get('/findUser/:id', findUser);  // 특정 유저 찾기
router.get('/delete/:id', deleteInfo);  // 회원탈퇴


// 응답 메세지 설정
var JsonData = function(status, reason, resultData) {
  this.status = status; // 성공 실패
  this.reason = reason; // 이유
  this.resultData = resultData; // 데이터
}

// 회원탈퇴
function deleteInfo(req, res) {
  var userId = req.params.id;
  db.member.findOne({id : userId}, {_id : false}, function (error, data) {
    if(error) {
       res.end(JSON.stringify(new JsonData('fail','시스템 오류', error)));
       return;
     } else {
       var resultData = data;
       if(resultData.myTeamName != null) {
         res.end(JSON.stringify(new JsonData('not null','구단을 탈퇴해주세요.', error)));
         return;
       } else {
         db.member.remove({id : userId}, function(error, data) {
           if(error) {
              res.end(JSON.stringify(new JsonData('fail','시스템 오류', error)));
              console.log(error);
              return;
           } else {
             res.end(JSON.stringify(new JsonData('success','회원탈퇴 성공', data)));
             console.log(data);
             return;
           }
         }); // db.member.remove End
       }
     }
  }); // db.member.findOne End
}

// 특정 유저 찾기
function findUser(req, res) {
  var userId = req.params.id;
  // {$regex:userId}    params.id가 들어가는 모든 데이터를 검색
  db.member.find({id : {$regex:userId}}, {_id : false}, function(error, data) {
    if(error) {
       res.end(JSON.stringify(new JsonData('fail','시스템 오류', error)));
       console.log(error);
       return;
    } else if(data == null) {
        res.end(JSON.stringify(new JsonData('not find','아이디가 없습니다.', null)));
        console.log(error);
        return;
    } else {
        res.end(JSON.stringify(new JsonData('success','유저 검색 성공', data)));
        console.log(data);
        return;
    }
  });
}

// 내 정보 변경
function modify(req, res) {
  var myId = req.params.id;
  var myInfo = req.body;
  db.member.findAndModify({
    query: { id : myId },
    update: { $set: { password : myInfo.password,
    name : myInfo.name,
    birth : myInfo.birth,
    location : myInfo.location,
    preferredPosition : myInfo.preferredPosition,
    myTeamName : myInfo.myTeamName,
    mySpeed : myInfo.mySpeed,
    acceleration : myInfo.acceleration,
    health : myInfo.health,
    agility : myInfo.agility,
    captain : myInfo.captain } }, new: true }, function(error, data) {
      if(error) {
         res.end(JSON.stringify(new JsonData('fail','정보변경 실패', error)));
         console.log(error);
         return;
      } else {
        res.end(JSON.stringify(new JsonData('success','정보변경 성공', null)));
        console.log(data);
        return;
      }
    });
}


//회원가입
function register(req, res) {
  var newMember = req.body;
  db.member.findOne({id : newMember.id}, {_id : false}, function (error, data) {
    if(error) {
       res.end(JSON.stringify(new JsonData('fail', '시스템 오류', error)));
       console.log(error);
       return;
    } else if(data == null) { // 아이디 없음
      db.member.save({
          id : newMember.id,
          password : newMember.password,
          name : newMember.name,
          birth : newMember.birth,
          location : newMember.location,
          preferredPosition : newMember.preferredPosition,
          myTeamName : newMember.myTeamName,
          mySpeed : 0,
          acceleration : 0,
          health : 0,
          agility : 0,
          captain : newMember.captain
      }, function(error, data) {
        if(error) {
           res.end(JSON.stringify(new JsonData('fail','회원가입 실패', error)));
           console.log(error);
           return;
        } else {
          res.end(JSON.stringify(new JsonData('success','회원가입 성공', null)));
          console.log(data);
          return;
        }
      });
    } else { // 아이디가 중복됨
      res.end(JSON.stringify(new JsonData('overlap', '아이디 중복', null)));
      console.log(data);
      return;
    }
  });
}

//로그인
function login(req, res) {
  var member = req.body;
    db.member.findOne({id : member.id, password : member.password}, {_id : false}, function (error, data) {
      console.log("아이디 : " + member.id + ", 비밀번호 : " + member.password);
       if(error) {
          res.end(JSON.stringify(new JsonData('fail', '시스템 오류', error)));
          console.log(error);
          return;
       } else if(data == null) {
         res.end(JSON.stringify(new JsonData('not find', '일치하는 데이터가 없습니다.', null)));
         console.log("에러");
         return;
       } else {
         //res.end(JSON.stringify({status : 'success'}));
         res.end(JSON.stringify(new JsonData('success', '로그인 성공', data)));
         console.log(data);
         return;
       }
     });
}

// 전체 회원 조회
function findAll(req, res) {
  db.member.find({},{ _id : false}, function (error, data) {
    if(error) {
      res.end(JSON.stringify(new JsonData('fail', '시스템 오류', error)));
      console.log(error);
      return;
    } else {
      res.end(JSON.stringify(new JsonData('success', '전체조회 성공', data)));
      console.log(data);
      return;
    }
  });
}

module.exports = router;
