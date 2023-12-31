import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';
import 'package:tracker_admin/app/settings/settings.cubit.dart';
import 'package:tracker_admin/app/settings/settings.state.dart';
import 'package:tracker_admin/routes/routes.dart';

class SettingsScreen extends StatefulWidget {
  const SettingsScreen({super.key});

  @override
  State<SettingsScreen> createState() => _SettingsScreenState();
}

class _SettingsScreenState extends State<SettingsScreen> {
  String notificationState = 'all';

  final distanceController = TextEditingController();

  SettingsCubit get cubit => BlocProvider.of(context);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Cài đặt'),
      ),
      body: BlocListener<SettingsCubit, SettingsState>(
        listener: (context, state) {
          if (state is NotificationChangedState) {
            setState(() {
              notificationState = state.state;
              distanceController.text = state.distance.toString();
            });
          }
        },
        child: ListView(
          children: [
            ListTile(
              leading: const Icon(Icons.notifications),
              isThreeLine: notificationState == 'partial',
              title: const Text('Nhận thông báo'),
              subtitle: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  if (notificationState == 'partial')
                    const Text('Nhận thông báo khi thiết bị di chuyển:')
                  else if (notificationState == 'all')
                    const Text('Nhận thông báo sau 5 phút')
                  else
                    const Text('Không nhận thông báo'),
                  if (notificationState == 'partial')
                    TextField(
                      controller: distanceController,
                      decoration: const InputDecoration(isDense: true, suffixText: 'Mét'),
                      onChanged: (value) {
                        cubit.distance = int.parse(value);
                      },
                      keyboardType: const TextInputType.numberWithOptions(
                        signed: true,
                      ),
                      textInputAction: TextInputAction.done,
                    ),
                ],
              ),
              trailing: Checkbox(
                tristate: true,
                value: notificationState == 'all'
                    ? true
                    : notificationState == 'partial'
                        ? null
                        : false,
                onChanged: (value) {
                  changeNotificationState();
                },
              ),
              onTap: changeNotificationState,
            ),
            ListTile(
              leading: const Icon(
                Icons.logout,
                color: Colors.red,
              ),
              title: const Text('Đăng xuất'),
              onTap: () {
                FirebaseAuth.instance.signOut().whenComplete(() {
                  context.goNamed(routes.login);
                });
              },
            ),
          ],
        ),
      ),
    );
  }

  void changeNotificationState() {
    notificationState = notificationState == 'partial'
        ? 'none'
        : notificationState == 'all'
            ? 'partial'
            : 'all';
    setState(() {
      cubit.notificationState = notificationState;
    });
  }
}
