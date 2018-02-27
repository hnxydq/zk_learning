package com.dongqiang.zk;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

/**
 * 订阅节点数据变化订阅
 * Created by dongqiang on 2018/2/27.
 */
public class DataChangeSubscription {

    public static void main(String[] args) throws InterruptedException {

        ZkClient zkClient = ZkUtil.connectZk();

        zkClient.subscribeDataChanges("/test", new IZkDataListener() {

            public void handleDataChange(String dataPath, Object data) throws Exception {
                System.out.println("订阅节点的内容发生变化" + ", datapath=" + dataPath + ", data=" + data);
            }

            public void handleDataDeleted(String dataPath) throws Exception {
                System.out.println("订阅节点的内容被删除" + ", datapath=" + dataPath);
            }
        });
        Thread.sleep(Integer.MAX_VALUE);
    }

}
