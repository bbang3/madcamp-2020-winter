import 'package:flutter/material.dart';
import 'dio_server.dart';
import 'settingsDrawer.dart';
import 'package:Madcamp_Week4/models.dart';

const _API_PREFIX = "http://192.249.18.244:8080";

class MenuPage extends StatefulWidget {
  String name = "";
  String description = "";
  List<PackageSize> sizes = [];
  String image;

  MenuPage(String name, String description, List<PackageSize> sizes, String image) {
    this.name = name;
    this.description = description;
    this.sizes = sizes;
    this.image = image;
  }

  @override
  _MenuPageState createState() => _MenuPageState();
}

class _MenuPageState extends State<MenuPage> {
  final GlobalKey<ScaffoldState> _scaffoldKey = GlobalKey<ScaffoldState>();

  @override
  Widget build(BuildContext context) {
    double ratio = MediaQuery.of(context).size.width / 540;

    return Scaffold(
      backgroundColor: Colors.white,
      key: _scaffoldKey,
      appBar: PreferredSize(
        preferredSize: Size.fromHeight(75* ratio),
        child: AppBar(
          elevation: 0,
          brightness: Brightness.light,
          leading: IconButton(
              padding: EdgeInsets.all(20* ratio),
              icon: Icon(Icons.arrow_back, color: Colors.black87),
              onPressed: () {
                Navigator.pop(context);
              }
          ),
          actions: <Widget>[
            IconButton(
              splashColor: Colors.transparent,
                padding: EdgeInsets.all(20* ratio),
                icon: Icon(Icons.menu, color: Colors.black87),
                onPressed: () => _scaffoldKey.currentState.openEndDrawer()
            ),
          ],
          backgroundColor: Colors.white,
        ),
      ),
      endDrawer: SettingsDrawer(context, null),
      body: SingleChildScrollView(
        child: Column(
          children: <Widget>[
            widget.image == null
                ? Image(image: AssetImage('assets/egg.png'), fit: BoxFit.contain, width: double.infinity, height: 300*ratio)
            //Image.network("https://makitweb.com/demo/broken_image/images/noimage.png", fit: BoxFit.cover, width: double.infinity, height: 300*ratio)
                : Image.network("$_API_PREFIX/image/menus/${widget.image}", fit: BoxFit.cover, width: double.infinity, height: 300* ratio),
            SizedBox(height: 50* ratio),
            Row(
                children: <Widget>[
                  SizedBox(width: 40* ratio),
                  Text(
                      widget.name,
                      style: TextStyle(
                          fontSize: 45* ratio,
                          fontWeight: FontWeight.bold
                      )
                  ),
                  SizedBox(width: 40* ratio),
                  Expanded(
                    child: Text(
                      widget.description,
                      style: TextStyle(
                        fontSize: 18* ratio
                      )
                    )
                  ),
                  SizedBox(width: 40* ratio)
                ]
            ),
            SizedBox(height: 50* ratio),
            Flex(
              direction: Axis.vertical,
              children: <Widget>[
                ListView.builder(
                    itemCount: widget.sizes.length,
                    scrollDirection: Axis.vertical,
                    physics: ClampingScrollPhysics(),
                    shrinkWrap: true,
                    itemBuilder: (BuildContext context, int index) {
                      return Container(
                            padding: EdgeInsets.symmetric(vertical: 10 * ratio, horizontal: 0),
                            width: 200 * ratio,
                            height: 130*ratio,
                            child: Row(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                mainAxisAlignment: MainAxisAlignment.start,
                                children: <Widget>[
                                  SizedBox(width: 50*ratio),
                                  Column(
                                    crossAxisAlignment: CrossAxisAlignment.start,
                                    children: [
                                      Text(
                                          widget.sizes[index].size,
                                          style: TextStyle(
                                              fontSize: 20* ratio
                                          )
                                      ),
                                      SizedBox(height: 15*ratio),
                                      Container(
                                        width: 200 *ratio,
                                        child: Text(
                                            "${widget.sizes[index].price.toString()}원",
                                            style: TextStyle(
                                                fontSize: 16* ratio
                                            )
                                        ),
                                      )
                                    ],
                                  )
                                ]
                            )
                        );

                      /*
                        Column(
                              children: <Widget>[
                                Container(
                                    padding: EdgeInsets.symmetric(vertical: 0* ratio, horizontal: 0),
                                    child: ListTile(
                                      //leading: SizedBox(width:10*ratio),
                                      title: Text(
                                          widget.sizes[index].size,
                                          style: TextStyle(
                                              fontSize: 20* ratio
                                          )
                                      ),
                                      subtitle: Text(
                                          "${widget.sizes[index].price.toString()}원",
                                          style: TextStyle(
                                              fontSize: 16* ratio
                                          )
                                      ),
                                      visualDensity: VisualDensity(
                                          vertical: 1
                                      ),
                                    )
                                ),
                              ]

                      );
                      */
                    }
                )
              ],
            ),
            SizedBox(height: 30* ratio)
          ],
        )
      )
    );
  }
}
