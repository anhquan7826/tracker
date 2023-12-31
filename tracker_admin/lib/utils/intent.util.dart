import 'package:flutter/services.dart';

class IntentUtil {
  IntentUtil._();

  static const _channel = MethodChannel('com.anhquan.tracker_admin/intent');

  static Future<void> openMap(String title, {required double lat, required double lon}) async {
    await _channel.invokeMethod('open_map', {'lat': lat, 'lon': lon, 'title': title});
  }
}
