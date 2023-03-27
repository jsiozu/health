package com.dale.health;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {

    public void deleteIMG() {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        //...其他参数参考类注释
        String accessKey = "JHgHy5Hz7g_Vgdda_VBpDc1EmKy5_pBE7o50kXlg";
        String secretKey = "641JsGhjFnB2kfQJKlHyAtMi5unon11zkKw3FAL6";
        String bucket = "dale-health-img";
        String key = "FuM1Sa5TtL_ekLsdkYWcf5pyjKGu";
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }
}
