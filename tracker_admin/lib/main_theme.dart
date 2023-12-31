import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

ThemeData get mainTheme => ThemeData(
  useMaterial3: true,
  colorScheme: ColorScheme.fromSeed(seedColor: Colors.green),
  appBarTheme: const AppBarTheme(
    centerTitle: true,
    systemOverlayStyle: SystemUiOverlayStyle(
      statusBarColor: Colors.white,
      statusBarIconBrightness: Brightness.dark,
      systemNavigationBarColor: Colors.white,
      systemNavigationBarIconBrightness: Brightness.dark,
    ),
    color: Colors.white,
    actionsIconTheme: IconThemeData(
      color: Colors.green,
    ),
    foregroundColor: Colors.green,
    surfaceTintColor: Colors.transparent,
  ),
  scaffoldBackgroundColor: Colors.white,
  inputDecorationTheme: InputDecorationTheme(
    filled: true,
    fillColor: Colors.green.withOpacity(0.1),
    border: OutlineInputBorder(
      borderSide: BorderSide.none,
      borderRadius: BorderRadius.circular(12)
    )
  ),
  listTileTheme: ListTileThemeData(
    shape: RoundedRectangleBorder(
      borderRadius: BorderRadius.circular(12),
    ),
    iconColor: Colors.green,
    tileColor: Colors.green.withOpacity(0.1),
  )
);