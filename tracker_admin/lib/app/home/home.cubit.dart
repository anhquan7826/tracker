import 'dart:async';

import 'package:firebase_database/firebase_database.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:tracker_admin/app/home/home.state.dart';
import 'package:tracker_admin/model/user.model.dart';

class HomeCubit extends Cubit<HomeState> {
  HomeCubit() : super(const HomeLoadingState()) {
    load();
  }

  Map<String, TrackedUser> users = {};

  StreamSubscription<DatabaseEvent>? _listener;

  Future<void> load() async {
    try {
      await _listener?.cancel();
      emit(const HomeLoadingState());
      final usersReference = FirebaseDatabase.instance.ref();
      final usersSnapshot = await usersReference.get();
      users = Map.fromEntries(usersSnapshot.children.map((userSnapshot) {
        return TrackedUser(
          userSnapshot.key!,
          email: userSnapshot.child('email').value.toString(),
        );
      }).map((e) => MapEntry(e.id, e)));
      _startListener(usersReference);
      emit(HomeLoadedState(users.values.toList()));
    } catch (_) {
      emit(HomeErrorState(_.toString()));
      rethrow;
    }
  }

  void _startListener(DatabaseReference ref) {
    _listener = ref.onChildAdded.listen((event) {
      final user = TrackedUser(
        event.snapshot.key!,
        email: event.snapshot.child('email').toString(),
      );
      if (!users.containsKey(user.id)) {
        users[user.id] = user;
        emit(NewUserAddedState(user));
      }
    });
  }

  @override
  Future<void> close() async {
    await _listener?.cancel();
    return super.close();
  }
}
