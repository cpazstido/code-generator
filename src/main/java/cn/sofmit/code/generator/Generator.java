
package cn.sofmit.code.generator;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.internal.DefaultShellCallback;

public class Generator {
	public static void main(String[] args) throws Exception {
		List<String> warnings = new ArrayList<String>();
		boolean overwrite = true;
		GeneratorConfig codeConfig = new GeneratorConfig("com.mysql.jdbc.Driver",
				"jdbc:mysql://10.10.1.79:3306/test?useSSL=false", "root", "root");

		// 项目目录
		codeConfig.setProjectDir("D:\\project\\idea\\test\\");
		// 生成代码目录
		codeConfig.setGeneratorDir("src/main/java");
		// dao包名
		codeConfig.setDaoPackageName("com.cf.sessiontest.dao");
		// 实体包名
		codeConfig.setEntityPackageName("com.cf.sessiontest.model");
		// mapper xml目录
		codeConfig.setMapperXmlDir("src/main/resources/mapper");
		// service 接口包名
		codeConfig.setServiceInterfacePackageName("com.cf.sessiontest.service");
		// service 实现类包名
		codeConfig.setServiceImplPackageName("com.cf.sessiontest.service.impl");

		// 数据库表过滤
		TableConfiguration table = new TableConfiguration(codeConfig.getContext());
		table.setTableName("user"); // %代表通配符
		// 使用实际列名作为字段名
		table.addProperty("useActualColumnNames", "true");
		// 生成构造方法
		table.addProperty("constructorBased", "true");
		codeConfig.getContext().addTableConfiguration(table);

		Configuration config = codeConfig.buildConfiguration();
		DefaultShellCallback callback = new DefaultShellCallback(overwrite);
		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
		myBatisGenerator.generate(null);
	}
}
