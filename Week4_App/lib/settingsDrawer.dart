import 'package:flutter/material.dart';
import 'package:Madcamp_Week4/loginPage.dart';
import 'package:Madcamp_Week4/secureStorage.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:Madcamp_Week4/settings.dart';
import 'package:Madcamp_Week4/dio_server.dart';

class SettingsDrawer extends StatefulWidget {
  //SettingsDrawer({Key key, this.currentContext}): super(key: key);
  //final BuildContext currentContext;

  BuildContext currentContext;
  Function refreshState;

  SettingsDrawer(BuildContext currentContext, Function refreshState) {
    this.currentContext = currentContext;
    this.refreshState = refreshState;
  }

  @override
  _SettingsDrawerState createState() => _SettingsDrawerState(currentContext, refreshState);
}

class _SettingsDrawerState extends State<SettingsDrawer> {
  final secureStorage = SecureStorage();
  BuildContext currentContext;
  Function refreshState;

  _SettingsDrawerState(BuildContext currentContext, Function refreshState) {
    this.currentContext = currentContext;
    this.refreshState = refreshState;
  }


  @override
  Widget build (BuildContext context) {
    return Drawer(
        child: CustomListView(currentContext, refreshState)
    );
  }
}


class CustomListView extends StatefulWidget {
  //CustomListView({Key key, this.currentContext}): super(key: key);
  //final BuildContext currentContext;
  BuildContext currentContext;
  Function refreshParentState;

  CustomListView(BuildContext currentContext, Function refreshState) {
    this.currentContext = currentContext;
    this.refreshParentState = refreshState;
  }

  @override
  _CustomListViewState createState() => _CustomListViewState(currentContext);
}

class _CustomListViewState extends State<CustomListView> {
  bool isLoggedIn = false;
  String nickname = "비회원";
  String role = "";

  BuildContext currentContext;
  final secureStorage = SecureStorage();
  var server = Server();

  _CustomListViewState(BuildContext currentContext) {
    this.currentContext = currentContext;
  }

  refreshState() {
    setState(() {
      secureStorage.readAllFromStorage()
          .then((value) {
        if (value['access_token'] != null) {
          setState(() {
            isLoggedIn = true;
            nickname = value['nickname'];
            role = value['role'];
          });
        }
      });
    });
  }

  void _backToLogin() {
    Navigator.push(
        widget.currentContext,
        MaterialPageRoute(
            builder: (context) => LoginPage()
        )
    );
  }

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
      if (value['access_token'] != null) {
        setState(() {
          isLoggedIn = true;
          nickname = value['nickname'];
          role = value['role'];
        });
      }
    });
    super.initState();
  }

  String roleTranslator(String role) {
    if (role == null) {
      return " ";
    } else if (role.compareTo("user") == 0) {
      return "사용자";
    } else if (role.compareTo("admin") == 0) {
      return "관리자";
    } else {
      return role;
    }
  }

  String nicknameTranslator(String nickname) {
    if (nickname == null || nickname.compareTo("") == 0) {
      return "닉네임을 설정하세요";
    } else {
      return nickname;
    }
  }

  @override
  Widget build(BuildContext context) {
    double ratio = MediaQuery.of(context).size.width / 540;

    if (isLoggedIn) {
      return Container(
        color: Colors.white,
        child: ListView(
            padding: EdgeInsets.only(left: 20* ratio, top: 30*ratio),
            children: <Widget>[
              SizedBox(height: 40* ratio),
              Row(
                children: <Widget>[
                  Container(
                      width: 60* ratio,
                      height: 60* ratio,
                      child: Icon(Icons.account_circle)
                  ),
                  Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: <Widget>[
                        Text(nicknameTranslator(nickname), style: TextStyle(fontSize: 16*ratio)),
                        SizedBox(height:10*ratio),
                        Text(roleTranslator(role), style: TextStyle(fontSize: 16*ratio))
                      ]
                  )
                ],
              ),
              SizedBox(height:20*ratio),
              ListTile(
                  title: Text("설정"),
                  onTap: () {
                    Navigator.push(
                        widget.currentContext,
                        MaterialPageRoute(
                            builder: (context) => Settings()
                        )
                    ).then((value) {
                      refreshState();
                      if (widget.refreshParentState != null) {
                        widget.refreshParentState();
                      }
                    });
                  }
              ),
              ListTile(
                  title: Text("로그아웃"),
                  onTap: () {
                    showToast("로그아웃 되었습니다.");
                    secureStorage
                        .deleteAllFromStorage()
                        .then((value) {
                      setState(() {
                        isLoggedIn = false;
                        nickname = "비회원";
                        role = "";
                      });
                      if (widget.refreshParentState != null) {
                        print("yooo");
                        widget.refreshParentState();
                      }
                    });
                  }
              )
            ]
        )
      );

    } else {
      return Container(
        color: Colors.white,
        child: ListView(
            padding: EdgeInsets.only(left: 20* ratio, top: 30*ratio),
            children: <Widget>[
              SizedBox(height: 40* ratio),
              Row(
                children: <Widget>[
                  Container(
                      width: 60* ratio,
                      height: 60* ratio,
                      child: Icon(Icons.account_circle)
                  ),
                  Column(
                      children: <Widget>[
                        Text("$nickname", style: TextStyle(fontSize: 16*ratio)),
                        SizedBox(height:10*ratio),
                        Text("$role", style: TextStyle(fontSize: 16*ratio))
                      ]
                  )
                ],
              ),
              SizedBox(height:20*ratio),
              ListTile(
                  title: Text("설정"),
                  onTap: () {
                    showToast("로그인이 필요합니다.");
                  }
              ),
              ListTile(
                  title: Text("로그인"),
                  onTap: () {
                    //_backToLogin();
                    Navigator.push(
                        widget.currentContext,
                        MaterialPageRoute(
                            builder: (context) => LoginPage()
                        )
                    ).then((value) {
                      refreshState();
                      if (widget.refreshParentState != null) {
                        widget.refreshParentState();
                      }
                    });
                  }
              )
            ]
        )
      );
    }
  }
}