import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';
import 'package:tracker_admin/app/device/device.cubit.dart';
import 'package:tracker_admin/app/device/device.view.dart';
import 'package:tracker_admin/app/history/history.cubit.dart';
import 'package:tracker_admin/app/history/history.view.dart';
import 'package:tracker_admin/app/home/home.cubit.dart';
import 'package:tracker_admin/app/home/home.view.dart';
import 'package:tracker_admin/app/login/login.cubit.dart';
import 'package:tracker_admin/app/login/login.view.dart';
import 'package:tracker_admin/app/splash/splash.view.dart';
import 'package:tracker_admin/model/device.model.dart';
import 'package:tracker_admin/model/user.model.dart';

const routes = (
  splash: 'splash',
  login: 'login',
  users: 'users',
  devices: 'devices',
  history: 'history',
);

final routerConfig = GoRouter(
  initialLocation: '/splash',
  routes: [
    GoRoute(
      name: routes.splash,
      path: '/splash',
      builder: (context, state) {
        return const SplashScreen();
      },
    ),
    GoRoute(
      name: routes.login,
      path: '/login',
      builder: (context, state) {
        return BlocProvider<LoginCubit>(
          create: (context) => LoginCubit(),
          child: const LoginScreen(),
        );
      },
    ),
    GoRoute(
        name: routes.users,
        path: '/users',
        builder: (context, state) {
          return BlocProvider<HomeCubit>(
            create: (context) => HomeCubit(),
            child: const HomeScreen(),
          );
        },
        routes: [
          GoRoute(
            name: routes.devices,
            path: ':uid/devices',
            builder: (context, state) {
              final user = state.extra as TrackedUser;
              return BlocProvider<DeviceCubit>(
                create: (context) => DeviceCubit(state.pathParameters['uid'].toString()),
                child: DeviceScreen(user: user),
              );
            },
            routes: [
              GoRoute(
                  name: routes.history,
                  path: ':deviceId/history',
                  builder: (context, state) {
                    final uid = state.pathParameters['uid'].toString();
                    final deviceId = state.pathParameters['deviceId'].toString();
                    return BlocProvider<HistoryCubit>(
                      create: (context) => HistoryCubit(uid: uid, deviceId: deviceId),
                      child: HistoryScreen(device: state.extra as TrackedDevice),
                    );
                  }),
            ],
          ),
        ]),
  ],
);
