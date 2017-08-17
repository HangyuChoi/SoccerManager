var express = require('express');
var router = express.Router();



// 디비 연결
var mongojs = require('mongojs');
var db = mongojs('smdb', ['board']);
require('date-utils');

/* GET home page. */
 router.get('/', function(req, res, next) {
   res.render('board', { title: 'Board' });
 });

router.post('/write', writeBoard); // 글쓰기
router.get('/search/:name', searchBoard); // 글조회
router.post('/delete/:id', deleteBoard); // 글삭제


// 응답 메세지 설정
var JsonData = function(status, reason, resultData) {
  this.status = status; // 성공 실패
  this.reason = reason; // 이유
  this.resultData = resultData; // 데이터
}

// 글삭제
function deleteBoard(req, res) {
  var userId = req.params.checkId;
  var deleteData = req.body;
  db.board.find({ id : deleteData.id, time : deleteData.time }, {_id : false}, function(error, data) {
    var resultData = data;
    if(error) {
      res.end(JSON.stringify(new JsonData('fail','글삭제 실패', error)));
      return;
    } else if(resultData.id != userId) {
      res.end(JSON.stringify(new JsonData('auth false','권한 없음', error)));
      return;
    } else {
      db.board.remove({id : deleteData.id, time : deleteData.time }, function(error, data) {
        if(error) {
          res.end(JSON.stringify(new JsonData('fail','글삭제 실패', error)));
          return;
        } else {
          res.end(JSON.stringify(new JsonData('success','글삭제 성공', data)));
          return;
        }
      });
    }
  });
}

// 글조회
function searchBoard(req, res) {
  var name = req.params.name;
  // 최신 글이 위로 올라오게
  db.board.find({teamName : name}).sort({ _id : -1 }, function(error, data) {
    if(error) {
      res.end(JSON.stringify(new JsonData('fail','글조회 실패', error)));
      return;
    } else {
      res.end(JSON.stringify(new JsonData('success','글조회 성공', data)));
      return;
    }
  });
}

// 글쓰기
function writeBoard(req, res) {
  var writeData = req.body;
  // 서버시간 구하기
  var newDate = new Date();
  var newTime = newDate.toFormat('YYYY-MM-DD HH24:MI:SS');
  db.board.save({
    title : writeData.title,
    id : writeData.id,
    writer : writeData.writer,
    teamName : writeData.teamName,
    contents : writeData.contents,
    time : newTime
  }, {_id : false}, function(error, data) {
    if(error) {
      res.end(JSON.stringify(new JsonData('fail','글쓰기 실패', error)));
      return;
    } else {
      res.end(JSON.stringify(new JsonData('success','글쓰기 성공', data)));
      return;
    }
  });
}

module.exports = router;
