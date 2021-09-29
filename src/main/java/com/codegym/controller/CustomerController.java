package com.codegym.controller;

import com.codegym.model.Customer;
import com.codegym.model.Province;
import com.codegym.service.impl.ICustomerService;
import com.codegym.service.impl.IProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@PropertySource("classpath:upload_file.properties")
public class CustomerController {

    @Value("${file-upload")
    private String fileUpload;

    @Autowired
    private ICustomerService customerService;
    @Autowired
    private IProvinceService provinceService;

    @ModelAttribute("provinces")
    public Iterable<Province> provinces(){
        return provinceService.findAll();
    }

    @RequestMapping("/")
    public ModelAndView listCustomer(@PageableDefault(sort = "firstName", size = 5) Pageable pageable){
        Page<Customer> customers= customerService.findAll(pageable);
        ModelAndView modelAndView= new ModelAndView("customer/list");
        modelAndView.addObject("listCustomer", customers);
        return modelAndView;
    }

    @GetMapping("/create/customer")
    public ModelAndView showCreateCustomerForm(){
        ModelAndView modelAndView= new ModelAndView("customer/add");
        modelAndView.addObject("customer",new Customer());
        return modelAndView;
    }

    @PostMapping("/create/customer")
    public ModelAndView saveCustomerForm(@ModelAttribute("customer") Customer customer){
        customerService.save(customer);
        ModelAndView modelAndView=new ModelAndView("customer/add");
        modelAndView.addObject("customer",new Customer());
        modelAndView.addObject("message","Create customer successfully");
        return modelAndView;
    }

    @GetMapping("/edit/customer/{id}")
    public ModelAndView showEditForm(@PathVariable Long id){
        Optional<Customer> customer= customerService.findById(id);
        if(customer.isPresent()){
            ModelAndView modelAndView= new ModelAndView("customer/edit");
            modelAndView.addObject("customer",customer.get());
            return modelAndView;
        }else {
            ModelAndView modelAndView= new ModelAndView("error.404");
            return modelAndView;
        }
    }

    @PostMapping("/edit/customer/")
    public ModelAndView updateCustomerForm(@ModelAttribute("customer") Customer customer){
        customerService.save(customer);
        ModelAndView modelAndView = new ModelAndView("customer/edit");
        modelAndView.addObject("customer", customer);
        modelAndView.addObject("message", "Customer updated successfully");
        return modelAndView;
    }

    @GetMapping("/delete/customer/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id){
        Optional<Customer> customer= customerService.findById(id);
        if(customer.isPresent()){
            ModelAndView modelAndView= new ModelAndView("customer/delete");
            modelAndView.addObject("customer",customer.get());
            return modelAndView;
        }else {
            ModelAndView modelAndView= new ModelAndView("error.404");
            return modelAndView;
        }
    }

    @PostMapping("/delete/customer/")
    public String deleteCustomerForm(@ModelAttribute("customer") Customer customer){
        customerService.remove(customer.getId());
        return "redirect:/";
    }
    @GetMapping("/detail/customer/{id}")
    public ModelAndView detailCustomerForm(@PathVariable Long id){
        Optional<Customer> customer= customerService.findById(id);
        ModelAndView modelAndView= new ModelAndView("customer/detail");
        modelAndView.addObject("customer", customer.get());
        return modelAndView;
    }

    //Optional<String> nhằm hỗ trợ trường hợp null (tức là hiển thị toàn bộ khách hàng mà không thực hiện tìm kiếm)
    @GetMapping("/search")
    public ModelAndView searchCustomerByFirstName(@RequestParam("search") Optional<String> search, Pageable pageable){
        Page<Customer> customers;
//        @PageableDefault(sort = "firstName", size = 5)
        if(search.isPresent()){
            customers=customerService.findAllByFirstNameContaining(search.get(), pageable);
        }else {
            customers=customerService.findAll(pageable);
        }
        ModelAndView modelAndView= new ModelAndView("customer/list");
        modelAndView.addObject("listCustomer",customers);
        return modelAndView;
    }
}
