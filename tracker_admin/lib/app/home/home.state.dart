import 'package:equatable/equatable.dart';
import 'package:tracker_admin/model/user.model.dart';

abstract class HomeState extends Equatable {
  const HomeState();
}

class HomeLoadingState extends HomeState {
  const HomeLoadingState();

  @override
  List<Object?> get props => [];
}

class HomeLoadedState extends HomeState {
  const HomeLoadedState(this.users);

  final List<TrackedUser> users;

  @override
  List<Object?> get props => [users];
}

class HomeErrorState extends HomeState {
  const HomeErrorState([this.error]);

  final String? error;

  @override
  List<Object?> get props => [error];
}

class NewUserAddedState extends HomeState {
  const NewUserAddedState(this.user);

  final TrackedUser user;

  @override
  List<Object?> get props => [user];
}