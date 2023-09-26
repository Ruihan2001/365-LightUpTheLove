package com.example.lightupthelove;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;


    //Implementation of App Widget functionality.


    //The implementation of app widget functionality is learned from tutorials.
    //Reference 1 URL: https://www.cnblogs.com/joy99/p/6346829.html
    //Reference 2 URL: https://blog.csdn.net/z957250254/article/details/52958363?locationNum=6&fps=1
public class NewAppWidget extends AppWidgetProvider {

    private static RemoteViews mRemoteViews;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, mRemoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}