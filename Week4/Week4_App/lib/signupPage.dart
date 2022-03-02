import 'package:Madcamp_Week4/secureStorage.dart';
import 'package:flutter/material.dart';
import 'package:Madcamp_Week4/mainPage.dart';
import 'package:Madcamp_Week4/dio_server.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:Madcamp_Week4/loginPage.dart';
import 'package:Madcamp_Week4/models.dart';

class SignupPage extends StatefulWidget {
  @override
  _SignupPageState createState() => _SignupPageState();
}

class _SignupPageState extends State<SignupPage> {
  final myControllerId = TextEditingController();
  final myControllerPw = TextEditingController();
  final myControllerPwCf = TextEditingController();
  bool same = true;
  Color idBorderColor = Colors.black87;
  Color borderColor = Colors.black87;
  Color hintColor = Colors.black54;


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

  onItemChanged(String value) {
    setState(() {
      if (this.myControllerPw.text.compareTo(this.myControllerPwCf.text) != 0) {
        this.same = false;
        borderColor = Colors.red;
        hintColor = Colors.red;
      } else {
        this.same = true;
        borderColor = Colors.black87;
        hintColor = Colors.black54;
      }
    });
  }

  showAlertDialogWrong(BuildContext buildContext) {
    Widget okButton = FlatButton(
      child: Text("확인"),
      onPressed: () { Navigator.of(context).pop();},
    );

    AlertDialog alert = AlertDialog(
      title: Text("회원가입"),
      content: Text("아이디와 비밀번호를 확인해주세요."),
      actions: [
        okButton,
      ],
    );

    showDialog(
        context: context,
        builder: (BuildContext context) {
          return alert;
        }
    );
  }

  showAlertDialog(BuildContext context) {
    Widget okButton = FlatButton(
      child: Text("확인"),
      onPressed: () {Navigator.of(context).pop(); },
    );

    AlertDialog alert = AlertDialog(
      title: Text("이메일 인증"),
      content: Text("${myControllerId.text}@gmail.com으로 인증 메일을 전송하였습니다. 인증이 완료되어야 로그인이 가능합니다."),
      actions: [
        okButton,
      ],
    );

    showDialog(
      context: context,
      builder: (BuildContext context) {
        return alert;
      }
    );
  }

  showAlertDialogRegisterFail(BuildContext context) {
    Widget okButton = FlatButton(
      child: Text("확인"),
      onPressed: () {Navigator.of(context).pop(); },
    );

    AlertDialog alert = AlertDialog(
      title: Text("회원가입"),
      content: Text("회원가입에 실패하였습니다. 중복된 아이디가 존재합니다."),
      actions: [
        okButton,
      ],
    );

    showDialog(
        context: context,
        builder: (BuildContext context) {
          return alert;
        }
    );
  }

  bool pwValidate(String password) {
    String checker = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789~!%<>_^&*";

    for (int i = 0; i < password.length; i++) {
      if (!checker.contains(password[i]))
        return false;
    }

    if (password.length < 8) {
      return false;
    }

    return true;
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
              Navigator.pop(context, null);
            }
          )
        )
      ),
      body: SingleChildScrollView(
        physics: ClampingScrollPhysics(),
        child: Container(
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
                        Text("아이디는 학교 이메일과 동일해야 합니다.", style: TextStyle(fontSize: 10))
                      ]
                    ),
                    Container(
                      height: 60*ratio,
                      child: TextField(
                        onChanged: (value) {
                          if (value.compareTo("") == 0) {
                            setState(() {
                              this.idBorderColor = Colors.red;
                            });
                          } else {
                            setState(() {
                              this.idBorderColor = Colors.black87;
                            });
                          }
                        },
                          controller: myControllerId,
                          decoration: InputDecoration(
                              fillColor: Colors.white,
                              filled: true,
                              enabledBorder: OutlineInputBorder(
                                borderRadius: BorderRadius.circular(50* ratio),
                                borderSide: BorderSide(
                                  color: idBorderColor,
                                  width: 1.3* ratio,
                                  //style: BorderStyle.none
                                ),
                              ),
                              focusedBorder: OutlineInputBorder(
                                borderRadius: BorderRadius.circular(50* ratio),
                                borderSide: BorderSide(
                                  color: idBorderColor,
                                  width: 1.3* ratio,
                                  //style: BorderStyle.none
                                ),
                              ),
                              hintText: '아이디'
                          )
                      ),
                    ),
                    SizedBox(height: 15* ratio),
                    Row(
                        children: <Widget>[
                          Icon(Icons.auto_fix_high),
                          Text("비밀번호는 영문, 숫자, 특수문자의 조합으로 8자리 이상 입력해주세요.", style: TextStyle(fontSize: 10)),
                        ]
                    ),
                    Container(
                      height: 60*ratio,
                      child: TextField(
                        onChanged: onItemChanged,
                          controller: myControllerPw,
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
                    ),
                    SizedBox(height: 15* ratio),
                    Row(
                        children: <Widget>[
                          Icon(Icons.auto_fix_high),
                          Text("비밀번호를 확인해주세요.", style: TextStyle(fontSize: 10))
                        ]
                    ),
                    Container(
                      height: 60*ratio,
                      child: TextField(
                        onChanged: onItemChanged,
                          controller: myControllerPwCf,
                          decoration: InputDecoration(
                              fillColor: Colors.white,
                              filled: true,
                              enabledBorder: OutlineInputBorder(
                                borderRadius: BorderRadius.circular(50* ratio),
                                borderSide: BorderSide(
                                  color: borderColor,
                                  width: 1.3* ratio,
                                  //style: BorderStyle.none
                                ),
                              ),
                              focusedBorder: OutlineInputBorder(
                                borderRadius: BorderRadius.circular(50* ratio),
                                borderSide: BorderSide(
                                  color: borderColor,
                                  width: 1.3* ratio,
                                  //style: BorderStyle.none
                                ),
                              ),
                              hintText: '비밀번호 확인',
                              hintStyle: TextStyle(
                                  color: hintColor
                              )
                          )
                      ),
                    ),
                    SizedBox(height:35* ratio),
                    ButtonTheme(
                        buttonColor: Colors.white,
                        height: 35* ratio,
                        minWidth: 150* ratio,
                        child: FlatButton(
                            child: Text('회원가입', style: TextStyle(fontSize: 25* ratio)),
                            shape: RoundedRectangleBorder(
                                borderRadius: BorderRadius.circular(50* ratio)
                            ),
                            onPressed: () {
                              if (myControllerId.text.compareTo("") != 0
                                  && pwValidate(myControllerPw.text)
                                  && myControllerPw.text.compareTo(myControllerPwCf.text) == 0) {

                                UserRegister user = UserRegister(myControllerId.text, myControllerPw.text, "", "user");
                                server.postRegister(user)
                                    .then((value) {
                                      if (value == 400) {
                                        showAlertDialogRegisterFail(context);
                                      } else if (value == 502) {
                                        showToast("502 bad gateway error!");
                                      } else if (value == 200) {
                                        showAlertDialog(context);
                                      }
                                    });

                              } else {
                                showAlertDialogWrong(context);
                              }
                            }
                        )
                    )
                        ]
                    )

              )
          ),
      )

    );
  }
}
