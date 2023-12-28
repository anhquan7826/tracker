import 'package:equatable/equatable.dart';
import 'package:tracker_admin/model/device.model.dart';

abstract class DeviceState extends Equatable {
  const DeviceState();
}

class DeviceLoadingState extends DeviceState {
  const DeviceLoadingState();

  @override
  List<Object?> get props => [];
}

class DeviceLoadedState extends DeviceState {
  const DeviceLoadedState(this.devices);

  final List<TrackedDevice> devices;

  @override
  List<Object?> get props => [devices];
}

class DeviceErrorState extends DeviceState {
  const DeviceErrorState([this.error]);

  final String? error;

  @override
  List<Object?> get props => [error];
}

class NewDeviceAddedState extends DeviceState {
  const NewDeviceAddedState(this.device);

  final TrackedDevice device;

  @override
  List<Object?> get props => [device];
}