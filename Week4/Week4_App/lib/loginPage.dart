import 'package:Madcamp_Week4/secureStorage.dart';
import 'package:flutter/material.dart';
import 'package:Madcamp_Week4/mainPage.dart';
import 'package:Madcamp_Week4/dio_server.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:Madcamp_Week4/signupPage.dart';
import 'package:Madcamp_Week4/models.dart';


class LoginPage extends StatefulWidget {
  @override
  _LoginPageState createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  final myControllerId = TextEditingController();
  final myControllerPw = TextEditingController();

  Server server = Server();
  final secureStorage = SecureStorage();

  String userInfo = "";

  void showToast(String message) {
    Fluttertoast.showToast(
      msg: message,
      textColor: Colors.white,
      backgroundColor: Colors.black45,
      toastLength: Toast.LENGTH_SHORT,
      gravity: ToastGravity.BOTTOM
    );
  }

  Future<String> getAccessToken() async {
    final storage = FlutterSecureStorage();
    String accessToken = await storage.read(key: "access_token");
    return accessToken;
  }

  @override
  Widget build (BuildContext context) {
    double ratio = MediaQuery.of(context).size.width / 540;

    return Scaffold(
        body: Container(
            color: Colors.white,
            child: Container(
                margin: EdgeInsets.all(40.0* ratio),
                child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: <Widget>[
                      Text(
                          "delivercity",
                          textAlign: TextAlign.center,
                          style: TextStyle(
                              fontWeight: FontWeight.bold,
                              fontSize: 50* ratio,
                              color: Colors.black87,
                            fontFamily: 'MajorMonoDisplay',

                          )
                      ),
                      SizedBox(height:45* ratio),
                      TextField(
                          controller: myControllerId,
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
                              hintText: '아이디'
                          )
                      ),
                      SizedBox(height: 15* ratio),
                      TextField(
                          controller: myControllerPw,
                          obscureText: true,
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
                              hintText: '비밀번호'
                          )
                      ),
                      SizedBox(height:15* ratio),
                      Row(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: <Widget>[
                            ButtonTheme(
                                buttonColor: Colors.blue,
                                height: 35* ratio,
                                minWidth: 135* ratio,
                                child: FlatButton(
                                    shape: RoundedRectangleBorder(
                                        borderRadius: BorderRadius.circular(50* ratio)
                                    ),
                                    child: Text('로그인', style: TextStyle(fontSize: 25* ratio)),
                                    onPressed: () {
                                      String username = myControllerId.text;
                                      String password = myControllerPw.text;
                                      User user = User(username, password);

                                      server.postLogIn(user)
                                          .then((value) {
                                            if (value == 400) {
                                              showToast("아이디 또는 비밀번호가 틀렸습니다.");
                                            } else if (value == 200) {
                                              Navigator.pop(context);
                                              /*
                                              Navigator.push(
                                                  context,
                                                  MaterialPageRoute(builder: (context) => MainPage()));

                                               */
                                            } else if (value == 401) {
                                                showToast("이메일 인증이 완료되지 않은 계정입니다.");
                                            } else {
                                              showToast("로그인 오류가 발생하였습니다.");
                                            }
                                      });
                                    }
                                )
                            ),

                            //SizedBox(width: 15),
                            ButtonTheme(
                                buttonColor: Colors.blue,
                                height: 35* ratio,
                                minWidth: 135* ratio,
                                child: FlatButton(
                                    shape: RoundedRectangleBorder(
                                        borderRadius: BorderRadius.circular(50* ratio)
                                    ),
                                    child: Text('비회원 모드', style: TextStyle(fontSize: 25* ratio)),
                                    onPressed: () {
                                      Navigator.pop(context);
                                      /*
                                      Navigator.push(
                                          context,
                                          MaterialPageRoute(
                                              builder: (context) => MainPage()
                                          )
                                      );

                                       */
                                    }
                                )
                            ),
                            //SizedBox(width: 15),

                            ButtonTheme(
                                buttonColor: Colors.white,
                                height: 35* ratio,
                                minWidth: 135* ratio,
                                child: FlatButton(
                                    child: Text('회원가입', style: TextStyle(fontSize: 25* ratio)),
                                    shape: RoundedRectangleBorder(
                                        borderRadius: BorderRadius.circular(50* ratio)
                                    ),
                                    onPressed: () {
                                      Navigator.push(
                                          context,
                                          MaterialPageRoute(builder: (context) => SignupPage()));
                                    }
                                )
                            )
                          ]
                      )
                    ]
                )
            )
        )
    );
  }
}