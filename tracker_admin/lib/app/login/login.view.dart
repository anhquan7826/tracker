import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:go_router/go_router.dart';
import 'package:tracker_admin/app/login/login.cubit.dart';
import 'package:tracker_admin/app/login/login.state.dart';
import 'package:tracker_admin/routes/routes.dart';

class LoginScreen extends StatefulWidget {
  const LoginScreen({super.key});

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final emailController = TextEditingController();
  final passwordController = TextEditingController();

  LoginCubit get cubit => BlocProvider.of(context);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Đăng nhập'),
      ),
      body: BlocConsumer<LoginCubit, LoginState>(
        listener: (context, state) {
          if (state is LogInErrorState) {
            Fluttertoast.showToast(msg: 'Sai email hoặc mật khẩu!');
          }
          if (state is LoggedInState) {
            context.goNamed(routes.users);
          }
        },
        builder: (context, state) {
          return Column(
            children: [
              TextField(
                controller: emailController,
                decoration: const InputDecoration(label: Text('Email'), hintText: 'Nhập email'),
                keyboardType: TextInputType.emailAddress,
                textInputAction: TextInputAction.next,
                inputFormatters: [FilteringTextInputFormatter(RegExp(r'\s'), allow: false)],
                onChanged: (value) => setState(() {}),
              ),
              TextField(
                controller: passwordController,
                decoration: const InputDecoration(label: Text('Mật khẩu'), hintText: 'Nhập mật khẩu'),
                keyboardType: TextInputType.visiblePassword,
                textInputAction: TextInputAction.done,
                obscureText: true,
                onChanged: (value) => setState(() {}),
              ),
              if (state is LoggingInState)
                FilledButton.icon(
                  onPressed: null,
                  icon: const SizedBox(
                    width: 16,
                    height: 16,
                    child: CircularProgressIndicator(),
                  ),
                  label: const Text('Đang đăng nhập'),
                )
              else
                FilledButton(
                  onPressed: cubit.validate(emailController.text.trim(), passwordController.text)
                      ? () {
                          cubit.login(emailController.text.trim(), passwordController.text);
                        }
                      : null,
                  child: const Text('Đăng nhập'),
                ),
            ],
          );
        },
      ),
    );
  }
}
