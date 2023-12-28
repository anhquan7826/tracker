import 'history.model.dart';

class TrackedDevice {
  TrackedDevice(this.id, {required this.name, this.history = const []});

  final String id;
  final String name;
  final List<TrackedHistory> history;
}