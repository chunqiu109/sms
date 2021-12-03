package com.danmi.sms.controller;


import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.danmi.sms.common.vo.Result;
import com.danmi.sms.dto.PageDTO;
import com.danmi.sms.dto.excel.CustomerDto;
import com.danmi.sms.entity.Customer;
import com.danmi.sms.entity.ImportError;
import com.danmi.sms.entity.User;
import com.danmi.sms.entity.request.CustomerRequest;
import com.danmi.sms.service.ICustomerService;
import com.danmi.sms.service.IImportErrorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author chunqiu
 * @since 2021-08-27
 */
@Controller
@RequestMapping("/customer")
@Slf4j
public class CustomerController {

    @Autowired
    private ICustomerService customerService;
    @Autowired
    private IImportErrorService importErrorService;

    @GetMapping("")
    public String toCustomerListUI() {
        return "customer/cusList";
    }

    @GetMapping("/list")
    @ResponseBody
    public Result<Object> getCustomerList(CustomerRequest param) {

        PageDTO<Customer> customerPageDTO = customerService.listCustomerPage(param);
        return Result.success(customerPageDTO.getRecords(), customerPageDTO.getTotal());
    }

    @GetMapping("/export")
    @ResponseBody
    public Result<Object> export(CustomerRequest param) {
        List<Customer> customers = customerService.export(param);
        return Result.success(customers);
    }

    @PostMapping("")
    @ResponseBody
    public Result<Object> addCustomer(Customer customer, HttpServletRequest request) {
        String type = request.getParameter("type");
        customer.setName("self");
        // 判断必须参数
        if (!StringUtils.hasLength(customer.getName()) || !StringUtils.hasLength(customer.getPhone())) {
            return Result.fail("必传参数不能为空！");
        }

        if (checkPhone(customer.getPhone())) {

            Object userInfo = request.getSession().getAttribute("userInfo");
            if (userInfo instanceof User) {
                User loginUser = (User) userInfo;
                customer.setCa(loginUser.getUsername());
            } else if ("1".equals(type)){
                customer.setCa("self");
            }

            customer.setCt(LocalDateTime.now());
            customerService.save(customer);
            return Result.success("新增员工成功！");
        } else {
            return Result.fail("手机号格式不匹配！");
        }

    }

    /**
     * 导入数据
     *
     * @param
     */
    @RequestMapping(value = "import")
    @ResponseBody
    public Result<Object> importData(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {

        List<CustomerDto> CustomerDtos = ExcelImportUtil.importExcel(
                file.getInputStream(),
                CustomerDto.class, new ImportParams());

        Object userInfo = request.getSession().getAttribute("userInfo");
        User loginUser = new User();
        if (userInfo instanceof User) {
            return Result.fail("用户未登录");
        }
        loginUser = (User) userInfo;

        String loginName = loginUser.getUsername();
        List<Customer> customers = new ArrayList<>();

        int total = CustomerDtos.size();
        List<CustomerDto> errorCus = CustomerDtos.stream().filter(i -> !checkPhone(i.getPhone())).collect(Collectors.toList());
        int errorCount = errorCus.size();

        errorCus.forEach(i -> {
            importErrorService.save(new ImportError()
                    .setCusName(i.getName())
                    .setCt(new Date())
                    .setCa(loginName)
                    .setPhone(i.getPhone()));
        });

        CustomerDtos = CustomerDtos.stream().filter(i -> checkPhone(i.getPhone())).collect(Collectors.toList());

        CustomerDtos.forEach(i -> {

            String ca = "default";
            if (StringUtils.hasLength(loginName)) {
                ca = loginName;
            } else if (StringUtils.hasLength(i.getCa())) {
                ca = i.getCa();
            }
            String finalCa = ca;

            customers.add(new Customer().setName(i.getName())
                    .setPhone(i.getPhone())
                    .setSex(i.getSex())
                    .setCa(finalCa)
                    .setCt(LocalDateTime.now()));
        });

        customerService.saveBatch(customers);
        String msg = "";
        if (errorCount == 0) {
            msg = "全部导入成功!，一共" + total + "条数据！";
        } else {
            msg = (total - errorCount) + "条数据成功," + (errorCount) + "条数据手机号格式不对！";
        }
        return Result.success(msg);
    }

    @DeleteMapping("/{ids}")
    @ResponseBody
    public Result<Object> deleteCustomerByIds(@PathVariable("ids") String ids) {
        List<String> cids = Arrays.asList(ids.split(","));
        customerService.removeByIds(cids);
        return Result.success("删除员工成功！");
    }

    @GetMapping("/add/ui")
    public String toAddUI() {
        return "customer/cusAdd";
    }

    @GetMapping("/import/ui")
    public String toImportUI() {
        return "customer/cusImport";
    }


    public Boolean checkPhone(String phone) {
        Pattern p = Pattern.compile("^[1][3,4,5,6,7,8,9][0-9]{9}$");
        Matcher m = p.matcher(phone);
        return m.matches();
    }

}
