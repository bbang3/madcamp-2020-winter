import 'package:flutter/material.dart';
import 'package:Madcamp_Week4/dio_server.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

class SecureStorage {
  final storage = FlutterSecureStorage();
  final server = Server();


  Future<Map<String, String>> readAllFromStorage() async {
    Map<String,String> value = await storage.readAll();

    return value;
  }


  Future<int> deleteAllFromStorage() async {
    await storage.deleteAll();
    return 0;
  }

  Future<int> saveAccessTokenToStorage(String accessToken) async {
    await storage.write(key: "access_token", value: accessToken);
    return 0;
  }

  Future<int> saveNicknameToStorage(String nickname) async {
    await storage.write(key: "nickname", value: nickname);
    return 0;
  }
}