package cn.sofmit.code.generator;

import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.config.ModelType;
import org.mybatis.generator.config.PluginConfiguration;

import tk.mybatis.mapper.util.StringUtil;

public class GeneratorConfig {

	// 项目目录
	private String projectDir;

	// 生成目录
	private String generatorDir;

	private String entityPackageName;

	private String daoPackageName;

	private String mapperXmlDir;

	private String serviceInterfacePackageName;

	private String serviceImplPackageName;

	private String dbDriver;
	private String dbUrl;
	private String dbUser;
	private String dbPwd;

	private Configuration configuration;
	private Context context;

	public GeneratorConfig(String dbDriver, String dbUrl, String dbUser, String dbPwd) {
		super();
		this.dbDriver = dbDriver;
		this.dbUrl = dbUrl;
		this.dbUser = dbUser;
		this.dbPwd = dbPwd;

		configuration = new Configuration();
		context = new Context(ModelType.FLAT);
		context.setId("MySql");
		context.setTargetRuntime("MyBatis3Simple");
		configuration.addContext(context);

		context.addProperty("javaFileEncoding", "UTF-8");
	}

	public Configuration buildConfiguration() {
		if (StringUtil.isEmpty(getGeneratorDir())) {
			throw new RuntimeException("generatorDir 不能为空!");
		}
		initPlugin();
		initGenerator();
		initJdbc();
		return this.configuration;
	}

	private void initPlugin() {
		initMapperPlugin();
		initMapperInterfacePlugin();
		initMapperXmlPlugin();
		initServicePlugin();
		initServiceImplPlugin();
	}

	private void initMapperPlugin() {
		PluginConfiguration plugin = new PluginConfiguration();
		plugin.setConfigurationType("tk.mybatis.mapper.generator.MapperPlugin");
		plugin.addProperty("mappers", "tk.mybatis.mapper.common.Mapper,tk.mybatis.mapper.hsqldb.HsqldbMapper");
		plugin.addProperty("caseSensitive", "false");
		plugin.addProperty("forceAnnotation", "false");
		plugin.addProperty("beginningDelimiter", "`");
		plugin.addProperty("endingDelimiter", "`");
		context.addPluginConfiguration(plugin);
	}

	private void initMapperInterfacePlugin() {
		if (StringUtil.isEmpty(getDaoPackageName())) {
			return;
		}

		PluginConfiguration plugin = new PluginConfiguration();
		plugin.setConfigurationType("tk.mybatis.mapper.generator.TemplateFilePlugin");
		plugin.addProperty("targetProject", getGeneratorDir());
		plugin.addProperty("targetPackage", getDaoPackageName());
		plugin.addProperty("templatePath", "template/mapper.ftl");
		plugin.addProperty("mapperSuffix", "Mapper");
		plugin.addProperty("fileName", "${tableClass.shortClassName}${mapperSuffix}.java");
		context.addPluginConfiguration(plugin);
	}

	private void initMapperXmlPlugin() {
		PluginConfiguration plugin = new PluginConfiguration();
		plugin.setConfigurationType("tk.mybatis.mapper.generator.TemplateFilePlugin");
		plugin.addProperty("targetProject", getMapperXmlDir());
		plugin.addProperty("targetPackage", "");
		plugin.addProperty("mapperPackage", getDaoPackageName());
		plugin.addProperty("templatePath", "template/mapperXml.ftl");
		plugin.addProperty("mapperSuffix", "Mapper");
		plugin.addProperty("fileName", "${tableClass.shortClassName}${mapperSuffix}.xml");
		context.addPluginConfiguration(plugin);
	}

	private void initServicePlugin() {
		if (StringUtil.isEmpty(getServiceInterfacePackageName())) {
			System.out.println("没有配置serviceInterfacePackageName 将不会生成Service 接口类");
			return;
		}

		PluginConfiguration plugin = new PluginConfiguration();
		plugin.setConfigurationType("tk.mybatis.mapper.generator.TemplateFilePlugin");
		plugin.addProperty("targetProject", getGeneratorDir());
		plugin.addProperty("targetPackage", getServiceInterfacePackageName());
		plugin.addProperty("servicePackage", getServiceInterfacePackageName());
		plugin.addProperty("templatePath", "template/service.ftl");
		plugin.addProperty("fileName", "I${tableClass.shortClassName}Service.java");
		plugin.addProperty("templateFormatter", "tk.mybatis.mapper.generator.formatter.FreemarkerTemplateFormatter");

		context.addPluginConfiguration(plugin);
	}

	private void initServiceImplPlugin() {
		if (StringUtil.isEmpty(getServiceImplPackageName())) {
			System.out.println("没有配置serviceImplPackageName 将不会生成ServiceImpl 实现类");
			return;
		}

		if (StringUtil.isEmpty(getServiceInterfacePackageName())) {
			System.out.println("没有配置serviceInterfacePackageName 将不会生成ServiceImpl 实现类");
			return;
		}

		PluginConfiguration plugin = new PluginConfiguration();
		plugin.setConfigurationType("tk.mybatis.mapper.generator.TemplateFilePlugin");
		plugin.addProperty("targetProject", getGeneratorDir());
		plugin.addProperty("targetPackage", getServiceImplPackageName());
		plugin.addProperty("servicePackage", getServiceInterfacePackageName());
		plugin.addProperty("templatePath", "template/serviceImpl.ftl");
		plugin.addProperty("fileName", "${tableClass.shortClassName}ServiceImpl.java");
		plugin.addProperty("templateFormatter", "tk.mybatis.mapper.generator.formatter.FreemarkerTemplateFormatter");
		context.addPluginConfiguration(plugin);
	}

	private void initGenerator() {
		initEntityGenerator();
	}

	private void initEntityGenerator() {
		if (StringUtil.isEmpty(getEntityPackageName())) {
			return;
		}
		JavaModelGeneratorConfiguration config = new JavaModelGeneratorConfiguration();
		config.setTargetProject(getGeneratorDir());
		config.setTargetPackage(getEntityPackageName());
		context.setJavaModelGeneratorConfiguration(config);
	}

	private void initJdbc() {
		JDBCConnectionConfiguration config = new JDBCConnectionConfiguration();
		config.setConnectionURL(this.dbUrl);
		config.setDriverClass(this.dbDriver);
		config.setPassword(dbPwd);
		config.setUserId(dbUser);
		context.setJdbcConnectionConfiguration(config);
	}

	public String getGeneratorDir() {

		if (StringUtil.isNotEmpty(getProjectDir())) {
			return getProjectDir() + "/" + generatorDir;
		}

		return generatorDir;
	}

	public void setGeneratorDir(String generatorDir) {
		this.generatorDir = generatorDir;
	}

	public String getEntityPackageName() {
		return entityPackageName;
	}

	public void setEntityPackageName(String entityPackageName) {
		this.entityPackageName = entityPackageName;
	}

	public String getDaoPackageName() {
		return daoPackageName;
	}

	public void setDaoPackageName(String daoPackageName) {
		this.daoPackageName = daoPackageName;
	}

	public String getMapperXmlDir() {

		if (StringUtil.isNotEmpty(getProjectDir())) {
			return getProjectDir() + "/" + mapperXmlDir;
		}

		return mapperXmlDir;
	}

	public void setMapperXmlDir(String mapperXmlDir) {
		this.mapperXmlDir = mapperXmlDir;
	}

	public String getServiceInterfacePackageName() {
		return serviceInterfacePackageName;
	}

	public void setServiceInterfacePackageName(String serviceInterfacePackageName) {
		this.serviceInterfacePackageName = serviceInterfacePackageName;
	}

	public String getServiceImplPackageName() {
		return serviceImplPackageName;
	}

	public void setServiceImplPackageName(String serviceImplPackageName) {
		this.serviceImplPackageName = serviceImplPackageName;
	}

	public Context getContext() {
		return context;
	}

	public String getProjectDir() {
		return projectDir;
	}

	public void setProjectDir(String projectDir) {
		this.projectDir = projectDir;
	}

}
