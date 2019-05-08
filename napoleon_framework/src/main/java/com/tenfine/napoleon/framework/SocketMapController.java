package com.tenfine.napoleon.framework;

import com.tenfine.napoleon.framework.bean.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * @author Hunter
 */
@Controller
public class SocketMapController {

    @RequestMapping("/socketMapPage")
    @ResponseBody
    public ModelAndView socket() {
        ModelAndView mav=new ModelAndView("socketMap");
        mav.addObject("cid", "默认");
        return mav;
    }

    @RequestMapping("/socket/push/{cid}")
    @ResponseBody
    public Result<?> pushToWeb(@PathVariable String cid, String message) {
        try {
            WebSocketServer.sendInfo(message,cid);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error(cid+"#"+e.getMessage());
        }
        return Result.success(cid);
    }

}
