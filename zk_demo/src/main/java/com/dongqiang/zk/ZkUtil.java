package com.dongqiang.zk;

import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.Watcher;

/**
 * 创建会话连接
 * Created by dongqiang on 2018/2/27.
 */
public class ZkUtil {

    public static ZkClient connectZk() {
        String zkServers = "127.0.0.1:2181";
        ZkClient zkClient = new ZkClient(zkServers, 10000, 10000, new SerializableSerializer());
        zkClient.subscribeStateChanges(new IZkStateListener() {
            public void handleStateChanged(Watcher.Event.KeeperState keeperState) throws Exception {
                System.out.println("handleStateChanged " + keeperState);
            }

            public void handleNewSession() throws Exception {
                System.out.println("handleNewSession");
            }
        });
        System.out.println("zk connect ok.");
        return zkClient;
    }
}
