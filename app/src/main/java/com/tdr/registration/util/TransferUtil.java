package com.tdr.registration.util;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/11.
 */

public class TransferUtil {

    static Map<String, WeakReference<Object>> data = new HashMap<String, WeakReference<Object>>();

    public static void save(String id, Object object) {
        mLog.e("弱引用存储："+id);
        data.put(id, new WeakReference<Object>(object));

    }

    public static Object retrieve(String id) {
        mLog.e("弱引用获取："+id);
        WeakReference<Object> objectWeakReference = data.get(id);
        if(objectWeakReference==null){
            return null;
        }
        return objectWeakReference.get();
    }
    public static void remove(String id) {
        mLog.e("弱引用删除："+id);
        if(data!=null){
            data.remove(id);
        }
    }
}
