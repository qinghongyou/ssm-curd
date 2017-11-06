package xiao.ssm.curd.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.ibatis.executor.ReuseExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import xiao.ssm.curd.bean.Employee;
import xiao.ssm.curd.bean.Msg;
import xiao.ssm.curd.service.EmployeeService;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
/**
 * 处理员工的CURD
 * @author Administrator
 *
 */
@Controller
public class EmployeeController {
	
	@Autowired
	EmployeeService employeeService;
	
	/**
	 * 批量和单个二合一
	 * 批量中：1-2-3
	 * 单个：1
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/emp/{ids}", method=RequestMethod.DELETE)
	@ResponseBody
	public Msg deleteEmpById(@PathVariable("ids")String ids){
		// 批量删除
		if(ids.contains("-")){
			List<Integer> del_ids = new ArrayList<Integer>();
			// 组装id 集合
			String[] str_ids = ids.split("-");
			for (String id : str_ids) {
				del_ids.add(Integer.parseInt(id));
			}
			employeeService.deleteBatch(del_ids);
		}else{
			// 单个删除
			Integer id = Integer.parseInt(ids);
			employeeService.deleteEmp(id);
		}
		return Msg.success();
	}
	
	/**
	 * 员工更新方法
	 * 如果直接发送ajax=PUT形式的请求
	 * 封装的数据
	 * Employee 
	 * [empId=1014, empName=null, gender=null, email=null, dId=null]
	 * 
	 * 问题：
	 * 请求体中有数据；
	 * 但是Employee对象封装不上；
	 * update tbl_emp  where emp_id = 1014;
	 * 
	 * 原因：
	 * Tomcat：
	 * 		1、将请求体中的数据，封装一个map。
	 * 		2、request.getParameter("empName")就会从这个map中取值。
	 * 		3、SpringMVC封装POJO对象的时候。
	 * 				会把POJO中每个属性的值，request.getParamter("email");
	 * AJAX发送PUT请求引发的血案：
	 * 		PUT请求，请求体中的数据，request.getParameter("empName")拿不到
	 * 		Tomcat一看是PUT不会封装请求体中的数据为map，只有POST形式的请求才封装请求体为map
	 * org.apache.catalina.connector.Request--parseParameters() (3111);
	 * 
	 * protected String parseBodyMethods = "POST";
	 * if( !getConnector().isParseBodyMethod(getMethod()) ) {
                success = true;
                return;
            }
	 * 
	 * 
	 * 解决方案；
	 * 我们要能支持直接发送PUT之类的请求还要封装请求体中的数据
	 * 1、配置上HttpPutFormContentFilter；
	 * 2、他的作用；将请求体中的数据解析包装成一个map。
	 * 3、request被重新包装，request.getParameter()被重写，就会从自己封装的map中取数据
	 * @param employee
	 * @return
	 */
	@RequestMapping(value="/emp/{empId}", method=RequestMethod.PUT)
	@ResponseBody
	public Msg saveEmp(Employee employee){
		employeeService.updateEmp(employee);
		return Msg.success();
	}
	
	/**
	 * 根据id查询员工信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/emp/{id}", method=RequestMethod.GET)
	@ResponseBody
	public Msg getEmp(@PathVariable("id") Integer id){
		Employee employee = employeeService.getEmp(id);
		return Msg.success().add("emp", employee);
	}
	
	@RequestMapping("/checkuser")
	@ResponseBody
	public Msg checkuser(@RequestParam("empName") String empName){
		// 判断用户名是否是合法的表达式
		String regx = "(^[a-zA-Z0-9_-]{6,16}$)|(^[\u2E80-\u9FFF]{2,5})";
		if(!empName.matches(regx)){
			return Msg.fail().add("va_msg", "用户名可以是2-5位的中文或者6-16的英文和数字组合");
		}
		// 用户名重复校验
		boolean b = employeeService.checkUser(empName);
		if(b){
			return Msg.success();
		}else{
			return Msg.fail().add("va_msg", "用户名不可用");
		}
	}
	
	/**
	 * 员工保存
	 * @param employee
	 * @return
	 */
	@RequestMapping(value="/emp", method=RequestMethod.POST)
	@ResponseBody
	public Msg saveEmp(@Valid Employee employee, BindingResult result){
		if(result.hasErrors()){
			// 校验失败，应该返回失败，在模态框中显示校验失败的信息
			Map<String, Object> map = new HashMap<>();
			List<FieldError> errors = result.getFieldErrors();
			for(FieldError fieldError: errors){
				map.put(fieldError.getField(), fieldError.getDefaultMessage());
			}
			return Msg.fail().add("errorsField", map);
		}else{
			employeeService.saveEmp(employee);
			return Msg.success();
		}
	}
	
	/**
	 * 导入jackson 包
	 * @param pn
	 * @return
	 */
	@RequestMapping("/emps")
	@ResponseBody
	public Msg getEmpsWithJson(@RequestParam(value="pn", defaultValue="1") Integer pn){
		// 引入PageHelper 分页插件
		// 在查询之前，需要传入页码， 和每页的大小
		PageHelper.startPage(pn, 8);
		// startPage后面紧跟的这个查询就是一个分页查询
		List<Employee> emps = employeeService.getAll();
		// 使用pageInfo 包装查询后的结果，只需要将pageInfo交给页面就行了
		// 封装了详细的分页信息，包括有我们查询出来的数据,连续显示的页数
		PageInfo page = new PageInfo(emps, 5);
		return Msg.success().add("pageInfo", page);
	}
	
	/**
	 * 插叙员工数据（分页查询）
	 * @return
	 */
	//@RequestMapping("/emps")
	public String getEmps(@RequestParam(value="pn", defaultValue="1") Integer pn,
			Model model){
		// 引入PageHelper 分页插件
		// 在查询之前，需要传入页码， 和每页的大小
		PageHelper.startPage(pn, 5);
		// startPage后面紧跟的这个查询就是一个分页查询
		List<Employee> emps = employeeService.getAll();
		// 使用pageInfo 包装查询后的结果，只需要将pageInfo交给页面就行了
		// 封装了详细的分页信息，包括有我们查询出来的数据,连续显示的页数
		PageInfo page = new PageInfo(emps, 5);
		model.addAttribute("pageInfo", page);
		return "list";
	}

}
