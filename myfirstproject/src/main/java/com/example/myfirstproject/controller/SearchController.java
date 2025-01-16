package com.example.myfirstproject.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class SearchController {

    @GetMapping("/search")
    public String mainSearch(@RequestParam("mainSearchKeyword")String searchKeyword) {
        log.info("검색어 : "+ searchKeyword );
        return "";
    }


}