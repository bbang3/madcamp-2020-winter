import 'dart:convert';
import 'package:Madcamp_Week4/secureStorage.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:dio/dio.dart';
import 'package:Madcamp_Week4/models.dart';

const _API_PREFIX = "http://192.249.18.244:8080";

class Server{
  final storage = FlutterSecureStorage();


  Category fromJsonCategory(dynamic json) {
    Category category = Category();

    category.setId(json['_id'] as String);
    category.name = json['name'] as String;

    return category;
  }


  Restaurant fromJsonRestaurant(dynamic json) {

    Restaurant restaurant = Restaurant();

    restaurant.setId(json['_id'] as String);
    restaurant.name = json['name'] as String;
    restaurant.telephone = json['telephone'] as String;
    restaurant.confirmed = json['confirmed'] as bool;
    restaurant.image = json['image'] as String;

    List stringOpeningHours = json['openingHours'];

    List<OpeningHour> openingHours = [];

    if (stringOpeningHours != null) {
      for (int i = 0; i < stringOpeningHours.length; i++) {
        OpeningHour openingHour = OpeningHour();
        var element = stringOpeningHours[i];
        openingHour.openTime = element['openTime'];
        openingHour.closeTime = element['closeTime'];
        openingHours.add(openingHour);
      }
    }

    restaurant.openingHours = openingHours;

    return restaurant;
  }

  RestaurantSpec fromJsonRestaurantSpec(dynamic json) {
    RestaurantSpec restaurantSpec = RestaurantSpec();

    List stringMenus = json['menus'];

    List<Menu> menus = [];

    if (stringMenus != null) {
      for (int i = 0; i < stringMenus.length; i++) {
        Menu menu = Menu();
        var element = stringMenus[i];
        menu.name = element['name'];
        menu.description = element['description'];
        List<dynamic> stringSizes = element['sizes'];
        List<PackageSize> sizes = [];
        for (int j = 0; j < stringSizes.length; j++) {
          PackageSize packageSize = PackageSize();
          var size = stringSizes[j];
          packageSize.size = size['size'];
          packageSize.price = size['price'];
          sizes.add(packageSize);
        }
        menu.sizes = sizes;
        menu.image = element['image'];
        menus.add(menu);
      }
    }

    restaurantSpec.menus = menus;

    List stringComments = json['comments'];

    List<Comment> comments = [];

    if (stringComments != null) {
      for (int i = 0; i < stringComments.length; i++) {
        Comment comment = Comment();
        var element = stringComments[i];
        comment = fromJsonComment(element);

        comments.add(comment);
      }
    }

    restaurantSpec.comments = comments;

    List stringOpeningHours = json['openingHours'];

    List<OpeningHour> openingHours = [];

    if (stringOpeningHours != null) {
      for (int i = 0; i < stringOpeningHours.length; i++) {
        OpeningHour openingHour = OpeningHour();
        var element = stringOpeningHours[i];
        openingHour.openTime = element['openTime'];
        openingHour.closeTime = element['closeTime'];
        openingHours.add(openingHour);
      }
    }

    restaurantSpec.openingHours = openingHours;

    var stringLocation = json['location'];

    Location location = Location();

    location.fullAddress = stringLocation['fullAddress'];
    location.extraAddress = stringLocation['extraAddress'];

    restaurantSpec.location = location;

    restaurantSpec.name = json['name'] as String;

    restaurantSpec.description = json['description'] as String;

    restaurantSpec.telephone = json['telephone'] as String;

    restaurantSpec.image = json['image'] as String;

    return restaurantSpec;

  }


  Menu fromJsonMenu(dynamic json) {
    Menu menu = Menu();
    String sizesJson = json['size'];

    menu.name = json['name'] as String;
    menu.description = json['description'] as String;
    menu.sizes = fromJsonPackageSize(json['size']) as List<PackageSize>;
    menu.image = json['image'] as String;

    return Menu();
  }


  PackageSize fromJsonPackageSize(dynamic json) {
    PackageSize packageSize = PackageSize();

    packageSize.size = json['size'] as String;
    packageSize.price = json['price'] as int;

    return packageSize;
  }


  UserResponse fromJsonUserResponse(dynamic json) {
    UserResponse userResponse = UserResponse();

    userResponse.setId(json['_id'] as String);
    userResponse.username = json['username'] as String;
    userResponse.nickname = json['nickname'] as String;
    userResponse.role = json['role'] as String;
    userResponse.accessToken = json['accessToken'] as String;
    userResponse.refreshToken = json['refreshToken'] as String;

    return userResponse;
  }

  Comment fromJsonComment(dynamic json) {
    Comment comment = Comment();

    comment.setId(json['_id'] as String);
    comment.userId = json['userId'] as String;
    comment.nickname = json['nickname'] as String;
    String dateString = json['date'] as String;
    String formattedString = dateString.replaceFirst("T", " ").substring(0,19);
    DateTime date = DateTime.parse(formattedString).add(Duration(hours: 9));
    comment.date = date;
    comment.body = json['body'] as String;

    return comment;
  }


  Future<List<Category>> getCategories() async {
    Response response;
    Dio dio = new Dio();
    response = await dio.get("$_API_PREFIX/category");
    List<Category> categories = [];
    List<dynamic> responseList = response.data as List;

    for(int i = 0; i < responseList.length; i++) {
      categories.add(fromJsonCategory(responseList[i]));
    }

    return categories;
  }


  Future<List<Restaurant>> getRestaurants(String categoryId) async {
    Response response;
    Dio dio = new Dio();
    response = await dio.get("$_API_PREFIX/restaurant?category=$categoryId");

    List<Restaurant> restaurants = [];
    List<dynamic> responseList = response.data as List;

    for (int i = 0; i < responseList.length; i++) {
      var restaurant = fromJsonRestaurant(responseList[i]);
      if (restaurant.confirmed) {
        restaurants.add(restaurant);
      }
    }

    return restaurants;
  }


  Future<RestaurantSpec> getRestaurant(String restaurantId) async {
    Response response;
    Dio dio = Dio();
    response = await dio.get("$_API_PREFIX/restaurant/$restaurantId");

    var restaurant = fromJsonRestaurantSpec(response.data);

    return restaurant;

  }


  Future<int> postRegister(UserRegister user) async {

    Response response;
    Dio dio = new Dio();

    try {
      response = await dio.post(
          "$_API_PREFIX/auth/register", data: jsonEncode(user.toJson()));
    } catch (e) {
      if (e is DioError) {
        return e.response.statusCode;
      }
    }

    return response.statusCode;
  }


  Future<int> postLogIn(User user) async {
    Response response;
    Dio dio = Dio();

    try {
      response = await dio.post(
          "$_API_PREFIX/auth/login", data: jsonEncode(user.toJson()));

      await storage.write(key: '_id', value: fromJsonUserResponse(response.data).getId());
      await storage.write(key: 'username', value: fromJsonUserResponse(response.data).username);
      await storage.write(key: 'access_token', value: fromJsonUserResponse(response.data).accessToken);
      await storage.write(key: 'refresh_token', value: fromJsonUserResponse(response.data).refreshToken);
      await storage.write(key: 'nickname', value: fromJsonUserResponse(response.data).nickname);
      await storage.write(key: 'role', value: fromJsonUserResponse(response.data).role);
      await storage.write(key: 'password', value: user.password);

    } catch (e) {
      if (e is DioError) {
        return e.response.statusCode;
      }
    }

    return response.statusCode;
  }


  Future<int> verifyAccessToken(String accessToken) async {
    Response response;
    Dio dio = Dio(BaseOptions(
      headers: {
        'token': accessToken
      }
    ));

    try {
      response = await dio.post(
          "$_API_PREFIX/auth/check-token");

    } catch (e) {
      if (e is DioError) {
        return e.response.statusCode;
      }
    }

    return response.statusCode;
  }


  Future<String> refreshAccessToken(String refreshToken) async {
    Response response;
    Dio dio = Dio(BaseOptions(
      headers: {
        'refresh-token': refreshToken
      }
    ));


    try {
      response = await dio.post(
          "$_API_PREFIX/auth/refresh-token");

    } catch (e) {
      if (e is DioError) {
        print(e);
      }
    }

    String accessToken = response.data['accessToken'];
    if (accessToken == null) {
      // refreshToken도 만료된 경우
    } else {
      SecureStorage secureStorage = SecureStorage();
      secureStorage.saveAccessTokenToStorage(accessToken).then((status) {
        return accessToken;
      });
      return accessToken;
    }

  }


  Future<int> updateUser(Map<String, String> map, String _id, String accessToken) async {
    Response response;
    Dio dio = Dio(BaseOptions(
      headers: {
        'token': accessToken
      }
    ));

    try {
      response = await dio.put(
        "$_API_PREFIX/user/$_id", data: map
      ) ;
      return response.statusCode;
    } catch (e) {
      if (e is DioError) {
        return e.response.statusCode;
      }
    }
  }


  Future<List<Comment>> getComments(String restaurantId) async {
    Response response;
    Dio dio = Dio();
    List<dynamic> responseList = [];
    List<Comment> comments = [];

    try {
      response = await dio.get(
        "$_API_PREFIX/restaurant/$restaurantId/comment"
      );
      responseList = response.data as List;

      for (int i = 0; i < responseList.length; i++) {
        Comment comment = fromJsonComment(responseList[i]);
        comments.add(comment);
      }
    } catch (e) {
      if (e is DioError) {

      }
    }

    return comments;
  }

  Future<int> postComment(String restaurantId, String comment, String accessToken) async{
    Response response;
    Dio dio = Dio(
      BaseOptions(
        headers: {
          'token': accessToken
        }
      )
    );

    Map<String, String> map = {"body": comment};

    try {
      response = await dio.post(
        "$_API_PREFIX/restaurant/$restaurantId/comment", data: map
      );
      return response.statusCode;
    } catch (e) {
      if (e is DioError) {
        return e.response.statusCode;
      }
    }
  }

  Future<int> deleteComment(String restaurantId, String commentId, String accessToken) async{
    Response response;
    Dio dio = Dio(
      BaseOptions(
        headers: {
          'token': accessToken
        }
      )
    );

    try{
      response = await dio.delete(
        "$_API_PREFIX/restaurant/$restaurantId/comment/$commentId"
      );
      return response.statusCode;
    } catch (e) {
      if (e is DioError) {
        return e.response.statusCode;
      }
    }
  }

  Future<int> updateComment(String comment, String restaurantId, String commentId, String accessToken) async{
    Response response;
    Dio dio = Dio(
      BaseOptions(
        headers: {
          'token': accessToken
        }
      )
    );

    Map<String, String> map = {"body": comment};

    try{
      response = await dio.put(
          "$_API_PREFIX/restaurant/$restaurantId/comment/$commentId", data: map
      );
      return response.statusCode;
    } catch (e) {
      if (e is DioError) {
        return e.response.statusCode;
      }
    }
  }
}


