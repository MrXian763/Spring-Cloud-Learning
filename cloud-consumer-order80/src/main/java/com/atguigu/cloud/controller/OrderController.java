package com.atguigu.cloud.controller;

import com.atguigu.cloud.entities.PayDTO;
import com.atguigu.cloud.resp.ResultData;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@RestController
@Slf4j
@RequestMapping(value = "/consumer")
public class OrderController {

    //     public static final String PaymentSrv_URL = "http://localhost:8001"; // 先写死，硬编码
    public static final String PaymentSrv_URL = "http://cloud-payment-service"; // 服务注册中心上的微服务名称

    @Resource
    private RestTemplate restTemplate;

    /**
     * 新增
     *
     * @param payDTO
     * @return
     */
    @PostMapping(value = "/pay/add")
    public ResultData addOrder(@RequestBody PayDTO payDTO) {
        return restTemplate.postForObject(PaymentSrv_URL + "/pay/add", payDTO, ResultData.class);
    }

    /**
     * 查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/pay/get/{id}")
    public ResultData getPayInfo(@PathVariable("id") Integer id) {
        return restTemplate.getForObject(PaymentSrv_URL + "/pay/get/" + id, ResultData.class, id);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/pay/delete/{id}")
    public ResultData deletePayInfo(@PathVariable("id") Integer id) {
        restTemplate.delete(PaymentSrv_URL + "/pay/delete/" + id, ResultData.class, id);
        return ResultData.success("delete success");
    }

    /**
     * 修改
     *
     * @param payDTO
     * @return
     */
    @PutMapping(value = "/pay/update")
    public ResultData updatePayInfo(@RequestBody PayDTO payDTO) {
        restTemplate.put(PaymentSrv_URL + "/pay/update", payDTO);
        return ResultData.success("update success");
    }

    /**
     * 测试负载均衡
     *
     * @return
     */
    @GetMapping(value = "/pay/get/info")
    private String getInfoByConsul() {
        return Objects.requireNonNull(restTemplate.getForObject(PaymentSrv_URL + "/pay/get/info", String.class));
    }

    /**
     * 负载均衡原理
     */
    @Resource
    private DiscoveryClient discoveryClient;

    @GetMapping("/discovery")
    public String discovery() {
        List<String> services = discoveryClient.getServices();
        for (String element : services) {
            System.out.println(element);
        }

        System.out.println("===================================");

        List<ServiceInstance> instances = discoveryClient.getInstances("cloud-payment-service");
        for (ServiceInstance element : instances) {
            System.out.println(element.getServiceId() + "\t" + element.getHost() + "\t" + element.getPort() + "\t" + element.getUri());
        }

        return instances.get(0).getServiceId() + ":" + instances.get(0).getPort();
    }

}
