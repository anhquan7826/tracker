import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:tracker_admin/app/settings/settings.state.dart';
import 'package:tracker_admin/utils/service.util.dart';
import 'package:tracker_admin/utils/shared_preferences.util.dart';

class SettingsCubit extends Cubit<SettingsState> {
  SettingsCubit() : super(const SettingsInitialState()) {
    load();
  }

  bool _hasChanges = false;

  int _distance = 0;
  String _notificationState = 'all';

  int get distance => _distance;
  set distance(int value) {
    _distance = value;
    _hasChanges = true;
  }

  String get notificationState => _notificationState;
  set notificationState(String value) {
    _notificationState = value;
    _hasChanges = true;
  }

  Future<void> load() async {
    _notificationState = await SharedPreferences.getString('notification') ?? 'all';
    _distance = await SharedPreferences.getInt('distance') ?? 0;
    emit(NotificationChangedState(state: notificationState, distance: distance));
  }

  @override
  Future<void> close() async {
    if (_hasChanges) {
      await SharedPreferences.putString('notification', value: notificationState);
      await SharedPreferences.putInt('distance', value: distance);
      await ServiceUtil.restartService();
    }
    return super.close();
  }
}
