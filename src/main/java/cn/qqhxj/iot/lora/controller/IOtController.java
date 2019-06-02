package cn.qqhxj.iot.lora.controller;


import cn.qqhxj.common.rxtx.processor.SerialDataProcessor;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.dm.api.DeviceInfo;
import com.aliyun.alink.dm.api.InitResult;
import com.aliyun.alink.dm.api.IoTApiClientConfig;
import com.aliyun.alink.linkkit.api.ILinkKitConnectListener;
import com.aliyun.alink.linkkit.api.IoTMqttClientConfig;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linkkit.api.LinkKitInitParams;
import com.aliyun.alink.linksdk.cmp.connect.channel.MqttPublishRequest;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

import static com.aliyun.alink.linksdk.tmp.utils.TmpConstant.TAG;

/**
 * @author han xinjian
 * @date 2019-05-29 16:35
 **/
@Slf4j
@RestController
public class IOtController implements SerialDataProcessor<String>, ApplicationRunner {


    @Value("${iot.productKey}")
    private String productKey ;
    @Value("${iot.deviceName}")
    private String deviceName ;
    @Value("${iot.deviceSecret}")
    private String deviceSecret;

    private static String tempStr;

    @Override
    public void processor(String s) {
        String str = s.replaceAll("[^0-9a-zA-Z{\"\'\t\\n:}_]J*", "");

        if (str.startsWith("{")&&!str.endsWith("}")){
            tempStr = str;
            return ;
        }
        if (!str.startsWith("{")&&str.endsWith("}")){
            str=tempStr+str;
        }


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", new Random().nextInt());
        jsonObject.put("params", str);
        MqttPublishRequest request;
        request = new MqttPublishRequest();
        request.topic = "/sys/" + productKey + "/" + deviceName + "/thing/event/property/post";
        request.qos = 0;
        request.payloadObj = jsonObject.toJSONString();
        log.info("上报数据 = {}", jsonObject.toJSONString());
        try {
            LinkKit.getInstance().publish(request, null);
        } catch (Exception e) {
            System.out.println(e);
        }


    }



    void init() {
        log.info("productKey={},deviceName={},deviceSecret={}",productKey,deviceName,deviceSecret);
        LinkKitInitParams params = new LinkKitInitParams();
        IoTMqttClientConfig config = new IoTMqttClientConfig();
        config.productKey = productKey;
        config.deviceName = deviceName;
        config.deviceSecret = deviceSecret;
        params.mqttClientConfig = config;
        params.connectConfig = new IoTApiClientConfig();
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.productKey = productKey;
        deviceInfo.deviceName = deviceName;
        deviceInfo.deviceSecret = deviceSecret;
        params.deviceInfo = deviceInfo;
        LinkKit.getInstance().init(params, new ILinkKitConnectListener() {
            @Override
            public void onError(AError aError) {
                ALog.e(TAG, "Init Error error=" + aError);
            }

            @Override
            public void onInitDone(InitResult initResult) {
                ALog.i(TAG, "onInitDone result=" + initResult);
            }
        });
    }



    @Override
    public void run(ApplicationArguments args) throws Exception {

        log.info("iot init");
        init();
    }
}
