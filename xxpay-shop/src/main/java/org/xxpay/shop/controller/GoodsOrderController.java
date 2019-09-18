package org.xxpay.shop.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.common.util.*;
import org.xxpay.shop.module.modle.*;
import org.xxpay.shop.module.service.*;
import org.xxpay.shop.util.Constant;
import org.xxpay.shop.util.config.PayConfig;
import org.xxpay.shop.util.vx.WxApi;
import org.xxpay.shop.util.vx.WxApiClient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@CrossOrigin
@Controller
@RequestMapping("/goods")
public class GoodsOrderController {
    private static MyLog _log = MyLog.getLog(GoodsOrderController.class);

    @Autowired
    private PayConfig payConfig;
    @Autowired
    private ClassifyService classifyService;
    @Autowired
    PaymentService paymentService;
    @Autowired
    WxPayLogService wxPayLogService;
    @Autowired
    UserService userService;
    @Autowired
    RunCommandService runCommandService;
    @Autowired
    TerminalService terminalService;
    @Autowired
    MachineService machineService;


    private AtomicLong seq = new AtomicLong(0L);
    @RequestMapping("/commodityInfQry")
    @ResponseBody
    public JSONObject commodityInfQry(@RequestParam long cl_id) {
        JSONObject resultJson =new JSONObject();
        Classify classify = classifyService.findClassifyByClId(cl_id);
        if(null==classify){
            _log.info("没有查询到该商品");
            resultJson.put("status","0");
            resultJson.put("desc","没有查询到该商品");
            return resultJson;
        }
        resultJson.put("status","1");
        resultJson.put("desc","success");
        resultJson.put("price",String.valueOf((int)(0.01*100)));
        return resultJson;
    }


    @RequestMapping("/get_classify")
    @ResponseBody
    public JSONObject getClassify(@RequestParam long t_id) {
        JSONObject resultJson =new JSONObject();
        List<Classify> classifyList = classifyService.findClassifyByTerminalId(t_id);
        if(null!=classifyList){
            classifyList.stream().forEach(classify -> {
//                classify
            });
            resultJson.put("status","1");
            resultJson.put("desc","获取成功");
            resultJson.put("data",classifyList);
        }else{
            resultJson.put("status","0");
            resultJson.put("desc","获取失败");
        }
        return resultJson;
    }

    @RequestMapping("/get_pay")
    @ResponseBody
    public JSONObject getPay(@RequestParam String order_sn) {
        JSONObject resultJson =new JSONObject();
        Payment payment = paymentService.findPaymentByOutTradeNo(order_sn);
        if(null!=payment){
            resultJson.put("status","1");
            resultJson.put("info","已支付");
        }else{
            resultJson.put("status","0");
            resultJson.put("info","未支付");
        }
        return resultJson;
    }

    private Map createPayOrder(String client,Payment payment, Map<String, Object> params,Long amount) {
        JSONObject paramMap = new JSONObject();
        if("wx".equals(client)){
            paramMap.put("mchId", payConfig.getWxMchId());                       // 商户ID
            paramMap.put("notifyUrl", payConfig.getWxNotifyUrl());         // 回调URL
        }else{
            paramMap.put("mchId", payConfig.getAliMchId());
            paramMap.put("notifyUrl", payConfig.getAliNotifyUrl());         // 回调URL
        }
        paramMap.put("mchOrderNo", payment.getOutTradeNo());           // 商户订单号
        paramMap.put("channelId", params.get("channelId"));             // 支付渠道ID, WX_NATIVE,ALIPAY_WAP
        paramMap.put("amount", amount);                          // 支付金额,单位分
        paramMap.put("currency", "cny");                    // 币种, cny-人民币
        paramMap.put("clientIp", "114.112.124.236");        // 用户地址,IP或手机号
        paramMap.put("device", "WEB");                      // 设备
        paramMap.put("subject", payment.getProductName());
        paramMap.put("body", payment.getProductName());
        paramMap.put("param1", "");                         // 扩展参数1
        paramMap.put("param2", "");                         // 扩展参数2
        JSONObject extra = new JSONObject();
        extra.put("openId", params.get("openId"));
        paramMap.put("extra", extra.toJSONString());  // 附加参数
        String reqSign = PayDigestUtil.getSign(paramMap, payConfig.getReqKey());
        paramMap.put("sign", reqSign);   // 签名
        String reqData = "params=" + paramMap.toJSONString();
        System.out.println("请求支付中心下单接口,请求数据:" + reqData);
        String url = payConfig.getBaseUrl() + "/api/pay/create_order?";
        String result = XXPayUtil.call4Post(url + reqData);
        System.out.println("请求支付中心下单接口,响应数据:" + result);
        Map retMap = JSON.parseObject(result);
        if("SUCCESS".equals(retMap.get("retCode"))) {
            // 验签
            String checkSign = PayDigestUtil.getSign(retMap, payConfig.getResKey(), "sign", "payParams");
            String retSign = (String) retMap.get("sign");
            if(checkSign.equals(retSign)) {
                System.out.println("=========支付中心下单验签成功=========");
            }else {
                System.err.println("=========支付中心下单验签失败=========");
                return null;
            }
        }
        return retMap;
    }

    @RequestMapping("/openQrPay.html")
    public String openQrPay(ModelMap model) {
        return "openQrPay";
    }

    @RequestMapping("/qrPay.html")
    public String qrPay(ModelMap model, HttpServletRequest request, Long cl_id,Long amount) {
        String logPrefix = "【二维码扫码支付】";
        String view = "qrPay";
        _log.info("====== 开始接收二维码扫码支付请求 ======");
        String ua = request.getHeader("User-Agent");
        _log.info("{}接收参数:cl_id={},amount={},ua={}", logPrefix, cl_id, amount, ua);
        String client = "alipay";
        String channelId = "ALIPAY_WAP";
        if(StringUtils.isBlank(ua)) {
            String errorMessage = "User-Agent为空！";
            _log.info("{}信息：{}", logPrefix, errorMessage);
            model.put("result", "failed");
            model.put("resMsg", errorMessage);
            return view;
        }else {
            if(ua.contains("Alipay")) {
                client = "alipay";
                channelId = "ALIPAY_WAP";
            }else if(ua.contains("MicroMessenger")) {
                client = "wx";
                channelId = "WX_JSAPI";
            }
        }
        if(client == null) {
            String errorMessage = "请用微信或支付宝扫码";
            _log.info("{}信息：{}", logPrefix, errorMessage);
            model.put("result", "failed");
            model.put("resMsg", errorMessage);
            return view;
        }
        // 先插入订单数据
        Payment payment = null;
        Map<String, String> orderMap = null;
        if ("alipay".equals(client)) {
            _log.info("{}{}扫码下单", logPrefix, "支付宝");
            Map params = new HashMap<>();
            params.put("channelId", channelId);
            // 下单
            payment = createPayment(cl_id);
            orderMap = createPayOrder(client,payment, params,amount);
        }else if("wx".equals(client)){
            _log.info("{}{}扫码", logPrefix, "微信");
            // 判断是否拿到openid，如果没有则去获取
            String openId = payConfig.getOpenId();
            if (StringUtils.isNotBlank(openId)) {
                _log.info("{}openId：{}", logPrefix, openId);
                Map params = new HashMap<>();
                params.put("channelId", channelId);
                params.put("openId", openId);
                payment = createPayment(cl_id);
                // 下单
                orderMap = createPayOrder(client,payment, params,amount);
            }else {
                String redirectUrl = payConfig.getWxPayUrl() + "?amount=" + amount;
                String url = payConfig.getWxGetOpenIdUR2() + "?redirectUrl=" + redirectUrl;
                _log.info("跳转URL={}", url);
                return "redirect:" + url;
            }
        }
        model.put("payment", payment);
        model.put("amount", AmountUtil.convertCent2Dollar(amount+""));
        if(orderMap != null) {
            model.put("orderMap", orderMap);
            String payOrderId = orderMap.get("payOrderId");
            payment.setOutTradeNo(payOrderId);
            payment.setType(channelId);
            paymentService.doSave(payment);
        }
        model.put("client", client);
        return view;
    }

    Payment createPayment(long cl_id) {
        //生成订单号
        // 先插入订单数据
        String outTradeNo =  String.format("%s%s%06d", "G", DateUtil.getSeqString(), (int) seq.getAndIncrement() % 1000000);
        _log.info("订单号：",outTradeNo);
        //根据cl_id查询商品相关信息
         Classify classify = classifyService.findClassifyByClId(cl_id);

        Payment payment = new Payment();
        payment.setOutTradeNo(outTradeNo);
        payment.setMoney(classify.getPrice());
        payment.setStatus(Constant.GOODS_ORDER_STATUS_INIT);
        payment.setCreateTime(System.currentTimeMillis()/1000);
        payment.setProductName(classify.getName());
        payment.setAdminId(classify.getOperator());
        payment.setClassifyId(cl_id);
        payment.setTerminalId(classify.getTerminalId());
        paymentService.doSave(payment);
        return payment;
    }

    /**
     * 获取code
     * @return
     */
    @RequestMapping("/getOpenId2")
    public void getOpenId2(HttpServletRequest request, HttpServletResponse response) throws IOException {
        _log.info("进入获取用户openID页面");
        String redirectUrl = request.getParameter("redirectUrl");
        String code = request.getParameter("code");
        String openId = "";
        if(!StringUtils.isBlank(code)){//如果request中包括code，则是微信回调
            try {
                openId = WxApiClient.getOAuthOpenId(payConfig.getWxAppID(), payConfig.getWxAppSecret(), code);
                _log.info("调用微信返回openId={}", openId);
            } catch (Exception e) {
                _log.error(e, "调用微信查询openId异常");
            }
            if(redirectUrl.indexOf("?") > 0) {
                redirectUrl += "&openId=" + openId;
            }else {
                redirectUrl += "?openId=" + openId;
            }
            response.sendRedirect(redirectUrl);
        }else{//oauth获取code
            //http://www.abc.com/xxx/get-weixin-code.html?appid=XXXX&scope=snsapi_base&state=hello-world&redirect_uri=http%3A%2F%2Fwww.xyz.com%2Fhello-world.html
            String redirectUrl4Vx = payConfig.getWxGetOpenIdUR2() + "?redirectUrl=" + redirectUrl;
            String url = String.format("http://www.xiaoshuding.com/get-weixin-code.html?appid=%s&scope=snsapi_base&state=hello-world&redirect_uri=%s", payConfig.getWxAppID(), WxApi.urlEnodeUTF8(redirectUrl4Vx));
            _log.info("跳转URL={}", url);
            response.sendRedirect(url);
        }
    }
    /**
     * 接收支付宝消息，并将其转发至支付中心进行校验
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/notify_alipay")
    public void notify_alipay(HttpServletRequest request, HttpServletResponse response) throws Exception {
        _log.info("====== 接收支付宝支付回调通知 ======");
        Map<String,Object> paramMap = request2payResponseMap(request);
        _log.info("支付中心通知请求参数,paramMap={}", paramMap);

        StringBuffer parametersBuff= new StringBuffer();
        Set<Map.Entry<String, Object>> set = paramMap.entrySet();
        _log.debug("==============================================================");
        for (Map.Entry entry : set) {
            parametersBuff.append("&").append(entry.getKey()).append("=");
            parametersBuff.append(URLEncoder.encode(entry.getValue().toString(), "utf8"));
        }
        _log.debug("=============================================================");
        String url = payConfig.getBaseUrl() + "/notify/pay/aliPayNotifyRes.htm?";
        _log.info("====== 支付宝回调通知转发至支付中心开始 ======");
        XXPayUtil.call4Post(url + parametersBuff.substring(1));
        _log.info("====== 支付宝回调通知转发至支付中心结束 ======");
    }


    /**
     * 接收支付中心校验后通知，并进行响应的业务处理
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/payNotify")
    @ResponseBody
    public String payNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String,Object> paramMap = request2payResponseMap(request);
        //第一步 插入回调
        wxPayLogService.doSave("收到回调");
        //将返回参数插入wxpaylog表中
        wxPayLogService.doSave(JSON.toJSONString(paramMap));
        //第二步 校验回调参数是否正确
        _log.info("====== 开始接收支付宝支付回调通知 ======");
        if("2".equals(paramMap.get("status"))){
            wxPayLogService.doSave("插入回调");
            //根据外部订单号和状态查询支付相关消息
            String out_trade_no = (String) paramMap.get("payOrderId");
            Payment payment = paymentService.findPaymentByOutTradeNo(out_trade_no);
            if (!(null!=payment && 0 == payment.getStatus())){
                _log.error("order not exists");
                return "fail";
            }

            Classify classify = classifyService.findClassifyByClId(payment.getClassifyId());
            if(null!=classify && classify.getStock() > 0){
                //更新支付信息表
                payment.setStatus(1);
                payment.setType("支付宝支付");
                payment.setPayTime(System.currentTimeMillis()/1000);
                paymentService.doSave(payment);

                //更新用户表
                User user = userService.findByAdminId(payment.getAdminId());
                user.setCommission(user.getCommission()+payment.getMoney());
                userService.doSave(user);

                //更新classify表
                classify.setStock(classify.getStock()-1);
                classifyService.doSave(classify);

                //查询设备表
                Terminal terminal = terminalService.findByTerminalId(payment.getTerminalId());

                //查询machine表
                Machine machine = machineService.findByImei(terminal.getDeviceNum());
                int road = classify.getRoad();
                String command;
                int x = (int)Math.ceil((double)road/4);
                int y = road % 4;
                if (y==0){
                    command = x == 0 ? (x - 1) +"8" : x + "8";
                }else if(y==1){
                    command = x == 0 ? (x - 1) +"1" : x + "1";
                }else if(y == 2){
                    command = x == 0 ? (x - 1) +"2" : x + "2";
                }else{
                    command = x == 0 ? (x - 1) +"6" : x + "6";
                }

                //插入执行命令表
                RunCommand runCommand = new RunCommand();
                Date sysDate = new Date();
                runCommand.setCreateTime(sysDate);
                runCommand.setExecTime(sysDate);
                runCommand.setToken(machine.getToken());
                runCommand.setTerminalId(payment.getTerminalId());
                runCommand.setRemark("打开货道");
                command = "68a21600" + machine.getMtoken() + command + "0100000000";
                runCommand.setCommand(command);
                runCommandService.doSave(runCommand);
            }else{
                _log.error("Stock not exists");
                return "fail";
            }
        }
        _log.info("====== 完成接收支付宝支付回调通知 ======");
        return "success";
    }


    @RequestMapping("/notify_test")
    public void notifyTest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        outResult(response, "success");
    }

//    @RequestMapping("/toAliPay.html")
//    @ResponseBody
//    public String toAliPay(HttpServletRequest request, Long amount, String channelId) {
//        String logPrefix = "【支付宝支付】";
//        _log.info("====== 开始接收支付宝支付请求 ======");
//        String goodsId = "G_0001";
//        _log.info("{}接收参数:goodsId={},amount={},channelId={}", logPrefix, goodsId, amount, channelId);
//        // 先插入订单数据
//        Map params = new HashMap<>();
//        params.put("channelId", channelId);
//        // 下单
//        GoodsOrder goodsOrder = createGoodsOrder(goodsId, amount);
//        Map<String, String> orderMap = createPayOrder("alipay",goodsOrder, params);
//        if(orderMap != null && "success".equalsIgnoreCase(orderMap.get("resCode"))) {
//            String payOrderId = orderMap.get("payOrderId");
//            GoodsOrder go = new GoodsOrder();
//            go.setGoodsOrderId(goodsOrder.getGoodsOrderId());
//            go.setPayOrderId(payOrderId);
//            go.setChannelId(channelId);
//            int ret = goodsOrderService.update(go);
//            _log.info("修改商品订单,返回:{}", ret);
//        }
//        if(PayConstant.PAY_CHANNEL_ALIPAY_MOBILE.equalsIgnoreCase(channelId)) return orderMap.get("payParams");
//        return orderMap.get("payUrl");
//    }

    void outResult(HttpServletResponse response, String content) {
        response.setContentType("text/html");
        PrintWriter pw;
        try {
            pw = response.getWriter();
            pw.print(content);
            _log.error("response xxpay complete.");
        } catch (IOException e) {
            _log.error(e, "response xxpay write exception.");
        }
    }

    public Map<String, Object> request2payResponseMap(HttpServletRequest request) {
        Map<String, Object> responseMap = new HashMap<>();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length >0) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    responseMap.put(paramName, paramValue);
                }
            }
        }
        return responseMap;
    }

    public boolean verifyPayResponse(Map<String,Object> map) {
        String appId = (String) map.get("app_id");
        String tradeNo = (String) map.get("trade_no");
        String outTradeNo = (String) map.get("out_trade_no");
        String amount = (String) map.get("total_amount");
        String sign = (String) map.get("sign");

        if (StringUtils.isEmpty(appId)) {
            _log.warn("Params error. appId={}", appId);
            return false;
        }
        if (StringUtils.isEmpty(tradeNo)) {
            _log.warn("Params error. payOrderId={}", tradeNo);
            return false;
        }
        if (StringUtils.isEmpty(amount)) {
            _log.warn("Params error. amount={}", amount);
            return false;
        }
        if (StringUtils.isEmpty(sign)) {
            _log.warn("Params error. sign={}", sign);
            return false;
        }

        // 验证签名
//        if (!verifySign(map)) {
//            _log.warn("verify params sign failed. outTradeNo={}", outTradeNo);
//            return false;
//        }
        // 根据payOrderId查询业务订单,验证订单是否存在
        Payment payment = paymentService.findPaymentByOutTradeNo(outTradeNo);
        if (null==payment){
            _log.warn("业务订单不存在,tradeNo={},mchOrderNo={}", tradeNo, outTradeNo);
            return false;
        }
        // 核对金额
        if(payment.getMoney()!=Double.parseDouble(amount)){
            _log.warn("支付金额不一致,dbPayPrice={},payPrice={}", payment.getMoney(), amount);
            return false;
        }
        return true;
    }

}