import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:Madcamp_Week4/dio_server.dart';
import 'package:Madcamp_Week4/settingsDrawer.dart';
import 'package:intl/intl.dart';
import 'package:wheel_chooser/wheel_chooser.dart';
import 'package:Madcamp_Week4/restaurantPage.dart';
import 'package:Madcamp_Week4/secureStorage.dart';
import 'package:Madcamp_Week4/models.dart';

const _API_PREFIX = "http://192.249.18.244:8080";

class MainPage extends StatefulWidget {
  @override
  _MainPageState createState() => _MainPageState();
}

class _MainPageState extends State<MainPage> {
  List<Category> categoryList = [];
  List<String> categoryTitleList = [];
  Server server = Server();
  var secureStorage = SecureStorage();
  final GlobalKey<ScaffoldState> _scaffoldKey = GlobalKey<ScaffoldState>();
  int index;
  List<Restaurant> restaurantList = [];
  List<Restaurant> newRestaurantList = [];
  final myControllerSearch = TextEditingController();
  bool timeFilter = false;



  @override
  void initState() {

    server.getCategories()
        .then((value) {
          setState(() {
            this.categoryList = value;

            if (value.length > 0) {

              server.getRestaurants(value[0].getId())
                  .then((list) {
                    setState(() {
                      this.restaurantList = list;
                      this.newRestaurantList = list;
                    });
              });
            }


            this.categoryTitleList = value.map((i) => i.name).toList();
          });
        })
        .catchError((error) => print(error));

    super.initState();
  }
  
  refreshState() {
    server.getCategories()
        .then((value) {
      setState(() {
        this.categoryList = value;
        this.categoryTitleList = value.map((i) => i.name).toList();
      });
    })
        .catchError((error) => print(error));

  }

  onItemChanged(String value) {
    setState(() {
      this.newRestaurantList = this.restaurantList
          .where((i) => i.name.toLowerCase().contains(value.toLowerCase()))
          .toList();

      if (timeFilter) {
        List<Restaurant> temp = [];
        for (int i = 0; i < newRestaurantList.length; i++) {
          Restaurant restaurant = newRestaurantList[i];
          List<OpeningHour> openingHours = restaurant.openingHours;
          String time = DateFormat('hh:mm').format(DateTime.now());
          String date = DateFormat('EEEE').format(DateTime.now());
          int intDate = dateToInt(date);

          if (inTimeRange(time, openingHours[intDate].openTime,
              openingHours[intDate].closeTime)) {
            temp.add(restaurant);
          } else {
            intDate = (intDate - 1) % 7;
            if (timeComparison(openingHours[intDate].openTime,
                openingHours[intDate].closeTime) < 0
                && inTimeRange(time, openingHours[intDate].openTime,
                    openingHours[intDate].closeTime)) {
              temp.add(restaurant);
            }
          }
        }
        this.newRestaurantList = temp;
      }
    });
  }


  int dateToInt(String date) {
    if (date.compareTo('Sunday') == 0)
      return 0;
    else if (date.compareTo('Monday') == 0)
      return 1;
    else if (date.compareTo('Tuesday') == 0)
      return 2;
    else if (date.compareTo('Wednesday') == 0)
      return 3;
    else if (date.compareTo('Thursday') == 0)
      return 4;
    else if (date.compareTo('Friday') == 0)
      return 5;
    else if (date.compareTo('Saturday') == 0)
      return 6;
    else
      return -1;

  }

  bool inTimeRange(String time, String start, String end) {
    int compareResult = timeComparison(start, end);

    if (compareResult < 0) {
      if (timeComparison(time, start) < 0 || timeComparison(end, time) < 0) {
        return false;
      } else {
        return true;
      }
    } else if (compareResult > 0) {
      if (timeComparison(time, start) < 0 && timeComparison(end, time) < 0) {
        return false;
      } else {
        return true;
      }
    } else {
      return true;
    }
  }

  int timeComparison(String start, String end) {
    List<String> startParsed = start.split(":");
    List<String> endParsed = end.split(":");
    int startHr = int.parse(startParsed[0]);
    int startMin = int.parse(startParsed[1]);
    int endHr = int.parse(endParsed[0]);
    int endMin = int.parse(endParsed[1]);

    if (startHr < endHr) {
      return -1;
    } else if (startHr == endHr) {
      if (startMin < endMin) {
        return -1;
      } else if (startMin == endMin) {
        return 0;
      } else {
        return 1;
      }
    } else {
      return 1;
    }
  }


  @override
  Widget build(BuildContext context) {
    double ratio = MediaQuery.of(context).size.width / 540;

    return Container(
        color: Colors.white,
        child: Scaffold(
          backgroundColor: Colors.white,
          key: _scaffoldKey,
            appBar: PreferredSize(
              preferredSize: Size.fromHeight(60 * ratio),
              child: AppBar(
                backgroundColor: Colors.white,
                brightness: Brightness.light,
                elevation: 0,
                actions: <Widget>[
                  IconButton(
                    splashColor: Colors.transparent,
                      padding: EdgeInsets.all(20 * ratio),
                      icon: Icon(Icons.menu, color: Colors.black87),
                      onPressed: () => _scaffoldKey.currentState.openEndDrawer()
                  )
                ]
              ),
            ),
            endDrawer: SettingsDrawer(context, null),
            body: Container(
                child: Column(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: <Widget>[
                      Container(
                        width: double.infinity,
                        decoration: BoxDecoration(
                          color: Colors.white,
                          borderRadius: BorderRadius.vertical(
                            bottom:Radius.circular(40 * ratio)
                          ),
                        ),
                        padding: EdgeInsets.only(left: 20 * ratio, right: 20 * ratio, bottom: 40*ratio),
                        child: Row(
                          children: <Widget>[
                            SizedBox(width: 60 * ratio),
                            Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: <Widget> [
                                Text("오늘", style: TextStyle(color: Colors.black87, fontSize:36 * ratio)),
                                SizedBox(height: 30 * ratio),
                                Text(
                                      "뭐 먹지?",
                                      style: TextStyle(
                                          color: Colors.black87,
                                          fontSize: 45 * ratio,
                                          fontWeight: FontWeight.w600
                                      )
                                  ),
                                SizedBox(height: 15 * ratio)
                              ]
                            ),
                            SizedBox(width: 20 * ratio),
                            Column(
                              children:<Widget>[
                                SizedBox(height: 20 *ratio),
                                Container(
                                    width: 250 * ratio,
                                    height: 200 * ratio,
                                    child: this.categoryTitleList.length == 0
                                        ? null
                                        : WheelChooser(
                                      datas: this.categoryTitleList,
                                      itemSize: 40 * ratio,
                                      magnification: 1.35,
                                      squeeze: 0.5,
                                      onValueChanged: (i) {
                                        myControllerSearch.clear();
                                        setState(() {
                                          this.timeFilter = false;
                                          this.index = this.categoryTitleList.indexOf(i);
                                          String categoryId = this.categoryList[index].getId();
                                          server.getRestaurants(categoryId)
                                              .then((value) {
                                            setState(() {
                                              this.restaurantList = value;
                                              if (value.length != 0) {

                                              }
                                              this.newRestaurantList = value;
                                            });
                                          }).catchError((error) => print(error));
                                        });
                                      },
                                      selectTextStyle: TextStyle(
                                          fontWeight: FontWeight.w500
                                      ),
                                      unSelectTextStyle: TextStyle(
                                          color: Colors.black38
                                      ),
                                    )
                                )
                              ]
                            )

                          ]
                        )
                      ),
                      Container(
                        margin: EdgeInsets.symmetric(vertical: 0, horizontal: 50 * ratio),
                        width: double.infinity,
                        height: 55 * ratio,
                        child: Row(
                          children: <Widget>[
                            Flexible(
                              child: TextField(
                                  onChanged: onItemChanged,
                                  controller: myControllerSearch,
                                  decoration: InputDecoration(
                                    prefixIcon: Icon(Icons.search),
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
                            SizedBox(width: 10 * ratio),
                            Container(
                              height: 60 * ratio,
                              width: 60 * ratio,
                              child: InkWell(
                                  onTap: () {
                                    setState(() {
                                      String value = myControllerSearch.text;
                                      this.timeFilter = !this.timeFilter;
                                      onItemChanged(value);
                                    });
                                  },
                                  child: Icon(
                                      Icons.access_time,
                                      color: this.timeFilter? Colors.black87: Colors.grey,
                                      size: 35 * ratio
                                  ),
                                  borderRadius: BorderRadius.circular(50 * ratio)
                              )
                            )
                          ]
                        )
                      ),
                      Expanded(
                          child: ListView.builder(
                              physics: ClampingScrollPhysics(),
                              itemCount: newRestaurantList.length,
                              scrollDirection: Axis.vertical,
                              itemBuilder: (BuildContext context, int index) {
                                return

                                  GestureDetector(
                                    onTap: () {
                                      Navigator.push(
                                          context,
                                          MaterialPageRoute(
                                              builder: (context) => RestaurantPage(restaurantId: newRestaurantList[index].getId())
                                          )
                                      ).then((value) => refreshState());
                                    },

                                    child:
                                        Container(
                                          padding: EdgeInsets.symmetric(horizontal: 70 * ratio, vertical: 40 * ratio),
                                          child: Column(
                                            children: [
                                              SizedBox(
                                                  height: index == 0
                                                      ? 40 * ratio
                                                      : 0
                                              ),
                                              Row(
                                                children: [
                                                  newRestaurantList[index].image == null
                                                      ? Image(image: AssetImage('assets/restaurant.png'), fit: BoxFit.contain, width: 130*ratio, height: 130*ratio)
                                                  // Image.network("https://makitweb.com/demo/broken_image/images/noimage.png", fit: BoxFit.cover, width: 130*ratio, height: 130 * ratio)
                                                      : Image.network("$_API_PREFIX/image/restaurants/${newRestaurantList[index].image}", fit: BoxFit.cover, width: 130*ratio, height: 130 * ratio),
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

                                                              Container(
                                                                width: 200*ratio,
                                                                child: Text(
                                                                    newRestaurantList[index].name,
                                                                    style: TextStyle(
                                                                        fontSize: 25 * ratio
                                                                    )
                                                                ),
                                                              ),
                                                              SizedBox(height: 15*ratio),
                                                              Text(
                                                                  newRestaurantList[index].telephone,
                                                                  style: TextStyle(
                                                                      fontSize: 16 * ratio,
                                                                      color: Colors.black45
                                                                  )
                                                              )
                                                            ],
                                                          )
                                                        ]
                                                      )
                                                  ),
                                                ],
                                              ),
                                              SizedBox(
                                                  height: index == newRestaurantList.length - 1
                                                      ? 10 * ratio
                                                      : 0
                                              ),
                                            ],
                                          ),
                                        )
                                );

                              }
                          )
                      )
            ])
        )
    ));
  }
}



