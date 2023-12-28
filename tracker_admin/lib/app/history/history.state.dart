import 'package:equatable/equatable.dart';
import 'package:tracker_admin/model/history.model.dart';

abstract class HistoryState extends Equatable {
  const HistoryState();
}

class HistoryLoadingState extends HistoryState {
  const HistoryLoadingState();

  @override
  List<Object?> get props => [];
}

class HistoryLoadedState extends HistoryState {
  const HistoryLoadedState(this.histories);

  final List<TrackedHistory> histories;

  @override
  List<Object?> get props => [...histories];
}

class HistoryErrorState extends HistoryState {
  const HistoryErrorState([this.error]);

  final String? error;

  @override
  List<Object?> get props => [error];
}

class NewHistoryAddedState extends HistoryState {
  const NewHistoryAddedState(this.history);

  final TrackedHistory history;

  @override
  List<Object?> get props => [history];
}