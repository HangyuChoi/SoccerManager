var express = require('express');
var router = express.Router();

// 디비 연결
var mongojs = require('mongojs');
var db = mongojs('smdb', ['team']);
var db_M = mongojs('smdb', ['member']);

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('team', { title: 'Team' });
});

router.post('/create', create);   // 구단 생성
router.get('/teamlist', teamlist); // 전체 구단 조회
router.post('/join', joinTeam); // 구단 가입
router.get('/findTeam/:name', findTeam); // 특정 구단 찾기
router.post('/modify/:id', modify); // 구단 변경
router.get('/delete/:id', deleteTeam); // 구단 해체
router.post('/quit', quitTeam); // 구단 탈퇴
router.get('/myteam/:id', myTeam); // 내 구단 조회
router.get('/myteam/search/:name', searchMyTeamMember); // 내 팀의 팀원 정보 조회

// 응답 메세지 설정
var JsonData = function(status, reason, resultData) {
  this.status = status; // 성공 실패
  this.reason = reason; // 이유
  this.resultData = resultData; // 데이터
}

// 구단 탈퇴
function quitTeam(req, res) {
  var quitData = req.body;
  if(quitData.captain == true) {
      res.end(JSON.stringify(new JsonData('captain','구단을 양도하고 다시 시도하세요.', error)));
      return;
  } else if (quitData.name == null) {
      res.end(JSON.stringify(new JsonData('not find','구단 없음', error)));
      return;
  } else {
    db_M.member.findAndModify({
    query: { id : quitData.id },
    update: { $set: { myTeamName : null, captain : null } },
    new: true }, function(error, data) {
      if(error) {
        res.end(JSON.stringify(new JsonData('fail','구단 탈퇴 실패', error)));
        return;
      } else {
        db.team.findAndModify({
          query: { name : quitData.name },
          update: { $inc: { count : -1} },
          new: true }, function(error, data) {
            if(error) {
              res.end(JSON.stringify(new JsonData('fail','구단 탈퇴 실패', error)));
              return;
            } else {
              res.end(JSON.stringify(new JsonData('success','구단 탈퇴 성공', null)));
              return;
            }
        });
      }
    });
  }
}

// 내 팀의 팀원정보 조회
function searchMyTeamMember(req, res) {
  var searchName = req.params.name;
  db_M.member.find({myTeamName : searchName}, {_id : false}, function(error, data) {
    if(error) {
      res.end(JSON.stringify(new JsonData('fail','내 팀원 조회 실패', error)));
      return;
    } else {
      res.end(JSON.stringify(new JsonData('success','내 팀원 조회 성공', data)));
      return;
    }
  });
}


// 내 구단 조회
function myTeam(req, res) {
  var myId = req.params.id;

  db_M.member.findOne({id : myId}, function(error, data) {
    if(error) {
      res.end(JSON.stringify(new JsonData('fail','내 구단 검색 실패', error)));
      return;
    } else {
      var resultData = data;
      if(resultData.myTeamName == null) {
        res.end(JSON.stringify(new JsonData('not find','구단이 없습니다.', error)));
        return;
      } else {
        db.team.findOne({name : resultData.myTeamName}, {_id : false}, function(error, data) {
          if(error) {
            res.end(JSON.stringify(new JsonData('fail','내 구단 검색 실패', error)));
            return;
          } else {
            res.end(JSON.stringify(new JsonData('success','내 구단 검색 성공', data)));
            return;
          }
        });
      }
    }
  });
}

// 구단 탈퇴
//function
// 구단 해체
function deleteTeam(req, res) {
  var deleteTeam = req.params.id;

  // 멤버 컬렉션에서 authId의 데이터를 검색한다.
  db_M.member.findOne({id : deleteTeam}, {_id : false}, function (error, data) {
      // 검색 된 데이터
      var resultData = data;
      if(error) {
        res.end(JSON.stringify(new JsonData('fail','구단해체 실패', error)));
        return;
      } else if(resultData.myTeamName == null){
        res.end(JSON.stringify(new JsonData('not find','구단 없음', error)));
        return;
      } else {
        // 주장이 맞는지 검사
        if(resultData.captain != 'true') {
          res.end(JSON.stringify(new JsonData('auth false','권한 오류', error)));
          return;
        } else {
          db.team.remove({name : resultData.myTeamName}, function(error, data) {
            if(error) {
              res.end(JSON.stringify(new JsonData('fail','구단해체 실패', error)));
              return;
            } else {
              db_M.member.update({myTeamName : resultData.myTeamName},
                {$set: { myTeamName : null, captain : null } }, {multi: true},
                function(error, data) {
                  if(error) {
                    res.end(JSON.stringify(new JsonData('fail','구단해체 실패', error)));
                    return;
                  } else {
                    res.end(JSON.stringify(new JsonData('success','구단해체 성공', null)));
                    return;
                  }
                }); // db_M.member.update End
             }
          });
        }
      }
  });
}

// 구단정보 변경
function modify(req, res) {
  // 변경하려는 사람의 아이디를 통해 구단정보를 변경할 수 있는 권한이 있는지 확인
  var authId = req.params.id;  // 변경하려는 사람의 아이디
  var modifyData = req.body; // 변경 될 정보
    // 멤버 컬렉션에서 authId의 데이터를 검색한다.
    db_M.member.findOne({id : authId}, {_id : false}, function (error, data) {
        if(error) {
          res.end(JSON.stringify(new JsonData('fail','구단명 변경 실패', error)));
          return;
        } else if(data == null){
          res.end(JSON.stringify(new JsonData('not find','가입 된 구단이 없습니다.', error)));
          return;
        } else {
          // 검색 된 데이터
          var resultData = data;
          // 주장이 맞는지 검사
          if(resultData.captain != 'true') {
            res.end(JSON.stringify(new JsonData('auth false','권한이 없습니다.', error)));
            return;
          } else {
            // 구단명을 변경하고자 할 때
            if(modifyData.name != null) {
              // 주장이고, 데이터가 존재하면, 그 데이터로 팀 컬렉션에 쿼리
              db.team.findAndModify ({
                query: { name : resultData.myTeamName },
                update: { $set: { name : modifyData.name }
              }, new: true }, function(error, data) {
                if(error) {
                  res.end(JSON.stringify(new JsonData('fail','구단명 변경 실패', error)));
                  return;
                } else {
                  db_M.member.update({myTeamName : resultData.myTeamName},
                    {$set: {myTeamName : modifyData.name}}, {multi: true},
                    function(error, data) {
                      if(error) {
                        res.end(JSON.stringify(new JsonData('fail','구단명 변경 실패', error)));
                        return;
                      } else {
                        res.end(JSON.stringify(new JsonData('success','구단명 변경 성공', null)));
                        return;
                      }
                    }); // db_M.member.update End
                  }
              }); // db.team.findAndModify End
            } // (modifyData.name != null) End
             else if (modifyData.captainName != null) { // 주장 변경
               db.team.findAndModify ({
                 query: { captain : resultData.name },
                 update: { $set: { captain : modifyData.captainName }
               }, new: true }, function(error, data) {
                 if(error) {
                   res.end(JSON.stringify(new JsonData('fail','주장 변경 실패', error)));
                   return;
                 } else {
                   var teamResultData = data;
                   db_M.member.findAndModify({
                     query: {name : modifyData.captainName},
                     update: {$set: {captain : 'true'}}, new: true }, function(error, data) {
                       if(error) {
                         res.end(JSON.stringify(new JsonData('fail','주장 변경 실패', error)));
                         return;
                       } else {
                         db_M.member.findAndModify({
                           query: {name : resultData.name},
                           update: {$set: {captain : 'false'}}, new: true }, function(error, data) {
                           if(error) {
                             res.end(JSON.stringify(new JsonData('fail','주장 변경 실패', error)));
                             return;
                           } else {
                             res.end(JSON.stringify(new JsonData('success','주장 변경 성공', null)));
                             return;
                          }
                        }); // db_M.member.findAndModify End
                      }
                    }); // db_M.member.findAndModify End
                  }
               }); // db.team.findAndModify End
            } // (modifyData.captainName != null) End
         }
      }
  }); // db_M.member.findOne End
} // function End

// 특정 구단 찾기
function findTeam(req, res) {
  var teamName = req.params.name;
  // {$regex:userId}    params.id가 들어가는 모든 데이터를 검색
  db.team.find({name : {$regex:teamName}}, {_id : false}, function(error, data) {
    if(error) {
       res.end(JSON.stringify(new JsonData('fail','시스템 오류', error)));
       return;
    } else if(data == null) {
        res.end(JSON.stringify(new JsonData('not find','일치하는 구단이 없습니다.', null)));
        return;
    } else {
        res.end(JSON.stringify(new JsonData('success','구단 검색 성공', data)));
        return;
    }
  });
}

// 구단 가입
function joinTeam(req, res) {
  var joinData = req.body;
  db_M.member.findOne({id : joinData.id}, function(error, data) {
    if(error) {
      res.end(JSON.stringify(new JsonData('fail','구단가입 실패', error)));
      return;
    } else {
      var resultData = data;
      if(resultData.myTeamName != null) {
        res.end(JSON.stringify(new JsonData('already','구단에 가입되어 있습니다.', resultData.myTeamName)));
        return;
      }
      // 회원정보에 구단명과 주장 값을 변경
      db_M.member.findAndModify({
        query: { id : joinData.id },
        update: { $set: {myTeamName : joinData.name, captain : 'false'}
      }, new: true }, function(error, data) {
          if(error) {
             res.end(JSON.stringify(new JsonData('fail','정보변경 실패', error)));
             console.log(error);
             return;
          } else {
            db.team.findAndModify({
              query: { name : joinData.name },
              update: { $inc: { count : +1 }
              }, new: true }, function(error, data) {
                if(error) {
                   res.end(JSON.stringify(new JsonData('fail','단원 수 변경 실패', error)));
                   return;
                } else {
                  res.end(JSON.stringify(new JsonData('success','구단가입 성공', null)));
                  return;
                }
              });
          }
        });
    }
  });
}

// 전체 구단 조회
function teamlist(req, res) {
  db.team.find({}, {_id : 0}, function (error, data) {
    if(error) {
      res.end(JSON.stringify(new JsonData('fail', '시스템 오류', error)));
      return;
    } else {
      res.end(JSON.stringify(new JsonData('success','구단 조회 성공', data)));
      return;
    }
  });
}

// 구단 생성
function create(req, res) {
  var createData = req.body;
  db_M.member.findOne({id : createData.id}, function(error, data) {
    if(error) {
      res.end(JSON.stringify(new JsonData('fail','구단생성 실패', error)));
      return;
    } else {
      var resultData = data;
      if(resultData.myTeamName != null) {
        res.end(JSON.stringify(new JsonData('already','구단에 가입되어 있습니다.', resultData.myTeamName)));
        return;
      }
      db_M.member.findAndModify({
        query: { id : createData.id },
        update: { $set: {myTeamName : createData.name, captain : 'true' }
      }, new: true }, function(error, data) {
          if(error) {
             res.end(JSON.stringify(new JsonData('fail','정보변경 실패', error)));
             console.log(error);
             return;
          } else {
            db.team.findOne({name : createData.name}, {_id : false}, function (error, data) {
              if(error) {
                 res.end(JSON.stringify(new JsonData('fail', '시스템 오류', error)));
                 return;
              } else if(data == null) { // 구단 없음
                  db.team.save({
                    name : createData.name,
                    count : 1,
                    captain : createData.captain,
                    location : createData.location
                  }, function(error, data) {
                      if(error) {
                        res.end(JSON.stringify(new JsonData('fail','구단생성 실패', error)));
                        return;
                      } else {
                        res.end(JSON.stringify(new JsonData('success','구단생성 성공', data)));
                        return;
                      }
                    });
                  } else { // 구단 이름 중복
                    res.end(JSON.stringify(new JsonData('overlap', '구단명 중복', null)));
                    return;
                  }
              });
              return;
          }
        });
        return;
    }
  });
}
module.exports = router;
