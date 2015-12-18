/**
 * 上午9:56:13
 * @author zhangyh2
 * $
 * a.java
 * TODO
 */
package com.darly.oop.ui.download;

import java.io.File;

/**
 * @author zhangyh2
 * a
 * $
 * 上午9:56:13
 * TODO
 */
public interface DownloadProgressListener {
    public void onDownloadSize(File saveFile, int size);
} 