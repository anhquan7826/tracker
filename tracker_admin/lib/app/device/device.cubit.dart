import 'dart:async';

import 'package:firebase_database/firebase_database.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:tracker_admin/app/device/device.state.dart';
import 'package:tracker_admin/model/device.model.dart';

class DeviceCubit extends Cubit<DeviceState> {
  DeviceCubit(this.uid) : super(const DeviceLoadingState()) {
    load();
  }

  final String uid;

  Map<String, TrackedDevice> devices = {};

  StreamSubscription<DatabaseEvent>? _listener;

  Future<void> load() async {
    try {
      await _listener?.cancel();
      emit(const DeviceLoadingState());
      final usersReference = FirebaseDatabase.instance.ref('$uid/devices');
      final usersSnapshot = await usersReference.get();
      devices = Map.fromEntries(usersSnapshot.children.map((deviceSnapshot) {
        return TrackedDevice(
          deviceSnapshot.key!,
          name: deviceSnapshot.child('name').value.toString(),
        );
      }).map((e) => MapEntry(e.id, e)));
      _startListener(usersReference);
      emit(DeviceLoadedState(devices.values.toList()));
    } catch (_) {
      emit(DeviceErrorState(_.toString()));
      rethrow;
    }
  }

  void _startListener(DatabaseReference ref) {
    _listener = ref.onChildAdded.listen((event) {
      final device = TrackedDevice(
        event.snapshot.key!,
        name: event.snapshot.child('name').toString(),
      );
      if (!devices.containsKey(device.id)) {
        devices[device.id] = device;
        emit(NewDeviceAddedState(device));
      }
    });
  }

  @override
  Future<void> close() async {
    await _listener?.cancel();
    return super.close();
  }
}
