package org.chromium.hat.utils;

import java.util.List;

/**
 * Created by Floatingmuseum on 2017/2/17.
 */

public class ListUtil {

    /**
     * 查看集合中是否存在数据，避免空指针以及角标越界
     *
     * @param list
     * @return
     */
    public static boolean hasData(List list) {
        return list != null && list.size() > 0;
    }
}
