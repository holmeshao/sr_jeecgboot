package org.jeecg.modules.online.cgform.c;

import org.jeecg.common.api.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "onlCgformApiControllerV2")
@RequestMapping({"/online/cgform/api"})
public class OnlCgformApiController {

    private static final Logger log = LoggerFactory.getLogger(OnlCgformApiController.class);

    @GetMapping("/health")
    public Result<String> health() {
        return Result.OK("ok");
    }
}


