import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:go_router/go_router.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:tracker_admin/app/home/home.cubit.dart';
import 'package:tracker_admin/app/home/home.state.dart';
import 'package:tracker_admin/routes/routes.dart';
import 'package:tracker_admin/utils/service.util.dart';

import '../../model/user.model.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  List<TrackedUser> users = [];
  TrackedUser? newUser;

  HomeCubit get cubit => BlocProvider.of(context);

  @override
  void initState() {
    super.initState();
    void permissionDeniedToast() {
      Fluttertoast.showToast(msg: 'Vui lòng cấp quyền thông báo để nhận thông báo!');
    }
    Permission.notification.onDeniedCallback(() {
      permissionDeniedToast();
    }).onPermanentlyDeniedCallback(() {
      permissionDeniedToast();
    }).onGrantedCallback(() {
      ServiceUtil.startService();
    }).request();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Tất cả người dùng'),
        actions: [
          IconButton(onPressed: () {
            context.pushNamed(routes.settings);
          }, icon: const Icon(Icons.settings),),
        ],
      ),
      body: BlocConsumer<HomeCubit, HomeState>(
        listener: (context, state) {
          if (state is HomeLoadedState) {
            users = state.users;
          }
          if (state is NewUserAddedState) {
            newUser = state.user;
            users.add(state.user);
          }
        },
        builder: (context, state) {
          if (state is HomeLoadingState) {
            return const Center(
              child: CircularProgressIndicator(),
            );
          }
          if (state is HomeErrorState) {
            return Padding(
              padding: const EdgeInsets.symmetric(horizontal: 16),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  const Padding(
                    padding: EdgeInsets.only(bottom: 8),
                    child: Text(
                      'Không thể tải dữ liệu!',
                      style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
                      textAlign: TextAlign.center,
                    ),
                  ),
                  if (state.error != null)
                    Text(
                      state.error!,
                      textAlign: TextAlign.center,
                      style: const TextStyle(
                        color: Colors.grey,
                      ),
                    ),
                  Padding(
                    padding: const EdgeInsets.only(top: 8),
                    child: FilledButton(
                      onPressed: () => cubit.load(),
                      child: const Text('Tải lại'),
                    ),
                  ),
                ],
              ),
            );
          }
          return Padding(
            padding: const EdgeInsets.symmetric(horizontal: 16),
            child: ListView(
              children: users.map((e) => buildUserList(e)).toList(),
            ),
          );
        },
      ),
    );
  }

  Widget buildUserList(TrackedUser user) {
    return Padding(
      padding: const EdgeInsets.only(top: 8),
      child: ListTile(
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(12),
        ),
        leading: Icon(
          Icons.person_outline_rounded,
          weight: 200,
          color: user == newUser ? Colors.deepOrangeAccent : null,
        ),
        title: Text(user.email),
        subtitle: Text(user.id),
        onTap: () {
          context.pushNamed(
            routes.devices,
            pathParameters: {
              'uid': user.id,
            },
            extra: user,
          );
        },
      ),
    );
  }
}
