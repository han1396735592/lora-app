package cn.qqhxj.iot.lora.ali.listener;


import cn.qqhxj.common.rxtx.SerialContext;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.cmp.core.base.AMessage;
import com.aliyun.alink.linksdk.cmp.core.base.ConnectState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author han xinjian
 * @date 2019-05-29 19:29
 **/
@Slf4j
@Component
public class PropertySetTopicListener extends TopicListener {



    public PropertySetTopicListener() {
        init();
    }


    @Override
    public void onNotify(String s, String s1, AMessage aMessage) {
        super.onNotify(s, s1, aMessage);
        String s2 = new String((byte[]) aMessage.data);
        String params = JSONObject.parseObject(s2).get("params").toString();
        log.info("收到云端的信息  = {}",params);
        SerialContext.sendData(params.getBytes());
    }

    @Override
    public boolean shouldHandle(String s, String s1) {
        return super.shouldHandle(s, s1);
    }

    @Override
    public void onConnectStateChange(String s, ConnectState connectState) {
        super.onConnectStateChange(s, connectState);
    }

    /**
     * 初始化
     */
    @PostConstruct
    @Override
    public void init() {
        LinkKit.getInstance().registerOnNotifyListener(this);
        log.debug("init TopicListener is {}", this);
    }



}
