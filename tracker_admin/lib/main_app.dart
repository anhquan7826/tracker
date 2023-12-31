import 'package:flutter/material.dart';
import 'package:tracker_admin/main_theme.dart';
import 'package:tracker_admin/routes/routes.dart';

class TrackerAdmin extends StatelessWidget {
  const TrackerAdmin({super.key});
  @override
  Widget build(BuildContext context) {
    return MaterialApp.router(
      debugShowCheckedModeBanner: false,
      title: 'Flutter Demo',
      theme: mainTheme,
      routerConfig: routerConfig,
    );
  }
}
