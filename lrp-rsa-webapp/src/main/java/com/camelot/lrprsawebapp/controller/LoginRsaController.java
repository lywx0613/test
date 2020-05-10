package com.camelot.lrprsawebapp.controller;

import com.camelot.lrprsawebapp.utils.IdWorker;
import com.camelot.lrprsawebapp.utils.RSAEncrypt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

@RestController
public class LoginRsaController {


    @RequestMapping("/getPasswprdKey")
    public String getPasswprdKey(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        try {
            System.out.println("测试");
            PrintWriter writer = httpServletResponse.getWriter();
            Map<Integer, String> map = RSAEncrypt.genKeyPair();
            IdWorker idWorker = new IdWorker();
//            consumerService.setRedis(String.valueOf(idWorker.nextId()),map.get(1), DBType.lockDB);
            writer.write(map.get(0));
            System.out.println("-----公钥----："+map.get(0));
            return null;
        } catch (Exception e) {
            return null;
        }
    }

}
