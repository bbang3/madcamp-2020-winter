import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:Madcamp_Week4/dio_server.dart';
import 'package:Madcamp_Week4/secureStorage.dart';
import 'package:Madcamp_Week4/models.dart';

class Settings extends StatefulWidget {
  @override
  _SettingsState createState() => _SettingsState();
}

class _SettingsState extends State<Settings> {
  var myControllerNickname = TextEditingController();
  SecureStorage secureStorage = SecureStorage();
  Server server = Server();
  UserResponse user = UserResponse();

  void showToast(String message) {
    Fluttertoast.showToast(
        msg: message,
        textColor: Colors.white,
        backgroundColor: Colors.black45,
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.BOTTOM
    );
  }

  @override
  void initState() {
    secureStorage.readAllFromStorage()
        .then((value) {
      //TODO: User 클래스 및 secureStorage.dart 수정하여 username 또는 _id 저장하게끔 하기
      //'변경사항 저장' 버튼 클릭 시 서버 db의 해당 user에 접근하여 수정해야하기 때문.
      setState(() {
        user.setId(value['_id']);
        user.username = value['username'];
        user.nickname = value['nickname'];
      });
    });

    super.initState();
  }


  @override
  Widget build(BuildContext context) {
    double ratio = MediaQuery.of(context).size.width / 540;

    return Scaffold(
      backgroundColor: Colors.white,
      appBar: PreferredSize(
          preferredSize: Size.fromHeight(75* ratio),
          child: AppBar(
              backgroundColor: Colors.white,
              brightness: Brightness.light,
              elevation: 0,
              leading: IconButton(
                  padding: EdgeInsets.all(20* ratio),
                  icon: Icon(Icons.arrow_back , color: Colors.black87),
                  onPressed: () {
                    Navigator.pop(context, true);
                  }
              )
          )
      ),
      body: Container(
        color: Colors.white,
        child: Container(
            margin: EdgeInsets.all(40.0* ratio),
            child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: <Widget>[
                  Row(
                      children: <Widget>[
                        Icon(Icons.auto_fix_high),
                        Text("새로운 닉네임을 입력하세요.")
                      ]
                  ),
                  TextField(
                      controller: myControllerNickname,
                      decoration: InputDecoration(
                          fillColor: Colors.white,
                          filled: true,
                          enabledBorder: OutlineInputBorder(
                            borderRadius: BorderRadius.circular(50* ratio),
                            borderSide: BorderSide(
                              color: Colors.black87,
                              width: 1.3* ratio,
                              //style: BorderStyle.none
                            ),
                          ),
                          focusedBorder: OutlineInputBorder(
                            borderRadius: BorderRadius.circular(50* ratio),
                            borderSide: BorderSide(
                              color: Colors.black87,
                              width: 1.3* ratio,
                              //style: BorderStyle.none
                            ),
                          ),
                          hintText: '닉네임'
                      )
                  ),

                  SizedBox(height:35* ratio),
                  ButtonTheme(
                      buttonColor: Colors.blue,
                      height: 35* ratio,
                      minWidth: 150* ratio,
                      child: FlatButton(
                          shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(50* ratio)
                          ),
                          child: Text('변경사항 저장', style: TextStyle(fontSize: 25* ratio)),
                          onPressed: () {
                            // TODO: 서버 db 업데이트, secureStorage 업데이트
                            if (myControllerNickname.text.length >= 11) {
                              showToast("닉네임은 최대 10자까지 가능합니다.");
                            } else {
                              String nickname = myControllerNickname.text;
                              Map<String, String> nicknameJson = {'nickname': nickname};

                              secureStorage.readAllFromStorage()
                                  .then((value) {
                                    server.updateUser(nicknameJson, user.getId(), value['access_token'])
                                    .then((statusCode) {
                                  if (statusCode == 200) {
                                    secureStorage.saveNicknameToStorage(
                                        myControllerNickname.text);
                                    showToast("닉네임이 변경되었습니다.");
                                  } else if (statusCode == 401) {
                                    server.refreshAccessToken(value['refresh_token'])
                                        .then((newAccessToken) {
                                          server.updateUser(nicknameJson, user.getId(), newAccessToken)
                                              .then((newStatusCode) {
                                                if (newStatusCode == 200) {
                                                  secureStorage.saveNicknameToStorage(
                                                    myControllerNickname.text
                                                  );
                                                  showToast("닉네임이 변경되었습니다.");
                                                } else {

                                                }
                                          });
                                    });

                                  } else {
                                    showToast("닉네임 변경 중 오류가 발생하였습니다.");
                                  }
                                });
                              });
                            }
                          }
                      )
                  )

                ]
            )

        )
    )
    );
  }
}
