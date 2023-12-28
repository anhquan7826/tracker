import 'device.model.dart';

class TrackedUser {
  TrackedUser(this.id, {required this.email, this.devices = const []});

  final String id;
  final String email;
  final List<TrackedDevice> devices;
}