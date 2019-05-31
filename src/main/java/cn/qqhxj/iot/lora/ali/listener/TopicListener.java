package cn.qqhxj.iot.lora.ali.listener;

import com.aliyun.alink.linksdk.cmp.core.base.AMessage;
import com.aliyun.alink.linksdk.cmp.core.base.ConnectState;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener;
import lombok.extern.slf4j.Slf4j;

/**
 * @author han xinjian
 * @date 2019-05-29 19:25
 **/
@Slf4j
abstract class TopicListener implements IConnectNotifyListener {


    public abstract void init();


    @Override
    public void onNotify(String s, String s1, AMessage aMessage) {

    }

    @Override
    public boolean shouldHandle(String s, String s1) {
        return false;
    }

    @Override
    public void onConnectStateChange(String s, ConnectState connectState) {

    }
}
