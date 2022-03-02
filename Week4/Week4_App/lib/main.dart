import 'package:flutter/material.dart';
import 'package:Madcamp_Week4/mainPage.dart';
import 'package:Madcamp_Week4/loginPage.dart';
import 'package:Madcamp_Week4/dio_server.dart';
import 'package:Madcamp_Week4/settingsDrawer.dart';
import 'package:Madcamp_Week4/restaurantPage.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build (BuildContext context) {

    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Madcamp Week 4',
      theme: ThemeData(primarySwatch: Colors.blue),
      home: MainPage(),

      builder: (context, child) {
        return ScrollConfiguration(
          behavior: MyBehavior(),
          child: child
        );
      }
    );
  }
}

class MyBehavior extends ScrollBehavior {
  @override
  Widget buildViewportChrome(
      BuildContext context, Widget child, AxisDirection axisDirection) {
    return child;
  }
}








