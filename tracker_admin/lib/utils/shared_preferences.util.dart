import 'package:flutter/services.dart';

class SharedPreferences {
  SharedPreferences._();

  static const _channel = MethodChannel('com.anhquan.tracker_admin/spf');

  static Future<String?> getString(String key) async {
    return (await _channel.invokeMethod('get_string', {'key': key})).toString();
  }

  static Future<void> putString(String key, {required String? value}) async {
    await _channel.invokeMethod('put_string', {'key': key, 'value': value});
  }

  static Future<bool?> getBool(String key) async {
    return (await _channel.invokeMethod('get_bool', {'key': key})) as bool?;
  }

  static Future<void> putBool(String key, {required bool? value}) async {
    await _channel.invokeMethod('put_bool', {'key': key, 'value': value});
  }

  static Future<int?> getInt(String key) async {
    return (await _channel.invokeMethod('get_int', {'key': key})) as int?;
  }

  static Future<void> putInt(String key, {required int? value}) async {
    await _channel.invokeMethod('put_int', {'key': key, 'value': value});
  }
}
