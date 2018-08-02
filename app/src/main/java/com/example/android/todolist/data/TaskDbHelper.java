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

import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.todolist.data.TaskContract.TaskEntry;

//Task这个前缀是历史遗留问题，等到时再解决

//这个类是创建存储任务数据的数据库
public class TaskDbHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "timerDb.db";


    private static final int VERSION = 1;


    // Constructor
    TaskDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

//创建数据库
        final String CREATE_TABLE = "CREATE TABLE "  + TaskEntry.TABLE_NAME + " (" +
                TaskEntry._ID                        + " INTEGER PRIMARY KEY, " +
                TaskEntry.COLUMN_TITLE               + " TEXT NOT NULL, " +
//                TaskEntry.COLUMN_CREATE_TIME         + " INTEGER NOT NULL, " +
//                TaskEntry.COLUMN_EXPIRATION_TIME     + " INTEGER NOT NULL, " +
                TaskEntry.COLUMN_PRIORITY            + " INTEGER NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskEntry.TABLE_NAME);
        onCreate(db);
    }
}
