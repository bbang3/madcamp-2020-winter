class Category {
  String _id;
  String name;
  int __v;

  Category();

  String getId() {
    return this._id;
  }

  setId(String id) {
    this._id = id;
  }
}

class RestaurantSpec{
  List<Menu> menus = [];
  List<Comment> comments = [];
  String name = "";
  String description = "";
  String telephone = "";
  bool confirmed = true;
  Location location = Location();
  List<OpeningHour> openingHours = [];
  String image;

  RestaurantSpec();

}

class Location {
  String fullAddress = "";
  String extraAddress = "";

  Location();
}

class OpeningHour {
  String openTime;
  String closeTime;

  OpeningHour();
}

class Restaurant {
  String _id;
  String name;
  String telephone;
  List<OpeningHour> openingHours;
  bool confirmed;
  String image;

  Restaurant();

  String getId() {
    return this._id;
  }

  setId(String id) {
    this._id = id;
  }
}

class Menu {
  String name;
  String description;
  List<PackageSize> sizes;
  String image;

  Menu();
}

class Comment {
  String _id;
  String userId;
  String nickname;
  String body;
  DateTime date;

  setId(String id) {
    this._id = id;
  }

  getId() {
    return this._id;
  }

  Comment();
}

class PackageSize {
  String size;
  int price;

  PackageSize();
}

class UserRegister {
  String username;
  String password;
  String nickname;
  String role;

  UserRegister(String username, String password, String nickname, String role) {
    this.username = username;
    this.password = password;
    this.nickname = nickname;
    this.role = role;
  }

  Map<String, dynamic> toJson() => {
    'username': this.username,
    'password': this.password,
    'nickname': this.nickname,
    'role': this.role
  };
}

class User{
  String username;
  String password;

  User(String username, String password) {
    this.username = username;
    this.password = password;
  }

  Map<String, dynamic> toJson() => {
    'username': this.username,
    'password': this.password
  };
}

class UserResponse{
  String _id;
  String username;
  String password;
  String nickname;
  String role;
  String accessToken;
  String refreshToken;
  bool confirmed;

  UserResponse();

  Map<String, dynamic> toJson() => {
    '_id': this._id,
    'username': this.username,
    'password': this.password,
    'nickname': this.nickname,
    'role': this.role,
    'confirmed': this.confirmed
  };

  setId(String id) {
    this._id = id;
  }

  getId() {
    return this._id;
  }

}
