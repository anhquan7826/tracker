import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:intl/intl.dart';
import 'package:tracker_admin/app/history/history.cubit.dart';
import 'package:tracker_admin/app/history/history.state.dart';
import 'package:tracker_admin/model/device.model.dart';
import 'package:tracker_admin/model/history.model.dart';

class HistoryScreen extends StatefulWidget {
  const HistoryScreen({super.key, required this.device});

  final TrackedDevice device;

  @override
  State<HistoryScreen> createState() => _HistoryScreenState();
}

class _HistoryScreenState extends State<HistoryScreen> {
  List<TrackedHistory> histories = [];
  TrackedHistory? newHistory;

  HistoryCubit get cubit => BlocProvider.of(context);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.device.name),
      ),
      body: BlocConsumer<HistoryCubit, HistoryState>(
        listener: (context, state) {
          if (state is HistoryLoadedState) {
            histories = state.histories;
          }
          if (state is NewHistoryAddedState) {
            newHistory = state.history;
            histories.add(state.history);
          }
        },
        builder: (context, state) {
          if (state is HistoryLoadingState) {
            return const Center(
              child: CircularProgressIndicator(),
            );
          }
          if (state is HistoryErrorState) {
            return Padding(
              padding: const EdgeInsets.symmetric(horizontal: 16),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  const Padding(
                    padding: EdgeInsets.only(bottom: 8),
                    child: Text(
                      'Không thể tải dữ liệu!',
                      style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
                      textAlign: TextAlign.center,
                    ),
                  ),
                  if (state.error != null)
                    Text(
                      state.error!,
                      textAlign: TextAlign.center,
                      style: const TextStyle(
                        color: Colors.grey,
                      ),
                    ),
                  Padding(
                    padding: const EdgeInsets.only(top: 8),
                    child: FilledButton(
                      onPressed: () => cubit.load(),
                      child: const Text('Tải lại'),
                    ),
                  ),
                ],
              ),
            );
          }
          return Column(
            children: [
              Expanded(
                child: buildHistoryList(),
              ),
            ],
          );
        },
      ),
    );
  }

  Widget buildHistoryList() {
    return ListView(
      padding: const EdgeInsets.all(16),
      children: histories.map((history) {
        return Padding(
          padding: const EdgeInsets.only(bottom: 8),
          child: ListTile(
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(12),
            ),
            title: Text(
              DateFormat('dd/MM/yyyy HH:mm').format(DateTime.fromMillisecondsSinceEpoch(history.time * 1000)),
            ),
            subtitle: Text('${history.lat}, ${history.lon}'),
            onTap: () {
              // TODO: show map
            },
            onLongPress: () {
              Clipboard.setData(ClipboardData(text: '${history.lat}, ${history.lon}')).whenComplete(() {
                Fluttertoast.showToast(msg: 'Đã sao chép tọa độ vào bộ nhớ!');
              });
            },
          ),
        );
      }).toList(),
    );
  }
}
