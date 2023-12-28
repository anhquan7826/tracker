import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';
import 'package:tracker_admin/app/device/device.cubit.dart';
import 'package:tracker_admin/app/device/device.state.dart';
import 'package:tracker_admin/model/device.model.dart';
import 'package:tracker_admin/model/user.model.dart';
import 'package:tracker_admin/routes/routes.dart';

class DeviceScreen extends StatefulWidget {
  const DeviceScreen({super.key, required this.user});

  final TrackedUser user;

  @override
  State<DeviceScreen> createState() => _DeviceScreenState();
}

class _DeviceScreenState extends State<DeviceScreen> {
  List<TrackedDevice> devices = [];
  TrackedDevice? newDevice;

  DeviceCubit get cubit => BlocProvider.of(context);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.user.email),
      ),
      body: BlocConsumer<DeviceCubit, DeviceState>(
        listener: (context, state) {
          if (state is DeviceLoadedState) {
            devices = state.devices;
          }
          if (state is NewDeviceAddedState) {
            newDevice = state.device;
            devices.add(state.device);
          }
        },
        builder: (context, state) {
          if (state is DeviceLoadingState) {
            return const Center(
              child: CircularProgressIndicator(),
            );
          }
          if (state is DeviceErrorState) {
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
              children: devices.map((e) => buildDeviceList(e)).toList(),
            ),
          );
        },
      ),
    );
  }

  Widget buildDeviceList(TrackedDevice device) {
    return Padding(
      padding: const EdgeInsets.only(top: 8),
      child: ListTile(
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(12),
        ),
        leading: Icon(
          Icons.phone_android_outlined,
          weight: 200,
          color: device == newDevice ? Colors.deepOrangeAccent : null,
        ),
        title: Text(device.name),
        subtitle: Text(device.id),
        onTap: () {
          context.pushNamed(
            routes.history,
            pathParameters: {
              'uid': widget.user.id,
              'deviceId': device.id,
            },
            extra: device,
          );
        },
      ),
    );
  }
}
