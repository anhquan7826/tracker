import 'package:equatable/equatable.dart';

abstract class SettingsState extends Equatable {
  const SettingsState();
}

class SettingsInitialState extends SettingsState {
  const SettingsInitialState();

  @override
  List<Object?> get props => [];
}

class NotificationChangedState extends SettingsState {
  const NotificationChangedState({this.distance = 0, this.state = 'all'});

  final String state;
  final int distance;

  @override
  List<Object?> get props => [state, distance];
}
