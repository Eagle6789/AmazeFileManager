/*
 * XzDecompressor.java
 *
 * Copyright © 2018 Raymond Lai <airwave209gt at gmail.com>.
 *
 * This file is part of AmazeFileManager.
 *
 * AmazeFileManager is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AmazeFileManager is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AmazeFileManager. If not, see <http ://www.gnu.org/licenses/>.
 */
package com.naga.filemanager.filesystem.compressed.showcontents.helpers;

import android.content.Context;

import com.naga.filemanager.adapters.data.CompressedObjectParcelable;
import com.naga.filemanager.asynchronous.asynctasks.AsyncTaskResult;
import com.naga.filemanager.asynchronous.asynctasks.compress.CompressedHelperTask;
import com.naga.filemanager.asynchronous.asynctasks.compress.XzHelperTask;
import com.naga.filemanager.filesystem.compressed.showcontents.Decompressor;
import com.naga.filemanager.utils.OnAsyncTaskFinished;

import java.util.ArrayList;

public class XzDecompressor extends Decompressor {

    public XzDecompressor(Context context) {
        super(context);
    }

    @Override
    public CompressedHelperTask changePath(String path, boolean addGoBackItem,
                                           OnAsyncTaskFinished<AsyncTaskResult<ArrayList<CompressedObjectParcelable>>> onFinish) {
        return new XzHelperTask(filePath, path, addGoBackItem, onFinish);
    }

}
