package com.dongqiang.zk;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

/**
 * zk节点操作
 * Created by dongqiang on 2018/2/27.
 */
public class NodeManager {

    /**
     * 创建节点
     * @param nodePath 节点路径
     */
    public void createNode(String nodePath) {
        ZkClient zkClient = ZkUtil.connectZk();
        User user = new User();
        user.setId(1);
        user.setName("zhangsan");
        String path = zkClient.create(nodePath, user, CreateMode.PERSISTENT);
        // 输出创建节点的路径
        System.out.println("创建节点:" + path);
    }

    /**
     * 修改节点
     * @param nodePath 节点路径
     */
    public void updateNode(String nodePath) {
        ZkClient zkClient = ZkUtil.connectZk();
        User user = new User();
        user.setId(2);
        user.setName("lisi");
        //节点的路径 user 传入的数据对象
        zkClient.writeData(nodePath, user);
        System.out.println("节点" + nodePath + "修改成功");
    }

    /**
     * 删除节点
     * @param nodePath 节点路径
     */
    public void deleteNode(String nodePath) {
        ZkClient zkClient = ZkUtil.connectZk();
        boolean b = zkClient.delete(nodePath);
        //返回 true表示节点成功 ，false表示删除失败
        System.out.println("删除" + nodePath + "节点是否成功：" + b);
    }

    public static void main(String[] args) {
        NodeManager nodeManager = new NodeManager();
        nodeManager.createNode("/test");
        nodeManager.createNode("/test/app");
        nodeManager.updateNode("/test");
        nodeManager.deleteNode("/test/app");
        nodeManager.deleteNode("/test");
    }

}
