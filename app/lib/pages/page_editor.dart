import 'package:auto_route/auto_route.dart';
import 'package:flutter/material.dart';

class PageEditor extends StatelessWidget {
  final String pageId;

  const PageEditor({Key? key, @PathParam("id") required this.pageId})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Text('Page Editor: $pageId'),
    );
  }
}
