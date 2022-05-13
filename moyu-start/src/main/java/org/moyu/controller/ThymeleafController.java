package org.moyu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author fuhaixin
 * @date 2022/5/13
 */
@Controller
public class ThymeleafController {
  @GetMapping("/")
  public ModelAndView index() {
    ModelAndView mv = new ModelAndView();
    mv.setViewName("index");
    mv.getModel().put("slogan", "hello world! Moyu-Group!");
    return mv;
  }
}
