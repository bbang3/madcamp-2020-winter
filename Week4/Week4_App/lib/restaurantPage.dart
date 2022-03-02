import 'package:Madcamp_Week4/secureStorage.dart';
import 'package:flutter/material.dart';
import 'package:Madcamp_Week4/loginPage.dart';
import 'package:Madcamp_Week4/dio_server.dart';
import 'package:Madcamp_Week4/settingsDrawer.dart';
import 'package:Madcamp_Week4/mainPage.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:intl/intl.dart';
import 'package:page_view_indicators/page_view_indicators.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:Madcamp_Week4/menuPage.dart';
import 'package:Madcamp_Week4/models.dart';

const _API_PREFIX = "http://192.249.18.244:8080";


class RestaurantPage extends StatefulWidget {
  RestaurantPage({Key key, this.restaurantId}): super(key: key);
  final String restaurantId;

  @override
  _RestaurantPageState createState() => _RestaurantPageState(restaurantId);
}

class _RestaurantPageState extends State<RestaurantPage> {
  String restaurantId;
  Server server = Server();
  SecureStorage secureStorage = SecureStorage();

  final GlobalKey<ScaffoldState> _scaffoldKey = GlobalKey<ScaffoldState>();

  List<Restaurant> restaurantList = [];
  List<Restaurant> newRestaurantList = [];
  List<Menu> menus = [];
  List<Comment> comments = [];
  RestaurantSpec restaurant = RestaurantSpec();
  final PageController pageController = PageController(initialPage: 0);
  final currentPageNotifier = ValueNotifier<int>(0);
  Page1Widget p1;
  Page2Widget p2;
  String userId;

  _RestaurantPageState(String restaurantId) {
    this.restaurantId = restaurantId;
  }

  @override
  void initState() {
    secureStorage.readAllFromStorage()
        .then((value) {
          setState(() {
            this.userId = value['_id'];
          });
    });

    server.getRestaurant(restaurantId)
        .then((value) {
      setState(() {
        this.restaurant = value;
      });
    })
        .catchError((error) => print(error));

    server.getComments(restaurantId)
        .then((value) {
          setState(() {
            this.comments = value;
          });
    });

    super.initState();
  }

  refreshState() {
    secureStorage.readAllFromStorage()
        .then((value) {
      setState(() {
        this.userId = value['_id'];
      });
    });


    server.getRestaurant(restaurantId)
        .then((value) {
      setState(() {
        this.restaurant = value;
      });
    })
        .catchError((error) => print(error));

    server.getComments(restaurantId)
        .then((value) {
      setState(() {
        this.comments = value;
      });
    });

  }

  @override
  Widget build(BuildContext context) {
    double ratio = MediaQuery.of(context).size.width / 540;

    return Scaffold(
          backgroundColor: Colors.white,
          key: _scaffoldKey,
            appBar: PreferredSize(
              preferredSize: Size.fromHeight(75 * ratio),
              child: AppBar(
                elevation: 0,
                brightness: Brightness.light,
                leading: IconButton(
                  padding: EdgeInsets.all(20 * ratio),
                  icon: Icon(Icons.arrow_back, color: Colors.black87),
                  onPressed: () {
                    Navigator.pop(context);
                  }
                ),
                actions: <Widget>[
                  IconButton(
                    splashColor: Colors.transparent,
                      padding: EdgeInsets.all(20 * ratio),
                      icon: Icon(Icons.menu, color: Colors.black87),
                      onPressed: () => _scaffoldKey.currentState.openEndDrawer()
                  ),
                ],
                backgroundColor: Colors.white,
              ),
            ),
        endDrawer: SettingsDrawer(context, refreshState),
            body: Container(
                margin: EdgeInsets.zero,
                child: Stack(
                    children: <Widget>[
                      PageView.builder(
                                itemCount: 2,
                                  controller: pageController,
                                  //scrollDirection: Axis.vertical,
                                  onPageChanged: (int index) {
                                    currentPageNotifier.value = index;
                                  },
                                  itemBuilder: (BuildContext context, int index) {
                                    this.p1 = Page1Widget(restaurant);
                                    this.p2 = Page2Widget(comments, restaurantId, refreshState, userId);
                                    List<Widget> list = [this.p1, this.p2];
                                    return list[index];
                                  }
                      ),
                      Positioned(
                        left: 0.0,
                        right: 0.0,
                        bottom: 0.0,
                        child: Padding(
                          padding: EdgeInsets.all(8.0 * ratio),
                          child: CirclePageIndicator(
                            itemCount: 2,
                            currentPageNotifier: this.currentPageNotifier
                          )
                        )
                      )
                    ]
                )
            )

    );

  }
}

class Page1Widget extends StatefulWidget {
  RestaurantSpec restaurant;

  Page1Widget(RestaurantSpec restaurant) {
    this.restaurant = restaurant;
  }

  @override
  _Page1WidgetState createState() => _Page1WidgetState();
}

class _Page1WidgetState extends State<Page1Widget> {

  @override
  Widget build(BuildContext context) {
    double ratio = MediaQuery.of(context).size.width / 540;

    return SingleChildScrollView(
      physics: AlwaysScrollableScrollPhysics(),
      scrollDirection: Axis.vertical,
      child: Column(
          children: <Widget>[
            widget.restaurant.image == null
                ? Image(image: AssetImage('assets/restaurant.png'), fit: BoxFit.contain, width: double.infinity, height: 300*ratio)
            //Image.network("https://makitweb.com/demo/broken_image/images/noimage.png", fit: BoxFit.cover, width: double.infinity, height: 300 * ratio)
                : Image.network("$_API_PREFIX/image/restaurants/${widget.restaurant.image}", fit: BoxFit.cover, width: double.infinity, height: 300 * ratio),
            SizedBox(height: 50 * ratio),
            Row(
                children: <Widget>[
                  SizedBox(width: 40 * ratio),
                  Text(
                      widget.restaurant.name,
                      style: TextStyle(
                          fontSize: 45 * ratio,
                          fontWeight: FontWeight.bold
                      )
                  )
                ]
            ),
            SizedBox(height: 20 * ratio),
            Row(
                children: <Widget>[
                  SizedBox(width: 50 * ratio),
                  Icon(
                      Icons.article_outlined
                  ),
                  SizedBox(width: 10 * ratio),
                  Container(
                    width: 390 * ratio,
                    child: Text(
                        widget.restaurant.description,
                        style: TextStyle(
                            fontSize: 16 * ratio
                        )
                    ),
                  )
                ]
            ),
            SizedBox(height: 13 * ratio),
            Row(
                children: <Widget> [
                  SizedBox(width: 50 * ratio),
                  Icon(
                      Icons.phone
                  ),
                  SizedBox(width: 10 * ratio),
                  InkWell(
                    child: Text(
                        widget.restaurant.telephone,
                        style: TextStyle(
                            fontSize: 16 * ratio
                        )
                    ),
                    onTap: () {
                      launch("tel:${widget.restaurant.telephone}");
                    }
                  )
                ]
            ),
            SizedBox(height: 13 * ratio),
            Row(
                children: <Widget> [
                  SizedBox(width: 50 * ratio),
                  Icon(
                      Icons.location_on_outlined
                  ),
                  SizedBox(width: 10 * ratio),
                  InkWell(
                    child: Text(
                        "${widget.restaurant.location.fullAddress}, ${widget.restaurant.location.extraAddress}",
                        style: TextStyle(
                            fontSize: 16 * ratio
                        )
                    ),
                    onTap: () {}
                  )

                ]
            ),
            SizedBox(height: 20* ratio),
            Flex(
                direction: Axis.vertical,
                children: <Widget>[
                  ListView.builder(
                    physics: ClampingScrollPhysics(),
                      itemCount: widget.restaurant.menus.length,
                      scrollDirection: Axis.vertical,
                      shrinkWrap: true,

                      itemBuilder: (BuildContext context, int index) {
                        return GestureDetector(
                            onTap: () {
                              String name = widget.restaurant.menus[index].name;
                              String description = widget.restaurant.menus[index].description;
                              List<PackageSize> sizes = widget.restaurant.menus[index].sizes;
                              String image = widget.restaurant.menus[index].image;

                              Navigator.push(
                                  context,
                                  MaterialPageRoute(
                                      builder: (context) => MenuPage(name, description, sizes, image)
                                  ));
                              //.then((value) => _RestaurantPageState(restaurantId).refreshState());
                            },
                            child: Container(
                              padding: EdgeInsets.symmetric(horizontal: 70 * ratio, vertical: 40 * ratio),
                              child: Column(
                                children: [
                                  Row(
                                    children: [
                                      widget.restaurant.menus[index].image == null
                                          ? Image(image: AssetImage('assets/egg.png'), fit: BoxFit.cover, width: 130*ratio, height: 130 * ratio)
                                      //Image.network("https://makitweb.com/demo/broken_image/images/noimage.png", fit: BoxFit.cover, width: 130*ratio, height: 130 * ratio)
                                          : Image.network("$_API_PREFIX/image/menus/${widget.restaurant.menus[index].image}", fit: BoxFit.cover, width: 130*ratio, height: 130 * ratio),
                                      SizedBox(width: 45*ratio),
                                      Container(
                                          padding: EdgeInsets.symmetric(vertical: 10 * ratio, horizontal: 0),
                                          width: 200 * ratio,
                                          height: 130*ratio,
                                          child: Row(
                                              crossAxisAlignment: CrossAxisAlignment.start,
                                              mainAxisAlignment: MainAxisAlignment.start,
                                              children: <Widget>[
                                                Column(
                                                  crossAxisAlignment: CrossAxisAlignment.start,
                                                  children: [
                                                    Text(
                                                        widget.restaurant.menus[index].name,
                                                        style: TextStyle(
                                                            fontSize: 25 * ratio
                                                        )
                                                    ),
                                                    SizedBox(height: 15*ratio),
                                                    Container(
                                                      width: 200 *ratio,
                                                      child: Text(
                                                          widget.restaurant.menus[index].description,
                                                          style: TextStyle(
                                                              fontSize: 16 * ratio,
                                                              color: Colors.black45
                                                          )
                                                      ),
                                                    )
                                                  ],
                                                )
                                              ]
                                          )
                                      ),
                                    ],
                                  ),
                                  SizedBox(
                                      height: index == widget.restaurant.menus.length - 1
                                          ? 10 * ratio
                                          : 0
                                  ),
                                ],
                              ),
                            )
                        );

                      }
                  ),
                ],
            ),
            SizedBox(height: 30 * ratio)
          ]
      )
    );
  }
}

class Page2Widget extends StatefulWidget {
  List<Comment> comments = [];
  String restaurantId;
  Function() refreshParent;
  String userId;

  Page2Widget(List<Comment> comments, String restaurantId, Function refreshParent, String userId) {
    this.comments = comments;
    this.restaurantId = restaurantId;
    this.refreshParent = refreshParent;
    this.userId = userId;
  }

  @override
  _Page2WidgetState createState() => _Page2WidgetState();
}

class _Page2WidgetState extends State<Page2Widget> {
  final myControllerComment = TextEditingController();
  Server server = Server();
  SecureStorage secureStorage = SecureStorage();


  void showToast(String message) {
    Fluttertoast.showToast(
        msg: message,
        textColor: Colors.white,
        backgroundColor: Colors.black45,
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.BOTTOM
    );
  }

  showAlertDialog(BuildContext context, int index, String text) {
    TextEditingController myController = TextEditingController();
    myController.text = text;

    Widget okButton = FlatButton(
      child: Text("확인"),
      onPressed: () {
          secureStorage.readAllFromStorage()
              .then((value) {
            server.updateComment(myController.text, widget.restaurantId, widget.comments[index].getId(), value['access_token'])
                .then((statusCode) {
              if (statusCode != 200) {
                server.refreshAccessToken(value['refresh_token']).then((newAccessToken) {
                  server.updateComment(myController.text, widget.restaurantId, widget.comments[index].getId(), newAccessToken)
                      .then((value) {
                    widget.refreshParent();
                    showToast("댓글이 수정되었습니다.");
                  });
                });
              } else {
                widget.refreshParent();
                showToast("댓글이 수정되었습니다.");
              }
            });
          });
        Navigator.of(context).pop();
      },
    );

    AlertDialog alert = AlertDialog(
      title: Text("댓글 수정"),
      content: TextField(
          controller: myController,
          decoration: InputDecoration(
              fillColor: Colors.white,
              filled: true,
              enabledBorder: OutlineInputBorder(
                borderRadius: BorderRadius.circular(50),
                borderSide: BorderSide(
                  color: Colors.black87,
                  width: 1.3,
                  //style: BorderStyle.none
                ),
              ),
              focusedBorder: OutlineInputBorder(
                borderRadius: BorderRadius.circular(50),
                borderSide: BorderSide(
                  color: Colors.black87,
                  width: 1.3,
                  //style: BorderStyle.none
                ),
              ),
              hintText: '댓글'
          )
      ),
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

  int isItMyComment(int index) {
    if (widget.userId == null) {
      return 0;
    } else {
      if ((widget.comments[index].userId).compareTo(widget.userId) == 0) {
        return 1;
      }
      return 0;
    }
  }


  @override
  Widget build(BuildContext context){
    double ratio = MediaQuery.of(context).size.width / 540;

    return Container(
      margin: EdgeInsets.only(left: 50* ratio, right: 50* ratio, bottom: 30* ratio),
      width: double.infinity,
      child: Scaffold(
        body:
        Container(
          margin: EdgeInsets.only(left: 15* ratio, right: 15* ratio, top: 8*ratio),
          child: ListView.builder(
              physics: AlwaysScrollableScrollPhysics(),
              itemCount: widget.comments.length,
              scrollDirection: Axis.vertical,
              shrinkWrap: true,
              itemBuilder: (BuildContext context, int index) {
                        if (isItMyComment(index) == 1) {
                          return Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              mainAxisAlignment: MainAxisAlignment.end,
                              children: <Widget>[
                                Row(
                                    children: <Widget>[
                                      Text(
                                          widget.comments[index].nickname,
                                          style: TextStyle(
                                              fontSize: 20* ratio,
                                              fontWeight: FontWeight.bold,
                                              color: Colors.blueAccent
                                          )
                                      ),
                                      Expanded(
                                          child: Align(
                                              alignment: Alignment.bottomRight,
                                              child: Text(
                                                  DateFormat('yyyy.MM.dd HH:mm:ss').format(widget.comments[index].date),
                                                  style: TextStyle(
                                                      fontSize: 14* ratio,
                                                      color: Colors.black45
                                                  )
                                              )
                                          )
                                      )
                                    ]
                                ),
                                Row(
                                    children: <Widget>[
                                      Container(
                                        width: 200*ratio,
                                        child: Text(
                                            widget.comments[index].body,
                                            style: TextStyle(
                                                fontSize: 16* ratio
                                            )
                                        ),
                                      ),
                                      Expanded(
                                          child: Align(
                                              alignment: Alignment.bottomRight,
                                              child: IconButton(
                                                  onPressed: () {
                                                    showAlertDialog(context, index, widget.comments[index].body);
                                                  },
                                                  icon: Icon(Icons.edit),
                                                  iconSize: 16* ratio
                                              )
                                          )
                                      ),
                                      IconButton(
                                          onPressed: () {
                                            secureStorage.readAllFromStorage()
                                                .then((value) {
                                              server.deleteComment(widget.restaurantId, widget.comments[index].getId(), value['access_token'])
                                                  .then((statusCode) {
                                                if (statusCode != 200) {
                                                  server.refreshAccessToken(value['refresh_token']).then((newAccessToken) {
                                                    server.deleteComment(widget.restaurantId, widget.comments[index].getId(), newAccessToken)
                                                        .then((value) {
                                                      widget.refreshParent();
                                                      showToast("댓글이 파괴되었습니다.");
                                                    });

                                                  });
                                                } else {
                                                  widget.refreshParent();
                                                  showToast("댓글이 파괴되었습니다.");
                                                }
                                              });
                                            });
                                          },
                                          icon: Icon(Icons.clear),
                                          iconSize: 16* ratio
                                      )
                                    ]
                                ),
                                SizedBox(height:20* ratio)
                              ]
                          );
                        } else {
                          return Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              mainAxisAlignment: MainAxisAlignment.end,
                              children: <Widget>[
                                Row(
                                    children: <Widget>[
                                      Container(
                                        //width: 200*ratio,
                                        child: Text(
                                            widget.comments[index].nickname,
                                            style: TextStyle(
                                                fontSize: 20* ratio,
                                                fontWeight: FontWeight.bold,
                                                color: Colors.black87
                                            )
                                        ),
                                      ),
                                      Expanded(
                                          child: Align(
                                              alignment: Alignment.bottomRight,
                                              child: Text(
                                                  DateFormat('yyyy.MM.dd HH:mm:ss').format(widget.comments[index].date),
                                                  style: TextStyle(
                                                      fontSize: 14* ratio,
                                                      color: Colors.black45
                                                  )
                                              )
                                          )
                                      )
                                    ]
                                ),
                                Row(
                                    children: <Widget>[
                                      Container(
                                        width: 200*ratio,
                                        child: Text(
                                            widget.comments[index].body,
                                            style: TextStyle(
                                                fontSize: 16 * ratio
                                            )
                                        ),
                                      ),
                                      Expanded(
                                          child: Align(
                                              alignment: Alignment.bottomRight,
                                              child: Visibility(
                                                visible: false,
                                                maintainSize: true,
                                                maintainAnimation: true,
                                                maintainState: true,
                                                child: IconButton(
                                                    icon: Icon(Icons.edit),
                                                    iconSize: 15* ratio
                                                ),
                                              )
                                          )
                                      ),
                                      Visibility(
                                        visible: false,
                                        maintainSize: true,
                                        maintainAnimation: true,
                                        maintainState: true,
                                        child: IconButton(
                                            icon: Icon(Icons.clear),
                                            iconSize: 15* ratio
                                        ),
                                      )

                                    ]
                                ),
                                SizedBox(height:20* ratio)
                              ]
                          );
                        }
                      }

          ),
        ),
        backgroundColor: Colors.white,
        bottomNavigationBar: BottomAppBar(
          color: Colors.transparent,
          elevation: 0,
          child: Row(
              children: <Widget>[
                    Container(
                      height: 55*ratio,
                      width: 385* ratio,

                      child: TextField(
                        textAlignVertical: TextAlignVertical.center,
                          controller: myControllerComment,
                          decoration: InputDecoration(
                            fillColor: Colors.white,
                            filled: true,
                            enabledBorder: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(50 * ratio),
                              borderSide: BorderSide(
                                color: Colors.black87,
                                width: 1.3 * ratio,
                                //style: BorderStyle.none
                              ),
                            ),
                            focusedBorder: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(50 * ratio),
                              borderSide: BorderSide(
                                color: Colors.black87,
                                width: 1.3 * ratio,
                                //style: BorderStyle.none
                              ),
                            ),
                          )
                      ),
                    ),
                    SizedBox(width: 20 * ratio, height: 100*ratio),
                    Container(
                        height: 60 * ratio,
                        width: 30 * ratio,
                        child: InkWell(
                            onTap: () {
                              String accessToken;
                              secureStorage
                                  .readAllFromStorage()
                                  .then((value) {

                                accessToken = value['access_token'];

                                server.postComment(widget.restaurantId, myControllerComment.text, accessToken)
                                    .then((statusCode) {
                                  if (statusCode == 401) {
                                    String refreshToken = value['refresh_token'];
                                    server.refreshAccessToken(refreshToken).then((newAccessToken){
                                      server.postComment(widget.restaurantId, myControllerComment.text, newAccessToken)
                                          .then((value) => widget.refreshParent());
                                      myControllerComment.clear();
                                    });
                                  } else {
                                    widget.refreshParent();
                                    myControllerComment.clear();
                                  }
                                });
                              });
                            },
                            child: Icon(
                                Icons.send,
                                size: 35 * ratio
                            ),
                            borderRadius: BorderRadius.circular(50 * ratio)
                        )
                    )
                  ]
              ),
          )
        ),
      );

  }
}