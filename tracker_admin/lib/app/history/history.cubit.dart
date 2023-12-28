import 'dart:async';

import 'package:firebase_database/firebase_database.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:tracker_admin/app/history/history.state.dart';
import 'package:tracker_admin/model/history.model.dart';

class HistoryCubit extends Cubit<HistoryState> {
  HistoryCubit({required this.uid, required this.deviceId}) : super(const HistoryLoadingState()) {
    load();
  }

  final String uid;
  final String deviceId;

  Map<int, TrackedHistory> devices = {};

  StreamSubscription<DatabaseEvent>? _listener;

  Future<void> load() async {
    try {
      await _listener?.cancel();
      emit(const HistoryLoadingState());
      final usersReference = FirebaseDatabase.instance.ref('$uid/devices/$deviceId/history');
      final usersSnapshot = await usersReference.get();
      devices = Map.fromEntries(usersSnapshot.children.map((historySnapshot) {
        return TrackedHistory(
          time: historySnapshot.child('time').value as int,
          lat: historySnapshot.child('lat').value as double,
          lon: historySnapshot.child('lon').value as double,
        );
      }).map((e) => MapEntry(e.time, e)));
      _startListener(usersReference);
      emit(HistoryLoadedState(devices.values.toList()));
    } catch (_) {
      emit(HistoryErrorState(_.toString()));
      rethrow;
    }
  }

  void _startListener(DatabaseReference ref) {
    _listener = ref.onChildAdded.listen((event) {
      final history = TrackedHistory(
        time: event.snapshot.child('time').value as int,
        lat: event.snapshot.child('lat').value as double,
        lon: event.snapshot.child('lon').value as double,
      );
      if (!devices.containsKey(history.time)) {
        devices[history.time] = history;
        emit(NewHistoryAddedState(history));
      }
    });
  }

  @override
  Future<void> close() async {
    await _listener?.cancel();
    return super.close();
  }
}
