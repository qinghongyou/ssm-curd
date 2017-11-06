package xiao.ssm.curd.test;

import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import xiao.ssm.curd.bean.Department;
import xiao.ssm.curd.bean.DepartmentExample;
import xiao.ssm.curd.bean.Employee;
import xiao.ssm.curd.dao.DepartmentMapper;
import xiao.ssm.curd.dao.EmployeeMapper;

/**
 * 测试dao 层工作
 * 推荐 Spring 的项目就可以使用 Spring 的单元测试，可以自动注入我们需要的组件
 * 1.导入SpringTest 模块
 * 2.@ContextConfiguration 指定Spring 配置文件的位置
 * 3.直接autowired 我们要使用的组件即可
 * @author Administrator
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class MapperTest {
	
	@Autowired
	DepartmentMapper departmentMapper;
	
	@Autowired
	EmployeeMapper employeeMapper;
	
	@Autowired
	SqlSession sqlSession;
	
	/**
	 * 测试DepartmentMapper
	 */
	@Test
	public void testCURD(){
		/*// 1.创建SpringIOC 容器
		ApplicationContext ioc = new ClassPathXmlApplicationContext("applicationContext.xml");
		// 2.从容器中获取mapper 
		DepartmentMapper mapper = ioc.getBean(DepartmentMapper.class);*/
		
		System.out.println(departmentMapper);
		
		// 1.插入部门
		//departmentMapper.insertSelective(new Department(null, "研发部"));
		
		// 2.生成员工数据，插入几条员工数据
		//employeeMapper.insertSelective(new Employee(null, "xiaoming", "1", "xiaoming@qq.com", 3));
		
		EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
		for(int i=0; i<1000; i++){
			String uuid = UUID.randomUUID().toString().substring(0, 5)+i;
			employeeMapper.insertSelective(new Employee(null, uuid, "M", uuid+"@qq.com", 3));
		}
		System.out.println("批量完成");
	}

}
