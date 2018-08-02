/*
* Copyright (C) 2016 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.example.android.todolist.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

import static com.example.android.todolist.data.TaskContract.TaskEntry.TABLE_NAME;

//uri是匹配用的通配符，根据uri找到想要访问的具体数据

//这个类的作用是覆盖操作数据的所有必要方法，crud操作
// Verify that TaskContentProvider extends from ContentProvider and implements required methods
public class TaskContentProvider extends ContentProvider {

    // Define final integer constants for the directory of tasks and a single item.
    // It's convention to use 100, 200, 300, etc for directories,
    // and related ints (101, 102, ..) for items in that directory.
    // 为任务目录和单个项目定义最后的整数常量。使用目录100, 200, 300等约定，
    //相关的整数（101, 102，..）在该目录项。
    public static final int TIMER = 100;
    public static final int TIMER_WITH_ID = 101;

    // CDeclare a static variable for the Uri matcher that you construct

//    建造一个cdeclare静态变量的URI匹配
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // Define a static buildUriMatcher method that associates URI's with their int match
    /**
     Initialize a new matcher object without any matches,
     then use .addURI(String authority, String path, int match) to add matches
     */
//    Uri代表要操作的数据，Android上可用的每种资源 - 图像、视频片段等都可以用Uri来表示。
//    uri由访问资源的命名机制。存放资源的主机名。资源自身的名称，由路径表示。
//    解析uri，从uri中获取数据

//    识别对uri做出正确的响应
//    UriMatcher操作uri，主要用于匹配Uri
//    自动识别俩种，更加方便，使用urimatcher处理不同的uri
    public static UriMatcher buildUriMatcher() {

        // 1.初始化UriMatcher
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);//空的匹配器

        /*
          All paths added to the UriMatcher have a corresponding int.
          For each kind of uri you may want to access, add the corresponding match with addURI.
          The two calls below add matches for the task directory and a single item by ID.
         */
//        2.注册需要的UriMatcher
//        3.与已经注册的Uri进行匹配:uri.parse

//        adduri传入三个参数构成uri，authority，path，code
        // code匹配的整型变量，authority，path是匹配你所要的那个数据

//        authority+path指向timer的数据库表，TIMER为刚刚我们定义的100
        uriMatcher.addURI(TaskContract.AUTHORITY, TaskContract.PATH_TIMER, TIMER);
        //        --# 号为通配符--* 号为任意字符
//        所以这个adduri代表指向数据库中的单行数据
        uriMatcher.addURI(TaskContract.AUTHORITY, TaskContract.PATH_TIMER + "/#", TIMER_WITH_ID);

        return uriMatcher;
    }


    private TaskDbHelper mTaskDbHelper;

//   初始化设置，包括数据库
    @Override
    public boolean onCreate() {
//        取得必要的context，准备进行数据库操作
        Context context = getContext();
        mTaskDbHelper = new TaskDbHelper(context);
        return true;
    }


    // 实现inset处理插入一个新数据行
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        // 访问任务数据库，将新数据写入
        final SQLiteDatabase db = mTaskDbHelper.getWritableDatabase();

        //编写URI匹配代码以标识任务目录的匹配
        //传入一个uri，match方法返回一个整型
        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned

        switch (match) {
            case TIMER:

//                在数据库中插入新值，将值插进任务表中
                long id = db.insert(TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(TaskContract.TaskEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            // Set the value for the returnedUri and write the default case for unknown URI's
            // Default case throws an UnsupportedOperationException
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }


    // 执行查询以通过uri处理数据请求
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // 访问底层数据库（只读查询）
        final SQLiteDatabase db = mTaskDbHelper.getReadableDatabase();

        // Write URI match code and set a variable to return a Cursor
        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        //查询任务目录 并编写默认案例
        switch (match) {
            //查询任务目录
            case TIMER:
                retCursor =  db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            // 默认异常
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor and return that Cursor
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }


    // 删除函数
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        // Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = mTaskDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        // Keep track of the number of deleted tasks
        int tasksDeleted; // starts as 0

        // Write the code to delete a single row of data
        // [Hint] Use selections to delete an item by its row ID
        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case TIMER_WITH_ID:
                // 从uri中获取路径
                String id = uri.getPathSegments().get(1);
                // 过滤id
                tasksDeleted = db.delete(TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change and return the number of items deleted
        if (tasksDeleted != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of tasks deleted
        return tasksDeleted;
    }


    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public String getType(@NonNull Uri uri) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

}
