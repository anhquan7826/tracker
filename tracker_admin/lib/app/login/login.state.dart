import 'package:equatable/equatable.dart';

abstract class LoginState extends Equatable {
  const LoginState();
}

class InitialLoginState extends LoginState {
  const InitialLoginState();

  @override
  List<Object?> get props => [];
}

class LoggingInState extends LoginState {
  const LoggingInState(this.email, this.password);


  final String email;
  final String password;

  @override
  List<Object?> get props => [email, password];
}

class LoggedInState extends LoginState {
  const LoggedInState(this.email, this.password);


  final String email;
  final String password;

  @override
  List<Object?> get props => [email, password];
}

class LogInErrorState extends LoginState {
  const LogInErrorState({this.error});


  final String? error;

  @override
  List<Object?> get props => [error];
}