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

import android.net.Uri;
import android.provider.BaseColumns;


//一修改数据就会出现问题，为何？
//直接在数据库中添加会出现什么问题吗？
public class TaskContract {


    // 这是代码如何知道要访问哪个内容提供者的权限
    public static final String AUTHORITY = "com.example.android.todolist";

    // The base content URI = "content://" + <authority>
    //        3.与已经注册的Uri进行匹配:uri.parse
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // 任务路径的目录
    public static final String PATH_TIMER = "timer";


//    将content、authority，path三个结合起来，然后再去匹配其他uri
    /* TaskEntry是一个内部类定义了任务表的内容 */
    public static final class TaskEntry implements BaseColumns {

//        TaskEntry content URI = base content URI + path
//    为条目创建完整的uri常量，即这个为完整uri，可以重复引用
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TIMER).build();


//        包含了表格名称和列的名称
//        添加了日期，但还没开始使用
        public static final String TABLE_NAME = "timer";


//        public static final String COLUMN_ID = "id";
//        public static final String COLUMN_CREATE_TIME = "createTime";
//        public static final String COLUMN_EXPIRATION_TIME = "expirationTime";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_PRIORITY = "priority";



    }
}
