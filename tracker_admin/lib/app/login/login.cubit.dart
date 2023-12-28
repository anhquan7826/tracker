import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:tracker_admin/app/login/login.state.dart';

class LoginCubit extends Cubit<LoginState> {
  LoginCubit() : super(const InitialLoginState());

  bool validate(String email, String password) {
    final regex = RegExp(r'^[\w-.]+@([\w-]+\.)+[\w-]{2,4}$');
    if (!regex.hasMatch(email)) {
      return false;
    }
    if (password.length < 6) {
      return false;
    }
    return true;
  }

  Future<void> login(String email, String password) async {
    try {
      emit(LoggingInState(email, password));
      await FirebaseAuth.instance.signInWithEmailAndPassword(email: email, password: password);
      emit(LoggedInState(email, password));
    } catch (_) {
      emit(LogInErrorState(error: _.toString()));
    }
  }
}
