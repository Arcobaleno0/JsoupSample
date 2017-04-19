package com.image.manager;

/**
 * by y on 2016/7/26.
 */
public interface ApiConfig {
    String SLASH = "/";
    String PAGE = "page";

    String DBMZ_URL = "http://www.dbmeinv.com/dbgroup/show.htm?cid=";
    String dou_ban_link = "&pager_offset=";

    String M_ZI_TU_URL = "http://www.mzitu.com/";

    String MM_URL = "http://www.mmjpg.com/";

    String MEIZITU_URL = "http://www.meizitu.com/";

    String KK_URL = "http://m.7kk.com/";

    interface Type {
        String DOU_BAN_MEI_ZI = "豆瓣妹子";
        String M_ZI_TU = "妹子图";
        String MM = "MM";
        String MEIZITU = "MeiZiTu";
        String KK = "7kk";
    }
}
