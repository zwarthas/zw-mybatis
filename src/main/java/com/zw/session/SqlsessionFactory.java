package com.zw.session;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.zw.config.Configuration;
import com.zw.config.MappedStatement;
import com.zw.constant.CommonConstant;

//1.生成sqlsession
//2.在实例化的时候生成config
public class SqlsessionFactory {
	private final Configuration conf = new Configuration();

	public SqlsessionFactory() {
		loadDbInfo();
		loadMappersInfo();
	}
	
	public Sqlsession openSession() {
		return new DefaultSqlsession(conf);
	}
	
	// 加载数据库配置信息
	private void loadDbInfo() {
		InputStream inputStream  = this.getClass().getClassLoader().getResourceAsStream(CommonConstant.DB_CONFIG_FILE);
		Properties p = new Properties();
		try {
			p.load(inputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conf.setJdbcDriver(p.getProperty(CommonConstant.JDBC_DRIVER));
		conf.setJdbcUrl(p.getProperty(CommonConstant.JDBC_URL));
		conf.setJdbcUsername(p.getProperty(CommonConstant.JDBC_USERNAME));
		conf.setJdbcPassword(p.getProperty(CommonConstant.JDBC_PASSWORD));
		
		System.out.println(CommonConstant.JDBC_DRIVER + ":" + p.getProperty(CommonConstant.JDBC_DRIVER));
		System.out.println(CommonConstant.JDBC_URL + ":" + p.getProperty(CommonConstant.JDBC_URL));
		System.out.println(CommonConstant.JDBC_USERNAME + ":" + p.getProperty(CommonConstant.JDBC_USERNAME));
		System.out.println(CommonConstant.JDBC_PASSWORD + ":" + p.getProperty(CommonConstant.JDBC_PASSWORD));
	}
	
	// 加载mapper.xml信息
	private void loadMappersInfo() {
		URL URL = this.getClass().getClassLoader().getResource(CommonConstant.MAPPER_CONFIG_LOCATION);
		File mapperFolder = new File(URL.getFile());
		if (mapperFolder.isDirectory()) {
			File[] files = mapperFolder.listFiles();
			for (File file : files) {
				loadMapperInfo(file);
				System.out.println("mapper:" + file.getName());
			}
		}
	}
	
	// 获取单独一个mapper.xml信息
	private void loadMapperInfo(File file) {
		// 创建saxReader对象  
		SAXReader reader = new SAXReader();  
		Document document = null;
		try {
			document = reader.read(file);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Element root = document.getRootElement();
		// 命名空间
		String namespace = root.attribute("namespace").getData().toString();
		
		// 遍历所有子节点
		List<Element> elements = root.elements("select");
		for (Element element : elements) {
			String id = element.attribute("id").getData().toString();
			String resultType = element.attribute("resultType").getData().toString();
			String sql = element.getData().toString();
			String sourceId = namespace + "." + id;
			//给mappedStatement属性赋值
			MappedStatement mappedStatement = new MappedStatement();
			mappedStatement.setNamespace(namespace);
			mappedStatement.setSourceId(sourceId);
			mappedStatement.setResultType(resultType);
			mappedStatement.setSql(sql);
			//注册到configuration对象中
			conf.getMappedStatements().put(sourceId, mappedStatement);
		}
	}
}
