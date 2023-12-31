import 'package:flutter/services.dart';

class ServiceUtil {
  ServiceUtil._();

  static const _channel = MethodChannel('com.anhquan.tracker_admin/service');

  static Future<void> startService() async {
    await _channel.invokeMethod('start_service');
  }

  static Future<void> stopService() async {
    await _channel.invokeMethod('stop_service');
  }

  static Future<void> restartService() async {
    await _channel.invokeMethod('restart_service');
  }
}