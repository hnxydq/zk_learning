package com.dongqiang.zk;


import org.apache.zookeeper.*;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * created by dongqiang 2019/02/18
 */
public class App {
    public static void main(String[] args) {
        String connectString = "127.0.0.1:2181";
        int sessionTimeout = 500000;
        try {
            ZooKeeper zk = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
                public void process(WatchedEvent event) {

                    System.out.println("something changed.");

                    Event.EventType type = event.getType();
                    Event.KeeperState state = event.getState();
                    int intValue = type.getIntValue();
                    System.out.println(intValue);

                    int stateIntValue = state.getIntValue();
                    System.out.println(stateIntValue);

                    switch (type) {
                        case None:
                            System.out.println("none事件触发");
                            break;
                        case NodeCreated:
                            System.out.println("创建节点事件发生了");
                            break;
                        case NodeDeleted:
                            System.out.println("删除节点事件发生了");
                            break;
                        case NodeDataChanged:
                            System.out.println("节点数据改变事件发生了");
                            break;
                        case NodeChildrenChanged:
                            System.out.println("子节点改变事件发生了");
                            break;
                        default:
                            System.out.println("unknown type.");
                            break;
                    }

                    switch (state) {
                        case Disconnected:
                            System.out.println("失去连接");
                            break;
                        case SyncConnected:
                            System.out.println("异步连接");
                            break;
                        case Expired:
                            System.out.println("超时过期");
                            break;
                        default:
                            System.out.println("other state");
                            break;
                    }
                }

            });

            List<String> zkChildren = zk.getChildren("/", true);
            for (String zkChild : zkChildren) {
                if (null != zkChild) {
                    String childrenpath = "/" + zkChild;
                    byte[] recivedata = zk.getData(childrenpath, true, null);
                    String recString = new String(recivedata, "UTF-8");
                    System.out.println("receive the path:" + childrenpath + ", data:"+ recString);
                }
            }

//            zk.delete("/app/child", -1);
//            zk.delete("/app", -1);

            zk.create("/app", "app节点".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

            zk.create("/app/child", "child数据".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT,
                    new AsyncCallback.StringCallback() {
                public void processResult(int rc, String path, Object ctx, String name) {
                    System.out.println("rc=" + rc);
                    System.out.println("path=" + path);
                    System.out.println("ctx=" + ctx);
                    System.out.println("name=" + name);
                }
            }, "ctx object");

            Thread.sleep(5000);

            zkChildren = zk.getChildren("/", true);
            for (String zkChild : zkChildren) {
                if (null != zkChild) {
                    String childrenpath = "/" + zkChild;
                    byte[] recivedata = zk.getData(childrenpath, true, null);
                    String recString = new String(recivedata, "UTF-8");
                    System.out.println("receive the path:" + childrenpath + ", data="+ recString);
                }
            }

            Stat stat = zk.setData("/app", "test".getBytes(), -1);
            System.out.println(stat.toString());

            zkChildren = zk.getChildren("/", true);
            for (String zkChild : zkChildren) {
                if (null != zkChild) {
                    String childrenpath = "/" + zkChild;
                    byte[] recivedata = zk.getData(childrenpath, true, null);
                    String recString = new String(recivedata, "UTF-8");
                    System.out.println("receive the path=" + childrenpath + ", data="+ recString);
                }
            }

            //创建节点时，对/app下的节点acl1, acl2添加访问权限
            List<ACL> acls = new ArrayList<ACL>();
            Id id1 = new Id("digest", DigestAuthenticationProvider.generateDigest("admin:admin123"));
            ACL acl1 = new ACL(ZooDefs.Perms.ALL, id1);

            Id id2 = new Id("digest", DigestAuthenticationProvider.generateDigest("guest:guest123"));
            ACL acl2 = new ACL(ZooDefs.Perms.ALL, id2);

            acls.add(acl1);
            acls.add(acl2);

            zk.create("/app/acl1", "acl1".getBytes(), acls, CreateMode.PERSISTENT);
            zk.create("/app/acl2", "acl2".getBytes(), acls, CreateMode.PERSISTENT);

            //添加访问认证，否则会抛异常，无访问权限
            //当创建节点时如果设置了权限，那么在进行访问或删除修改时必须加入认证信息
            zk.addAuthInfo("digest", "guest:guest123".getBytes());

            byte[] acl1data = zk.getData("/app/acl1", true, null);
            String acl1Str = new String(acl1data, "UTF-8");
            System.out.println("acl1Str=" + acl1Str);

            byte[] acl2data = zk.getData("/app/acl2", true, null);
            String acl2Str = new String(acl2data, "UTF-8");
            System.out.println("acl1Str=" + acl1Str + ", acl2Str=" + acl2Str);

            zk.delete("/app/child", -1);
            zk.delete("/app/acl1", -1);
            zk.delete("/app/acl2", -1);
            zk.delete("/app", -1);

            zk.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
