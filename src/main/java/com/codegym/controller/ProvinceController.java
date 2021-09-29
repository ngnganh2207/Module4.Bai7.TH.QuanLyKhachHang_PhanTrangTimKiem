package com.codegym.controller;


import com.codegym.model.Customer;
import com.codegym.model.Province;
import com.codegym.service.impl.ICustomerService;
import com.codegym.service.impl.IProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@PropertySource("classpath:upload_file.properties")
public class ProvinceController {
    @Value("${file-upload")
    private String fileUpload;

    @Autowired
    private IProvinceService provinceService;
    @Autowired
    private ICustomerService customerService;

    @RequestMapping("/provinces")
    public ModelAndView listProvinces(){
        Iterable<Province> provinces= provinceService.findAll();
        ModelAndView modelAndView= new ModelAndView("province/list");
        modelAndView.addObject("provinces", provinces);
        return modelAndView;
    }

    @GetMapping("/create/province")
    public ModelAndView showCreateForm(){
        ModelAndView modelAndView= new ModelAndView("province/add");
        modelAndView.addObject("province",new Province());
        return modelAndView;
    }

    @PostMapping("/create/province")
    public ModelAndView saveProvince(@ModelAttribute("province") Province province){
        provinceService.save(province);

        ModelAndView modelAndView= new ModelAndView("province/add");
        modelAndView.addObject("province", new Province());
        modelAndView.addObject("message", "Create new province successfully");
        return modelAndView;
    }

    @GetMapping("/edit/province/{id}")
    public ModelAndView editProvinceForm(@PathVariable("id") Long id){
        Optional<Province> province= provinceService.findById(id);
        if(province.isPresent()){
            ModelAndView modelAndView= new ModelAndView("province/edit");
            modelAndView.addObject("province", province.get());
            return modelAndView;
        }else {
            ModelAndView modelAndView= new ModelAndView("error.404");
            return modelAndView;
        }
    }

    @PostMapping("/edit/province")
    public ModelAndView updateProvince(@ModelAttribute("province") Province province){
        provinceService.save(province);
        ModelAndView modelAndView= new ModelAndView("province/edit");
        modelAndView.addObject("province", province);
        modelAndView.addObject("message", "Province update successfully");
        return modelAndView;
    }

    @GetMapping("/delete/province/{id}")
    public ModelAndView showDeleteForm(@PathVariable("id") Long id){
        Optional<Province> province= provinceService.findById(id);
        if(province.isPresent()){
            ModelAndView modelAndView= new ModelAndView("province/delete");
            modelAndView.addObject("province", province.get());
            return modelAndView;
        }else {
            ModelAndView modelAndView= new ModelAndView("error.404");
            return modelAndView;
        }
    }

    @PostMapping("/delete/province")
    public String deleteProvince(@ModelAttribute("province") Province province){
        provinceService.remove(province.getId());
        return "redirect:/provinces";
    }

    @GetMapping("/view/province/{id}")
    public ModelAndView viewProvince(@PathVariable("id") Long id){
        Optional<Province> provinceOptional = provinceService.findById(id);
        if(!provinceOptional.isPresent()){
            return new ModelAndView("error.404");
        }

        Iterable<Customer> customers = customerService.findAllByProvince(provinceOptional.get());

        ModelAndView modelAndView = new ModelAndView("province/view");
        modelAndView.addObject("province", provinceOptional.get());
        modelAndView.addObject("customers", customers);
        return modelAndView;
    }


}
