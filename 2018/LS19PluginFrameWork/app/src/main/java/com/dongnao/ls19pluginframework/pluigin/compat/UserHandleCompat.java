/*
**        DroidPlugin Project
**
** Copyright(c) 2015 Andy Zhang <zhangyong232@gmail.com>
**
** This file is part of DroidPlugin.
**
** DroidPlugin is free software: you can redistribute it and/or
** modify it under the terms of the GNU Lesser General Public
** License as published by the Free Software Foundation, either
** version 3 of the License, or (at your option) any later version.
**
** DroidPlugin is distributed in the hope that it will be useful,
** but WITHOUT ANY WARRANTY; without even the implied warranty of
** MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
** Lesser General Public License for more details.
**
** You should have received a copy of the GNU Lesser General Public
** License along with DroidPlugin.  If not, see <http://www.gnu.org/licenses/lgpl.txt>
**
**/

package com.dongnao.ls19pluginframework.pluigin.compat;

import android.os.Build;
import android.os.UserHandle;

import com.dongnao.ls19pluginframework.pluigin.utils.reflect.MethodUtils;


/**
 * Created by Andy Zhang(zhangyong232@gmail.com) on 2015/4/13.
 */
public class UserHandleCompat {

    //    UserHandle.getCallingUserId()
    public static int getCallingUserId() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                return (int) MethodUtils.invokeStaticMethod(UserHandle.class, "getCallingUserId");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
